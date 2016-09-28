package busexplorer.panel.configuration.admins;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.LNG;

import javax.swing.*;
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
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public AdminEditAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(AdminEditAction.class.getSimpleName() +
      ".name"));
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
      getPanelComponent(), admin);
    dialog.showDialog();
    AdminWrapper adminWrapper = getPanelComponent().getSelectedElement();
    dialog.setEditionMode(adminWrapper);
  }

}
