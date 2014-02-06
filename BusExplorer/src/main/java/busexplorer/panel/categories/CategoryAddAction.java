package busexplorer.panel.categories;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.wrapper.CategoryInfo;

/**
 * Classe de ação para criar uma categoria. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CategoryAddAction extends OpenBusAction<CategoryInfo> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
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
