package reuse.modified.logistic.client.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import reuse.modified.logistic.client.util.UI;
import reuse.modified.logistic.client.util.text.EditFloatFormat;
import reuse.modified.logistic.client.util.text.Formatter;
import reuse.modified.logistic.client.action.FrameCancelAction;
import tecgraf.javautils.LNG;
import admin.BusAdmin;

/**
 * Interface para entrada de dados.
 * 
 * Modificada para usar JFrame, ao inv�s de GenericFrame, e um objeto de
 * acesso ao barramento.
 * 
 */
public abstract class InputDialog extends JFrame {

  /**
   * Formatador de n�meros.
   */
  protected static final EditFloatFormat editFormatter = Formatter
    .getNumberEditFormat();

  /**
   * Formatador de datas.
   */
  protected static final SimpleDateFormat dateFormatter = Formatter
    .getDateFormat();

  /**
   * Imagem de aviso colocada se houver problemas no preenchimento de dados.
   */
  private static final ImageIcon warningIcon = UI.WARNING_ICON;

  /**
   * Bot�o de confirma��o de dados.
   */
  protected JButton accept;

  /**
   * Bot�o de cancelamento de dados.
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
   * Painel de bot�es
   */
  private JPanel buttons;

  /**
   * Painel de mensagem de texto.
   */
  protected JTextPane messageText;

  private JFrame parentWindow;

  protected BusAdmin admin;

  /**
   * Construtor.
   * 
   * @param owner janela pai.
   * @param title t�tulo da janela.
   * @param suffix texto a ser apresentado ap�s o t�tulo da janela.
   * @param tooltip dica no bot�o aceitar.
   * @param blockType modo de bloqueio da janela.
   */
  public InputDialog(JFrame parentWindow, String title, BusAdmin admin) {
    super(title);
    this.parentWindow = parentWindow;
    this.admin = admin;
    messageText = new JTextPane();
    messageText.setFocusable(false);
    buttons = buildButtons();
  }

  /**
   * Indica se o di�logo foi cancelado.
   * 
   * @return verdadeiro se o di�logo foi cancelado.
   */
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Obt�m o bot�o de aceita��o do di�logo.
   * 
   * @return bot�o de aceita��o do di�logo.
   */
  public JButton getAcceptButton() {
    return accept;
  }

  /**
   * Obt�m o bot�o de aceita��o do di�logo.
   * 
   * @return bot�o de aceita��o do di�logo.
   */
  public JButton getCancelButton() {
    return cancel;
  }

  /**
   * Obt�m a janela deste di�logo.
   * 
   * @return janela deste di�logo.
   */
  public JFrame getWindow() {
    return this;
  }

  /**
   * Obt�m a janela pai deste di�logo
   * 
   * @return janela pai deste di�logo
   */
  @Override
  public JFrame getOwner() {
    return parentWindow;
  }

  /**
   * Cria e apresenta um di�logo para entrada dos dados.
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
   * Monta a �rea de mensagens de erro com o campo messageText.
   * 
   * @return �rea de mensagens de erro com o campo messageText.
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
   * Constr�i um painel para entrada dos dados.
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
   * Cria os bot�es para alterar os dados da aloca��o e para cancelar.
   * 
   * @param tooltip dica a ser apresentada no bot�o de ACEITAR.
   * 
   * @return painel com os bot�es.
   */
  private JPanel buildButtons() {
    accept = new JButton(LNG.get("InputDialog.confirm.button"));
    accept.setToolTipText(LNG.get("InputDialog.confirm.tooltip"));
    accept.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ev) {
        if (accept()) {
          cancelled = false;
          dispose();
        }
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

    return UI.buildButtonPanel(new JButton[] { accept, cancel });
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
    messageText.insertIcon(warningIcon);
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
   * Indica se o di�logo est� com mensagem de erro.
   * 
   * @return verdadeiro se o di�logo estiver com mensagem de erro.
   */
  public boolean hasErrorMessage() {
    return hasError;
  }

  /**
   * M�todo abstrato para montagem do painel de entrada de dados.
   * 
   * @return o painel a ser colocado no centro do di�logo para entrada de dados.
   */
  protected abstract JPanel buildFields();

  /**
   * M�todo (a ser definido pela aplica��o) de aceita��o dos valores colocados
   * no painel principal do di�logo.
   * 
   * @return um indicativo booleano da aceita��o.
   */
  protected boolean accept() {
    return false;
  }

  public boolean hasValidFields() {

    return true;
  }

  /**
   * Altera o nome dos bot�es.
   * 
   * @param acceptText nome do bot�o de aceitar.
   * @param cancelText nome do bot�o de cancelar.
   */
  public void setButtonTexts(String acceptText, String cancelText) {
    accept.setText(acceptText);
    cancel.setText(cancelText);
  }

  /**
   * Altera a a��o do bot�o de aceitar.
   * 
   * @param action a��o do bot�o de aceitar.
   */
  public void setAcceptAction(AbstractAction action) {
    accept.setAction(action);
  }

  /**
   * M�todo que habilita o bot�o de aceita��o da janela.
   */
  public void setAcceptButtonEnabled() {
  }

}
