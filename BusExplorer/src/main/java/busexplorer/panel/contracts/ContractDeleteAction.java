package busexplorer.panel.contracts;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;

import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import busexplorer.Application;
import busexplorer.desktop.dialog.ConsistencyValidationDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;

/**
 * Classe de a��o para a remo��o de uma entidade.
 *
 * @author Tecgraf
 */
public class ContractDeleteAction extends OpenBusAction<ContractWrapper> {

  /**
   * Construtor da a��o.
   *  @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   *
   */
  public ContractDeleteAction(Window parentWindow) {
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
    Collection<ContractWrapper> contracts = getTablePanelComponent().getSelectedElements();

    BusExplorerTask<Void> deleteContractTask =
      DeleteContractTask(contracts, getTablePanelComponent()::removeSelectedElements, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteContractTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (ExecuteAllDependencyCheckTasks(parentWindow, contracts, consistencyValidationResult)) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

  public static boolean ExecuteAllDependencyCheckTasks(Window parentWindow,
                                                       Collection<ContractWrapper> contracts,
                                                       ConsistencyValidationResult consistencyValidationResult) {

    String title = Language.get(ConsistencyValidationDialog.class, "waiting.dependency.title");
    String waitingMessage = Language.get(ContractDeleteAction.class, "waiting.dependency.msg");

    BusExplorerTask extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(contracts, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    return extensionDependencyCheckTask.getStatus() &&
      ProviderDeleteAction
      .ExecuteAllDependencyCheckTasks(parentWindow,
        consistencyValidationResult.getInconsistentProviders().values(), consistencyValidationResult);
  }

  public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<ContractWrapper> contracts,
                                                                   ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        for (ContractWrapper contractToRemove : contracts) {
          String contractBeingRemovedName = contractToRemove.name();
          for (Integration integration : Application.login().extension.getIntegrationRegistry().integrations()) {
            for (Contract contract : integration.contracts()) {
              int integrationId = integration.id();
              if (contract.name().equals(contractBeingRemovedName)
                && !consistencyValidationResult.getInconsistentIntegrations().containsKey(integrationId)) {
                consistencyValidationResult.getInconsistentIntegrations().put(integrationId, new IntegrationWrapper(integration));
              }
            }
          }
          for (Provider provider : Application.login().extension.getProviderRegistry().providers()) {
            String providerName = provider.name();
            for (Contract contract : provider.contracts()) {
              if (contract.name().equals(contractBeingRemovedName)
                && !consistencyValidationResult.getInconsistentProviders().containsKey(providerName)) {
                consistencyValidationResult.getInconsistentProviders().put(providerName, new ProviderWrapper(provider));
              }
            }
          }
          this.setProgressStatus(100*i/contracts.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> DeleteContractTask(Collection<ContractWrapper> contracts, Runnable delegateAfterTaskUI,
                                                         ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                         ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.Service) {
      private final String removeDependenciesTitle =
        Language.get(ContractDeleteAction.class, "waiting.removing.dependencies.title");
      private final String removeDependenciesMessage =
        Language.get(ContractDeleteAction.class, "waiting.removing.dependencies.msg");
      @Override
      protected void doPerformTask() throws Exception {
        if (removeFlags.isFullyGovernanceRemoval()) {
          ProviderDeleteAction.DeleteProviderTask(consistencyValidationResult
            .getInconsistentProviders().values(), null, removeFlags, consistencyValidationResult)
            .execute(parentWindow, removeDependenciesTitle, removeDependenciesMessage, 2, 0, true, false);
        }
        int i = 0;
        for (ContractWrapper contract : contracts) {
          Application.login().extension.getContractRegistry().remove(contract.name());
          this.setProgressStatus(100*i/contracts.size());
          i++;
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus() && (delegateAfterTaskUI != null)) {
          delegateAfterTaskUI.run();
        }
      }
    };
  }
}
