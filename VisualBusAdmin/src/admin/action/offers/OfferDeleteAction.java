package admin.action.offers;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.OfferWrapper;

/**
 * A��o que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de ofertas */
  private CRUDPanel<OfferWrapper> panel;

  public OfferDeleteAction(SimpleWindow parentWindow,
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
    new SimpleWindowRemoteTask(parentWindow, LNG
      .get("ListAction.waiting.title"), LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      public void performTask() throws Exception {
        List<OfferWrapper> selectedWrappers = panel.getSelectedInfos();
        for (OfferWrapper wrapper : selectedWrappers) {
          ServiceOfferDesc offer = wrapper.getOffer();
          admin.removeOffer(offer);
        }
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
          panel.removeSelectedInfos();
          panel.getTableModel().fireTableDataChanged();
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
