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
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Contract;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Classe de ação para a remoção de uma interface.
 * 
 * @author Tecgraf
 */
public class InterfaceDeleteAction extends OpenBusAction<InterfaceWrapper> {

  public static final String OPENBUS_COMPONENT_INTERFACE = "openbus.component.interface";

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

    BusExplorerTask<Void> governanceDependencyCheckTask =
      GovernanceDependencyCheckTask(interfaces, consistencyValidationResult);

    governanceDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    BusExplorerTask<Void> extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(interfaces, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    BusExplorerTask<Void> contractDependencyCheckTask =
      ContractDeleteAction.ExtensionDependencyCheckTask(consistencyValidationResult
        .getInconsistentContracts().values(), consistencyValidationResult);

    contractDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    BusExplorerTask<Void> deleteInterfaceTask =
      DeleteInterfaceTask(interfaces, getTablePanelComponent()::removeSelectedElements, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteInterfaceTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus()) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
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
              if (property.name.equals(OPENBUS_COMPONENT_INTERFACE) && property.value.equals(interfaceInfo.getName())) {
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
        for (InterfaceWrapper ifaceWrapper : interfaces) {
          if (Application.login().extension.isExtensionCapable()) {
            for (Contract contract : Application.login().extension.getContracts()) {
              ContractWrapper wrapper = new ContractWrapper(contract);
              // if this interface is the only one in contract, we must remove the contract!
              String ifaceToRemove = ifaceWrapper.getName();
              if ((wrapper.interfaces().size() == 1) && (ifaceToRemove.equals(wrapper.interfaces().get(0)))) {
                consistencyValidationResult.getInconsistentContracts().put( wrapper.name(), wrapper);
              }
            }
          }
          this.setProgressStatus(100*i/interfaces.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> DeleteInterfaceTask(Collection<InterfaceWrapper> interfaces, Runnable delegateAfterTaskUI,
                                                          ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                          ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {

    @Override
    protected void doPerformTask() throws Exception {
      int i = 0;
      int step = 25;
      if (Application.login().extension.isExtensionCapable()) {
        if (removeFlags.isFullyGovernanceRemoval()) {
          // proceed to contracts removal
          if (!consistencyValidationResult.getInconsistentContracts().isEmpty()) {
            ContractDeleteAction.DeleteContractTask(consistencyValidationResult
              .getInconsistentContracts().values(), null, removeFlags, consistencyValidationResult);
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
        List<OfferWrapper> inconsistentOffers = consistencyValidationResult.getInconsistentOffers();
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
      List<AuthorizationWrapper> inconsistentAuthorizations = consistencyValidationResult.getInconsistentAuthorizations();
      for (AuthorizationWrapper authorization : inconsistentAuthorizations) {
        Application.login().admin.revokeAuthorization(authorization.getEntityId(), authorization.getInterface());
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
