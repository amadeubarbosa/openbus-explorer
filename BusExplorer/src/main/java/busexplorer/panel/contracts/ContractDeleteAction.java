package busexplorer.panel.contracts;

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
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Classe de ação para a remoção de uma entidade.
 *
 * @author Tecgraf
 */
public class ContractDeleteAction extends OpenBusAction<ContractWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
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

    BusExplorerTask<Void> extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(contracts, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    Task providerDependencyCheckTask = ProviderDeleteAction
      .ExtensionDependencyCheckTask(consistencyValidationResult.getInconsistentProviders().values(),
        consistencyValidationResult);
    providerDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    Task providerDependencyGovernanceCheckTask = ProviderDeleteAction
      .GovernanceDependencyCheckTask(consistencyValidationResult.getInconsistentProviders().values(),
        consistencyValidationResult);
    providerDependencyGovernanceCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    Runnable effectiveDeletion = () -> deleteContractTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (extensionDependencyCheckTask.getStatus() && providerDependencyCheckTask.getStatus()
      && providerDependencyGovernanceCheckTask.getStatus()) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

  public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<ContractWrapper> contracts,
                                                                   ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
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
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (removeFlags.isFullyGovernanceRemoval()) {
          Task deleteDependencies = ProviderDeleteAction.DeleteProviderTask(consistencyValidationResult.getInconsistentProviders().values(),
            null, removeFlags, consistencyValidationResult);
          deleteDependencies.execute(parentWindow, Language.get(ContractDeleteAction.class, "waiting.removing.dependencies.title"),
            Language.get(ContractDeleteAction.class, "waiting.removing.dependencies.msg"), 2, 0, true, false);
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
