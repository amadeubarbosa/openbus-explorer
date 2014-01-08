package busexplorer.exception;

import exception.handling.ExceptionContext;
import exception.handling.ExceptionType;
import exception.handling.HandlingException;

/**
 * Wrapper de exce��es espec�ficos para os demos de colabora��o. Esta classe
 * esta associada a enumera��o {@link ExceptionType}.
 * 
 * @author Tecgraf
 */
public class BusExplorerHandlingException extends
  HandlingException<ExceptionType> {

  /**
   * Mensagem de erro a ser exibida caso apresente um di�logo como tratamento da
   * exce��o.
   */
  private String errorMsg = null;

  /**
   * Construtor.
   * 
   * @param exception a exce��o a ser tratada
   * @param context o contexto no qual a exce��o ocorreu.
   */
  public BusExplorerHandlingException(Exception exception,
    ExceptionContext context) {
    super(exception, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ExceptionType getTypeFromException(Exception exception) {
    return ExceptionType.getType(exception);
  }

  /**
   * Configura a mensagem de erro a ser exibida.
   * 
   * @param message a mensagem de erro a ser associada � exce��o.
   */
  public void setErrorMessage(String message) {
    this.errorMsg = message;
  }

  /**
   * Recupera a mensagem de erro a ser exibida.
   * 
   * @return a mensagem de erro constru�da para a exce��o tratada.
   */
  public String getErrorMessage() {
    return this.errorMsg;
  }

}