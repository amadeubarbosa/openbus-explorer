package busexplorer.panel.offers;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOffer;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import exception.handling.ExceptionContext;

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

  @Override
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    Task<Object> task = new
    BusExplorerTask<Object>(Application.exceptionHandler(),
      ExceptionContext.BusCore) {
      @Override
      protected void performTask() throws Exception {
        OfferWrapper offer = getPanelComponent().getSelectedElement();
        ServiceOffer ref = offer.getDescriptor().ref;
        ref.remove();
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
