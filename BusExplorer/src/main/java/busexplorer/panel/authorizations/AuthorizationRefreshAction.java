package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.admin.BusAdminFacade;

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
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public AuthorizationRefreshAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
    BusExplorerTask<List<AuthorizationWrapper>> task =
      new BusExplorerTask<List<AuthorizationWrapper>>(
        Application.exceptionHandler(), ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(AuthorizationWrapper.convertToInfo(admin.getAuthorizations()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
