package admin.action.offers;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.OfferWrapper;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de ofertas */
  private CRUDPanel<OfferWrapper> panel;

  public OfferDeleteAction(JFrame parentWindow,
    CRUDPanel<OfferWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin,
      LNG.get("OfferDeleteAction.name"));
    this.panel = panel;
  }

  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_REMOVE;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<OfferWrapper> selectedWrappers = panel.getSelectedInfos();
        for (OfferWrapper wrapper : selectedWrappers) {
          ServiceOfferDesc offer = wrapper.getOffer();
          admin.removeOffer(offer);
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
