package busexplorer.panel.logins;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.wrapper.LoginInfoInfo;
import exception.handling.ExceptionContext;

/**
 * A��o que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class LoginDeleteAction extends OpenBusAction<LoginInfoInfo> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin
   */
  public LoginDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(LoginDeleteAction.class.getSimpleName() +
      ".name"));
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
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    Task<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {
      @Override
      protected void performTask() throws Exception {
        LoginInfoInfo login = getPanelComponent().getSelectedElement();
        admin.invalidateLogin(login.getInfo());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}