package busexplorer.action.entities;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.RegisteredEntityDescWrapper;
import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;

/**
 * Ação que atualiza a tabela de entidades
 * 
 * @author Tecgraf
 * 
 */
public class EntityRefreshAction extends BusAdminAbstractAction {

  public EntityRefreshAction(JFrame parentWindow, JTable table, BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("EntityRefreshAction.name"));
  }

  @Override
  public int crudActionType() {
    return TYPE_ACTION_OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<RegisteredEntityDesc> entities = null;

      @Override
      protected void performTask() throws Exception {
        entities = admin.getEntities();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<RegisteredEntityDescWrapper> wrappersList =
            new LinkedList<RegisteredEntityDescWrapper>();

          for (RegisteredEntityDesc entity : entities) {
            wrappersList.add(new RegisteredEntityDescWrapper(entity,
              entity.category.id()));
          }

          ObjectTableModel<RegisteredEntityDescWrapper> m =
            new ModifiableObjectTableModel<RegisteredEntityDescWrapper>(
              wrappersList, new EntityTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }

}
