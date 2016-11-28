package busexplorer.panel.offers;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOffer;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * A��o que atualiza a tabela de ofertas
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
    return Application.login() != null && Application.login().hasAdminRights();
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
        OfferWrapper offer = getTablePanelComponent().getSelectedElement();
        ServiceOffer ref = offer.getDescriptor().ref;
        ref.remove();
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
