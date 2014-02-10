package busexplorer.panel.offers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class OfferWrapper {
  /** Objeto descritor da oferta */
  private ServiceOfferDesc desc;

  /** Identificador da entidade */
  private final String entity;
  /** Nome da interface */
  private final Vector<String> interfaces;
  /** Data da oferta */
  private final Date date;

  /**
   * /** Construtor.
   * 
   * @param desc descritor da oferta
   */
  public OfferWrapper(ServiceOfferDesc desc) {
    this.desc = desc;
    this.entity = Utils.getProperty(desc, "openbus.offer.entity");
    this.interfaces = Utils.getProperties(desc, "openbus.component.interface");
    int year = Integer.parseInt(Utils.getProperty(desc, "openbus.offer.year"));
    // precisa decrementar o mes em 1
    int month =
      Integer.parseInt(Utils.getProperty(desc, "openbus.offer.month")) - 1;
    int day = Integer.parseInt(Utils.getProperty(desc, "openbus.offer.day"));
    int hour = Integer.parseInt(Utils.getProperty(desc, "openbus.offer.hour"));
    int min = Integer.parseInt(Utils.getProperty(desc, "openbus.offer.minute"));
    int sec = Integer.parseInt(Utils.getProperty(desc, "openbus.offer.second"));
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day, hour, min, sec);
    this.date = calendar.getTime();
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
  public Date getDate() {
    return date;
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
   * {@link OfferWrapper}
   * 
   * @param offers a lista de {@link ServiceOfferDesc}
   * @return a lista de {@link OfferWrapper}
   */
  public static List<OfferWrapper> convertToInfo(List<ServiceOfferDesc> offers) {
    List<OfferWrapper> list = new ArrayList<OfferWrapper>();
    for (ServiceOfferDesc offer : offers) {
      list.add(new OfferWrapper(offer));
    }
    return list;
  }

}
