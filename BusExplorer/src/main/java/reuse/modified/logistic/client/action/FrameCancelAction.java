/**
 * 
 */
package reuse.modified.logistic.client.action;

import tecgraf.javautils.core.lng.LNG;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * A��o de cancelar uma janela
 * 
 * Modificada para usar JFrame, ao inv�s de GenericFrame.
 * 
 * @author allan
 * 
 */
public class FrameCancelAction extends AbstractAction {

  /** Nome dessa a��o */
  public static final String NAME = "FrameCancelAction.name";
  /** Mnemonico dessa a��o */
  public static final String MNEMONIC_KEY = "FrameCancelAction.mnemonic";
  /** Janela pai dessa a��o. */
  private JFrame owner;

  /**
   * Construtor.
   * 
   * @param owner a janela que originou essa a��o
   */
  public FrameCancelAction(JFrame owner) {
    super(NAME);
    putValue(Action.NAME, LNG.get(NAME));
    putValue(Action.MNEMONIC_KEY, (int) LNG.get(MNEMONIC_KEY).charAt(0));
    this.owner = owner;

    KeyStroke escapeKeyStroke =
      KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);

    this.owner.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
      .put(escapeKeyStroke, "ESCAPE");
    this.owner.getRootPane().getActionMap().put("ESCAPE", this);
  }

  /**
   * Construtor.
   */
  public FrameCancelAction() {
    super(NAME);
    putValue(Action.NAME, LNG.get(NAME));
    putValue(Action.MNEMONIC_KEY, (int) LNG.get(MNEMONIC_KEY).charAt(0));
  }

  /**
   * Atribui a janela que � fechada como resultado desta a��o.
   * 
   * @param owner a janela a ser fechada
   */
  public void setFrame(JFrame owner) {
    this.owner = owner;
  }

  /**
   * Executa a a��o de fechar a janela.
   * 
   * @param evt o evento ocorrido
   */
  @Override
  public void actionPerformed(final ActionEvent evt) {
    if (owner != null) {
      owner.dispose();
    }
  }
}
