package busexplorer.utils;

import org.jacorb.orb.ORB;
import org.jacorb.orb.ParsedIOR;
import org.jacorb.orb.iiop.IIOPAddress;
import org.jacorb.orb.iiop.IIOPProfile;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.Object;
import org.omg.ETF.Profile;
import tecgraf.openbus.admin.IncompatibleBus;
import tecgraf.openbus.core.v2_1.BusObjectKey;

import java.util.Properties;

/**
 * Representação do endereço de um barramento.
 * 
 * @author Tecgraf
 */
public class BusAddress {
  /** Descrição do barramento. */
  private String description;
  /** Host do barramento. */
  private String host;
  /** Porta do barramento. */
  private short port;
  /** IOR */
  private String ior;
  /** Tipo de endereço */
  private AddressType type;
  /** ORB puro sem interceptadores */
  private static final ORB orb;

  static {
    Properties props = new Properties();
    props.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
    props.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
    orb = (org.jacorb.orb.ORB) org.omg.CORBA.ORB.init(new String[]{} , props);
  }

  /** Representação de um endereço não especificado. */
  public static final BusAddress UNSPECIFIED_ADDRESS = new BusAddress();

  /**
   * Construtor para endereços pelo par de host e porta.
   *
   * @param description Descrição do barramento.
   * @param host host.
   * @param port porta.
   */
  private BusAddress(String description, String host, short port) {
    this.description = description;
    this.host = host;
    this.port = port;
    this.type = AddressType.Address;
  }

  /**
   * Construtor para endereços IOR
   * 
   * @param description Descrição do barramento.
   * @param ior ior.
   */
  private BusAddress(String description, String ior) {
    this.description = description;
    this.ior = ior;
    this.type = AddressType.Reference;
  }

  /**
   * Representação de endereço não especificado.
   */
  private BusAddress() {
    this.description = Language.get(BusAddress.class, "unspecified");
    this.type = AddressType.Unspecified;
  }

  /**
   * Validação do endereço do barramento através da análise dos objetos de {@link Profile}
   * contidos no {@link ParsedIOR} gerado a partir do endereço.
   *
   * @throws IllegalArgumentException caso não seja possível realizar o parsing do IOR através do {@link ParsedIOR}
   *                                  ou não seja identificado nenhum object key conhecido
   * @throws IncompatibleBus caso seja identificado um endereço do barramento da versão anterior
   *                         contendo um object key com valor {@link tecgraf.openbus.core.v2_0.BusObjectKey#value}
   */
  public void checkBusVersion() throws IncompatibleBus {
    String stringfiedIOR = this.toIOR();
    try {
      for (Profile profile : new ParsedIOR(orb, stringfiedIOR).getProfiles()) {
        String objectKey = new String(profile.get_object_key());
        if (objectKey.equals(tecgraf.openbus.core.v2_0.BusObjectKey.value)) {
          throw new IncompatibleBus(
            Language.get(BusAddress.class, "legacy.version.unsupported", stringfiedIOR));
        } else if (objectKey.equals(BusObjectKey.value)) {
          return;
        }
      }
    } catch (MARSHAL | BAD_PARAM e) {
      throw new IllegalArgumentException(Language.get(BusAddress.class,
        "parsedior.fail", stringfiedIOR, e.toString()));
    }
    throw new IllegalArgumentException(stringfiedIOR);
  }

  /**
   * Validação da responsividade do endereço do barramento através da chamada de {@link Object#_non_existent()}
   * ao objeto que será criado a partir do {@link #toIOR()}.
   *
   * @throws org.omg.CORBA.COMM_FAILURE caso haja falha na comunicação de rede
   * @throws org.omg.CORBA.TRANSIENT caso o endereço seja inalcancável na rede
   * @throws org.omg.CORBA.OBJECT_NOT_EXIST caso o endereço remoto exista mas não haja nenhum objeto remoto conhecido
   */
  public void checkBusReference() {
    if (orb.string_to_object(toIOR())._non_existent()) {
      throw new OBJECT_NOT_EXIST();
    }
  }

  /**
   * Obtém uma string descritiva do endereço.
   *
   * @return String descritiva do endereço, no formato "Descrição (host:porta)".
   */
  public String toString() {
    String addressFormat = "%s:%s - SSL: %s";
    String text = "";
    switch (type) {
      case Address:
        text = String.format(addressFormat, host, port, "off");
        break;
      case Reference:
        IIOPProfile profile =
          ((IIOPProfile) new ParsedIOR(orb, ior).getProfiles().get(0));
        IIOPAddress address = (IIOPAddress) profile.getAddress();
        int sslPort = profile.getSSLPort();
        if (sslPort != -1) {
          text = String.format(addressFormat, address.getIP(), sslPort, "on");
        } else {
          text = String.format(addressFormat, address.getIP(), address.getPort(), "off");
        }
        break;
      case Unspecified:
        return description;
    }
    if (description != null) {
      text = description + " (" + text + ")";
    }
    return text;
  }

  /**
   * Obtém o host especificado.
   *
   * @return O host especificado.
   */
  public String getHost() {
    return host;
  }

  /**
   * Obtém a porta especificada.
   *
   * @return A porta especificada.
   */
  public short getPort() {
    return port;
  }

  /**
   * Obtém o ior especificado.
   *
   * @return O ior especificado.
   */
  public String getIOR() {
    return ior;
  }

  /**
   * Converte a representação interna do endereço para um IOR
   *
   * @return a string contendo o IOR (similar ao {@link BusAddress#getIOR()}) ou contendo o corbaloc
   * construído a partir do uso dos métodos {@link BusAddress#getHost()} e {@link BusAddress#getPort()}
   */
  public String toIOR() {
    String corbaloc = "";
    switch (this.getType()) {
      case Address:
        corbaloc = String.format("corbaloc::1.0@%s:%d/%s",
          this.getHost(), this.getPort(),
          tecgraf.openbus.core.v2_1.BusObjectKey.value);
        break;
      case Reference:
        corbaloc = this.ior;
        break;
    }
    return corbaloc;
  }

  /**
   * Recupera o nome descritivo do endereço
   * 
   * @return a descrição
   */
  public String getDescription() {
    return description;
  }

  /**
   * Recupera o tipo de endereço
   * 
   * @return o tipo de endereço
   */
  public AddressType getType() {
    return type;
  }

  /**
   * Converte uma string que representa o endereço do barramento para um objeto
   * BusAddress.
   * 
   * @param description descrição do barramento
   * @param addressStr String que representa o endereço do barramento.
   * @return instância de busaddress
   */
  public static BusAddress toAddress(String description, String addressStr) {
    BusAddress address;
    try {
      if (addressStr.matches("^IOR:.+") || addressStr.matches("^corbaloc:.+")) {
        address = new BusAddress(description, addressStr);
      }
      else {
        String[] addressContents = addressStr.split(":");
        short port = Short
          .parseShort(addressContents[1]);
        if (port < 0) {
          throw new IllegalArgumentException();
        }
        address =
          new BusAddress(description, addressContents[0], port);
      }
    }
    catch (Exception e) {
      address = new BusAddress();
    }
    return address;
  }

  public enum AddressType {
    Address,
    Reference,
    Unspecified,
  }
}
