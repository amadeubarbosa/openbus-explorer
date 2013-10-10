package admin.action.authorizations;

import java.awt.event.ActionEvent;
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
 * Classe de ação para a remoção de uma autorização.
 *
 * @author Tecgraf
 */
public class AuthorizationDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de autorizações */
  private CRUDPanel<AuthorizationWrapper> panel;

  /**
   * Construtor da ação
   *
   * @param parentWindow janela mãe do diálogo a ser criado pela ação
   * @param panel painel de CRUD
   * @param admin
   */
   public AuthorizationDeleteAction(SimpleWindow parentWindow,
     CRUDPanel<AuthorizationWrapper> panel, BusAdmin admin) {
     super(parentWindow, panel.getTable(), admin,
       LNG.get("AuthorizationDeleteAction.name"));
     this.panel = panel;
   }

  /**
   * {@inheritDoc}
   */
  @Override  
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    new SimpleWindowRemoteTask(parentWindow,
      LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      public void performTask() throws Exception {
        List<AuthorizationWrapper> selectedWrappers = panel.getSelectedInfos();
        for (AuthorizationWrapper wrapper : selectedWrappers) {
          String entityID = wrapper.getEntity().id;
          String interfaceName = wrapper.getInterface();
          admin.revokeAuthorization(entityID, interfaceName);
        }
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
          panel.removeSelectedInfos();
          panel.getTableModel().fireTableDataChanged();
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
