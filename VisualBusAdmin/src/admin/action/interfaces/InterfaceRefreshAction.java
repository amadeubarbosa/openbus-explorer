package admin.action.interfaces;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.wrapper.InterfaceWrapper;

/**
 * Ação que atualiza a tabela de interfaces
 * 
 * @author Tecgraf
 * 
 */
public class InterfaceRefreshAction extends BusAdminAbstractAction {

  public InterfaceRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("InterfaceRefreshAction.name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return TYPE_ACTION_OTHER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<String> interfaces = null;

      @Override
      protected void performTask() throws Exception {
        interfaces = admin.getInterfaces();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<InterfaceWrapper> wrappersList =
            new LinkedList<InterfaceWrapper>();

          for (String interfaceName : interfaces) {
            wrappersList.add(new InterfaceWrapper(interfaceName));
          }

          ObjectTableModel<InterfaceWrapper> m =
            new ModifiableObjectTableModel<InterfaceWrapper>(wrappersList,
              new InterfaceTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"));
  }

}
