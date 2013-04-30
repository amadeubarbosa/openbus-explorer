package admin.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.dialog.ProgressDialog;

/**
 * Estende o comportamento de RemoteTask, permitindo o cancelamento das tarefas
 * remotas pelo usuário, além de criar uma janela de progresso SimpleWindow,
 * caso a tarefa remota ultrapasse o limite de tempo. Se cancelada a tarefa, a
 * não é passada para a EDT (método invokeLater de SwingUtilities) a thread que
 * atualiza a UI.
 * 
 * @author Tecgraf
 */
public abstract class SimpleWindowRemoteTask extends RemoteTask {
  /** Indica se o usuário clicou no botão de Cancelar a tarefa remota */
  protected volatile boolean remoteTaskCanceled = false;

  /**
   * Construtor.
   * 
   * @param parentWindow
   * @param titleProgressWindow
   * @param messageProgressWindow
   * @param blockType
   */
  public SimpleWindowRemoteTask(SimpleWindow parentWindow,
    String titleProgressWindow, String messageProgressWindow,
    SimpleWindowBlockType blockType) {
    super(parentWindow, titleProgressWindow, messageProgressWindow, blockType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void buildProgressDialog() {
    progressDialog =
      new ProgressDialog(new SimpleWindow(titleProgressWindow),
        messageProgressWindow, new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            remoteTaskCanceled = true;
          }
        }).getDialog();
  }

  /**
   * @param worker thread que executa a tarefa remota
   * @return a ação que verifica se a tarefa remota terminou ou foi cancelada,
   *         alterando a UI, caso verdadeiro
   */
  @Override
  protected ActionListener createRemoteTaskProgressCheckActionListener(
    final Thread worker) {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (remoteTaskCanceled
          || ((!worker.isAlive()) && dialogClosed.compareAndSet(false, true))) {
          owner.unblockWindow(blockType);
          progressDialog.dispose();
          closeDialogTimer.stop();

        }
      }
    };
  }

  /**
   * Método chamado quando a thread é executada pelo método <code>start</code>,
   * que não atualiza a UI, caso a tarefa remota tenha sido cancela pelo usuário
   */
  @Override
  public void run() {
    try {
      performTask();
    }
    catch (Exception e) {
      exception = e;
    }
    if (!remoteTaskCanceled) {
      SwingUtilities.invokeLater(doFinished);
    }
  }

}
