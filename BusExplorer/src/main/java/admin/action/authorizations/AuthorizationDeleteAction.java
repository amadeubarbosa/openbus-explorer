package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
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
    super(parentWindow, panel.getTable(), admin, LNG
      .get("AuthorizationDeleteAction.name"));
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
    int option =
      JOptionPane.showConfirmDialog(parentWindow, LNG
        .get("DeleteAction.confirm.msg"),
        LNG.get("DeleteAction.confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

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

    task.execute(parentWindow, LNG.get("DeleteAction.waiting.title"), LNG
      .get("DeleteAction.waiting.msg"));
  }
}
