package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import busexplorer.utils.Utils;

/**
 * Classe que detém as informações locais da oferta para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class OfferInfo {
  /** Objeto descritor da oferta */
  private ServiceOfferDesc desc;

  /** Identificador da entidade */
  private final String entity;
  /** Nome da interface */
  private final Vector<String> interfaces;
  /** Data da oferta */
  private final String date;
  /** Hora da oferta */
  private final String time;

  /**
   * /** Construtor.
   * 
   * @param desc descritor da oferta
   */
  public OfferInfo(ServiceOfferDesc desc) {
    this.desc = desc;
    this.entity = Utils.getProperty(desc, "openbus.offer.entity");
    this.interfaces = Utils.getProperties(desc, "openbus.component.interface");
    this.date =
      String.format("%s/%s/%s", Utils.getProperty(desc, "openbus.offer.day"),
        Utils.getProperty(desc, "openbus.offer.month"), Utils.getProperty(desc,
          "openbus.offer.year"));
    this.time =
      String.format("%s:%s:%s", Utils.getProperty(desc, "openbus.offer.hour"),
        Utils.getProperty(desc, "openbus.offer.minute"), Utils.getProperty(
          desc, "openbus.offer.second"));
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
   * Obtém nome da interface da oferta.
   * 
   * @return nome da interface.
   */
  public Vector<String> getInterfaces() {
    return interfaces;
  }

  /**
   * Obtém data de início da oferta.
   * 
   * @return String que representa a data de início da oferta.
   */
  public String getDate() {
    return date;
  }

  /**
   * Obtém hora de início da oferta.
   * 
   * @return String que representa a hora de início da oferta.
   */
  public String getTime() {
    return time;
  }

  /**
   * Recupera o próprio objeto descritor de oferta.
   * 
   * @return o objeto descritor.
   */
  public ServiceOfferDesc getDescriptor() {
    return desc;
  }

  /**
   * Método utilitário para converter lista de {@link ServiceOfferDesc} para
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
