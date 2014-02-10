package busexplorer.utils;

import java.awt.Dialog.ModalityType;

import tecgraf.javautils.gui.StandardDialogs;
import busexplorer.exception.BusExplorerHandlingException;
import exception.handling.ExceptionContext;
import exception.handling.ExceptionHandler;

/**
 * A classe abstrata BusExplorerTask permite a realiza��o das tarefas do
 * BusExplorer em threads separadas. Ela realiza o tratamento padr�o de exce��es
 * para tais tarefas.
 * 
 * @author Tecgraf
 * @param <T> A classe do resultado da tarefa.
 */
public abstract class BusExplorerTask<T> extends tecgraf.javautils.gui.Task<T> {

  /** Manipulador de exce��es */
  private ExceptionHandler<BusExplorerHandlingException> handler;
  /** Contexto das exce��es recebidas */
  private ExceptionContext context;

  /**
   * Construtor.
   * 
   * @param handler Manipulador de exce��es.
   * @param context Contexto das exce��es recebidas.
   */
  public BusExplorerTask(
    ExceptionHandler<BusExplorerHandlingException> handler,
    ExceptionContext context) {
    super();
    this.handler = handler;
    this.context = context;
  }

  /**
   * Construtor.
   * 
   * @param modality Tipo de modalidade do di�logo.
   * @param handler Manipulador de exce��es.
   * @param context Contexto das exce��es recebidas.
   */
  public BusExplorerTask(ModalityType modality,
    ExceptionHandler<BusExplorerHandlingException> handler,
    ExceptionContext context) {
    super(modality);
    this.handler = handler;
    this.context = context;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleError(Exception exception) {
    BusExplorerHandlingException handlingException =
      handler.process(exception, context);
    if (exception != null) {
      StandardDialogs.showErrorDialog(parentWindow, taskTitle,
        handlingException.getErrorMessage());
    }
  }
}
