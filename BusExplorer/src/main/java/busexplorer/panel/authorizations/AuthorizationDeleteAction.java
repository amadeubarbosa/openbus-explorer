package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de a��o para a remo��o de uma autoriza��o.
 * 
 * @author Tecgraf
 */
public class AuthorizationDeleteAction extends OpenBusAction<AuthorizationWrapper>
  {

  /**
   * Construtor da a��o
   * 
   * @param parentWindow janela m�e do di�logo a ser criado pela a��o
   * @param admin inst�ncia do busadmin
   */
  public AuthorizationDeleteAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
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

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        List<AuthorizationWrapper> authorizations =
          getTablePanelComponent().getSelectedElements();

        for (AuthorizationWrapper authorization : authorizations) {
          RegisteredEntity ref = authorization.getEntityDescriptor().ref;
          String interfaceName = authorization.getInterface();

          ref.revokeInterface(interfaceName);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
