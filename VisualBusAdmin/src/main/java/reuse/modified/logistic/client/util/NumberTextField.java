package reuse.modified.logistic.client.util;

import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import reuse.modified.logistic.client.util.text.Formatter;

/**
 * Campo que s� permite a digita��o de n�meros, ".", ",", "-", <i>backspace</i>,
 * <i>delete</i>, e as teclas de navega��o <i>home</i>, <i>end</i>, <i>left</i>
 * e <i>right</i>. Tamb�m � formatado para n�meros de acordo com <i>pattern</i>
 * passado.
 * 
 * @author Tecgraf
 */
public class NumberTextField extends NumberField {

  /** Formatador */
  private NumberFormat format;

  /** Indicador se este number text field aceita nulo como um valor v�lido */
  private boolean nullable = false;

  /**
   * Construtor.
   * 
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   */
  public NumberTextField(JLabel msgLabel) {
    this(msgLabel, "");
  }

  /**
   * Construtor.
   * 
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param pattern o pattern do formato ao qual o campo obedecer�
   */
  public NumberTextField(JLabel msgLabel, String pattern) {
    this(msgLabel, pattern, false);
  }

  /**
   * Construtor.
   * 
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param pattern o pattern do formato ao qual o campo obedecer�
   * @param nullable indicador se aceita valores nulos
   */
  public NumberTextField(JLabel msgLabel, String pattern, boolean nullable) {
    super(buildFormat(pattern), msgLabel);
    this.nullable = nullable;
    setFormatPattern(pattern);
    setInputVerifier(new FieldVerifier());
  }

  /**
   * Construtor.
   * 
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param pattern o pattern do formato ao qual o campo obedecer�
   * @param errorMsg a mensagem de erro
   */
  public NumberTextField(JLabel msgLabel, String pattern, String errorMsg) {
    this(msgLabel, pattern, errorMsg, false);
  }

  /**
   * Construtor.
   * 
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param pattern o pattern do formato ao qual o campo obedecer�
   * @param errorMsg a mensagem de erro
   * @param nullable indicador se aceita valores nulos
   */
  public NumberTextField(JLabel msgLabel, String pattern, String errorMsg,
    boolean nullable) {
    super(buildFormat(pattern), msgLabel, errorMsg);
    this.nullable = nullable;
    setFormatPattern(pattern);
    setInputVerifier(new FieldVerifier());
  }

  /**
   * Sobrescrito para travar caracteres indesejados.
   * 
   * @param e o evento de teclado.
   */
  @Override
  protected void processKeyEvent(KeyEvent e) {

    int keyCode = e.getKeyCode();
    char keyChar = e.getKeyChar();

    if (((keyChar >= '0') && (keyChar <= '9'))
      || ((keyChar == '.' || keyChar == ',') && !format.isParseIntegerOnly())
      || (keyChar == '-') || (keyCode == KeyEvent.VK_BACK_SPACE)
      || (keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_ENTER)
      || (keyCode == KeyEvent.VK_TAB) || (keyCode == KeyEvent.VK_DELETE)
      || (keyCode == KeyEvent.VK_HOME) || (keyCode == KeyEvent.VK_END)
      || (keyCode == KeyEvent.VK_LEFT) || (keyCode == KeyEvent.VK_RIGHT)) {
      super.processKeyEvent(e);
    }
    else {
      return;
    }
  }

  /**
   * Construtor de formatos para o campo de acordo com o padr�o passado.
   * 
   * @param pattern o padr�o
   * @return o formatador.
   */
  synchronized private static NumberFormat buildFormat(String pattern) {
    NumberFormat format;
    if (pattern == null || pattern.equals("")) {
      format = Formatter.getNumberShowFormat("#,##0.00");
    }
    else {
      format = Formatter.getNumberShowFormat(pattern);
    }
    return format;
  }

  /**
   * Define o padr�o a ser usado para a formata��o do campo.
   * 
   * @param pattern o padr�o
   */
  public void setFormatPattern(String pattern) {
    this.format = buildFormat(pattern);
    if (nullable) {
      setFormatterFactory(new DefaultFormatterFactory(
        new NullableNumberFormatter(this.format)));
    }
    else {
      setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(
        this.format)));
    }
  }

  /**
   * Define o formato ao qual o valor do campo estar� restrito.
   * 
   * @param format o formato
   */
  public void setFormat(NumberFormat format) {
    this.format = format;
    if (nullable) {
      setFormatterFactory(new DefaultFormatterFactory(
        new NullableNumberFormatter(this.format)));
    }
    else {
      setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(
        this.format)));
    }
  }

  /**
   * Determina se avalia nulo como v�lido ou n�o.
   * 
   * @param nullable indicador se aceita valores nulos
   */
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  /**
   * {@inheritDoc}
   * 
   * Verifica se o valor do campo est� entre o m�nimo e o m�ximo especificado.
   */
  @Override
  public boolean isValid(Object o) {
    if (nullable && (o == null)) {
      return true;
    }
    return super.isValid(o);
  }

  /**
   * Verifica o valor digitado como entrada no campo.
   */
  public class FieldVerifier extends InputVerifier {
    /**
     * Fun��o de verifica��o.
     * 
     * @param comp o componente associado.
     * @return um indicativo de verifica��o ok.
     */
    @Override
    public boolean verify(JComponent comp) {
      JTextField textField = (JTextField) comp;
      String content = textField.getText();
      return checkInput(content);
    }

    /**
     * Determina se a entrada � v�lida. Se for, retorna verdadeiro, indicando
     * que o foco pode mudar. Caso contr�rio, retorna falso para que o foco
     * fique preso at� que a entrada seja v�lida. Este m�todo dispara
     * propertyChange do tipo "input valid"
     * 
     * @param input o componente de entrada.
     * @return um indicativo (flag).
     */
    @Override
    public boolean shouldYieldFocus(JComponent input) {
      return true;
    }

  }

  /**
   * Verifica se a entrada � vazia.
   * 
   * @return Retorna verdadeiro se a entrada for vazia, e retorna falso caso
   *         contr�rio.
   */
  public boolean isEmpty() {
    return getText().isEmpty();
  }

  /**
   * Verifica se a cadeia fornecida � um n�mero bem formado
   * 
   * @return Verdadeiro se o n�mero � bem formado, e falso caso contr�rio.
   */
  public boolean isWellFormed() {
    try {
      format.parseObject(getText());
    }
    catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Verifica se a entrada � v�lida.
   * 
   * @param proposedValue Entrada a ser verificada.
   * @return <code>True</code> se a entrada for v�lida, <code>false</code> caso
   *         contr�rio.
   */
  private boolean checkInput(String proposedValue) {
    try {
      if (proposedValue.length() > 0) {
        Object value = format.parseObject(proposedValue);
        if (!isValid(value)) {
          return false;
        }
        setValue(value);
        return true;
      }
      if (nullable) {
        setValue(null);
      }
      return nullable;
    }
    catch (ParseException e) {
      return false;
    }
  }

}
