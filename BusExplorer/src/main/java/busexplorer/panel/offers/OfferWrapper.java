package busexplorer.panel.offers;

import busexplorer.Application;
import busexplorer.utils.Status;
import busexplorer.utils.Utils;
import org.jacorb.orb.ORB;
import org.jacorb.orb.ParsedIOR;
import org.jacorb.orb.iiop.IIOPAddress;
import org.jacorb.orb.iiop.IIOPProfile;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.TRANSIENT;
import org.omg.ETF.Profile;
import scs.core.IComponent;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static busexplorer.utils.Status.FAILURE;
import static busexplorer.utils.Status.ONLINE;
import static busexplorer.utils.Status.UNKNOWN;
import static busexplorer.utils.Status.UNREACHABLE;

/**
 * Classe que det�m as informa��es locais da oferta para apresenta��o em
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
  /** Nome do componente */
  private final String name;
  /** Vers�o do componente */
  private final String version;
  /** Status da conectividade */
  private Integer status = UNKNOWN;

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
    this.name = Utils.getProperty(desc, "openbus.component.name");
    String version = Utils.getProperty(desc, "openbus.component.version.major");
    version += "." + Utils.getProperty(desc, "openbus.component.version.minor");
    version += "." + Utils.getProperty(desc, "openbus.component.version.patch");
    this.version = version;
  }

  /**
   * Compara um objeto � inst�ncia de {@link OfferWrapper}.
   *
   * O m�todo n�o leva em considera��o o objeto descritor da oferta.
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
    return entity.equals(other.entity) && interfaces.equals(other.interfaces) &&
      date.equals(other.date);
  }

  /**
   * C�digo hash do objeto.
   *
   * @return C�digo hash do objeto.
   */
  @Override
  public int hashCode() {
    return entity.hashCode() ^ interfaces.hashCode() ^ date.hashCode();
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
  public Vector<String> getInterfaces() {
    return interfaces;
  }

  /**
   * Obt�m data de in�cio da oferta.
   * 
   * @return String que representa a data de in�cio da oferta.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Recupera o pr�prio objeto descritor de oferta.
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
   * Recupera a vers�o do componente da oferta.
   * 
   * @return a string da vers�o do componente.
   */
  public String getVersion() {
    return version;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link ServiceOfferDesc} para
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
   * Recupera a vers�o do componente da oferta.
   *
   * @return a string da vers�o do componente.
   */
  public Vector<String> getEndpoints() {
    ArrayList<String> results = new ArrayList<>();
    IComponent comp = this.getDescriptor().service_ref;
    ORB orb = (ORB) Application.login().getOpenBusContext().ORB();

    ParsedIOR ior = new ParsedIOR(orb, comp.toString());

    for (Profile profile : ior.getProfiles()) {
      IIOPAddress address = (IIOPAddress) ((IIOPProfile) profile).getAddress();
      //TODO: address.getSSLPort() se retornar -1 n�o h� profile SSLIOP
      String endpoint = String.format("%s:%s",address.getOriginalHost(), address.getPort());
      if (!results.contains(endpoint))
        results.add(endpoint);
    }

    return new Vector<>(results);
  }

  /**
   * Recupera o estado da refer�ncia remota, se est� alcan��vel ou n�o.
   *
   * @return um valor entre os previstos em {@link Status}
   *
   */
  public Integer getStatus() {
    return this.status;
  }

  /**
   * Atualiza o estado da conectividade da refer�ncia.
   *
   * @param code um valor entre os previstos em {@link Status}
   */
  public void setStatus(Integer code) {
    this.status = code;
  }
}
