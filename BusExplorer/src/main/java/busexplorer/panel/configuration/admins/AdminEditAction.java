package busexplorer.panel.configuration.admins;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.LNG;

import javax.swing.*;
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
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
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
