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
 * Diálogo para entrada de dados com suporte embutido à internacionalização
 * do título através do uso de {@link Language}.
 *
 */
public abstract class InputDialog extends JFrame {

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

  /**
   * Construtor básico com referência à janela pai e preenche o título a partir do
   *
   * @param parentWindow janela pai.
   */
  public InputDialog(Window parentWindow) {
    this.setTitle(Language.get(this.getClass(), "title"));
    setIconImages(Arrays.asList(ApplicationIcons.BUSEXPLORER_LIST));
    init(parentWindow);
  }

  /**
   * Construtor para criar o frame com um título pré-definido.
   * 
   * @param parentWindow janela pai.
   * @param title título da janela.
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
   * Insere os botões passados como parâmetro em um painel com BoxLayout e
   * iguala o tamanho dos botões.
   *
   * @param buttons array com os botões
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
   * Iguala o tamanho dos botões contidos no array passado como parâmetro.
   *
   * @param buttons o array com os botões
   */
  public static void equalizeButtonSizes(JButton[] buttons) {
    Dimension maxSize = new Dimension(0, 0);
    int i;

    // Encontra maior dimensão
    for (i = 0; i < buttons.length; ++i) {
      maxSize.width =
        Math.max(maxSize.width, buttons[i].getPreferredSize().width);
      maxSize.height =
        Math.max(maxSize.height, buttons[i].getPreferredSize().height);
    }

    // Atribui novos valores para "preferred" e "maximum size", uma vez que
    // BoxLayout leva ambos os valores em consideração. */
    for (i = 0; i < buttons.length; ++i) {
      buttons[i].setPreferredSize((Dimension) maxSize.clone());
      buttons[i].setMaximumSize((Dimension) maxSize.clone());
      buttons[i].setMinimumSize((Dimension) maxSize.clone());
    }
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
    JScrollPane pane = new JScrollPane(messageText);
    pane.setPreferredSize(new Dimension(160, 12));
    pane.setBorder(null);
    return pane;
  }

  /**
   * Constrói um painel para entrada dos dados.
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
   * Cria os botões para alterar os dados da alocação e para cancelar.
   * 
   * @return painel com os botões.
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
  public abstract JPanel buildFields();

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
