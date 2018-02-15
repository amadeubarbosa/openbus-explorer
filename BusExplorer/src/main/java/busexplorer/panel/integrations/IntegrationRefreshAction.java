package busexplorer.panel.integrations;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

/**
 * Ação que atualiza a tabela de entidades
 * 
 * @author Tecgraf
 * 
 */
public class IntegrationRefreshAction extends OpenBusAction<IntegrationWrapper> {

  /**
   * Construtor.
   *  @param parentWindow janela pai.
   *
   */
  public IntegrationRefreshAction(JFrame parentWindow) {
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
    BusExplorerTask<List<IntegrationWrapper>> task =
      new BusExplorerTask<List<IntegrationWrapper>>(ExceptionContext.Service) {

        @Override
        protected void doPerformTask() throws Exception {
          setResult(IntegrationWrapper.convertToInfo(Application.login().extension.getIntegrations()));
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
