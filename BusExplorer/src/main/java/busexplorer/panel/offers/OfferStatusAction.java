package busexplorer.panel.offers;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.TRANSIENT;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.Action;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import static busexplorer.utils.Availability.Status.FAILURE;
import static busexplorer.utils.Availability.Status.ONLINE;
import static busexplorer.utils.Availability.Status.UNEXPECTED;
import static busexplorer.utils.Availability.Status.UNREACHABLE;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferStatusAction extends OpenBusAction<OfferWrapper> {

  public OfferStatusAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
    putValue(Action.SMALL_ICON, ApplicationIcons.ICON_VALIDATE_16);
    putValue(Action.SHORT_DESCRIPTION, getString("tooltip"));
  }

  @Override
  public ActionType getActionType() {
    return ActionType.OTHER_MULTI_SELECTION;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return ((Application.login() != null) &&
            (getTablePanelComponent().getSelectedElements().size() > 0));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        int i = 0;
        List<OfferWrapper> offers = getTablePanelComponent().getSelectedElements();
        for (OfferWrapper offer : offers) {
          try {
            offer.getDescriptor().service_ref.getComponentId();
            offer.updateStatus(ONLINE, null);
          } catch (TRANSIENT e) {
            offer.updateStatus(UNREACHABLE, e);
          } catch (COMM_FAILURE e) {
            offer.updateStatus(FAILURE, e);
          } catch (Exception e) {
            offer.updateStatus(UNEXPECTED, e);
          }
          i++;
          this.setProgressStatus(100*i/offers.size());
        }
      }

      @Override
      protected void afterTaskUI() {
        getTablePanelComponent().updateUI();
      }
    };
    task.setProgressDialogDelay(1);
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);
  }
}
