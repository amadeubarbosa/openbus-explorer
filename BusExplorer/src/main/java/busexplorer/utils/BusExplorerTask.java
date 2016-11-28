package busexplorer.utils;

import busexplorer.desktop.dialog.ExceptionDialog;
import busexplorer.exception.BusExplorerHandlingException;
import exception.handling.ExceptionContext;
import exception.handling.ExceptionHandler;
import exception.handling.ExceptionType;
import tecgraf.javautils.gui.StandardDialogs;

import java.awt.Dialog.ModalityType;


/**
 * A classe abstrata BusExplorerTask permite a realização das tarefas do
 * BusExplorer em threads separadas. Ela realiza o tratamento padrão de exceções
 * para tais tarefas.
 * 
 * @author Tecgraf
 * @param <T> A classe do resultado da tarefa.
 */
public abstract class BusExplorerTask<T> extends tecgraf.javautils.gui.Task<T> {

  /** Manipulador de exceções */
  private ExceptionHandler<BusExplorerHandlingException> handler;
  /** Contexto das exceções recebidas */
  private ExceptionContext context;

  /**
   * Construtor.
   * 
   * @param handler Manipulador de exceções.
   * @param context Contexto das exceções recebidas.
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
   * @param modality Tipo de modalidade do diálogo.
   * @param handler Manipulador de exceções.
   * @param context Contexto das exceções recebidas.
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
      Exception e = handlingException.getException();

      if (ExceptionType.getType(e) == ExceptionType.Unspecified) {
        ExceptionDialog.createDialog(parentWindow, taskTitle,
          handlingException.getException(), "").setVisible(true);
      } else {
        StandardDialogs.showErrorDialog(parentWindow, taskTitle,
          handlingException.getErrorMessage());
      }
    }
  }
}
