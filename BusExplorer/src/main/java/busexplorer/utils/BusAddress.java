package busexplorer.utils;

import java.text.MessageFormat;

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

  /** Representação de um endereço não especificado. */
  public static final BusAddress UNSPECIFIED_ADDRESS =
    new BusAddress(Utils.getString(BusAddress.class, "unspecified"), null);

  /**
   * Construtor.
   *
   * @param description Descrição do barramento.
   * @param authority Endereço do barramento, no formato "host:porta".
   */
  public BusAddress(String description, String authority) {
    this(BusAddress.toAddress(authority));
    this.description = description;
  }

  /**
   * Construtor.
   */
  private BusAddress() {
  }

  private BusAddress(BusAddress address) {
    this.description = address.description;
    this.host = address.host;
    this.port = address.port;
  }

  /**
   * Obtém uma string descritiva do endereço.
   *
   * @return String descritiva do endereço, no formato "Descrição (host:porta)".
   */
  public String toString() {
    String address = host + ":" + port;
    if (host.equals("")) {
      return description;
    }
    if (description.equals("")) {
      return address;
    }
    return description + " (" + address + ")";

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
   * Converte uma string que representa o endereço do barramento para um objeto
   * BusAddress.
   *
   * @param addressStr String que representa o endereço do barramento.
   */
  public static BusAddress toAddress(String addressStr) {
    BusAddress address = new BusAddress(); 

    address.description = "";
    address.host = "";
    address.port = 2089;

    try {
      String[] addressContents = addressStr.split(":");
      address.host = addressContents[0];
      address.port = Integer.parseInt(addressContents[1]);
    }
    catch (Exception e) {
      MessageFormat messageFormat = new MessageFormat(
        Utils.getString(BusAddress.class, "warning.unreadableAddress"));

      messageFormat.format(new Object[]{ addressStr });
    }

    return address;
  }
}
