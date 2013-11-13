package busexplorer.action.offers;

import busexplorer.wrapper.OfferWrapper;
import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class OffersTableProvider extends
  VerifiableModifiableObjectTableProvider {

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade", "Interface", "Data", "Hora" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses =
      { String.class, String.class, String.class, String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
    OfferWrapper offerWrapper = (OfferWrapper) row;

    ServiceOfferDesc offer = offerWrapper.getOffer();

  }

  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public boolean isValid(Object row, int columnIndex) {
    return true;
  }

  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] getCellValues(Object row) {
    final OfferWrapper offerWrapper = (OfferWrapper) row;

    final ServiceOfferDesc offer = offerWrapper.getOffer();
    return new Object[] { getEntityID(offer), getInterfaceName(offer),
        getDate(offer), getTime(offer) };
  }

  private String getEntityID(ServiceOfferDesc offer) {
    return offer.properties[2].value;
  }

  private String getInterfaceName(ServiceOfferDesc offer) {
    return offer.properties[18].value;
  }

  private String getDate(ServiceOfferDesc offer) {
    return offer.properties[6].value + "/" + offer.properties[5].value + "/"
      + offer.properties[4].value;
  }

  private String getTime(ServiceOfferDesc offer) {
    return offer.properties[7].value + ":" + offer.properties[8].value + ":"
      + offer.properties[9].value;
  }
}
