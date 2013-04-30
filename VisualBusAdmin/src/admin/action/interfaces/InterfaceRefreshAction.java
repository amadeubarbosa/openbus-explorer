package admin.action.interfaces;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
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
    new SimpleWindowRemoteTask(parentWindow, LNG
      .get("ListAction.waiting.title"), LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {
      List<String> interfaces = null;

      @Override
      public void performTask() throws Exception {
        interfaces = admin.getInterfaces();
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
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
        else {
          JOptionPane.showMessageDialog(parentWindow, getTaskException()
            .getMessage(), LNG.get("ProgressDialog.error.title"),
            JOptionPane.ERROR_MESSAGE);
        }
      }

    }.start();
  }

}