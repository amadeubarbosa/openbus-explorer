package busexplorer.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.AuthorizationWrapper;

/**
 * Classe de ação para criar uma autorização. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AuthorizationAddAction extends BusAdminAbstractAction {

  public AuthorizationAddAction(JFrame parentWindow,
    CRUDPanel<AuthorizationWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("AuthorizationAddAction.name"));
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
    Task task = new Task() {
      List<String> interfacesList = null;
      List<String> entitiesIDList = null;

      @Override
      protected void performTask() throws Exception {
        entitiesIDList = new LinkedList<String>();
        List<RegisteredEntityDesc> entitiesDescList = admin.getEntities();
        interfacesList = admin.getInterfaces();
        for (RegisteredEntityDesc entityDesc : entitiesDescList) {
          entitiesIDList.add(entityDesc.id);
        }
      }

      @Override
      protected void afterTaskUI() {
        ObjectTableModel<AuthorizationWrapper> model =
          (ObjectTableModel<AuthorizationWrapper>) AuthorizationAddAction.this.table
            .getModel();
        new AuthorizationInputDialog(AuthorizationAddAction.this.parentWindow,
          LNG.get("AuthorizationAddAction.inputDialog.title"), model, admin,
          entitiesIDList, interfacesList).showDialog();
      }
    };

    task.execute(parentWindow, LNG.get("AddAction.waiting.title"), LNG
      .get("AddAction.waiting.msg"));
  }
}
