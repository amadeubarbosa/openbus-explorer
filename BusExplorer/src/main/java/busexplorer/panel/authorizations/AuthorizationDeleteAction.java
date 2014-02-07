package busexplorer.panel.authorizations;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.wrapper.AuthorizationInfo;
import exception.handling.ExceptionContext;

/**
 * Classe de a��o para a remo��o de uma autoriza��o.
 * 
 * @author Tecgraf
 */
public class AuthorizationDeleteAction extends OpenBusAction<AuthorizationInfo>
  {

  /**
   * Construtor da a��o
   * 
   * @param parentWindow janela m�e do di�logo a ser criado pela a��o
   * @param admin
   */
  public AuthorizationDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(AuthorizationDeleteAction.class.getSimpleName() + ".name"));
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

    Task<Object> task = new
    BusExplorerTask<Object>(Application.exceptionHandler(),
      ExceptionContext.BusCore) {
      @Override
      protected void performTask() throws Exception {
        AuthorizationInfo authorization =
          getPanelComponent().getSelectedElement();

        RegisteredEntity ref = authorization.getEntityDescriptor().ref;
        String interfaceName = authorization.getInterface();

        ref.revokeInterface(interfaceName);
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