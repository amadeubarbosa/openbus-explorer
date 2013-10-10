package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
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
   public AuthorizationDeleteAction(JFrame parentWindow,
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
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<AuthorizationWrapper> selectedWrappers = panel.getSelectedInfos();
        for (AuthorizationWrapper wrapper : selectedWrappers) {
          String entityID = wrapper.getEntity().id;
          String interfaceName = wrapper.getInterface();
          admin.revokeAuthorization(entityID, interfaceName);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          panel.removeSelectedInfos();
          panel.getTableModel().fireTableDataChanged();
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"));
  }   
}
