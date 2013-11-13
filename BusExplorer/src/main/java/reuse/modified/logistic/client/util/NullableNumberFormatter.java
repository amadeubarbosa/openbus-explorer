package reuse.modified.logistic.client.util;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.NumberFormatter;

/**
 * Formatador de números com um comportamento similar à classe
 * <i>NumberFormatter</i>, diferenciando-se da mesma, apenas por permitir uma
 * entrada de valor vazia como um valor válido.
 * 
 * @author Tecgraf
 */
public class NullableNumberFormatter extends NumberFormatter {

  /**
   * Construtor.
   */
  public NullableNumberFormatter() {
    super();
  }

  /**
   * Construtor.
   * 
   * @param format o formato desejado.
   */
  public NullableNumberFormatter(NumberFormat format) {
    super(format);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object stringToValue(String text) throws ParseException {
    if (text.isEmpty()) {
      return null;
    }
    return super.stringToValue(text);
  }
}
