package tecgraf.openbus.admin;

/**
 * Erro caso o BusExplorer tente se conectar a um barramento incompatível.
 *
 * @author Tecgraf/PUC-Rio
 */
public class IncompatibleBus extends RuntimeException {

  /**
   * Construtor.
   * 
   * @param msg mensagem de erro.
   */
  public IncompatibleBus(String msg) {
    super(msg);
  }
}
