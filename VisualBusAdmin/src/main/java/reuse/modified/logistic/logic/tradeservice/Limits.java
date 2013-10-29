package reuse.modified.logistic.logic.tradeservice;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe que define limites de data.
 */
public class Limits implements Serializable, Comparable<Limits>,
  Copyable<Limits> {
  /** Data inicial */
  private Long startDate;
  /** Data final */
  private Long endDate;

  /**
   * Construtor.
   * 
   * @param startDate data inicial.
   * @param endDate data final.
   */
  public Limits(Long startDate, Long endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Obtém a data inicial.
   * 
   * @return data inicial.
   */
  public Long getStartLimit() {
    return startDate;
  }

  /**
   * Altera a data inicial.
   * 
   * @param startDate data inicial.
   */
  public void setStartLimit(Long startDate) {
    this.startDate = startDate;
  }

  /**
   * Obtém a data final.
   * 
   * @return data final
   */
  public Long getEndLimit() {
    return endDate;
  }

  /**
   * Altera a data final.
   * 
   * @param endDate data final.
   */
  public void setEndLimit(Long endDate) {
    this.endDate = endDate;
  }

  /**
   * Compara o limite com outro, ordenando de acordo com as datas.
   * 
   * @param that limite a ser comparado.
   * 
   * @return um número negativo, zero ou positivo caso o limite seja anterior,
   *         igual ou posterior.
   */
  @Override
  public int compareTo(Limits that) {
    if (that == null) {
      return 1;
    }
    if (this.startDate == null && this.endDate == null) {
      if (that.startDate == null && that.endDate == null) {
        return 0;
      }
      return -1;
    }
    if (that.startDate == null && that.endDate == null) {
      return 1;
    }
    if (this.startDate != null && that.startDate != null) {
      return this.startDate.compareTo(that.startDate);
    }
    if (this.endDate != null && that.endDate != null) {
      return this.endDate.compareTo(that.endDate);
    }
    if (this.startDate != null) {
      return this.startDate.compareTo(that.endDate);
    }
    return this.endDate.compareTo(that.startDate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || !(obj instanceof Limits)) {
      return false;
    }
    Limits that = (Limits) obj;
    return (equals(startDate, that.getStartLimit()) && equals(endDate, that
      .getEndLimit()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if ((startDate == null) && (endDate == null)) {
      return "";
    }
    String startDateFormatted =
      (startDate == null) ? "    " : new Date(startDate).toString();
    String endDateFormatted =
      (endDate == null) ? "    " : new Date(endDate).toString();
    String limitsString = startDateFormatted + " - " + endDateFormatted;
    return limitsString;
  }

  /**
   * Verifica se dois Long são iguais.
   * 
   * @param l1 long a ser comparado.
   * @param l2 long a ser comparado.
   * 
   * @return verdadeiro se os dois forem iguais
   */
  private boolean equals(Long l1, Long l2) {
    if ((l1 == null) && (l2 == null)) {
      return true;
    }
    if ((l1 == null) || (l2 == null)) {
      return false;
    }
    return l1.equals(l2);
  }

  /**
   * Verifica se este limite é exatamente igual a outro.
   * 
   * @param other o limite a ser comparado.
   * 
   * @return verdadeiro se forem iguais.
   */
  public boolean isIdentical(Limits other) {
    return equalsOrNull(this.startDate, other.startDate)
      && equalsOrNull(this.endDate, other.endDate);
  }

  /**
   * Verifica se os dois objetos são iguais ou são nulos.
   * 
   * @param one um objeto.
   * @param other outro objeto.
   * 
   * @return verdadeiro se os dois objetos forem iguais ou nulos.
   */
  private boolean equalsOrNull(Object one, Object other) {
    return (one == null && other == null)
      || ((one != null) && one.equals(other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Limits copy() {
    return new Limits(startDate, endDate);
  }

  /**
   * Verifica se este limite tem interseção com outro limite.
   * 
   * @param other o limite a ser verificado.
   * 
   * @return verdadeiro se tiver interseção. Falso, caso contrário.
   */
  public boolean intersects(Limits other) {
    if (other == null) {
      return false;
    }
    if (startDate != null && other.startDate != null && endDate != null
      && other.endDate != null) {
      return (startDate <= other.endDate) && (endDate >= other.startDate);
    }
    return isIdentical(other);
  }
}
