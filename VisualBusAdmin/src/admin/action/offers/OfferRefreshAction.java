package admin.action.offers;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.wrapper.OfferWrapper;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferRefreshAction extends BusAdminAbstractAction {

  public OfferRefreshAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin) {
    super(parentWindow, table, admin, LNG.get("OfferRefreshAction.name"));
  }

  @Override
  public int crudActionType() {
    return TYPE_ACTION_OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      List<ServiceOfferDesc> offers = null;

      @Override
      protected void performTask() throws Exception {
        offers = admin.getOffers();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          List<OfferWrapper> wrappersList = new LinkedList<OfferWrapper>();

          for (ServiceOfferDesc offer : offers) {
            wrappersList.add(new OfferWrapper(offer));
          }

          ObjectTableModel<OfferWrapper> m =
            new ModifiableObjectTableModel<OfferWrapper>(wrappersList,
              new OffersTableProvider());

          table.setModel(m);
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"));
  }

}
