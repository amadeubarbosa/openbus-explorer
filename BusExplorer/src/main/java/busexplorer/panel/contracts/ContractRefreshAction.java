package busexplorer.panel.contracts;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

public class ContractRefreshAction extends OpenBusAction<ContractWrapper> {

  public ContractRefreshAction(JFrame parentWindow) {
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
    BusExplorerTask<List<ContractWrapper>> task =
      new BusExplorerTask<List<ContractWrapper>>(ExceptionContext.Service) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(ContractWrapper.convertToInfo(Application.login().extension.getContracts()));
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
