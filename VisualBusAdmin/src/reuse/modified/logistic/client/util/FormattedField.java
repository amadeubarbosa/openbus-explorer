package reuse.modified.logistic.client.util;

import java.awt.Toolkit;
import java.text.Format;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Um campo formatado. Sempre que for digitado um valor que n�o corresponde ao
 * formato especificado, escreve uma mensagem em um r�tulo usado como status.
 * 
 * @author mjulia
 */
public class FormattedField extends JFormattedTextField {
  /** O formator usado por esse componente. */
  private Format formatter;
  /** O r�tulo onde a mensagem de erro deve ser escrita. */
  private JLabel msgLabel;
  /** A mensagem de erro. */
  private String errorMsg;

  /**
   * Verifica o valor digitado como entrada no campo.
   */
  public class FieldVerifier extends InputVerifier {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(JComponent comp) {
      JTextField textField = (JTextField) comp;
      String content = textField.getText();
      return checkInput(content);
    }

    /**
     * {@inheritDoc}
     * 
     * Determina se a entrada � v�lida. Se for, retorna verdadeiro, indicando
     * que o foco pode mudar. Caso contr�rio, retorna falso para que o foco
     * fique preso at� que a entrada seja v�lida. Este m�todo dispara
     * propertyChange do tipo "input valid"
     */
    @Override
    public boolean shouldYieldFocus(JComponent input) {
      boolean valid = super.shouldYieldFocus(input);
      if (!valid) {
        showErrorMsg(true);
        firePropertyChange("input valid", true, false);
      }
      else {
        showErrorMsg(false);
        firePropertyChange("input valid", false, true);
      }
      return valid;
    }
  }

  /**
   * Construtor.
   * 
   * @param formater o formatador usado no campo
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   * @param errorMsg a mensagem de erro
   */
  public FormattedField(Format formater, JLabel msgLabel, String errorMsg) {
    super(formater);
    this.formatter = formater;
    this.msgLabel = msgLabel;
    this.errorMsg = errorMsg;
    setFocusLostBehavior(JFormattedTextField.COMMIT);
    setInputVerifier(new FieldVerifier());
  }

  /**
   * Construtor.
   * 
   * @param formater o formatador usado no campo
   * @param msgLabel o r�tulo de status onde a mensagem de erro � escrita
   */
  public FormattedField(Format formater, JLabel msgLabel) {
    this(formater, msgLabel, "");
  }

  /**
   * Muda a mensagem de erro a ser exibida.
   * 
   * @param errorMsg a nova mensagem de erro
   */
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  /**
   * Verifica se a entrada � v�lida.
   * 
   * @param proposedValue .
   * 
   * @return .
   */
  private boolean checkInput(String proposedValue) {
    try {
      if (proposedValue.length() > 0) {
        Object value = formatter.parseObject(proposedValue);
        if (!isValid(value))
          return false;
        setValue(value);
      }
      else {
        setValue(null);
      }
      return true;
    }
    catch (ParseException e) {
      return false;
    }
  }

  /**
   * Verifica se um valor � v�lido. Pode ser redefinido para fazer outras
   * valida��es al�m daquelas j� impostas pelo formatador sendo utilizado.
   * 
   * @param value o valor a ser validado
   * @return verdadeiro ou falso.
   */
  public boolean isValid(Object value) {
    return true;
  }

  /**
   * Verifica se o valor corrente � v�lido.
   * 
   * @return verdadeiro se o valor corrente for v�lido, falso caso contr�rio
   */
  @Override
  public boolean isEditValid() {
    try {
      commitEdit();
      Object value = getValue();
      boolean valid = isValid(value);
      showErrorMsg(!valid);
      return valid;
    }
    catch (ParseException e) {
      showErrorMsg(true);
      return false;
    }
  }

  /**
   * Escreve um texto na mensagem de erro, indicando que o valor do campo est�
   * incorreto ou limpa a mensagem se o valor estiver correto.
   * 
   * @param error verdadeiro ou falso
   */
  protected void showErrorMsg(boolean error) {
    if (error) {
      Toolkit.getDefaultToolkit().beep();
      if (msgLabel != null) {
        msgLabel.setText(errorMsg);
        msgLabel.setIcon(UI.WARNING_ICON);
      }
    }
    else {
      if (msgLabel != null && msgLabel.getText().equals(errorMsg)) {
        msgLabel.setText(" ");
        msgLabel.setIcon(null);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void firePropertyChange(String type, Object oldVal, Object newVal) {
    super.firePropertyChange(type, oldVal, newVal);
  }
}
