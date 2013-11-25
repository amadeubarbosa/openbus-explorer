package busexplorer.action.logins;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.LoginInfoWrapper;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class LoginRefreshAction extends BusAdminAbstractAction {

  public LoginRefreshAction(JFrame parentWindow, JTable table, BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("LoginRefreshAction.name"));
  }

  @Override
  public CRUDActionType crudActionType() {
    return CRUDActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<LoginInfo> logins = null;

      @Override
      protected void performTask() throws Exception {
        logins = admin.getLogins();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
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
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }
}
