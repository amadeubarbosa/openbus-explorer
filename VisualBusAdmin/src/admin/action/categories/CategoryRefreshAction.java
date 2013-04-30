package admin.action.categories;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.EntityCategoryDescWrapper;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CategoryRefreshAction extends BusAdminAbstractAction {
  public CategoryRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("CategoryRefreshAction.name"));
  }

  @Override
  public int crudActionType() {
    return TYPE_ACTION_OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    new SimpleWindowRemoteTask(parentWindow, LNG
      .get("ListAction.waiting.title"), LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {
      List<EntityCategoryDesc> categories = null;

      @Override
      public void performTask() throws Exception {
        categories = admin.getCategories();
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
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
        else {
          JOptionPane.showMessageDialog(parentWindow, getTaskException()
            .getMessage(), LNG.get("ProgressDialog.error.title"),
            JOptionPane.ERROR_MESSAGE);
        }
      }

    }.start();
  }

}