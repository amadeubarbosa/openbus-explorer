package admin.action.logins;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
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

  public LoginDeleteAction(SimpleWindow parentWindow,
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
    new SimpleWindowRemoteTask(parentWindow, LNG
      .get("ListAction.waiting.title"), LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      public void performTask() throws Exception {
        List<LoginInfoWrapper> selectedWrappers = panel.getSelectedInfos();
        for (LoginInfoWrapper wrapper : selectedWrappers) {
          LoginInfo loginInfo = wrapper.getLoginInfo();
          admin.invalidateLogin(loginInfo);
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
