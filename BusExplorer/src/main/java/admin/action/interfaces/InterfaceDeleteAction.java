package admin.action.interfaces;

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
import admin.wrapper.InterfaceWrapper;

/**
 * Classe de a��o para a remo��o de uma interface.
 * 
 * @author Tecgraf
 */
public class InterfaceDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de interfaces */
  private CRUDPanel<InterfaceWrapper> panel;

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param panel painel de CRUD
   * @param admin
   */
  public InterfaceDeleteAction(JFrame parentWindow,
    CRUDPanel<InterfaceWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("InterfaceDeleteAction.name"));
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
        List<InterfaceWrapper> selectedWrappers = panel.getSelectedInfos();
        for (InterfaceWrapper wrapper : selectedWrappers) {
          String interfaceName = wrapper.getInterface();
          admin.removeInterface(interfaceName);
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