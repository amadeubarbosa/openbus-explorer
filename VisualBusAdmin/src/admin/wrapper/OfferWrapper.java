package admin.wrapper;

import logistic.logic.common.Code;
import logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;

public class OfferWrapper implements Identifiable<OfferWrapper> {
  private ServiceOfferDesc offer;

  public OfferWrapper(ServiceOfferDesc offer) {
    this.offer = offer;
  }

  @Override
  public Code<OfferWrapper> getId() {
    return new Code<OfferWrapper>(offer.service_ref.getComponentId());
  }

  public ServiceOfferDesc getOffer() {
    return offer;
  }

}
