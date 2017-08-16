package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.services.governance.v1_0.Integration;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe de ação para a remoção de uma entidade.
 * 
 * @author Tecgraf
 */
public class ConsumerDeleteAction extends OpenBusAction<ConsumerWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ConsumerDeleteAction(JFrame parentWindow) {
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

    // [0] = flag para remoção das integrações
    final AtomicBoolean shouldRemoveDependents = new AtomicBoolean(false);
    HashMap<Integer, IntegrationWrapper> integrationsAffected = new HashMap<>();
    List<ConsumerWrapper> consumers = getTablePanelComponent().getSelectedElements();

    BusExplorerTask<Void> removeConsumerTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          List<ConsumerWrapper> consumers = getTablePanelComponent().getSelectedElements();
          if (shouldRemoveDependents.get()) {
            for (Integer id : integrationsAffected.keySet()) {
              Application.login().extension.getIntegrationRegistry().remove(id);
            }
          }
          for (ConsumerWrapper consumer : consumers) {
            Application.login().extension.getConsumerRegistry().remove(consumer.name());
            this.setProgressStatus(100*i/consumers.size());
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
          int i = 0;
          for (ConsumerWrapper consumer : consumers) {
            String consumerName = consumer.remote().name();
            for (Integration integration : Application.login().extension.getIntegrationRegistry().integrations()) {
              if (integration.consumer().name().equals(consumerName)) {
                integrationsAffected.put(integration.id(), new IntegrationWrapper(integration));
              }
            }
            this.setProgressStatus(100*i/consumers.size());
            i++;
          }
        }
      };

    dependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    if (dependencyCheckTask.getStatus()) {
      if (integrationsAffected.isEmpty()) {
        removeConsumerTask.execute(parentWindow, getString("waiting.title"),
          getString("waiting.msg"), 2, 0, true, false);
      } else {
        BusExplorerAbstractInputDialog confirmation =
          new ConsistencyValidationDialog(integrationsAffected, shouldRemoveDependents, removeConsumerTask);
        confirmation.showDialog();
      }
    }
  }

  private class ConsistencyValidationDialog extends BusExplorerAbstractInputDialog {

    private final HashMap<Integer, IntegrationWrapper> integrationsAffected;
    private final AtomicBoolean shouldRemoveDependents;
    private final BusExplorerTask<Void> removeConsumerTask;

    public ConsistencyValidationDialog(HashMap<Integer, IntegrationWrapper> integrationsAffected,
                                       AtomicBoolean shouldRemoveDependents, BusExplorerTask<Void> removeConsumerTask) {
      super(parentWindow, ConsumerDeleteAction.this.getString("confirm.title"));
      this.integrationsAffected = integrationsAffected;
      this.shouldRemoveDependents = shouldRemoveDependents;
      this.removeConsumerTask = removeConsumerTask;
    }

    @Override
    protected JPanel buildFields() {

      JPanel panel = new JPanel(new MigLayout("fill, flowy"));
      panel.add(new JLabel(
        ConsumerDeleteAction.this.getString("consistency.message")), "grow");
      panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
      panel.add(new JLabel(Language.get(MainDialog.class, "integration.title")), "grow");

      ObjectTableModel<IntegrationWrapper> model = new ObjectTableModel<>(
        new ArrayList<>(integrationsAffected.values()), new IntegrationTableProvider());
      RefreshablePanel pane = new TablePanelComponent<>(model, new ArrayList<>(), false);
      pane.setPreferredSize(new Dimension(100, 150));
      panel.add(pane, "grow, push, pad 0 10 0 -10");

      panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
      panel.add(new JLabel(ConsumerDeleteAction.this.getString("options.label")), "grow");

      JRadioButton removeDependenciesOption = new JRadioButton(
        Language.get(ConsumerDeleteAction.class, "consistency.remove.dependencies"));
      removeDependenciesOption.setSelected(false);
      removeDependenciesOption.addItemListener(itemEvent -> {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
          shouldRemoveDependents.set(true);
        }
      });

      JRadioButton removeIndexesOnlyOption = new JRadioButton(
        Language.get(ConsumerDeleteAction.class, "consistency.remove.indexesonly"));
      removeIndexesOnlyOption.setSelected(true);
      removeIndexesOnlyOption.addItemListener(itemEvent -> {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
          shouldRemoveDependents.set(false);
        }
      });

      ButtonGroup group = new ButtonGroup();
      group.add(removeDependenciesOption);
      group.add(removeIndexesOnlyOption);

      panel.add(removeDependenciesOption, "grow");
      panel.add(removeIndexesOnlyOption, "grow");

      return panel;
    }

    @Override
    protected boolean accept() {
      removeConsumerTask.execute(parentWindow, getString("waiting.title"),
        getString("waiting.msg"), 2, 0);
      return true;
    }

    @Override
    protected boolean hasValidFields() {
      return true;
    }
  }
}
