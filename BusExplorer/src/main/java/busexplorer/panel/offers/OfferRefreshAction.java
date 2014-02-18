package busexplorer.panel.offers;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferRefreshAction extends OpenBusAction<OfferWrapper> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public OfferRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(OfferRefreshAction.class.getSimpleName()
      + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<OfferWrapper>> task =
      new BusExplorerTask<List<OfferWrapper>>(Application.exceptionHandler(),
      ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(OfferWrapper.convertToInfo(admin.getOffers()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
