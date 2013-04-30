package admin.remote;

import javax.swing.SwingUtilities;

/**
 * A classe <code>SimpleRemoteTask</code> modela uma tarefa executada no cliente
 * que envolve uma chamada ao servidor e, portanto, pode ser demorada. Uma
 * thread é criada para a execução da tarefa, para que eventos de interface
 * (como redesenho) possam ser atendidos.
 * 
 * @author Tecgraf
 */
public abstract class SimpleRemoteTask implements Runnable {
  /** Exceção gerada pela tarefa */
  protected Exception exception = null;

  /**
   * Esse método é implementado na sub-classe de <code>RemoteTask</code> e
   * contém o código <i>não relacionado a UI</i> que é executado em uma thread
   * diferente da thread do Swing.
   * 
   * @throws Exception se ocorrer uma exceção durante a execução da tarefa
   */
  protected abstract void performTask() throws Exception;

  /**
   * Esse método é implementado na sub-classe de <code>RemoteTask</code> e
   * contém o código <i>relacionado a UI</i> que é executado na thread do Swing
   * após a finalização da tarefa remota.
   */
  protected void updateUI() {
  }

  /** Para ser executado na thread do Swing após a finalização da tarefa */
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
   * Método chamado quando a thread é executada pelo método <code>start</code>
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
   * Inicia a execução da tarefa remota
   */
  public void start() {
    final Thread worker = new Thread(this);
    worker.start();
  }

  /**
   * @return se a tarefa remota recebeu alguma exceção durante a sua execução
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
   * @return exceção recebida durante a execução da tarefa remota
   */
  public Exception getTaskException() {
    return this.exception;
  }

}