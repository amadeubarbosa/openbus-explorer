package busexplorer.exception;

import java.awt.Dialog.ModalityType;

import exception.handling.ExceptionContext;
import exception.handling.ExceptionHandler;

import tecgraf.javautils.gui.StandardDialogs;

public abstract class BusExplorerTask<T> extends tecgraf.javautils.gui.Task<T> {

  private ExceptionHandler<BusExplorerHandlingException> handler;
  private ExceptionContext context;

  public BusExplorerTask(ExceptionHandler<BusExplorerHandlingException> handler,
    ExceptionContext context) {
    super();
    this.handler = handler;
    this.context = context;
  }

  public BusExplorerTask(ModalityType modality,
    ExceptionHandler<BusExplorerHandlingException> handler,
    ExceptionContext context) {
    super(modality);
    this.handler = handler;
    this.context = context;
  }

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
