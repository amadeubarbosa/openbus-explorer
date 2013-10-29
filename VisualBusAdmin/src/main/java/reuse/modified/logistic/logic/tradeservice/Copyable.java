package reuse.modified.logistic.logic.tradeservice;

/**
 * Interface de um objeto que pode ser copiado.
 * 
 * @param <T> tipo do objeto que pode ser copiado.
 */
public interface Copyable<T> {

  /**
   * Obtém a cópia do objeto.
   * 
   * @return cópia do objeto.
   */
  public T copy();
}
