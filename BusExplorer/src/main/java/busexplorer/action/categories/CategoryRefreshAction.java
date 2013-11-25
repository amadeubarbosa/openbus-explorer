package busexplorer.action.categories;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.EntityCategoryDescWrapper;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CategoryRefreshAction extends BusAdminAbstractAction {
  public CategoryRefreshAction(JFrame parentWindow, JTable table, BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("CategoryRefreshAction.name"));
  }

  @Override
  public CRUDActionType crudActionType() {
    return CRUDActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<EntityCategoryDesc> categories = null;

      @Override
      protected void performTask() throws Exception {
        categories = admin.getCategories();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<EntityCategoryDescWrapper> wrappersList =
            new LinkedList<EntityCategoryDescWrapper>();

          for (EntityCategoryDesc category : categories) {
            wrappersList.add(new EntityCategoryDescWrapper(category));
          }

          ObjectTableModel<EntityCategoryDescWrapper> m =
            new ModifiableObjectTableModel<EntityCategoryDescWrapper>(
              wrappersList, new CategoryTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }

}
