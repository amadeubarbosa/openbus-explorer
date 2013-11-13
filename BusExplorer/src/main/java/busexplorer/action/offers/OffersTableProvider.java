package busexplorer.action.offers;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import busexplorer.wrapper.OfferWrapper;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class OffersTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade", "Interface", "Data", "Hora" };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses =
      { String.class, String.class, String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(Object row, int columnIndex) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getCellValues(Object row) {
    final OfferWrapper offerWrapper = (OfferWrapper) row;

    final ServiceOfferDesc offer = offerWrapper.getOffer();
    return new Object[] { getEntityID(offer), getInterfaceName(offer),
        getDate(offer), getTime(offer) };
  }

  /**
   * Obtém ID da entidade à qual a oferta está vinculada.
   * 
   * @param offer Descritor da oferta.
   * @return ID da entidade.
   */
  private String getEntityID(ServiceOfferDesc offer) {
    return offer.properties[2].value;
  }

  /**
   * Obtém nome da interface da oferta.
   * 
   * @param offer Descritor da oferta.
   * @return Nome da interface.
   */
  private String getInterfaceName(ServiceOfferDesc offer) {
    return offer.properties[18].value;
  }

  /**
   * Obtém data de início da oferta.
   * 
   * @param offer Descritor da oferta.
   * @return String que representa a data de início da oferta.
   */
  private String getDate(ServiceOfferDesc offer) {
    return offer.properties[6].value + "/" + offer.properties[5].value + "/"
      + offer.properties[4].value;
  }

  /**
   * Obtém hora de início da oferta.
   * 
   * @param offer Descritor da oferta.
   * @return String que representa a hora de início da oferta.
   */
  private String getTime(ServiceOfferDesc offer) {
    return offer.properties[7].value + ":" + offer.properties[8].value + ":"
      + offer.properties[9].value;
  }
}
