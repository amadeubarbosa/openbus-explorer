package admin.action.certificates;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.IdentifierWrapper;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CertificateAddAction extends BusAdminAbstractAction {

  private CRUDPanel<IdentifierWrapper> panel;

  public CertificateAddAction(JFrame parentWindow,
    CRUDPanel<IdentifierWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CertificateAddAction.name"));
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
    new CertificateInputDialog(parentWindow, LNG
      .get("CertificateAddAction.inputDialog.title"), panel, admin).showDialog();
  }

}
