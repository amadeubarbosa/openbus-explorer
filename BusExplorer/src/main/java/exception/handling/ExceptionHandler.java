package exception.handling;

/**
 * Classe utilit�ria para reusar os c�digos de tratamento de exce��es.
 * 
 * @author Tecgraf
 * @param <T> O tipo de exce��o que ser� tratado por esta classe.
 */
public abstract class ExceptionHandler<T extends HandlingException<?>> {

  /**
   * M�todo a ser disparado pela aplica��o para tratar a exce��o ocorrida no
   * contexto indicado.
   * 
   * @param theException a exce��o.
   * @param context o contexto de execu��o em que o erro ocorreu.
   * @return um enum do tipo da exce��o para que a aplica��o possa fazer uso de
   *         switch, facilitando o tratamento da exce��o que � particular a cada
   *         contexto de execu��o (parte do tratamento da exce��o que n�o �
   *         reutiliz�vel para todos os contextos)
   */
  public T process(Exception theException, ExceptionContext context) {
    T exception = getHandlingException(theException, context);
    handleException(exception);
    return exception;
  }

  /**
   * A id�ia deste m�todo � chamar o construtor de {@link HandlingException}
   * espec�fco, que por sua vez ir� utilizar o m�todo
   * {@link HandlingException#getTypeFromException(Exception)} para obter a
   * enumera��o espec�fica ({@link ExceptionType}).
   * 
   * @param realException a exce��o ocorrida.
   * @param context o contexto da exce��o.
   * @return a inst�ncia de {@link HandlingException} a ser tratada.
   */
  protected abstract T getHandlingException(Exception realException,
    ExceptionContext context);

  /**
   * M�todo a ser definido pela aplica��o para de fato explicitar o tratamento
   * da exce��o que deve ser reutilizado.
   * 
   * @param exception a exce��o a ser tratada.
   */
  protected abstract void handleException(T exception);

}
