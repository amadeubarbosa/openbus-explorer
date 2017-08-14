package busexplorer.desktop.dialog;

import busexplorer.ApplicationIcons;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * Di�logo para entrada de dados com suporte embutido � internacionaliza��o
 * do t�tulo atrav�s do uso de {@link Language}.
 *
 */
public abstract class InputDialog extends JFrame {

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

  private Window parentWindow;

  /**
   * Construtor b�sico com refer�ncia � janela pai e preenche o t�tulo a partir do
   *
   * @param parentWindow janela pai.
   */
  public InputDialog(Window parentWindow) {
    this.setTitle(Language.get(this.getClass(), "title"));
    setIconImages(Arrays.asList(ApplicationIcons.BUSEXPLORER_LIST));
    init(parentWindow);
  }

  /**
   * Construtor para criar o frame com um t�tulo pr�-definido.
   * 
   * @param parentWindow janela pai.
   * @param title t�tulo da janela.
   */
  public InputDialog(Window parentWindow, String title) {
    super(title);
    init(parentWindow);
  }

  private void init(Window parentWindow) {
    this.parentWindow = parentWindow;
    messageText = new JTextPane();
    messageText.setFocusable(false);
    buttons = buildButtons();
  }

  /**
   * Insere os bot�es passados como par�metro em um painel com BoxLayout e
   * iguala o tamanho dos bot�es.
   *
   * @param buttons array com os bot�es
   *
   * @return o painel criado.
   */
  public static JPanel buildButtonPanel(JButton... buttons) {
    equalizeButtonSizes(buttons);
    JPanel buttonPanel = new JPanel(new MigLayout("fill","[grow][]"));

    for (JButton button : buttons) {
      buttonPanel.add(button, "gapleft push");
    }
    return buttonPanel;
  }

  /**
   * Iguala o tamanho dos bot�es contidos no array passado como par�metro.
   *
   * @param buttons o array com os bot�es
   */
  public static void equalizeButtonSizes(JButton[] buttons) {
    Dimension maxSize = new Dimension(0, 0);
    int i;

    // Encontra maior dimens�o
    for (i = 0; i < buttons.length; ++i) {
      maxSize.width =
        Math.max(maxSize.width, buttons[i].getPreferredSize().width);
      maxSize.height =
        Math.max(maxSize.height, buttons[i].getPreferredSize().height);
    }

    // Atribui novos valores para "preferred" e "maximum size", uma vez que
    // BoxLayout leva ambos os valores em considera��o. */
    for (i = 0; i < buttons.length; ++i) {
      buttons[i].setPreferredSize((Dimension) maxSize.clone());
      buttons[i].setMaximumSize((Dimension) maxSize.clone());
      buttons[i].setMinimumSize((Dimension) maxSize.clone());
    }
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
  public Window getOwner() {
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
    JScrollPane pane = new JScrollPane(messageText);
    pane.setPreferredSize(new Dimension(160, 12));
    pane.setBorder(null);
    return pane;
  }

  /**
   * Constr�i um painel para entrada dos dados.
   * 
   * @return painel principal.
   */
  private JPanel getMainPane() {
    JPanel panel = new JPanel(new MigLayout("fill","[grow]"));
    panel.add(buildFields(),"grow, wrap");
    panel.add(buildMessagePane(), "grow, wrap");
    panel.add(new JSeparator(JSeparator.HORIZONTAL),"growx, wrap");
    panel.add(buttons, "grow");

    return panel;
  }

  /**
   * Cria os bot�es para alterar os dados da aloca��o e para cancelar.
   * 
   * @return painel com os bot�es.
   */
  private JPanel buildButtons() {
    accept = new JButton(getString("confirm.button"));
    accept.setToolTipText(getString("confirm.tooltip"));
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
    cancel.setToolTipText(getString("cancel.tooltip"));

    return buildButtonPanel(accept, cancel);
  }

  private String getString(String key) {
    String acceptText;
    if (Language.hasKey(this.getClass(),key)) {
      acceptText = Language.get(this.getClass(), key);
    } else {
      acceptText = Language.get(InputDialog.class, key);
    }
    return acceptText;
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
    messageText.insertIcon(ApplicationIcons.ICON_CANCEL_16);
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
  public abstract JPanel buildFields();

  /**
   * M�todo (a ser definido pela aplica��o) de aceita��o dos valores colocados
   * no painel principal do di�logo.
   * 
   * @return um indicativo booleano da aceita��o.
   */
  protected abstract boolean accept();

  /**
   * M�todo a ser sobrescrito para defini��o de regras de valida��o de dados do
   * di�logo.
   * 
   * @return <code>true</code> caso os dados sejam v�lidos ou false caso
   *         contr�rio.
   */
  protected abstract boolean hasValidFields();

}
