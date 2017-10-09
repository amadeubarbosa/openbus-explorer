package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de ação para editar um administrador. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AdminEditAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
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
