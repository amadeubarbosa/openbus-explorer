package busexplorer.panel.offers;

import busexplorer.Application;
import busexplorer.utils.Availability;
import org.jacorb.orb.ORB;
import org.jacorb.orb.ParsedIOR;
import org.jacorb.orb.iiop.IIOPAddress;
import org.jacorb.orb.iiop.IIOPProfile;
import org.omg.ETF.Profile;
import scs.core.IComponent;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Classe que detém as informações locais da oferta para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class OfferWrapper {
  public static final String OPENBUS_COMPONENT_INTERFACE = "openbus.component.interface";
  /** Objeto descritor da oferta */
  private ServiceOfferDesc desc;

  /** Identificador da entidade */
  private final String entity;
  /** Identificador da oferta no registro de ofertas */
  private final String id;
  /** Nome da interface */
  private final Vector<String> interfaces;
  /** Data da oferta */
  private final Date date;
  /** Nome do componente */
  private final String name;
  /** Versão do componente */
  private final String version;
  /** Status da conectividade */
  private Availability availability = new Availability(Availability.Status.UNKNOWN);

  /**
   * /** Construtor.
   * 
   * @param desc descritor da oferta
   */
  public OfferWrapper(ServiceOfferDesc desc) {
    this.desc = desc;
    this.entity = getProperty(desc, "openbus.offer.entity");
    this.id = getProperty(desc, "openbus.offer.id");
    this.interfaces = getProperties(desc, OPENBUS_COMPONENT_INTERFACE);
    int year = Integer.parseInt(getProperty(desc, "openbus.offer.year"));
    // precisa decrementar o mes em 1
    int month =
      Integer.parseInt(getProperty(desc, "openbus.offer.month")) - 1;
    int day = Integer.parseInt(getProperty(desc, "openbus.offer.day"));
    int hour = Integer.parseInt(getProperty(desc, "openbus.offer.hour"));
    int min = Integer.parseInt(getProperty(desc, "openbus.offer.minute"));
    int sec = Integer.parseInt(getProperty(desc, "openbus.offer.second"));
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day, hour, min, sec);
    this.date = calendar.getTime();
    this.name = getProperty(desc, "openbus.component.name");
    String version = getProperty(desc, "openbus.component.version.major");
    version += "." + getProperty(desc, "openbus.component.version.minor");
    version += "." + getProperty(desc, "openbus.component.version.patch");
    this.version = version;
  }

  /**
   * Recupera o valor de uma dada propriedade em uma oferta.
   *
   * @param offer o descritor da oferta
   * @param prop a propriedade sendo buscada
   * @return o valor da propriedade na oferta, ou <code>null</code> caso não a
   *         propriedade não exista.
   */
  static public String getProperty(ServiceOfferDesc offer, String prop) {
    ServiceProperty[] properties = offer.properties;
    for (ServiceProperty property : properties) {
      if (property.name.equals(prop)) {
        return property.value;
      }
    }
    return null;
  }

  /**
   * Recupera os valores de uma dada propriedade em uma oferta.
   *
   * @param offer o descritor da oferta
   * @param prop a propriedade sendo buscada
   * @return a lista de valores na propriedade na oferta.
   */
  static public Vector<String> getProperties(ServiceOfferDesc offer, String prop) {
    Vector<String> list = new Vector<>();
    ServiceProperty[] properties = offer.properties;
    for (ServiceProperty property : properties) {
      if (property.name.equals(prop)) {
        list.add(property.value);
      }
    }
    return list;
  }

  /**
   * Compara um objeto à instância de {@link OfferWrapper}.
   *
   * O método não leva em consideração o objeto descritor da oferta.
   * 
   * @param o Objeto a ser comparado.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OfferWrapper)) {
      return false;
    }
    OfferWrapper other = (OfferWrapper) o;
    return entity.equals(other.entity) && id.equals(other.id);
  }

  /**
   * Código hash do objeto.
   *
   * @return Código hash do objeto.
   */
  @Override
  public int hashCode() {
    return entity.hashCode() ^ id.hashCode();
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
   * Recupera o nome do componente da oferta.
   * 
   * @return o nome do componente.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Recupera a versão do componente da oferta.
   * 
   * @return a string da versão do componente.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Método utilitário para converter lista de {@link ServiceOfferDesc} para
   * {@link OfferWrapper}
   * 
   * @param offers a lista de {@link ServiceOfferDesc}
   * @return a lista de {@link OfferWrapper}
   */
  public static List<OfferWrapper> convertToInfo(List<ServiceOfferDesc> offers) {
    List<OfferWrapper> list = new ArrayList<>();
    for (ServiceOfferDesc offer : offers) {
      list.add(new OfferWrapper(offer));
    }
    return list;
  }

  /**
   * Recupera a versão do componente da oferta.
   *
   * @return a string da versão do componente.
   */
  public Vector<String> getEndpoints() {
    ArrayList<String> results = new ArrayList<>();
    IComponent comp = this.getDescriptor().service_ref;
    ORB orb = (ORB) Application.login().getORB();

    ParsedIOR ior = new ParsedIOR(orb, orb.object_to_string(comp));

    for (Profile profile : ior.getProfiles()) {
      IIOPAddress address = (IIOPAddress) ((IIOPProfile) profile).getAddress();
      //TODO: address.getSSLPort() se retornar -1 não há profile SSLIOP
      String endpoint = String.format("%s:%s",address.getOriginalHost(), address.getPort());
      if (!results.contains(endpoint))
        results.add(endpoint);
    }

    return new Vector<>(results);
  }

  /**
   * Recupera o estado da referência remota, se está alcançável ou não.
   *
   * @return um valor entre os previstos em {@link Availability.Status}
   *
   */
  public Availability getStatus() {
    return this.availability;
  }

  /**
   * Atualiza o estado da conectividade da referência.
   *
   * @param status um valor entre os previstos em {@link Availability.Status}
   * @param exception uma exceção se ocorrer ou {@code null} caso contrário
   */
  public void updateStatus(Availability.Status status, Exception exception) {
    this.availability.status = status;
    if (exception != null) {
      this.availability.detail = exception.getMessage();
    } else {
      this.availability.detail = "";
    }
  }
}
