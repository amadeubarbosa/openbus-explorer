package admin.remote;

import javax.swing.SwingUtilities;

/**
 * A classe <code>SimpleRemoteTask</code> modela uma tarefa executada no cliente
 * que envolve uma chamada ao servidor e, portanto, pode ser demorada. Uma
 * thread � criada para a execu��o da tarefa, para que eventos de interface
 * (como redesenho) possam ser atendidos.
 * 
 * @author Tecgraf
 */
public abstract class SimpleRemoteTask implements Runnable {
  /** Exce��o gerada pela tarefa */
  protected Exception exception = null;

  /**
   * Esse m�todo � implementado na sub-classe de <code>RemoteTask</code> e
   * cont�m o c�digo <i>n�o relacionado a UI</i> que � executado em uma thread
   * diferente da thread do Swing.
   * 
   * @throws Exception se ocorrer uma exce��o durante a execu��o da tarefa
   */
  protected abstract void performTask() throws Exception;

  /**
   * Esse m�todo � implementado na sub-classe de <code>RemoteTask</code> e
   * cont�m o c�digo <i>relacionado a UI</i> que � executado na thread do Swing
   * ap�s a finaliza��o da tarefa remota.
   */
  protected void updateUI() {
  }

  /** Para ser executado na thread do Swing ap�s a finaliza��o da tarefa */
  protected Runnable doFinished;

  /**
   * Construtor.
   */
  public SimpleRemoteTask() {
    this.doFinished = new Runnable() {
      @Override
      public void run() {
        updateUI();
      }
    };
  }

  /**
   * M�todo chamado quando a thread � executada pelo m�todo <code>start</code>
   */
  @Override
  public void run() {
    try {
      performTask();
    }
    catch (Exception e) {
      exception = e;
    }

    SwingUtilities.invokeLater(doFinished);
  }

  /**
   * Inicia a execu��o da tarefa remota
   */
  public void start() {
    final Thread worker = new Thread(this);
    worker.start();
  }

  /**
   * @return se a tarefa remota recebeu alguma exce��o durante a sua execu��o
   * 
   */
  public boolean hasNoException() {
    if (this.exception == null) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * @return exce��o recebida durante a execu��o da tarefa remota
   */
  public Exception getTaskException() {
    return this.exception;
  }

}