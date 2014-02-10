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
public class CategoryEditAction extends OpenBusAction<CategoryWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public CategoryEditAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(CategoryEditAction.class.getSimpleName() +
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
  public void actionPerformed(ActionEvent arg0) {
    CategoryInputDialog dialog = new CategoryInputDialog(parentWindow,
      getPanelComponent(), admin);
    dialog.showDialog();
    CategoryWrapper category = getPanelComponent().getSelectedElement();
    dialog.setEditionMode(category);
  }

}
