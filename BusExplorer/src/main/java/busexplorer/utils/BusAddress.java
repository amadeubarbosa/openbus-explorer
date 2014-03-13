package busexplorer.utils;

import java.text.MessageFormat;

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

  /** Representa��o de um endere�o n�o especificado. */
  public static final BusAddress UNSPECIFIED_ADDRESS =
    new BusAddress(Utils.getString(BusAddress.class, "unspecified"), null);

  /**
   * Construtor.
   *
   * @param description Descri��o do barramento.
   * @param authority Endere�o do barramento, no formato "host:porta".
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
   * Obt�m uma string descritiva do endere�o.
   *
   * @return String descritiva do endere�o, no formato "Descri��o (host:porta)".
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
   * Converte uma string que representa o endere�o do barramento para um objeto
   * BusAddress.
   *
   * @param addressStr String que representa o endere�o do barramento.
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
