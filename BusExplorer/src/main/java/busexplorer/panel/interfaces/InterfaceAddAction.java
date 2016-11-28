package busexplorer.panel.interfaces;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.core.lng.LNG;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class InterfaceAddAction extends OpenBusAction<InterfaceWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public InterfaceAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(InterfaceAddAction.class.getSimpleName()
      + ".name"));
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
    new InterfaceInputDialog(parentWindow, getTablePanelComponent(),
      admin).showDialog();
  }

}
