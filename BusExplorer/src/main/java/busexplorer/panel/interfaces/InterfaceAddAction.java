package busexplorer.panel.interfaces;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

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
   *  @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   *
   */
  public InterfaceAddAction(JFrame parentWindow) {
    super(parentWindow);
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
    new InterfaceInputDialog(parentWindow, getTablePanelComponent()
    ).showDialog();
  }

}
