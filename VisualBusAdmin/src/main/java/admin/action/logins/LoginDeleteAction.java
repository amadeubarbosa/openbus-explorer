package admin.action.logins;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.LoginInfoWrapper;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class LoginDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de logins */
  private CRUDPanel<LoginInfoWrapper> panel;

  public LoginDeleteAction(JFrame parentWindow,
    CRUDPanel<LoginInfoWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin,
      LNG.get("LoginDeleteAction.name"));
    this.panel = panel;
  }

  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_REMOVE;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<LoginInfoWrapper> selectedWrappers = panel.getSelectedInfos();
        for (LoginInfoWrapper wrapper : selectedWrappers) {
          LoginInfo loginInfo = wrapper.getLoginInfo();
          admin.invalidateLogin(loginInfo);
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
