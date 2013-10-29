package reuse.modified.logistic.client.util;

import java.text.Format;

import javax.swing.JLabel;

import tecgraf.javautils.LNG;

/**
 * Campo formatado para entradas de n�meros. Sempre que for digitado um valor
 * que n�o seja um n�mero, escreve uma mensagem de erro em um r�tulo usado como
 * status.
 */
public class NumberField extends FormattedField {
  /**
   * O valor m�ximo permitido, onde <code>null</code> indica que n�o h� valor
   * m�ximo.
   */
  private Number maxValue;
  /**
   * O valor m�nimo permitido, onde <code>null</code> indica que n�o h� valor
   * m�nimo.
   */
  private Number minValue;

  /**
   * Construtor.
   * 
   * @param format o formatador usado no campo
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param errorMsg a mensagem de erro
   */
  public NumberField(Format format, JLabel msgLabel, String errorMsg) {
    super(format, msgLabel, errorMsg);
  }

  /**
   * Construtor.
   * 
   * @param format o formatador usado no campo
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   */
  public NumberField(Format format, JLabel msgLabel) {
    this(format, msgLabel, LNG.get("NumberField.invalid.number"));
  }

  /**
   * Obt�m o valor do campo
   * 
   * @return o valor do campo
   */
  public Number getNumber() {
    return (Number) getValue();
  }

  /**
   * Muda o valor do campo
   * 
   * @param value o novo valor do campo
   */
  public void setNumber(Number value) {
    setValue(value);
  }

  /**
   * Define o valor m�ximo permitido.
   * 
   * @param maxValue Se o valor fornecido for diferente de <code>null</code>,
   *        ent�o esse valor � o valor m�ximo permitido. Caso contr�rio, o
   *        n�mero n�o possui um valor m�ximo.
   */
  public void setMaxValue(Number maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Verifica se o n�mero possui um valor m�ximo permitido.
   * 
   * @return Vedadeiro se o n�mero � diferente de <code>null</code>, e falso
   *         caso contr�rio
   */
  public boolean hasMaxValue() {
    return maxValue != null;
  }

  /**
   * Verifica se o n�mero possui um valor m�nimo permitido.
   * 
   * @return Vedadeiro se o n�mero � diferente de <code>null</code>, e falso
   *         caso contr�rio
   */
  public boolean hasMinValue() {
    return minValue != null;
  }

  /**
   * Obt�m o valor m�ximo permitido.
   * 
   * @return Caso exista um valor m�ximo permitido, ele � retornado. Caso
   *         contr�rio, <code>null</code> � retornado.
   * 
   */
  public Double getMaxValue() {
    if (hasMaxValue()) {
      return maxValue.doubleValue();
    }
    return null;
  }

  /**
   * Define o valor m�nimo permitido.
   * 
   * @param minValue Se o valor fornecido for diferente de <code>null</code>,
   *        ent�o esse valor � o valor m�nimo permitido. Caso contr�rio, o
   *        n�mero n�o possui um valor m�nimo.
   */
  public void setMinValue(Number minValue) {
    this.minValue = minValue;
  }

  /**
   * Obt�m o valor m�nimo permitido.
   * 
   * @return Caso exista um valor m�nimo permitido, ele � retornado. Caso
   *         contr�rio, <code>null</code> � retornado.
   * 
   */
  public Double getMinValue() {
    if (hasMinValue()) {
      return minValue.doubleValue();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * Verifica se o valor do campo est� entre o m�nimo e o m�ximo especificado.
   */
  @Override
  public boolean isValid(Object o) {
    Number value = (Number) o;
    double currentValue = value.floatValue();
    boolean valid = true;
    if (hasMinValue()) {
      valid = currentValue >= minValue.floatValue();
    }
    if (hasMaxValue()) {
      valid = valid && (currentValue <= maxValue.floatValue());
    }
    return valid;
  }
}
