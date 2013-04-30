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
 * remotas pelo usu�rio, al�m de criar uma janela de progresso SimpleWindow,
 * caso a tarefa remota ultrapasse o limite de tempo. Se cancelada a tarefa, a
 * n�o � passada para a EDT (m�todo invokeLater de SwingUtilities) a thread que
 * atualiza a UI.
 * 
 * @author Tecgraf
 */
public abstract class SimpleWindowRemoteTask extends RemoteTask {
  /** Indica se o usu�rio clicou no bot�o de Cancelar a tarefa remota */
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
   * @return a a��o que verifica se a tarefa remota terminou ou foi cancelada,
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
   * M�todo chamado quando a thread � executada pelo m�todo <code>start</code>,
   * que n�o atualiza a UI, caso a tarefa remota tenha sido cancela pelo usu�rio
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
