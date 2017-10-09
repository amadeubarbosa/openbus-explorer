package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de a��o para editar um administrador. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class AdminEditAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor da a��o.
   *  @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   *
   */
  public AdminEditAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.EDIT;
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
  public void actionPerformed(ActionEvent arg0) {
    AdminInputDialog dialog = new AdminInputDialog(parentWindow,
      getTablePanelComponent());
    dialog.showDialog();
    AdminWrapper adminWrapper = getTablePanelComponent().getSelectedElement();
    dialog.setEditionMode(adminWrapper);
  }

}
