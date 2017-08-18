package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.desktop.dialog.ConsistencyValidationDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.contracts.ContractDeleteAction;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Provider;

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

import static busexplorer.panel.offers.OfferWrapper.OPENBUS_COMPONENT_INTERFACE;

/**
 * Classe de ação para a remoção de uma autorização.
 * 
 * @author Tecgraf
 */
public class AuthorizationDeleteAction extends OpenBusAction<AuthorizationWrapper>
  {

  /**
   * Construtor da ação
   *  @param parentWindow janela mãe do diálogo a ser criado pela ação
   *
   */
  public AuthorizationDeleteAction(JFrame parentWindow) {
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
    Collection<AuthorizationWrapper> authorizations = getTablePanelComponent().getSelectedElements();

    BusExplorerTask<Void> deleteAuthorizationTask =
      DeleteAuthorizationTask(authorizations, getTablePanelComponent()::removeSelectedElements, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteAuthorizationTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (ExecuteAllDependencyCheckTasks(parentWindow, authorizations, consistencyValidationResult)) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

    public static boolean ExecuteAllDependencyCheckTasks(Window parentWindow,
                                                         Collection<AuthorizationWrapper> authorizations,
                                                         ConsistencyValidationResult consistencyValidationResult) {
      String title = Language.get(ConsistencyValidationDialog.class, "waiting.dependency.title");
      String waitingMessage = Language.get(AuthorizationDeleteAction.class,"waiting.dependency.msg");

      BusExplorerTask governanceDependencyCheckTask =
        GovernanceDependencyCheckTask(authorizations, consistencyValidationResult);

      BusExplorerTask extensionDependencyCheckTask =
        ExtensionDependencyCheckTask(authorizations, consistencyValidationResult);

      governanceDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

      extensionDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

      return extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus()
        && ContractDeleteAction.ExecuteAllDependencyCheckTasks(parentWindow, consistencyValidationResult
        .getInconsistentContracts().values(), consistencyValidationResult);
    }

    public static BusExplorerTask<Void> GovernanceDependencyCheckTask(Collection<AuthorizationWrapper> authorizations,
                                                                      ConsistencyValidationResult consistencyValidationResult) {
      return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
        @Override
        protected void doPerformTask() throws Exception {
          setProgressDialogEnabled(true);
          int i = 0;
          List<ServiceOfferDesc> offers = Application.login().admin.getOffers();
          for (ServiceOfferDesc offer : offers) {
            for (AuthorizationWrapper auth : authorizations) {
              for (ServiceProperty property : offer.properties) {
                if (property.name.equals(OPENBUS_COMPONENT_INTERFACE) && property.value.equals(auth.getInterface())) {
                  consistencyValidationResult.getInconsistentOffers()
                    .add(new OfferWrapper(offer));
                  break;
                }
              }
            }
            this.setProgressStatus(100 * i / offers.size());
            i++;
          }
        }
      };
    }

    public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<AuthorizationWrapper> authorizations,
                                                                     ConsistencyValidationResult consistencyValidationResult) {
      return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
        @Override
        protected void doPerformTask() throws Exception {
          setProgressDialogEnabled(true);
          int i = 0;
          if (Application.login().extension.isExtensionCapable()) {
            LinkedHashMap<String, List<String>> contractsMaybeBroken = new LinkedHashMap<>();
            int innerStatusSum = 0;
            for (Provider remote : Application.login().extension.getProviders()) {
              innerStatusSum += remote.contracts().length;
            }
            for (Provider remote : Application.login().extension.getProviders()) {
              ProviderWrapper provider = new ProviderWrapper(remote);
              for (RegisteredEntityDesc entityProvider : new BusQuery(provider.busquery()).filterEntities()) {
                for (AuthorizationWrapper authorization : authorizations) {
                  if (entityProvider.id.equals(authorization.getEntityId())) {
                    for (String contractName : provider.contracts()) {
                      ContractWrapper contract = new ContractWrapper(
                        Application.login().extension.getContractRegistry().get(contractName));
                      String iface = authorization.getInterface();
                      // if the contract for this interface...
                      if (contract.interfaces().contains(iface)) {
                        // mark to futher analysis
                        if (contractsMaybeBroken.get(contract.name()) == null) {
                          contractsMaybeBroken.put(contract.name(), new ArrayList<>());
                        }
                        contractsMaybeBroken.get(contract.name()).add(iface);
                      }
                    }
                  }
                }
              }
              this.setProgressStatus(70*i/innerStatusSum);
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

    public static BusExplorerTask<Void> DeleteAuthorizationTask(Collection<AuthorizationWrapper> authorizations,
                                                                Runnable delegateAfterTaskUI,
                                                                ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                                ConsistencyValidationResult consistencyValidationResult) {
      return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
        private final String removeDependenciesTitle =
          Language.get(AuthorizationDeleteAction.class, "waiting.removing.dependencies.title");
        private final String removeDependenciesMessage =
          Language.get(AuthorizationDeleteAction.class, "waiting.removing.dependencies.msg");
        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          if (Application.login().extension.isExtensionCapable()) {
            if (removeFlags.isFullyGovernanceRemoval()) {
              // proceed to contracts removal
              if (!consistencyValidationResult.getInconsistentContracts().isEmpty()) {
                ContractDeleteAction.DeleteContractTask(consistencyValidationResult
                  .getInconsistentContracts().values(), null, removeFlags, consistencyValidationResult)
                  .execute(parentWindow, removeDependenciesTitle, removeDependenciesMessage, 2, 0, true, false);;
              }
            }
            for (AuthorizationWrapper authorization : authorizations) {
              // if no contracts should be removed we just remove the interface from that contract
              for (Contract contract : Application.login().extension.getContracts()) {
                String[] interfaceContractList = contract.interfaces();
                for (String inContract : interfaceContractList) {
                  if (inContract.equals(authorization.getInterface())) {
                    contract.removeInterface(inContract);
                    break;
                  }
                }
              }
              this.setProgressStatus(25*i/authorizations.size());
              i++;
            }
          }
          i = 0;
          if (removeFlags.isFullyGovernanceRemoval()) {
            // some offers probably was already removed when extension is enabled
            Set<OfferWrapper> inconsistentOffers = consistencyValidationResult.getInconsistentOffers();
            for (OfferWrapper offer : inconsistentOffers) {
              LoginInfo login = offer.getDescriptor().ref.owner();
              Application.login().admin.invalidateLogin(login);
              for (AuthorizationWrapper authorization : authorizations) {
                Application.login().admin.revokeAuthorization(login.entity, authorization.getInterface());
              }
              this.setProgressStatus(25+(35*i/inconsistentOffers.size()));
              i++;
            }
          }
          i = 0;
          for (AuthorizationWrapper authorization : authorizations) {
            RegisteredEntity ref = authorization.getEntityDescriptor().ref;
            String interfaceName = authorization.getInterface();
            ref.revokeInterface(interfaceName);

            this.setProgressStatus(60+(40*i/authorizations.size()));
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
