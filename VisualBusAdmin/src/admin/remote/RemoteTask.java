package admin.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Timer;

import admin.desktop.BlockableWindow;
import admin.desktop.SimpleWindowBlockType;

/**
 * A classe <code>RemoteTask</code> modela uma tarefa executada no cliente que
 * envolve uma chamada ao servidor e, portanto, pode ser demorada. Uma thread �
 * criada para a execu��o da tarefa, para que eventos de interface (como
 * redesenho) possam ser atendidos. Caso a dura��o da tarefa ultrapasse um valor
 * limite, um di�logo contendo uma barra de progresso ser� exibido. Este di�logo
 * ser� fechado automaticamente ao final da tarefa.
 * 
 * A tarefa permite o bloqueio/desbloqueio das janelas da aplica��o, que deve
 * ser implementado pela janela, pela extens�o da classe BlockableWindow.
 * 
 * @author Tecgraf
 */
public abstract class RemoteTask extends SimpleRemoteTask {
  /** Tempo de espera antes da abertura do di�logo de progresso. */
  protected static final int PROGRESS_DIALOG_DELAY = 500;
  /** Tempo de espera entre as verifica��es de t�rmino da tarefa. */
  protected static final int PROGRESS_CHECK_DELAY = 500;
  /**
   * Timer usado para verificar quando a worker thread para e a janela de
   * progresso deve ser fechada
   */
  protected Timer closeDialogTimer = new Timer(PROGRESS_CHECK_DELAY, null);
  /** A janela m�e da janela de progresso */
  protected BlockableWindow owner;
  /** Di�logo de espera, indicando atividade. */
  protected BlockableWindow progressDialog;
  /** Indica se a janela de progresso j� foi fechada */
  protected AtomicBoolean dialogClosed = new AtomicBoolean(false);
  /** T�tulo da janela de progresso */
  protected String titleProgressWindow;
  /** Mensagem da janela de progresso */
  protected String messageProgressWindow;
  /** Tipo de bloqueio da janela m�e */
  protected SimpleWindowBlockType blockType;

  /** Cria a janela de progresso */
  protected abstract void buildProgressDialog();

  /**
   * @param owner janela m�e
   * @param titleProgressWindow t�tulo da janela de progresso da tarefa remota
   * @param messageProgressWindow mensagem da janela de progresso da tarefa
   *        remota
   * @param blockType tipo de bloqueio da janela m�e
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

    // Exibe o di�logo de progresso
    progressDialog.setVisible(true);
  }

  /**
   * @param worker thread que executa a tarefa remota
   * @return a a��o que verifica se a tarefa remota terminou, alterando a UI,
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
