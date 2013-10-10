/*
 * $Id$
 */
package reuse.modified.logistic.logic.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tecgraf.javautils.concurrent.locks.SharedAccessObject;

/**
 * Classe genérica usada para encapsular códigos de objetos. O tipo paramétrico
 * T é a classe para a qual o código está sendo criado. Por exemplo Code<User>
 * representa a classe que encapsula um código de usuário. Funciona como um
 * "wrapper" para o código que, na maior parte dos casos, é proveniente do banco
 * de dados.
 * 
 * @param <T> o tipo do objeto que possui esse identificador.
 * 
 * @author mjulia
 */
public class Code<T> implements Serializable, Comparable<Code<T>>,
  SharedAccessObject {

  /**
   * Identificação da versão da classe, segundo a serialização padrão de Java.
   */
  private static final long serialVersionUID = 7617061903509182552L;

  /** O código. */
  private final Object code;

  /** Representação string do código */
  private final String trimmedString;

  /**
   * Constrói um código para um objeto da classe parametrizada por T.
   * 
   * @param code o valor do código
   */
  public Code(Object code) {
    this.code = code;
    this.trimmedString = ("" + code).trim();
  }

  /**
   * Obtém o valor do código.
   * 
   * @return o código encapsulado
   */
  public Object getCode() {
    return code;
  }

  /**
   * @return A própria instância da classe.
   */
  public Code<T> getObject() {
    return this;
  }

  /**
   * Método utilitário que gera uma lista com identificadores do tipo T.
   * 
   * @param <T> Tipo do objeto da lista.
   * @param originalList Lista original de objetos.
   * 
   * @see Identifiable
   * @see Code
   * 
   * @return Lista com identificadores de um tipo T.
   */
  public static <T extends Identifiable<T>> List<Code<T>> getCodeList(
    List<T> originalList) {
    List<Code<T>> codeList = new ArrayList<Code<T>>();
    for (T t : originalList) {
      codeList.add(t.getId());
    }
    return codeList;
  }

  /**
   * Compara se esse objeto é igual a outro. Somente são iguais se os códigos
   * também são.
   * 
   * @param other o objeto com o qual esse está sendo comparado.
   * @return verdadeiro, se forem iguais ou falso, caso contrário.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Code<?>)) {
      return false;
    }
    Code<?> that = (Code<?>) other;
    if (that.code == null) {
      return this.code == null;
    }
    return trimmedString.equals(that.trimmedString);
  }

  /**
   * Obtém o valor de hashcode desse objeto. Corresponde ao próprio valor hash
   * do código.
   * 
   * @return o hashcode do código
   */
  @Override
  public int hashCode() {
    return trimmedString.hashCode();
  }

  /**
   * Obtém o texto a ser apresentado.
   * 
   * @return texto referente ao código.
   */
  @Override
  public String toString() {
    return trimmedString;
  }

  /**
   * {@inheritDoc}
   * 
   * Os identificadores são transformados em Strings e ordenados da seguinte
   * forma: se são numéricos, ordenados em ordem crescente. Caso contrário,
   * ordenados de forma alfabética.
   */
  @Override
  public int compareTo(Code<T> other) {
    if (other == null) {
      return 1;
    }
    try {
      int thisInt = Integer.parseInt(trimmedString);
      int otherInt = Integer.parseInt(other.trimmedString);
      return (thisInt < otherInt) ? -1 : (thisInt == otherInt) ? 0 : 1;
    }
    catch (NumberFormatException e) {
      return (trimmedString).compareTo(other.trimmedString);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object getUniqueCode() {
    return code;
  }

  /**
   * Converte o tipo do identificador sem warnings. Esse método é necessário,
   * pois existem diversas conversões que são legais, mas não são possíveis com
   * um cast normal.<br>
   * <br>
   * 
   * Suponha que você tem uma variável que é um Code de SinglePoint<?>. <br>
   * Essa variável deveria poder ser atribuída a um Code de Point<?> já que
   * SinglePoint<?> herda de Point<?>, mas isso gera um erro. <br>
   * Nem com um cast, isso funciona. <br>
   * Você pode fazer o cast para Code sem tipo definido, mas aí será necessário
   * suprimir dois warnings: unchecked e rawtypes. <br>
   * Já com o método Code.cast(), você pode fazer essa conversão sem warning nem
   * SuppressWarning.<br>
   * Em alguns casos, quando não for possível inferir sozinho, é necessário
   * explicitar o tipo.<br>
   * 
   * <pre>
   * <code>
   * Code<SinglePoint<?>> code1 = getCodeFromSomewhere();
   * //Code<Point<?>> code2 = (Code<Point<?>>) code1; // ERRO!
   * @SuppressWarnings({ "unchecked", "rawtypes" })
   * Code<Point<?>> code2 = (Code) code1; // WARNING!
   * Code<Point<?>> code3 = code1.cast();
   * pointCodeList.add(code1.<Point<?>> cast());
   * </code>
   * </pre>
   * 
   * @param <E> O novo tipo do identificador
   * @return O identificador
   */
  @SuppressWarnings("unchecked")
  public <E> Code<E> cast() {
    return (Code<E>) this;
  }
}
