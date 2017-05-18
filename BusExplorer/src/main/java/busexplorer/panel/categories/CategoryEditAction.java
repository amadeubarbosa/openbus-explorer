package busexplorer.panel.categories;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

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
  public CategoryEditAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
    CategoryInputDialog dialog = new CategoryInputDialog(parentWindow,
      getTablePanelComponent(), admin);
    dialog.showDialog();
    CategoryWrapper category = getTablePanelComponent().getSelectedElement();
    dialog.setEditionMode(category);
  }

}
