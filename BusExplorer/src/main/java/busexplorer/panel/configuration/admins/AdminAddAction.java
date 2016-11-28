package busexplorer.panel.configuration.admins;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.core.lng.LNG;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de ação para adicionar um administrador. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AdminAddAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public AdminAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(AdminAddAction.class.getSimpleName() +
      ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.ADD;
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
    new AdminInputDialog(parentWindow, getTablePanelComponent(),
      admin).showDialog();
  }

}
