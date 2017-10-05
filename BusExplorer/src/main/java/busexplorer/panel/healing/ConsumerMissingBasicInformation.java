package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.consumers.ConsumerRefreshAction;
import busexplorer.panel.consumers.ConsumerTableProvider;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderEditAction;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.services.governance.v1_0.Consumer;

public class ConsumerMissingBasicInformation extends ConsumerRefreshAction {

  public ConsumerMissingBasicInformation(JFrame parentWindow) {
    super(parentWindow);
  }

  protected TablePanelComponent<ConsumerWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new ProviderDeleteAction(parentWindow));
      actions.add(new ProviderEditAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new ConsumerTableProvider()),
        actions, false, false));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<ConsumerWrapper>> task =
      new BusExplorerTask<List<ConsumerWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          ArrayList<Consumer> result = new ArrayList<>();
          for (Consumer consumer : Application.login().extension.getConsumers()) {
            if (consumer.code().isEmpty() || consumer.manageroffice().isEmpty() || consumer.supportoffice().isEmpty()
              || consumer.manager().length == 0 || consumer.support().length == 0) {
              result.add(consumer);
            }
          }
          setResult(ConsumerWrapper.convertToInfo(result));
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().setElements(getResult());
          }
        }
      };

    task.execute(parentWindow, Language.get(this.getClass().getSuperclass(), "waiting.title"),
      Language.get(this.getClass().getSuperclass(), "waiting.msg"), 2, 0);
  }
}