package busexplorer.panel.interfaces;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.wrapper.InterfaceInfo;

/**
 * Classe de a��o para criar uma interface. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class InterfaceAddAction extends OpenBusAction<InterfaceInfo> {

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
  public void actionPerformed(ActionEvent arg0) {
    new InterfaceInputDialog(parentWindow, getPanelComponent(),
      admin).showDialog();
  }

}