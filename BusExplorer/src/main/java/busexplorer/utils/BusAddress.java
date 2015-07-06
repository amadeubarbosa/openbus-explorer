package busexplorer.utils;

/**
 * Reprecentação do endereço de um barramento.
 * 
 * @author Tecgraf
 */
public class BusAddress {
  /** Descrição do barramento. */
  private String description;
  /** Host do barramento. */
  private String host;
  /** Porta do barramento. */
  private int port;
  /** IOR */
  private String ior;
  /** Tipo de endereço */
  private AddressType type;

  /** Representação de um endereço não especificado. */
  public static final BusAddress UNSPECIFIED_ADDRESS = new BusAddress();

  /**
   * Construtor.
   *
   * @param description Descrição do barramento.
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
    this.description = Utils.getString(BusAddress.class, "unspecified");
    this.type = AddressType.Unspecified;
  }

  /**
   * Obtém uma string descritiva do endereço.
   *
   * @return String descritiva do endereço, no formato "Descrição (host:porta)".
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
  public int getPort() {
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
