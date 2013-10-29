/*
 * $Id$
 */
package reuse.modified.logistic.logic.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tecgraf.javautils.concurrent.locks.SharedAccessObject;

/**
 * Classe gen�rica usada para encapsular c�digos de objetos. O tipo param�trico
 * T � a classe para a qual o c�digo est� sendo criado. Por exemplo Code<User>
 * representa a classe que encapsula um c�digo de usu�rio. Funciona como um
 * "wrapper" para o c�digo que, na maior parte dos casos, � proveniente do banco
 * de dados.
 * 
 * @param <T> o tipo do objeto que possui esse identificador.
 * 
 * @author mjulia
 */
public class Code<T> implements Serializable, Comparable<Code<T>>,
  SharedAccessObject {

  /**
   * Identifica��o da vers�o da classe, segundo a serializa��o padr�o de Java.
   */
  private static final long serialVersionUID = 7617061903509182552L;

  /** O c�digo. */
  private final Object code;

  /** Representa��o string do c�digo */
  private final String trimmedString;

  /**
   * Constr�i um c�digo para um objeto da classe parametrizada por T.
   * 
   * @param code o valor do c�digo
   */
  public Code(Object code) {
    this.code = code;
    this.trimmedString = ("" + code).trim();
  }

  /**
   * Obt�m o valor do c�digo.
   * 
   * @return o c�digo encapsulado
   */
  public Object getCode() {
    return code;
  }

  /**
   * @return A pr�pria inst�ncia da classe.
   */
  public Code<T> getObject() {
    return this;
  }

  /**
   * M�todo utilit�rio que gera uma lista com identificadores do tipo T.
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
   * Compara se esse objeto � igual a outro. Somente s�o iguais se os c�digos
   * tamb�m s�o.
   * 
   * @param other o objeto com o qual esse est� sendo comparado.
   * @return verdadeiro, se forem iguais ou falso, caso contr�rio.
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
   * Obt�m o valor de hashcode desse objeto. Corresponde ao pr�prio valor hash
   * do c�digo.
   * 
   * @return o hashcode do c�digo
   */
  @Override
  public int hashCode() {
    return trimmedString.hashCode();
  }

  /**
   * Obt�m o texto a ser apresentado.
   * 
   * @return texto referente ao c�digo.
   */
  @Override
  public String toString() {
    return trimmedString;
  }

  /**
   * {@inheritDoc}
   * 
   * Os identificadores s�o transformados em Strings e ordenados da seguinte
   * forma: se s�o num�ricos, ordenados em ordem crescente. Caso contr�rio,
   * ordenados de forma alfab�tica.
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
   * Converte o tipo do identificador sem warnings. Esse m�todo � necess�rio,
   * pois existem diversas convers�es que s�o legais, mas n�o s�o poss�veis com
   * um cast normal.<br>
   * <br>
   * 
   * Suponha que voc� tem uma vari�vel que � um Code de SinglePoint<?>. <br>
   * Essa vari�vel deveria poder ser atribu�da a um Code de Point<?> j� que
   * SinglePoint<?> herda de Point<?>, mas isso gera um erro. <br>
   * Nem com um cast, isso funciona. <br>
   * Voc� pode fazer o cast para Code sem tipo definido, mas a� ser� necess�rio
   * suprimir dois warnings: unchecked e rawtypes. <br>
   * J� com o m�todo Code.cast(), voc� pode fazer essa convers�o sem warning nem
   * SuppressWarning.<br>
   * Em alguns casos, quando n�o for poss�vel inferir sozinho, � necess�rio
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
