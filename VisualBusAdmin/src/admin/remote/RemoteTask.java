package admin.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Timer;

import admin.desktop.BlockableWindow;
import admin.desktop.SimpleWindowBlockType;

/**
 * A classe <code>RemoteTask</code> modela uma tarefa executada no cliente que
 * envolve uma chamada ao servidor e, portanto, pode ser demorada. Uma thread é
 * criada para a execução da tarefa, para que eventos de interface (como
 * redesenho) possam ser atendidos. Caso a duração da tarefa ultrapasse um valor
 * limite, um diálogo contendo uma barra de progresso será exibido. Este diálogo
 * será fechado automaticamente ao final da tarefa.
 * 
 * A tarefa permite o bloqueio/desbloqueio das janelas da aplicação, que deve
 * ser implementado pela janela, pela extensão da classe BlockableWindow.
 * 
 * @author Tecgraf
 */
public abstract class RemoteTask extends SimpleRemoteTask {
  /** Tempo de espera antes da abertura do diálogo de progresso. */
  protected static final int PROGRESS_DIALOG_DELAY = 500;
  /** Tempo de espera entre as verificações de término da tarefa. */
  protected static final int PROGRESS_CHECK_DELAY = 500;
  /**
   * Timer usado para verificar quando a worker thread para e a janela de
   * progresso deve ser fechada
   */
  protected Timer closeDialogTimer = new Timer(PROGRESS_CHECK_DELAY, null);
  /** A janela mãe da janela de progresso */
  protected BlockableWindow owner;
  /** Diálogo de espera, indicando atividade. */
  protected BlockableWindow progressDialog;
  /** Indica se a janela de progresso já foi fechada */
  protected AtomicBoolean dialogClosed = new AtomicBoolean(false);
  /** Título da janela de progresso */
  protected String titleProgressWindow;
  /** Mensagem da janela de progresso */
  protected String messageProgressWindow;
  /** Tipo de bloqueio da janela mãe */
  protected SimpleWindowBlockType blockType;

  /** Cria a janela de progresso */
  protected abstract void buildProgressDialog();

  /**
   * @param owner janela mãe
   * @param titleProgressWindow título da janela de progresso da tarefa remota
   * @param messageProgressWindow mensagem da janela de progresso da tarefa
   *        remota
   * @param blockType tipo de bloqueio da janela mãe
   */
  public RemoteTask(BlockableWindow owner, String titleProgressWindow,
    String messageProgressWindow, SimpleWindowBlockType blockType) {
    super();
    this.owner = owner;
    this.titleProgressWindow = titleProgressWindow;
    this.messageProgressWindow = messageProgressWindow;
    this.blockType = blockType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {

    owner.blockWindow(blockType);

    final Thread worker = new Thread(this);
    worker.start();

    try {
      worker.join(PROGRESS_DIALOG_DELAY);
    }
    catch (InterruptedException e) {
    }

    if (!worker.isAlive()) {
      owner.unblockWindow(blockType);
      return;
    }

    buildProgressDialog();

    closeDialogTimer
      .addActionListener(createRemoteTaskProgressCheckActionListener(worker));

    closeDialogTimer.start();

    // Exibe o diálogo de progresso
    progressDialog.setVisible(true);
  }

  /**
   * @param worker thread que executa a tarefa remota
   * @return a ação que verifica se a tarefa remota terminou, alterando a UI,
   *         caso verdadeiro
   */
  protected ActionListener createRemoteTaskProgressCheckActionListener(
    final Thread worker) {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if ((!worker.isAlive()) && dialogClosed.compareAndSet(false, true)) {
          owner.unblockWindow(blockType);
          progressDialog.dispose();
          closeDialogTimer.stop();
        }
      }
    };
  }
}
