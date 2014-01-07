package busexplorer.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.AuthorizationInfo;

/**
 * Ação que atualiza a tabela de autorizações
 * 
 * @author Tecgraf
 * 
 */
public class AuthorizationRefreshAction extends
  OpenBusAction<AuthorizationInfo> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public AuthorizationRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(AuthorizationRefreshAction.class.getSimpleName() + ".name"));
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
    Task<List<AuthorizationInfo>> task = new Task<List<AuthorizationInfo>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(AuthorizationInfo.convertToInfo(admin.getAuthorizations()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
