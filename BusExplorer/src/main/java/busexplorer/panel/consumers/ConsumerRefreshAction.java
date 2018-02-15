package busexplorer.panel.consumers;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

public class ConsumerRefreshAction extends OpenBusAction<ConsumerWrapper> {

  public ConsumerRefreshAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<ConsumerWrapper>> task =
      new BusExplorerTask<List<ConsumerWrapper>>(ExceptionContext.Service) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(ConsumerWrapper.convertToInfo(Arrays.asList(
          Application.login().extension.getConsumerRegistry().consumers())));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
