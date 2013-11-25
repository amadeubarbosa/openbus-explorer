package busexplorer.action.certificates;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.IdentifierWrapper;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CertificateRefreshAction extends BusAdminAbstractAction {
  public CertificateRefreshAction(JFrame parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("CertificateRefreshAction.name"));
  }

  @Override
  public CRUDActionType crudActionType() {
    return CRUDActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<String> identifiers = null;

      @Override
      protected void performTask() throws Exception {
        identifiers = admin.getEntitiesWithCertificate();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<IdentifierWrapper> wrappersList =
            new LinkedList<IdentifierWrapper>();

          for (String identifier : identifiers) {
            wrappersList.add(new IdentifierWrapper(identifier));
          }

          ObjectTableModel<IdentifierWrapper> m =
            new ModifiableObjectTableModel<IdentifierWrapper>(wrappersList,
              new CertificateTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }

}
