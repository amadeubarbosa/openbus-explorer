package admin.action.logins;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
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
public class LoginRefreshAction extends BusAdminAbstractAction {

  public LoginRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("LoginRefreshAction.name"));
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
      List<LoginInfo> logins;

      @Override
      public void performTask() throws Exception {
        logins = admin.getLogins();
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
          List<LoginInfoWrapper> wrappersList =
            new LinkedList<LoginInfoWrapper>();

          for (LoginInfo loginInfo : logins) {
            wrappersList.add(new LoginInfoWrapper(loginInfo));
          }

          ObjectTableModel<LoginInfoWrapper> m =
            new ModifiableObjectTableModel<LoginInfoWrapper>(wrappersList,
              new LoginTableProvider());

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