/*
 * $Id$
 */
package reuse.modified.logistic.logic.common;

/**
 * 
 * Representa um objeto que possui um identificador un�voco. Esse identificador
 * � usado tanto na aplica��o (em cache, por exemplo) como tamb�m nas tabelas do
 * banco de dados.
 * 
 * @param <T> a classe do objeto que possui um identificador.
 * 
 * @author mjulia
 */
public interface Identifiable<T> {

  /**
   * Obt�m o identificador <code>Code</code> desse objeto.
   * 
   * @return o identificador desse objeto
   */
  Code<T> getId();
}
