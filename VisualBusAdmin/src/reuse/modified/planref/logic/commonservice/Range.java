package reuse.modified.planref.logic.commonservice;

import java.io.Serializable;

/**
 * Representa uma faixa de valores, valores estes que devem implementar a
 * interface <code>Comparable</code>.
 * 
 * @author Tecgraf
 * @param <T> o tipo dos valores que a faixa conterá.
 * @see java.lang.Comparable
 */
public class Range<T extends Comparable<? super T>> implements Serializable,
  Cloneable {

  /** O início da faixa */
  private T start = null;
  /** O fim da faixa */
  private T end = null;

  /**
   * @param start
   * @param end
   */
  public Range(T start, T end) {
    super();
    this.start = start;
    this.end = end;
  }

  /**
   * @param value
   * @return <code>true</code> se o valor passado encontra-se dentro da faixa.
   *         <code>false</code> se o valor passado não encontra-se dentro da
   *         faixa.
   */
  public boolean includes(T value) {
    if (start != null) {
      if (start.compareTo(value) > 0) {
        return false;
      }
    }
    if (end != null) {
      if (end.compareTo(value) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return Returns the end.
   */
  public T getEnd() {
    return end;
  }

  /**
   * @param end The end to set.
   */
  public void setEnd(T end) {
    this.end = end;
  }

  /**
   * @return Returns the start.
   */
  public T getStart() {
    return start;
  }

  /**
   * @param start The start to set.
   */
  public void setStart(T start) {
    this.start = start;
  }

  /** {@inheritDoc} */
  @Override
  public Range<T> clone() {
    return new Range<T>(start, end);
  }

}
