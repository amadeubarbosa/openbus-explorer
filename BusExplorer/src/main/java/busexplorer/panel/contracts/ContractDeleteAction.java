package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
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
  public ContractDeleteAction(JFrame parentWindow) {
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
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }


    final boolean[] shouldRemoveDependents = {false};
    HashMap<Integer, String> integrationsAffected = new HashMap<>();
    HashMap<String, String> providersAffected = new HashMap<>();

    BusExplorerTask<Void> removeContractTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          List<ContractWrapper> contracts = getTablePanelComponent().getSelectedElements();
          for (ContractWrapper contract : contracts) {
            Application.login().extension.getContractRegistry().remove(contract.name());
          }
          if (shouldRemoveDependents[0]) {
            for (Integer id : integrationsAffected.keySet()) {
              Application.login().extension.getIntegrationRegistry().remove(id);
            }
            for (String name : providersAffected.keySet()) {
              Application.login().extension.getProviderRegistry().remove(name);
            }
          }
          this.setProgressStatus(100*i/contracts.size());
          i++;
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
                if (contract.name().equals(contractBeingRemovedName) && !integrationsAffected.containsKey(integrationId)) {
                  integrationsAffected.put(integrationId, IntegrationWrapper.describe(integration));
                }
              }
            }
            for (Provider provider : Application.login().extension.getProviderRegistry().providers()) {
              String providerName = provider.name();
              for (Contract contract : provider.contracts()) {
                if (contract.name().equals(contractBeingRemovedName) && !providersAffected.containsKey(providerName)) {
                  providersAffected.put(providerName, ProviderWrapper.describe(provider));
                }
              }
            }
            this.setProgressStatus(100*i/elements.size());
            i++;
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            if (integrationsAffected.isEmpty() && providersAffected.isEmpty()) {
              removeContractTask.execute(parentWindow, getString("waiting.title"),
                getString("waiting.msg"), 2, 0, true, false);
            } else {
              BusExplorerAbstractInputDialog confirmation =
                new BusExplorerAbstractInputDialog(parentWindow,
                  getString("confirm.title")) {

                  @Override
                  protected JPanel buildFields() {
                    setMinimumSize(new Dimension(500, 420));
                    JPanel panel = new JPanel(new MigLayout("fill, ins 0, flowy"));
                    panel.add(new JLabel(
                      ContractDeleteAction.this.getString("affected.others")), "grow");
                    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

                    panel.add(new JLabel(Language.get(MainDialog.class, "integration.title")), "grow");

                    JList<String> affectedIntegrationsList = new JList<>(
                      integrationsAffected.values()
                        .toArray(new String[integrationsAffected.size()]));
                    affectedIntegrationsList.setFocusable(false);
                    affectedIntegrationsList.setVisibleRowCount(4);
                    affectedIntegrationsList.setBackground(getContentPane().getBackground());

                    panel.add(new JScrollPane(affectedIntegrationsList), "grow, pad 0 10 0 -10");

                    panel.add(new JLabel(Language.get(MainDialog.class, "extension.provider.title")), "grow");

                    JList<String> affectedProvidersList = new JList<>(
                      providersAffected.values()
                        .toArray(new String[providersAffected.size()]));
                    affectedProvidersList.setFocusable(false);
                    affectedProvidersList.setVisibleRowCount(4);
                    affectedProvidersList.setBackground(getContentPane().getBackground());

                    panel.add(new JScrollPane(affectedProvidersList), "grow, pad 0 10 0 -10");

                    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
                    JPanel checkBoxesGroup = new JPanel(new MigLayout("fill, ins 0, flowx"));

                    JCheckBox contractValidationBox = new JCheckBox();
                    contractValidationBox.setSelected(false);
                    contractValidationBox.addChangeListener(el ->
                      shouldRemoveDependents[0] = !shouldRemoveDependents[0]);
                    checkBoxesGroup.add(contractValidationBox);

                    JLabel contractValidationLabel =
                      new JLabel(Language.get(ContractDeleteAction.class, "affected.others.removal"));
                    checkBoxesGroup.add(contractValidationLabel, "grow");
                    contractValidationLabel.addMouseListener(new MouseAdapter() {
                      @Override
                      public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        contractValidationBox.setSelected(!contractValidationBox.isSelected());
                      }
                    });

                    panel.add(checkBoxesGroup);
                    return panel;
                  }

                  @Override
                  protected boolean accept() {
                    removeContractTask.execute(parentWindow, getString("waiting.title"),
                      getString("waiting.msg"), 2, 0);
                    return removeContractTask.getStatus();
                  }

                  @Override
                  protected boolean hasValidFields() {
                    return true;
                  }
                };
              confirmation.showDialog();
            }
          }
        }
      };

    dependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);
  }
}
