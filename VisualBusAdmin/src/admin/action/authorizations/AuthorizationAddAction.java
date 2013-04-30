package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.AuthorizationWrapper;

/**
 * Classe de ação para criar uma autorização. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AuthorizationAddAction extends BusAdminAbstractAction {

  private CRUDPanel<AuthorizationWrapper> panel;

  private List<String> interfacesList;
  private List<String> entitiesIDList;

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
    entitiesIDList = new LinkedList<String>();

    new SimpleWindowRemoteTask(parentWindow,
      LNG.get("AddAction.waiting.title"), LNG.get("AddAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      protected void performTask() throws Exception {
        List<RegisteredEntityDesc> entitiesDescList = admin.getEntities();

        interfacesList = admin.getInterfaces();

        for (RegisteredEntityDesc entityDesc : entitiesDescList) {
          entitiesIDList.add(entityDesc.id);
        }

      }

      @Override
      protected void updateUI() {

        if (hasNoException()) {

          new AuthorizationInputDialog(parentWindow, LNG
            .get("AuthorizationAddAction.inputDialog.title"),
            new SimpleWindowBlockType(Type.BLOCK_THIS), panel, admin,
            entitiesIDList, interfacesList).showDialog();
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
