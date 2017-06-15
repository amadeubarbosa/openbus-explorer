package busexplorer.panel.providers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.services.governance.v1_0.Integration;

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
public class ProviderDeleteAction extends OpenBusAction<ProviderWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ProviderDeleteAction(JFrame parentWindow) {
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

    BusExplorerTask<Void> removeProviderTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          List<ProviderWrapper> providers = getTablePanelComponent().getSelectedElements();
          for (ProviderWrapper provider : providers) {
            Application.login().extension.getProviderRegistry().remove(provider.name());
          }
          if (shouldRemoveDependents[0]) {
            for (Integer id : integrationsAffected.keySet()) {
              Application.login().extension.getIntegrationRegistry().remove(id);
            }
          }
          this.setProgressStatus(100*i/providers.size());
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
          List<ProviderWrapper> providers = getTablePanelComponent().getSelectedElements();
          int i = 0;
          for (ProviderWrapper provider : providers) {
            String providerName = provider.remote().name();
            for (Integration integration : Application.login().extension.getIntegrationRegistry().integrations()) {
              if (integration.provider().name().equals(providerName)) {
                integrationsAffected.put(integration.id(), IntegrationWrapper.describe(integration));
              }
            }
            this.setProgressStatus(100*i/providers.size());
            i++;
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            if (integrationsAffected.isEmpty()) {
              removeProviderTask.execute(parentWindow, getString("waiting.title"),
                getString("waiting.msg"), 2, 0, true, false);
            } else {
              BusExplorerAbstractInputDialog confirmation =
                new BusExplorerAbstractInputDialog(parentWindow,
                  getString("confirm.title")) {

                @Override
                protected JPanel buildFields() {
                  setMinimumSize(new Dimension(500, 320));
                  JPanel panel = new JPanel(new MigLayout("fill, ins 0, flowy"));
                  panel.add(new JLabel(
                    ProviderDeleteAction.this.getString("affected.integrations")), "grow");
                  panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
                  panel.add(new JLabel(Language.get(MainDialog.class, "integration.title")), "grow");

                  JList<String> affectedList = new JList<>(
                    integrationsAffected.values()
                      .toArray(new String[integrationsAffected.size()]));
                  affectedList.setFocusable(false);
                  affectedList.setVisibleRowCount(4);
                  affectedList.setBackground(getContentPane().getBackground());

                  JScrollPane pane = new JScrollPane(affectedList);
                  panel.add(pane, "grow, pad 0 10 0 -10");

                  panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

                  JPanel checkBoxesGroup = new JPanel(new MigLayout("fill, ins 0, flowx"));

                  JCheckBox contractValidationBox = new JCheckBox();
                  contractValidationBox.setSelected(false);
                  contractValidationBox.addChangeListener(el ->
                    shouldRemoveDependents[0] = !shouldRemoveDependents[0]);
                  checkBoxesGroup.add(contractValidationBox);

                  JLabel contractValidationLabel =
                    new JLabel(Language.get(ProviderDeleteAction.class, "affected.integrations.removal"));
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
                  removeProviderTask.execute(parentWindow, getString("waiting.title"),
                      getString("waiting.msg"), 2, 0);
                  return removeProviderTask.getStatus();
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
