package busexplorer.panel.interfaces;

import busexplorer.Application;
import busexplorer.desktop.dialog.ConsistencyValidationDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.contracts.ContractDeleteAction;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Contract;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe de ação para a remoção de uma interface.
 * 
 * @author Tecgraf
 */
public class InterfaceDeleteAction extends OpenBusAction<InterfaceWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public InterfaceDeleteAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (InputDialog.showConfirmDialog(parentWindow,
      getString("confirm.msg"),
      getString("confirm.title")) != JOptionPane.YES_OPTION) {
      return;
    }

    final ConsistencyValidationDialog.DeleteOptions removeFlags = new ConsistencyValidationDialog.DeleteOptions();
    ConsistencyValidationResult consistencyValidationResult = new ConsistencyValidationResult();
    Collection<InterfaceWrapper> interfaces = getTablePanelComponent().getSelectedElements();

    BusExplorerTask<Void> deleteInterfaceTask =
      DeleteInterfaceTask(interfaces, getTablePanelComponent()::removeSelectedElements, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteInterfaceTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (ExecuteAllDependencyCheckTasks(parentWindow, interfaces, consistencyValidationResult)) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

  public static boolean ExecuteAllDependencyCheckTasks(Window parentWindow,
                                                       Collection<InterfaceWrapper> interfaces,
                                                       ConsistencyValidationResult consistencyValidationResult) {
    String title = Language.get(ConsistencyValidationDialog.class, "waiting.dependency.title");
    String waitingMessage = Language.get(InterfaceDeleteAction.class,"waiting.dependency.msg");

    BusExplorerTask governanceDependencyCheckTask =
      GovernanceDependencyCheckTask(interfaces, consistencyValidationResult);

    BusExplorerTask extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(interfaces, consistencyValidationResult);

    governanceDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    extensionDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    return extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus()
      && ContractDeleteAction.ExecuteAllDependencyCheckTasks(parentWindow, consistencyValidationResult
      .getInconsistentContracts().values(), consistencyValidationResult);
  }

  public static BusExplorerTask<Void> GovernanceDependencyCheckTask(Collection<InterfaceWrapper> interfaces,
                                                                    ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        // authorizations being used
        int i = 0;
        int step = 50;
        List<ServiceOfferDesc> offers = Application.login().admin.getOffers();
        for (ServiceOfferDesc offer : offers) {
          for (InterfaceWrapper interfaceInfo : interfaces) {
            for (ServiceProperty property : offer.properties) {
              if (property.name.equals(OfferWrapper.OPENBUS_COMPONENT_INTERFACE) && property.value.equals(interfaceInfo.getName())) {
                RegisteredEntity entity = Application.login().admin.getEntity(offer.ref.owner().entity);
                consistencyValidationResult.getInconsistentAuthorizations()
                  .add(new AuthorizationWrapper(entity.describe(), interfaceInfo.getName()));
                consistencyValidationResult.getInconsistentOffers()
                  .add(new OfferWrapper(offer));
                break;
              }
            }
          }
          this.setProgressStatus(step * i / offers.size());
          i++;
        }
        // authorizations being not used
        i = 0;
        Map<RegisteredEntityDesc, List<String>> authorizations = Application.login().admin.getAuthorizations();
        int authorizationsSum = 0;
        for (List<String> item : authorizations.values()) {
          authorizationsSum += item.size();
        }
        for (Map.Entry<RegisteredEntityDesc, List<String>> entry : authorizations.entrySet()) {
          for (String grantedInterface : entry.getValue()) {
            if (interfaces.contains(new InterfaceWrapper(grantedInterface))) {
              AuthorizationWrapper authWrapper = new AuthorizationWrapper(entry.getKey(), grantedInterface);
              if (!consistencyValidationResult.getInconsistentAuthorizations().contains(authWrapper)) {
                consistencyValidationResult.getInconsistentAuthorizations().add(authWrapper);
                break;
              }
            }
            this.setProgressStatus(step+(step*i/authorizationsSum));
            i++;
          }
        }
      }
    };
  }

  public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<InterfaceWrapper> interfaces,
                                                                   ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        if (Application.login().extension.isExtensionCapable()) {
          LinkedHashMap<String, List<String>> contractsMaybeBroken = new LinkedHashMap<>();
          for (InterfaceWrapper ifaceWrapper : interfaces) {
            for (Contract contract : Application.login().extension.getContracts()) {
              ContractWrapper contractWrapper = new ContractWrapper(contract);
              String iface = ifaceWrapper.getName();
              // if the contract for this interface...
              if (contractWrapper.interfaces().contains(iface)) {
                // mark to futher analysis
                if (contractsMaybeBroken.get(contractWrapper.name()) == null) {
                  contractsMaybeBroken.put(contractWrapper.name(), new ArrayList<>());
                }
                contractsMaybeBroken.get(contractWrapper.name()).add(iface);
              }
            }
            this.setProgressStatus(70*i/interfaces.size());
            i++;
          }
          i = 0;
          for (Map.Entry<String, List<String>> entry : contractsMaybeBroken.entrySet()) {
            Contract contract = Application.login().extension.getContractRegistry().get(entry.getKey());
            ContractWrapper wrapper = new ContractWrapper(contract);
            // if the contract will be wipe out
            if (wrapper.interfaces().size() == entry.getValue().size()) {
              consistencyValidationResult.getInconsistentContracts().put(wrapper.name(), wrapper);
            }
            this.setProgressStatus(70+(30*i/contractsMaybeBroken.entrySet().size()));
            i++;
          }
        }
      }
    };
  }

  public static BusExplorerTask<Void> DeleteInterfaceTask(Collection<InterfaceWrapper> interfaces, Runnable delegateAfterTaskUI,
                                                          ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                          ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      private final String removeDependenciesTitle =
        Language.get(InterfaceDeleteAction.class, "waiting.removing.dependencies.title");
      private final String removeDependenciesMessage =
        Language.get(InterfaceDeleteAction.class, "waiting.removing.dependencies.msg");
      @Override
      protected void doPerformTask() throws Exception {
        int i = 0;
        int step = 25;
        if (Application.login().extension.isExtensionCapable()) {
          if (removeFlags.isFullyGovernanceRemoval()) {
            // proceed to contracts removal
            if (!consistencyValidationResult.getInconsistentContracts().isEmpty()) {
              ContractDeleteAction.DeleteContractTask(consistencyValidationResult
                .getInconsistentContracts().values(), null, removeFlags, consistencyValidationResult)
                .execute(parentWindow, removeDependenciesTitle, removeDependenciesMessage, 2, 0, true, false);;
            }
          }
          for (InterfaceWrapper interfaceInfo : interfaces) {
            // if no contracts should be removed we just remove the interface from that contract
            for (Contract contract : Application.login().extension.getContracts()) {
              String[] interfaceContractList = contract.interfaces();
              for (String inContract : interfaceContractList) {
                if (inContract.equals(interfaceInfo.getName())) {
                  contract.removeInterface(inContract);
                  break;
                }
              }
            }
            this.setProgressStatus(step*i/interfaces.size());
            i++;
          }
        }
        i = 0;
        // fully removal is indeed to remove interface without any restrictions (force authorization removal for instance)
        if (removeFlags.isFullyGovernanceRemoval()) {
          // some offers probably was already removed when extension is enabled
          Set<OfferWrapper> inconsistentOffers = consistencyValidationResult.getInconsistentOffers();
          for (OfferWrapper offer : inconsistentOffers) {
            LoginInfo login = offer.getDescriptor().ref.owner();
            Application.login().admin.invalidateLogin(login);
            for (InterfaceWrapper interfaceInfo : interfaces) {
              Application.login().admin.revokeAuthorization(login.entity, interfaceInfo.getName());
            }
            this.setProgressStatus(step+(step*i/inconsistentOffers.size()));
            i++;
          }
        }
        i = 0;
        // some authorizations probably was already removed when extension is enabled
        Set<AuthorizationWrapper> inconsistentAuthorizations = consistencyValidationResult.getInconsistentAuthorizations();
        for (AuthorizationWrapper authorization : inconsistentAuthorizations) {
          try {
            Application.login().admin.revokeAuthorization(authorization.getEntityId(), authorization.getInterface());
          } catch (NullPointerException e) {
            // ignore because means it was already removed..
          }
          this.setProgressStatus((step*2)+(step*i/inconsistentAuthorizations.size()));
          i++;
        }
        i = 0;
        for (InterfaceWrapper interfaceInfo : interfaces) {
          String interfaceName = interfaceInfo.getName();
          Application.login().admin.removeInterface(interfaceName);
          this.setProgressStatus((step*3)+(step*i/interfaces.size()));
          i++;
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus() && delegateAfterTaskUI != null) {
          delegateAfterTaskUI.run();
        }
      }
    };
  }
}
