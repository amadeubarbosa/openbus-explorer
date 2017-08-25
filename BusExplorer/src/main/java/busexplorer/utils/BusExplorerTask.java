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
 * A classe abstrata BusExplorerTask permite a realização das tarefas do
 * BusExplorer em threads separadas. Ela realiza o tratamento padrão de exceções
 * para tais tarefas.
 * 
 * @author Tecgraf
 * @param <T> A classe do resultado da tarefa.
 */
public abstract class BusExplorerTask<T> extends Task<T> {

  /** Manipulador de exceções */
  private ExceptionHandler<BusExplorerHandlingException> handler;
  /** Contexto das exceções recebidas */
  private ExceptionContext context;

  /**
   * Construtor para garantir o uso do {@link ExceptionHandler<BusExplorerHandlingException>}
   * definido na {@link Application#exceptionHandler()}.
   *
   * @param context Contexto das exceções recebidas.
   */
  public BusExplorerTask(ExceptionContext context) {
    this.handler = Application.exceptionHandler();
    this.context = context;
  }

  /**
   * Construtor para permitir a definição de um {@link ExceptionHandler}
   * diferente do padrão definido na {@link Application#exceptionHandler()} e também
   * permite configurar o modal da janela, com uso do construtor {@link Task#Task(ModalityType)}.
   * 
   * @param modality Tipo de modalidade do diálogo.
   * @param handler Manipulador de exceções.
   * @param context Contexto das exceções recebidas.
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
   * Tarefa a ser executada. Esse método deve ser implementado para executar a
   * tarefa.
   *
   * @throws Exception quando a ocorrência de um erro (representado pela
   *         exceção) impediu a realização da tarefa
   *
   * @see Task#performTask()
   */
  protected abstract void doPerformTask() throws Exception;

  /**
   * Método para estender o tratamento de erros da {@link Task#performTask()} que não
   * fornece tratamento para erros derivados de {@link java.lang.Error}.
   * <p>
   * Para contornar essa limitação, e poder apresentar os detalhes ao usuário, é lançado
   * um {@link RuntimeException} com o erro original aninhado.
   *
   * @throws Exception a exceção original caso o {@link BusExplorerTask#doPerformTask()}
   *         tenha lançado uma exceção.
   * @throws RuntimeException uma exceção de runtime contendo o {@link java.lang.Error}
   *         caso o {@link BusExplorerTask#doPerformTask()} tenha lançado um erro.
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
