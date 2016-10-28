package reuse.modified.logistic.client.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import reuse.modified.logistic.client.action.FrameCancelAction;
import tecgraf.javautils.LNG;
import admin.BusAdmin;

/**
 * Interface para entrada de dados.
 * 
 * Modificada para usar JFrame, ao invés de GenericFrame, e um objeto de acesso
 * ao barramento.
 * 
 */
public abstract class InputDialog extends JFrame {

  /**
   * Imagem de aviso colocada se houver problemas no preenchimento de dados.
   */
  private static final ImageIcon errorIcon = UI.ERROR_ICON;

  /**
   * Botão de confirmação de dados.
   */
  protected JButton accept;

  /**
   * Botão de cancelamento de dados.
   */
  protected JButton cancel;

  /**
   * Flag indicativo de cancelamento
   */
  protected boolean cancelled = true;

  /**
   * Indicativo de erro no preenchimento de dados.
   */
  protected boolean hasError;

  /**
   * Painel de botões
   */
  private JPanel buttons;

  /**
   * Painel de mensagem de texto.
   */
  protected JTextPane messageText;

  private Window parentWindow;

  protected BusAdmin admin;

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param title título da janela.
   * @param admin instância do busadmin
   */
  public InputDialog(Window parentWindow, String title, BusAdmin admin) {
    super(title);
    this.parentWindow = parentWindow;
    this.admin = admin;
    messageText = new JTextPane();
    messageText.setFocusable(false);
    buttons = buildButtons();
  }

  /**
   * Indica se o diálogo foi cancelado.
   * 
   * @return verdadeiro se o diálogo foi cancelado.
   */
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Obtém o botão de aceitação do diálogo.
   * 
   * @return botão de aceitação do diálogo.
   */
  public JButton getAcceptButton() {
    return accept;
  }

  /**
   * Obtém o botão de aceitação do diálogo.
   * 
   * @return botão de aceitação do diálogo.
   */
  public JButton getCancelButton() {
    return cancel;
  }

  /**
   * Obtém a janela deste diálogo.
   * 
   * @return janela deste diálogo.
   */
  public JFrame getWindow() {
    return this;
  }

  /**
   * Obtém a janela pai deste diálogo
   * 
   * @return janela pai deste diálogo
   */
  @Override
  public Window getOwner() {
    return parentWindow;
  }

  /**
   * Cria e apresenta um diálogo para entrada dos dados.
   */
  public void showDialog() {
    getContentPane().add(getMainPane());
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent ev) {
        cancelled = true;
        dispose();
      }
    });
    pack();
    setLocationRelativeTo(parentWindow);
    setVisible(true);
  }

  /**
   * Monta a área de mensagens de erro com o campo messageText.
   * 
   * @return área de mensagens de erro com o campo messageText.
   */
  private JScrollPane buildMessagePane() {
    messageText.setEditable(false);
    messageText.setBackground(getContentPane().getBackground());
    messageText.setFont(new Font("Arial", Font.PLAIN, 11));
    JScrollPane pane = new JScrollPane(messageText);
    pane.setPreferredSize(new Dimension(160, 40));
    pane.setBorder(null);
    return pane;
  }

  /**
   * Constrói um painel para entrada dos dados.
   * 
   * @return painel principal.
   */
  private JPanel getMainPane() {
    JPanel panel = new JPanel(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(12, 12, 0, 11);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.EAST;
    panel.add(buildFields(), c);

    c.insets = new Insets(0, 12, 0, 11);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.WEST;
    panel.add(buildMessagePane(), c);

    c.insets = new Insets(0, 12, 11, 11);
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.EAST;
    panel.add(buttons, c);

    return panel;
  }

  /**
   * Cria os botões para alterar os dados da alocação e para cancelar.
   * 
   * @return painel com os botões.
   */
  private JPanel buildButtons() {
    accept = new JButton(LNG.get("InputDialog.confirm.button"));
    accept.setToolTipText(LNG.get("InputDialog.confirm.tooltip"));
    accept.addActionListener(ev -> {
      if (accept()) {
        cancelled = false;
        dispose();
      }
    });

    JRootPane rp = getRootPane();
    rp.setDefaultButton(accept);
    cancel = new JButton(new FrameCancelAction(this) {
      @Override
      public void actionPerformed(ActionEvent ev) {
        cancelled = true;
        super.actionPerformed(ev);
      }
    });
    cancel.setToolTipText(LNG.get("InputDialog.cancel.tooltip"));

    return UI.buildButtonPanel(accept, cancel);
  }

  /**
   * Limpa o campo de mensagens de erro.
   */
  public void clearErrorMessage() {
    messageText.setText("");
    hasError = false;
  }

  /**
   * Escreve uma mensagem de erro.
   * 
   * @param msg mensagem.
   */
  public void setErrorMessage(String msg) {
    if (msg == null || msg.trim().length() == 0) {
      return;
    }
    messageText.setText("  " + msg);
    messageText.setCaretPosition(0);
    messageText.insertIcon(errorIcon);
    hasError = true;
  }

  /**
   * Retorna a mensagem de erro.
   * 
   * @return mensagem de erro.
   */
  public String getErrorMessage() {
    return messageText.getText();
  }

  /**
   * Indica se o diálogo está com mensagem de erro.
   * 
   * @return verdadeiro se o diálogo estiver com mensagem de erro.
   */
  public boolean hasErrorMessage() {
    return hasError;
  }

  /**
   * Método abstrato para montagem do painel de entrada de dados.
   * 
   * @return o painel a ser colocado no centro do diálogo para entrada de dados.
   */
  protected abstract JPanel buildFields();

  /**
   * Método (a ser definido pela aplicação) de aceitação dos valores colocados
   * no painel principal do diálogo.
   * 
   * @return um indicativo booleano da aceitação.
   */
  protected abstract boolean accept();

  /**
   * Método a ser sobrescrito para definição de regras de validação de dados do
   * diálogo.
   * 
   * @return <code>true</code> caso os dados sejam válidos ou false caso
   *         contrário.
   */
  protected abstract boolean hasValidFields();

}
