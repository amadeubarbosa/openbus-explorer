package admin.action.certificates;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.CertificateRegistry;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.IdentifierWrapper;

/**
 * Classe de a��o para a remo��o de uma categoria.
 *
 * @author Tecgraf
 */
public class CertificateDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de categorias */
  private CRUDPanel<IdentifierWrapper> panel;

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param panel painel de CRUD
   * @param admin 
   */
  public CertificateDeleteAction(JFrame parentWindow,
    CRUDPanel<IdentifierWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CertificateDeleteAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return TYPE_ACTION_REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<IdentifierWrapper> selectedWrappers = panel.getSelectedInfos();
        for (IdentifierWrapper wrapper : selectedWrappers) {
          String identifier = wrapper.getIdentifier();
          admin.removeCertificate(identifier);
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

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"));
  }
}