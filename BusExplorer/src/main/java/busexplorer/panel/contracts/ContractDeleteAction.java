package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
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
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;

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

    final busexplorer.desktop.dialog.ConsistencyValidationDialog.DeleteOptions removeFlags = new busexplorer.desktop.dialog.ConsistencyValidationDialog.DeleteOptions();
    ConsistencyValidationResult consistencyValidationResult = new ConsistencyValidationResult();

    BusExplorerTask<Void> removeContractTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          if (removeFlags.isFullyGovernanceRemoval()) {
            Task deleteDependencies = ProviderDeleteAction.DeleteProviderTask(consistencyValidationResult.getInconsistentProviders().values(),
              null, removeFlags, consistencyValidationResult);
            deleteDependencies.execute(parentWindow, getString("waiting.removing.dependencies.title"),
              getString("waiting.removing.dependencies.msg"), 2, 0, true, false);
          }
          int i = 0;
          List<ContractWrapper> contracts = getTablePanelComponent().getSelectedElements();
          for (ContractWrapper contract : contracts) {
            Application.login().extension.getContractRegistry().remove(contract.name());
            this.setProgressStatus(100*i/contracts.size());
            i++;
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().removeSelectedElements();
          }
        }
      };

    BusExplorerTask<Void> dependencyCheckTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {
        @Override
        protected void doPerformTask() throws Exception {
          setProgressDialogEnabled(true);
          List<ContractWrapper> elements = getTablePanelComponent().getSelectedElements();
          int i = 0;
          for (ContractWrapper contractToRemove : elements) {
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
            this.setProgressStatus(100*i/elements.size());
            i++;
          }
        }
      };

    dependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    if (dependencyCheckTask.getStatus()) {
      if (consistencyValidationResult.isEmpty()) {
        removeContractTask.execute(parentWindow, getString("waiting.title"),
          getString("waiting.msg"), 2, 0, true, false);
      } else {
        BusExplorerAbstractInputDialog confirmation =
          new ConsistencyValidationDialog(consistencyValidationResult, removeFlags, removeContractTask);
        confirmation.showDialog();
      }
    }
  }

  private class ConsistencyValidationDialog extends BusExplorerAbstractInputDialog {

    private final busexplorer.desktop.dialog.ConsistencyValidationDialog.DeleteOptions removeFlags;
    private final BusExplorerTask<Void> removeContractTask;
    private final ConsistencyValidationResult consistencyValidationResult;

    public ConsistencyValidationDialog(ConsistencyValidationResult consistencyValidationResult,
                                       busexplorer.desktop.dialog.ConsistencyValidationDialog.DeleteOptions removeFlags,
                                       BusExplorerTask<Void> removeContractTask) {
      super(parentWindow, ContractDeleteAction.this.getString("confirm.title"));
      this.consistencyValidationResult = consistencyValidationResult;
      this.removeFlags = removeFlags;
      this.removeContractTask = removeContractTask;
    }

    @Override
    protected JPanel buildFields() {
      setMinimumSize(new Dimension(500, 420));
      setPreferredSize(new Dimension(750, 420));
      JPanel panel = new JPanel(new MigLayout("fill, flowy"));
      panel.add(new JLabel(
        ContractDeleteAction.this.getString("consistency.message")), "grow");
      panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

      busexplorer.desktop.dialog.ConsistencyValidationDialog
        .addAllExtensionCheckList(panel, consistencyValidationResult);

      busexplorer.desktop.dialog.ConsistencyValidationDialog
        .addAllGovernanceCheckList(panel, consistencyValidationResult);

      panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
      panel.add(new JLabel(ContractDeleteAction.this.getString("options.label")), "grow");

      JRadioButton removeDependenciesOption = new JRadioButton(
        Language.get(ContractDeleteAction.class, "consistency.remove.dependencies"));
      removeDependenciesOption.setSelected(false);
      removeDependenciesOption.addItemListener(removeFlags.fullyGovernanceRemovalChangeListener);

      JRadioButton removeIndexesOnlyOption = new JRadioButton(
        Language.get(ContractDeleteAction.class, "consistency.remove.indexesonly"));
      removeIndexesOnlyOption.setSelected(true);

      ButtonGroup group = new ButtonGroup();
      group.add(removeDependenciesOption);
      group.add(removeIndexesOnlyOption);

      panel.add(removeDependenciesOption, "grow");
      panel.add(removeIndexesOnlyOption, "grow");

      return panel;
    }

    @Override
    protected boolean accept() {
      removeContractTask.execute(parentWindow, getString("waiting.title"),
        getString("waiting.msg"), 2, 0);
      return true;
    }

    @Override
    protected boolean hasValidFields() {
      return true;
    }
  }
}
