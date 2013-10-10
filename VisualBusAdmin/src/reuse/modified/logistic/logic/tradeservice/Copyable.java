package reuse.modified.logistic.logic.tradeservice;

/**
 * Interface de um objeto que pode ser copiado.
 * 
 * @param <T> tipo do objeto que pode ser copiado.
 */
public interface Copyable<T> {

  /**
   * Obt�m a c�pia do objeto.
   * 
   * @return c�pia do objeto.
   */
  public T copy();
}
