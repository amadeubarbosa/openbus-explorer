package reuse.modified.logistic.client.util;

import java.text.Format;

import javax.swing.JLabel;

import tecgraf.javautils.LNG;

/**
 * Campo formatado para entradas de números. Sempre que for digitado um valor
 * que não seja um número, escreve uma mensagem de erro em um rótulo usado como
 * status.
 */
public class NumberField extends FormattedField {
  /**
   * O valor máximo permitido, onde <code>null</code> indica que não há valor
   * máximo.
   */
  private Number maxValue;
  /**
   * O valor mínimo permitido, onde <code>null</code> indica que não há valor
   * mínimo.
   */
  private Number minValue;

  /**
   * Construtor.
   * 
   * @param format o formatador usado no campo
   * @param msgLabel o rótulo de status onde a mensagem de erro é escrita
   * @param errorMsg a mensagem de erro
   */
  public NumberField(Format format, JLabel msgLabel, String errorMsg) {
    super(format, msgLabel, errorMsg);
  }

  /**
   * Construtor.
   * 
   * @param format o formatador usado no campo
   * @param msgLabel o rótulo de status onde a mensagem de erro é escrita
   */
  public NumberField(Format format, JLabel msgLabel) {
    this(format, msgLabel, LNG.get("NumberField.invalid.number"));
  }

  /**
   * Obtém o valor do campo
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
   * Define o valor máximo permitido.
   * 
   * @param maxValue Se o valor fornecido for diferente de <code>null</code>,
   *        então esse valor é o valor máximo permitido. Caso contrário, o
   *        número não possui um valor máximo.
   */
  public void setMaxValue(Number maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Verifica se o número possui um valor máximo permitido.
   * 
   * @return Vedadeiro se o número é diferente de <code>null</code>, e falso
   *         caso contrário
   */
  public boolean hasMaxValue() {
    return maxValue != null;
  }

  /**
   * Verifica se o número possui um valor mínimo permitido.
   * 
   * @return Vedadeiro se o número é diferente de <code>null</code>, e falso
   *         caso contrário
   */
  public boolean hasMinValue() {
    return minValue != null;
  }

  /**
   * Obtém o valor máximo permitido.
   * 
   * @return Caso exista um valor máximo permitido, ele é retornado. Caso
   *         contrário, <code>null</code> é retornado.
   * 
   */
  public Double getMaxValue() {
    if (hasMaxValue()) {
      return maxValue.doubleValue();
    }
    return null;
  }

  /**
   * Define o valor mínimo permitido.
   * 
   * @param minValue Se o valor fornecido for diferente de <code>null</code>,
   *        então esse valor é o valor mínimo permitido. Caso contrário, o
   *        número não possui um valor mínimo.
   */
  public void setMinValue(Number minValue) {
    this.minValue = minValue;
  }

  /**
   * Obtém o valor mínimo permitido.
   * 
   * @return Caso exista um valor mínimo permitido, ele é retornado. Caso
   *         contrário, <code>null</code> é retornado.
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
   * Verifica se o valor do campo está entre o mínimo e o máximo especificado.
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
