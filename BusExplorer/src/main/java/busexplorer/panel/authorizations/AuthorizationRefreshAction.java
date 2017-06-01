package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ação que atualiza a tabela de autorizações
 * 
 * @author Tecgraf
 * 
 */
public class AuthorizationRefreshAction extends
  OpenBusAction<AuthorizationWrapper> {

  /**
   * Construtor.
   *  @param parentWindow janela pai.
   *
   */
  public AuthorizationRefreshAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   *  {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   *  {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<AuthorizationWrapper>> task = new BusExplorerTask<List<AuthorizationWrapper>>(
      ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(AuthorizationWrapper.convertToInfo(Application.login().admin.getAuthorizations()));
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
