package busexplorer.exception;

import exception.handling.ExceptionContext;
import exception.handling.ExceptionType;
import exception.handling.HandlingException;

/**
 * Wrapper de exceções específicos para os demos de colaboração. Esta classe
 * esta associada a enumeração {@link ExceptionType}.
 * 
 * @author Tecgraf
 */
public class BusExplorerHandlingException extends
  HandlingException<ExceptionType> {

  /**
   * Mensagem de erro a ser exibida caso apresente um diálogo como tratamento da
   * exceção.
   */
  private String errorMsg = null;

  /**
   * Construtor.
   * 
   * @param exception a exceção a ser tratada
   * @param context o contexto no qual a exceção ocorreu.
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
   * @param message a mensagem de erro a ser associada à exceção.
   */
  public void setErrorMessage(String message) {
    this.errorMsg = message;
  }

  /**
   * Recupera a mensagem de erro a ser exibida.
   * 
   * @return a mensagem de erro construída para a exceção tratada.
   */
  public String getErrorMessage() {
    return this.errorMsg;
  }

}