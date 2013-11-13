package admin.action.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.AuthorizationWrapper;

/**
 * Ação que atualiza a tabela de autorizações
 * 
 * @author Tecgraf
 * 
 */
public class AuthorizationRefreshAction extends BusAdminAbstractAction {

  public AuthorizationRefreshAction(JFrame parentWindow, JTable table,
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
    Task task = new Task() {
      Map<RegisteredEntityDesc, List<String>> authorizationsMap = null;

      @Override
      protected void performTask() throws Exception {
        authorizationsMap = admin.getAuthorizations();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<AuthorizationWrapper> wrappersList =
            new LinkedList<AuthorizationWrapper>();

          for (Map.Entry<RegisteredEntityDesc, List<String>> authorizations : authorizationsMap
            .entrySet()) {
            RegisteredEntityDesc entity = authorizations.getKey();
            for (String interfaceName : authorizations.getValue()) {
              wrappersList.add(new AuthorizationWrapper(entity, interfaceName));
            }
          }

          ObjectTableModel<AuthorizationWrapper> m =
            new ModifiableObjectTableModel<AuthorizationWrapper>(wrappersList,
              new AuthorizationTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }

}
