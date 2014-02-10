package busexplorer.panel.categories;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

/**
 * Classe de ação para criar uma categoria. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CategoryEditAction extends OpenBusAction<CategoryWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
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
