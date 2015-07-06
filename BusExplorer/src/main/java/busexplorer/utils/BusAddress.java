package busexplorer.utils;

/**
 * Reprecenta��o do endere�o de um barramento.
 * 
 * @author Tecgraf
 */
public class BusAddress {
  /** Descri��o do barramento. */
  private String description;
  /** Host do barramento. */
  private String host;
  /** Porta do barramento. */
  private int port;
  /** IOR */
  private String ior;
  /** Tipo de endere�o */
  private AddressType type;

  /** Representa��o de um endere�o n�o especificado. */
  public static final BusAddress UNSPECIFIED_ADDRESS = new BusAddress();

  /**
   * Construtor.
   *
   * @param description Descri��o do barramento.
   * @param host host.
   * @param port porta.
   */
  private BusAddress(String description, String host, int port) {
    this.description = description;
    this.host = host;
    this.port = port;
    this.type = AddressType.Address;
  }

  /**
   * Construtor.
   * 
   * @param description Descri��o do barramento.
   * @param ior ior.
   */
  private BusAddress(String description, String ior) {
    this.description = description;
    this.ior = ior;
    this.type = AddressType.Reference;
  }

  /**
   * Representa��o de endere�o n�o especificado.
   */
  private BusAddress() {
    this.description = Utils.getString(BusAddress.class, "unspecified");
    this.type = AddressType.Unspecified;
  }

  /**
   * Obt�m uma string descritiva do endere�o.
   *
   * @return String descritiva do endere�o, no formato "Descri��o (host:porta)".
   */
  public String toString() {
    String text = null;
    switch (type) {
      case Address:
        text = host + ":" + port;
        break;
      case Reference:
        text = "Hash(IOR)=" + ior.hashCode();
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
   * Obt�m o host especificado.
   *
   * @return O host especificado.
   */
  public String getHost() {
    return host;
  }

  /**
   * Obt�m a porta especificada.
   *
   * @return A porta especificada.
   */
  public int getPort() {
    return port;
  }

  /**
   * Obt�m o ior especificado.
   *
   * @return O ior especificado.
   */
  public String getIOR() {
    return ior;
  }

  /**
   * Recupera o nome descritivo do endere�o
   * 
   * @return a descri��o
   */
  public String getDescription() {
    return description;
  }

  /**
   * Recupera o tipo de endere�o
   * 
   * @return o tipo de endere�o
   */
  public AddressType getType() {
    return type;
  }

  /**
   * Converte uma string que representa o endere�o do barramento para um objeto
   * BusAddress.
   * 
   * @param description descri��o do barramento
   * @param addressStr String que representa o endere�o do barramento.
   * @return
   */
  public static BusAddress toAddress(String description, String addressStr) {
    BusAddress address;
    try {
      if (addressStr.matches("^IOR:.+")) {
        address = new BusAddress(description, addressStr);
      }
      else {
        String[] addressContents = addressStr.split(":");
        address =
          new BusAddress(description, addressContents[0], Integer
            .parseInt(addressContents[1]));
      }
    }
    // TODO
    catch (Exception e) {
      String msg =
        String.format(Utils.getString(BusAddress.class,
          "warning.unreadableAddress"), new Object[] { addressStr });
      address = new BusAddress();
    }
    return address;
  }

  public static enum AddressType {
    Address,
    Reference,
    Unspecified;
  }
}
