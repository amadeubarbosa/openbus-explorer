package admin.action.entities;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.RegisteredEntityDescWrapper;

/**
 * A��o que atualiza a tabela de entidades
 * 
 * @author Tecgraf
 * 
 */
public class EntityRefreshAction extends BusAdminAbstractAction {

  public EntityRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("EntityRefreshAction.name"));
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
      List<RegisteredEntityDesc> entities = null;

      @Override
      public void performTask() throws Exception {
        entities = admin.getEntities();
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
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
        else {
          JOptionPane.showMessageDialog(parentWindow, getTaskException()
            .getMessage(), LNG.get("ProgressDialog.error.title"),
            JOptionPane.ERROR_MESSAGE);
        }
      }

    }.start();
  }

}