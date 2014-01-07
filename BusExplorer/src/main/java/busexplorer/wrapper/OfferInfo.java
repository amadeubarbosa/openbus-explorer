package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;

/**
 * Classe que det�m as informa��es locais da oferta para apresenta��o em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class OfferInfo {
  /** Identificador da entidade */
  private final String entity;
  /** Nome da interface */
  private final String interfaceName;
  /** Data da oferta */
  private final String date;
  /** Hora da oferta */
  private final String time;

  /**

  /**
   * Construtor.
   * 
   * @param desc descritor da oferta
   */
  public OfferInfo(ServiceOfferDesc desc) {
    this.entity = desc.properties[2].value;
    this.interfaceName = desc.properties[18].value;
    this.date = desc.properties[6].value + "/" + desc.properties[5].value + "/"
      + desc.properties[4].value;
    this.time = desc.properties[7].value + ":" + desc.properties[8].value + ":"
      + desc.properties[9].value;
  }

  /**
   * Recupera o identificador da oferta.
   * 
   * @return o identificador.
   */
  public String getEntityId() {
    return entity;
  }

  /**
   * Obt�m nome da interface da oferta.
   *
   * @return nome da interface.
   */
  public String getInterfaceName() {
    return interfaceName;
  }

  /**
   * Obt�m data de in�cio da oferta.
   * 
   * @return String que representa a data de in�cio da oferta.
   */
  public String getDate() {
    return date;
  }

  /**
   * Obt�m hora de in�cio da oferta.
   * 
   * @return String que representa a hora de in�cio da oferta.
   */
  public String getTime() {
    return time;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link ServiceOfferDesc} para
   * {@link OfferInfo}
   * 
   * @param offers a lista de {@link ServiceOfferDesc}
   * @return a lista de {@link OfferInfo}
   */
  public static List<OfferInfo> convertToInfo(List<ServiceOfferDesc> offers) {
    List<OfferInfo> list = new ArrayList<OfferInfo>();
    for (ServiceOfferDesc offer : offers) {
      list.add(new OfferInfo(offer));
    }
    return list;
  }

}
