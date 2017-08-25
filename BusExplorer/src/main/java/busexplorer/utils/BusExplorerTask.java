package busexplorer.utils;

import busexplorer.Application;
import busexplorer.desktop.dialog.ExceptionDialog;
import busexplorer.exception.BusExplorerHandlingException;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.exception.handling.ExceptionHandler;
import busexplorer.exception.handling.ExceptionType;
import tecgraf.javautils.gui.Task;

import java.awt.Dialog.ModalityType;


/**
 * A classe abstrata BusExplorerTask permite a realiza��o das tarefas do
 * BusExplorer em threads separadas. Ela realiza o tratamento padr�o de exce��es
 * para tais tarefas.
 * 
 * @author Tecgraf
 * @param <T> A classe do resultado da tarefa.
 */
public abstract class BusExplorerTask<T> extends Task<T> {

  /** Manipulador de exce��es */
  private ExceptionHandler<BusExplorerHandlingException> handler;
  /** Contexto das exce��es recebidas */
  private ExceptionContext context;

  /**
   * Construtor para garantir o uso do {@link ExceptionHandler<BusExplorerHandlingException>}
   * definido na {@link Application#exceptionHandler()}.
   *
   * @param context Contexto das exce��es recebidas.
   */
  public BusExplorerTask(ExceptionContext context) {
    this.handler = Application.exceptionHandler();
    this.context = context;
  }

  /**
   * Construtor para permitir a defini��o de um {@link ExceptionHandler}
   * diferente do padr�o definido na {@link Application#exceptionHandler()} e tamb�m
   * permite configurar o modal da janela, com uso do construtor {@link Task#Task(ModalityType)}.
   * 
   * @param modality Tipo de modalidade do di�logo.
   * @param handler Manipulador de exce��es.
   * @param context Contexto das exce��es recebidas.
   */
  public BusExplorerTask(ExceptionContext context,
                         ExceptionHandler<BusExplorerHandlingException> handler,
                         ModalityType modality) {
    super(modality);
    this.context = context;
    this.handler = handler;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleError(Exception exception) {
    BusExplorerHandlingException handlingException =
      handler.process(exception, context);
    if ((exception != null) &&
      (ExceptionType.getType(exception) == ExceptionType.Unspecified)) {
      ExceptionDialog.createDialog(parentWindow, taskTitle,
        handlingException.getException(), "").setVisible(true);
    } else {
      ExceptionDialog.createDialog(parentWindow, taskTitle,
        handlingException.getException(),
        handlingException.getErrorMessage()).setVisible(true);
    }
  }

  /**
   * Tarefa a ser executada. Esse m�todo deve ser implementado para executar a
   * tarefa.
   *
   * @throws Exception quando a ocorr�ncia de um erro (representado pela
   *         exce��o) impediu a realiza��o da tarefa
   *
   * @see Task#performTask()
   */
  protected abstract void doPerformTask() throws Exception;

  /**
   * M�todo para estender o tratamento de erros da {@link Task#performTask()} que n�o
   * fornece tratamento para erros derivados de {@link java.lang.Error}.
   * <p>
   * Para contornar essa limita��o, e poder apresentar os detalhes ao usu�rio, � lan�ado
   * um {@link RuntimeException} com o erro original aninhado.
   *
   * @throws Exception a exce��o original caso o {@link BusExplorerTask#doPerformTask()}
   *         tenha lan�ado uma exce��o.
   * @throws RuntimeException uma exce��o de runtime contendo o {@link java.lang.Error}
   *         caso o {@link BusExplorerTask#doPerformTask()} tenha lan�ado um erro.
   *
   * @see Task#performTask()
   */
  protected final void performTask() throws Exception {
    try {
      this.doPerformTask();
    } catch (Error e) {
      throw new RuntimeException(e);
    }
  }
}
