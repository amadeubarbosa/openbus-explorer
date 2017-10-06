package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.consumers.ConsumerDeleteAction;
import busexplorer.panel.consumers.ConsumerEditAction;
import busexplorer.panel.consumers.ConsumerRefreshAction;
import busexplorer.panel.consumers.ConsumerTableProvider;
import busexplorer.panel.consumers.ConsumerWrapper;
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
      actions.add(new ConsumerDeleteAction((JFrame) parentWindow));
      actions.add(new ConsumerEditAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new ConsumerTableProvider()),
        actions, false, false));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    getTablePanelComponent().getElements().clear();
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<ConsumerWrapper>> task =
      new BusExplorerTask<List<ConsumerWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          ArrayList<Consumer> result = new ArrayList<>();
          List<Consumer> consumers = Application.login().extension.getConsumers();
          for (Consumer consumer : consumers) {
            if (consumer.code().isEmpty() || consumer.manageroffice().isEmpty() || consumer.supportoffice().isEmpty()
              || consumer.manager().length == 0 || consumer.support().length == 0) {
              result.add(consumer);
            }
            setProgressStatus(100*i/consumers.size());
            i++;
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
      Language.get(this.getClass().getSuperclass(), "waiting.msg"), 2, 0, true, false);
  }
}
