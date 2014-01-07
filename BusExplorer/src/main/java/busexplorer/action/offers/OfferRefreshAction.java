package busexplorer.action.offers;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.action.BusAdminAbstractAction;
import busexplorer.wrapper.OfferInfo;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class OfferRefreshAction extends OpenBusAction<OfferInfo> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public OfferRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(OfferRefreshAction.class.getSimpleName()
      +  ".name"));
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
    Task<List<OfferInfo>> task = new Task<List<OfferInfo>>() {

      @Override
      protected void performTask() throws Exception {
          setResult(OfferInfo.convertToInfo(admin.getOffers()));
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
