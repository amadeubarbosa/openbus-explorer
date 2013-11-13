package busexplorer.action.categories;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.EntityCategoryDescWrapper;
import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import admin.BusAdmin;

/**
 * Classe de ação para criar uma categoria. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CategoryAddAction extends BusAdminAbstractAction {

  private CRUDPanel<EntityCategoryDescWrapper> panel;

  public CategoryAddAction(JFrame parentWindow,
    CRUDPanel<EntityCategoryDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CategoryAddAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_ADD;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    new CategoryInputDialog(parentWindow, LNG
      .get("CategoryAddAction.inputDialog.title"), panel, admin).showDialog();
  }

}
