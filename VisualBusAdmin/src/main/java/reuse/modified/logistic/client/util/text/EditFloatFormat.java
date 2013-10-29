package reuse.modified.logistic.client.util.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import tecgraf.javautils.LNG;

/**
 * Classe que formata números decimais a serem editados.
 */
public class EditFloatFormat extends Format {

  /**
   * {@inheritDoc}
   */
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo,
    FieldPosition pos) {
    if (obj == null) {
      return null;
    }
    String text = obj.toString();
    if (!(obj instanceof Float)) {
      return toAppendTo.append(obj.toString());
    }
    if (text.matches(".*\\.999+[0-9]$")) {
      float f = (Float) obj;
      text = String.valueOf(++f).replaceFirst("\\.999+[0-9]$", "");
    }
    text = text.replaceFirst("\\.000+[0-9]$", "");
    text = text.replace('.', ',').replaceFirst(",0$", "");
    return toAppendTo.append(text);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parseObject(String source, ParsePosition pos) {
    if (pos == null) {
      throw new NullPointerException();
    }
    try {
      return Float.valueOf(source.replace(',', '.'));
    }
    catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parseObject(String source) throws ParseException {
    Object result = parseObject(source, new ParsePosition(0));
    if (result == null) {
      throw new ParseException(LNG.get("EditFloatFormat.invalid.float"), 0);
    }
    return result;
  }

  /**
   * Transforma um texto em um número decimal.
   * 
   * @param text texto a ser transformado.
   * 
   * @return número decimal.
   * @throws ParseException erro na transformação do texto em número.
   */
  public Float parse(String text) throws ParseException {
    return (Float) parseObject(text);
  }
}
