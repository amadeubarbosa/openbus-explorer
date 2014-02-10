package busexplorer.panel.categories;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

/**
 * Classe de a��o para criar uma categoria. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class CategoryAddAction extends OpenBusAction<CategoryWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public CategoryAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(CategoryAddAction.class.getSimpleName() +
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
  public void actionPerformed(ActionEvent arg0) {
    new CategoryInputDialog(parentWindow, getPanelComponent(),
      admin).showDialog();
  }

}
