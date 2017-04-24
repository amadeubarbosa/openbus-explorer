package busexplorer.panel.offers;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.TRANSIENT;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.Action;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import static busexplorer.utils.Status.FAILURE;
import static busexplorer.utils.Status.ONLINE;
import static busexplorer.utils.Status.UNEXPECTED;
import static busexplorer.utils.Status.UNREACHABLE;

/**
 * A��o que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferStatusAction extends OpenBusAction<OfferWrapper> {

  public OfferStatusAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(OfferStatusAction.class.getSimpleName() +
      ".name"));
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
    if ((Application.login() != null) && (getTablePanelComponent().getSelectedElements().size() > 0)) {
      return true;
    }
    return false;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        int i = 0;
        List<OfferWrapper> offers = getTablePanelComponent().getSelectedElements();
        for (OfferWrapper offer : offers) {
          try {
            if (offer.getDescriptor().service_ref._non_existent()) {
              offer.setStatus(UNREACHABLE); // OBJECT_NOT_EXIST
            } else {
              offer.setStatus(ONLINE);
            }
          } catch (TRANSIENT e) {
            offer.setStatus(UNREACHABLE);
          } catch (COMM_FAILURE e) {
            offer.setStatus(FAILURE);
          } catch (Exception e) {
            // TODO: status poderia ser um objeto { int, string } para salvar a stacktrace
            offer.setStatus(UNEXPECTED);
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
