package busexplorer.panel.offers.properties;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOffer;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import exception.handling.ExceptionContext;

/**
 * A��o que atualiza a tabela de ofertas
 * 
 * @author Tecgraf
 * 
 */
public class PropertiesRefreshAction extends OpenBusAction<ServiceProperty> {

  /** Oferta cujas propriedades s�o exibidas. */
  private ServiceOffer offer;

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param offer oferta cujas propriedades ser�o exibidas
   */
  public PropertiesRefreshAction(Window parentWindow, OfferWrapper offer) {
    super(parentWindow, null, Utils.getString(PropertiesRefreshAction.class,
      "name"));
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
      new BusExplorerTask<List<ServiceProperty>>(
        Application.exceptionHandler(), ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(Arrays.asList(offer.describe().properties));
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
