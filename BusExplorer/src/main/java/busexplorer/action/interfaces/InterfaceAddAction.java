package busexplorer.action.interfaces;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.InterfaceWrapper;
import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import admin.BusAdmin;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class InterfaceAddAction extends BusAdminAbstractAction {

  private CRUDPanel<InterfaceWrapper> panel;

  public InterfaceAddAction(JFrame parentWindow,
    CRUDPanel<InterfaceWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("InterfaceAddAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_ADD;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    new InterfaceInputDialog(parentWindow, LNG
      .get("InterfaceAddAction.inputDialog.title"), panel, admin).showDialog();
  }

}
