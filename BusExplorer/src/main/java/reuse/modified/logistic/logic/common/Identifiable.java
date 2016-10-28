/*
 * $Id$
 */
package reuse.modified.logistic.logic.common;

/**
 * 
 * Representa um objeto que possui um identificador unívoco. Esse identificador
 * é usado tanto na aplicação (em cache, por exemplo) como também nas tabelas do
 * banco de dados.
 * 
 * @param <T> a classe do objeto que possui um identificador.
 * 
 * @author mjulia
 */
public interface Identifiable<T> {

  /**
   * Obtém o identificador <code>Code</code> desse objeto.
   * 
   * @return o identificador desse objeto
   */
  Code<T> getId();
}
