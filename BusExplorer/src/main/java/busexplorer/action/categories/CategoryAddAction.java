package busexplorer.action.categories;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.EntityCategoryDescWrapper;

/**
 * Classe de ação para criar uma categoria. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CategoryAddAction extends BusAdminAbstractAction {

  public CategoryAddAction(JFrame parentWindow,
    CRUDPanel<EntityCategoryDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CategoryAddAction.name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CRUDActionType crudActionType() {
    return CRUDActionType.ADD;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    ObjectTableModel<EntityCategoryDescWrapper> model =
      (ObjectTableModel<EntityCategoryDescWrapper>) this.table.getModel();
    new CategoryInputDialog(parentWindow, LNG
      .get("CategoryAddAction.inputDialog.title"), model, admin).showDialog();
  }

}
