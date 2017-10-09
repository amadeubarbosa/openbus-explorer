package busexplorer.panel.offers.properties;

import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOffer;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Ação que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class PropertiesRefreshAction extends OpenBusAction<ServiceProperty> {

  /** Oferta cujas propriedades são exibidas. */
  private ServiceOffer offer;

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param offer oferta cujas propriedades serão exibidas
   */
  public PropertiesRefreshAction(Window parentWindow, OfferWrapper offer) {
    super(parentWindow);
    this.offer = offer.getDescriptor().ref;
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
    BusExplorerTask<List<ServiceProperty>> task =
      new BusExplorerTask<List<ServiceProperty>>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(Arrays.asList(offer.describe().properties));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
