package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.AuthorizationWrapper;

/**
 * Ação que atualiza a tabela de autorizações
 * 
 * @author Tecgraf
 * 
 */
public class AuthorizationRefreshAction extends BusAdminAbstractAction {

  public AuthorizationRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG
      .get("AuthorizationRefreshAction.name"));
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
      Map<RegisteredEntityDesc, List<String>> authorizationsMap = null;

      @Override
      public void performTask() throws Exception {
        authorizationsMap = admin.getAuthorizations();
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
          List<AuthorizationWrapper> wrappersList =
            new LinkedList<AuthorizationWrapper>();

          for (Map.Entry<RegisteredEntityDesc, List<String>> authorization : authorizationsMap
            .entrySet()) {
            wrappersList.add(new AuthorizationWrapper(authorization));
          }

          ObjectTableModel<AuthorizationWrapper> m =
            new ModifiableObjectTableModel<AuthorizationWrapper>(wrappersList,
              new AuthorizationTableProvider());

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