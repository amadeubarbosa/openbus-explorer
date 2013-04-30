package admin.desktop.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import admin.desktop.BlockableWindow;

/** Di�logo de progresso de execu��o de tarefa remota */
public class ProgressDialog {

  /**
   * O di�logo de progresso
   */
  private BlockableWindow dialog;

  /**
   * Construtor do di�logo de progresso
   * 
   * @param dialog di�logo a ser constru�do
   * @param msg mensagem do di�logo
   * @param cancelAction a��o de cancelamento da tarefa remota
   */
  public ProgressDialog(BlockableWindow dialog, String msg,
    AbstractAction cancelAction) {
    this.dialog = dialog;
    buildProgressDialogComponents(msg, cancelAction);
  }

  /**
   * Inicializa os componentes do di�logo, contendo uma mensagem e uma barra de
   * progresso de dura��o "indeterminada".
   * 
   * @param msg mensagem a ser exibida
   * @param cancelAction a��o que cancela a execu��o da tarefa remota
   */
  private void buildProgressDialogComponents(String msg,
    AbstractAction cancelAction) {
    JProgressBar progress = new JProgressBar();
    progress.setIndeterminate(true);
    Container cp = dialog.getContentPane();
    cp.setLayout(new GridBagLayout());
    Box bComp = Box.createVerticalBox();
    bComp.add(Box.createVerticalStrut(5));
    bComp.add(new JLabel(msg, JLabel.LEFT));
    bComp.add(Box.createVerticalStrut(5));
    bComp.add(progress);
    bComp.add(Box.createVerticalStrut(5));
    Box bProgress = Box.createHorizontalBox();
    bProgress.add(Box.createHorizontalStrut(5));
    bProgress.add(bComp);
    bProgress.add(Box.createHorizontalStrut(5));
    cp.add(bProgress, new GBC(0, 0));
    JButton btnCancel = new JButton(LNG.get("ProgressDialog.cancel.button"));
    btnCancel.setToolTipText(LNG.get("ProgressDialog.cancel.help"));
    btnCancel.addActionListener(cancelAction);
    cp.add(btnCancel, new GBC(0, 1));
    dialog.setResizable(false);
    dialog.pack();
    Dimension wSize = dialog.getSize();
    Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
    int newX = (sSize.width - wSize.width) / 2;
    int newY = (sSize.height - wSize.height) / 2;
    dialog.setLocation(newX, newY);
  }

  /**
   * @return o di�logo de progresso
   */
  public BlockableWindow getDialog() {
    return this.dialog;
  }

}
