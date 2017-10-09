package busexplorer.desktop.dialog;

import busexplorer.utils.Language;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Ação de cancelar uma janela
 * 
 * Modificada para usar JFrame, ao invés de GenericFrame, e
 * assim poder usar a tecla de ESCAPE para fechar a janela.
 * 
 * @author Tecgraf
 * 
 */
public class CancelAction extends AbstractAction {

  /** Janela pai dessa ação. */
  private JDialog owner;

  /**
   * Construtor.
   * 
   * @param owner a janela que originou essa ação
   */
  public CancelAction(JDialog owner) {
    this();
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
  public CancelAction() {
    putValue(Action.NAME, getString("name"));
    putValue(Action.MNEMONIC_KEY, (int) getString("mnemonic").charAt(0));
  }

  /**
   * Executa a ação de fechar a janela.
   * 
   * @param evt o evento ocorrido
   */
  @Override
  public void actionPerformed(final ActionEvent evt) {
    if (owner != null) {
      owner.dispose();
    }
  }

  protected String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
