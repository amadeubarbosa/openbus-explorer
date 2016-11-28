package busexplorer.panel.interfaces;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.core.lng.LNG;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de a��o para criar uma interface. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class InterfaceAddAction extends OpenBusAction<InterfaceWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
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
