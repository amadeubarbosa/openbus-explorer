package busexplorer.panel.offers;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOffer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferDeleteAction extends OpenBusAction<OfferWrapper> {

  public OfferDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(OfferDeleteAction.class.getSimpleName() +
      ".name"));
  }

  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    if (Application.login() != null) {
      if (Application.login().hasAdminRights()) {
        return true;
      } else {
        List<OfferWrapper> offers = getTablePanelComponent().getSelectedElements();
        if (offers.size() > 0) {
          for (OfferWrapper offer : offers) {
            if (!offer.getEntityId().equals(Application.login().entity))
              return false;
          }
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        List<OfferWrapper> offers = getTablePanelComponent().getSelectedElements();
        for (OfferWrapper offer : offers) {
          ServiceOffer ref = offer.getDescriptor().ref;
          ref.remove();
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
