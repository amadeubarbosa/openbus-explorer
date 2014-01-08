package busexplorer.panel.interfaces;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import busexplorer.panel.BusAdminAbstractAction;
import busexplorer.wrapper.InterfaceWrapper;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class InterfaceAddAction extends BusAdminAbstractAction {

  public InterfaceAddAction(JFrame parentWindow,
    CRUDPanel<InterfaceWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("InterfaceAddAction.name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CRUDActionType crudActionType() {
    return CRUDActionType.ADD;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    ObjectTableModel<InterfaceWrapper> model =
      (ObjectTableModel<InterfaceWrapper>) this.table.getModel();
    new InterfaceInputDialog(parentWindow, LNG
      .get("InterfaceAddAction.inputDialog.title"), model, admin).showDialog();
  }

}
