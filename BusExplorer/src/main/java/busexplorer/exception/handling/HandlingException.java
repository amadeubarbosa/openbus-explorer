package busexplorer.exception.handling;

/**
 * Esta classe atua como um wrapper sobre as exce��es espec�ficas que desejamos
 * tratar.
 * 
 * @author Tecgraf
 * @param <T> a enumera��o de tipos de exce��es a serem tratadas.
 */
public abstract class HandlingException<T extends Enum<?>> {

  /** A exce��o */
  private Exception theException;

  /** O tipo */
  private T theType;

  /** O contexto */
  private ExceptionContext theContext;

  /**
   * Construtor.
   * 
   * @param exception a exce��o
   * @param context o contexto
   */
  public HandlingException(Exception exception, ExceptionContext context) {
    this.theException = exception;
    this.theType = getTypeFromException(exception);
    this.theContext = context;
  }

  /**
   * Recuper o tipo enum da exce��o.
   * 
   * @param exception
   * @return o tipo Enum da exce��o.
   */
  protected abstract T getTypeFromException(Exception exception);

  /**
   * Recupera a exce��o
   * 
   * @return a exce��o
   */
  public Exception getException() {
    return theException;
  }

  /**
   * Recupera o contexto
   * 
   * @return o contexto
   */
  public ExceptionContext getContext() {
    return theContext;
  }

  /**
   * Recupera o tipo da exce��o
   * 
   * @return o tipo
   */
  public T getType() {
    return theType;
  }

}