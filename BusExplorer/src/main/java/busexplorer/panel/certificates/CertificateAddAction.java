package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import busexplorer.panel.BusAdminAbstractAction;
import busexplorer.wrapper.IdentifierWrapper;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CertificateAddAction extends BusAdminAbstractAction {

  public CertificateAddAction(JFrame parentWindow,
    CRUDPanel<IdentifierWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CertificateAddAction.name"));
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
    ObjectTableModel<IdentifierWrapper> model =
      (ObjectTableModel<IdentifierWrapper>) this.table.getModel();
    new CertificateInputDialog(parentWindow, LNG
      .get("CertificateAddAction.inputDialog.title"), model, admin)
      .showDialog();
  }

}
