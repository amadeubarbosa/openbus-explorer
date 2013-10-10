package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.wrapper.AuthorizationWrapper;

/**
 * Classe de ação para criar uma autorização. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AuthorizationAddAction extends BusAdminAbstractAction {

  private CRUDPanel<AuthorizationWrapper> panel;

  public AuthorizationAddAction(SimpleWindow parentWindow,
    CRUDPanel<AuthorizationWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("AuthorizationAddAction.name"));
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
        new AuthorizationInputDialog(AuthorizationAddAction.this.parentWindow,
          LNG.get("AuthorizationAddAction.inputDialog.title"),
          panel, admin, entitiesIDList, interfacesList).showDialog();
      }
    };

    task.execute(parentWindow, LNG.get("AddAction.waiting.title"),
      LNG.get("AddAction.waiting.msg"));
  }
}
