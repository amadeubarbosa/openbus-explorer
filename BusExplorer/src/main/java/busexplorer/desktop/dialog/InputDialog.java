package busexplorer.desktop.dialog;

import busexplorer.ApplicationIcons;
import busexplorer.utils.Language;
import busexplorer.utils.SwingUtilities;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.Cursor;
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
public abstract class InputDialog extends JDialog {

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
  protected JLabel messageText;

  private Window parentWindow;

  /**
   * Construtor básico com referência à janela pai e preenche o título a partir do
   *
   * @param parentWindow janela pai.
   */
  public InputDialog(Window parentWindow) {
    this.setTitle(Language.get(this.getClass(), "title"));
    init(parentWindow);
  }

  /**
   * Construtor para criar o frame com um título pré-definido.
   * 
   * @param parentWindow janela pai.
   * @param title título da janela.
   */
  public InputDialog(Window parentWindow, String title) {
    super(parentWindow, title);
    init(parentWindow);
  }

  private void init(Window parentWindow) {
    setIconImages(Arrays.asList(ApplicationIcons.BUSEXPLORER_LIST));
    this.parentWindow = parentWindow;
    this.messageText = new JLabel();
    this.messageText.setOpaque(true);
    this.messageText.setFocusable(true);
    this.buttons = buildButtons();
  }

  public static int showConfirmDialog(Window parentWindow, String message, String title) {
    // using JOptionPane for best look and feel
    JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
    JDialog dialog = optionPane.createDialog(parentWindow,title);
    // specific YES NO buttons to support mnemonic on actions
    JButton yes = new JButton();
    yes.setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        optionPane.setValue(JOptionPane.YES_OPTION);
        dialog.dispose();
      }
    });
    yes.setText(Language.get(InputDialog.class, "confirm.button"));
    yes.setMnemonic(Language.get(InputDialog.class, "confirm.button.mnemonic").charAt(0));

    JButton no = new JButton();
    no.setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        optionPane.setValue(JOptionPane.NO_OPTION);
        dialog.dispose();
      }
    });
    no.setText(Language.get(CancelAction.class, "name"));
    no.setMnemonic(Language.get(CancelAction.class, "mnemonic").charAt(0));
    // equalize buttons sizes
    SwingUtilities.equalizeComponentSize(yes, no);
    optionPane.setOptions(new Object[] { yes, no });
    optionPane.setInitialValue(yes);
    dialog.getRootPane().setDefaultButton(yes);
    // show alternative JOptionPane dialog
    dialog.setVisible(true);
    // return the option choosed
    return ((Integer)optionPane.getValue()).intValue();
  }

  /**
   * Insere os botões passados como parâmetro em um painel com MigLayout e
   * iguala o tamanho dos botões.
   *
   * @param buttons array com os botões
   *
   * @return o painel criado.
   */
  public static JPanel buildButtonPanel(JButton... buttons) {
    SwingUtilities.equalizeComponentSize(buttons);
    JPanel buttonPanel = new JPanel(new MigLayout("fill, insets 0 0 0 0","[grow][]"));

    for (JButton button : buttons) {
      buttonPanel.add(button, "gapleft push");
    }
    return buttonPanel;
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
    getContentPane().add(buildMainPane());
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

  private JPanel buildMainPane() {
    JPanel bottomPanel = new JPanel(new MigLayout("fill, flowy"));
    JScrollPane scrollPane = buildErrorMessagePane();
    //scrollpane is optional
    if (scrollPane != null) {
      bottomPanel.add(scrollPane, "grow, push");
    }
    bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
    bottomPanel.add(this.buttons, "grow");

    JPanel panel = new JPanel(new MigLayout("fill, flowy"));
    panel.add(buildFields(),"grow");
    panel.add(bottomPanel, "grow, dock south");
    return panel;
  }

  protected JScrollPane buildErrorMessagePane() {
    JScrollPane scrolledMessageText = new JScrollPane(messageText);
    scrolledMessageText.setViewportBorder(null);
    scrolledMessageText.setBorder(null);
    scrolledMessageText.setMinimumSize(new Dimension(160, 25));
    return scrolledMessageText;
  }

  /**
   * Cria os botões para alterar os dados da alocação e para cancelar.
   * 
   * @return painel com os botões.
   */
  private JPanel buildButtons() {
    accept = new JButton(getString("confirm.button"));
    accept.setToolTipText(getString("confirm.tooltip"));
    accept.setMnemonic(getString("confirm.button.mnemonic").charAt(0));
    accept.addActionListener(ev -> {
      if (acceptActionPerformed()) {
        cancelled = false;
        dispose();
      }
    });

    JRootPane rp = getRootPane();
    rp.setDefaultButton(accept);
    cancel = new JButton(new CancelAction(this));
    cancel.addActionListener(ev -> cancelled = true);
    cancel.setToolTipText(getString("cancel.tooltip"));

    return buildButtonPanel(accept, cancel);
  }

  protected String getString(String key) {
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
    messageText.setIcon(null);
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
    messageText.setIcon(ApplicationIcons.ICON_CANCEL_16);
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
   * Implementação padrão da ação do botão de confirmação para apresentar o cursor de espera.
   *
   * @return o retorno da chamada do método {@link #accept()}
   */
  private boolean acceptActionPerformed() {
    this.setCursor(Cursor
      .getPredefinedCursor(Cursor.WAIT_CURSOR));
    boolean result = accept();
    this.setCursor(Cursor
      .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    return result;
  }

  /**
   * Método a ser sobrescrito para definição de regras de validação de dados do
   * diálogo.
   * 
   * @return <code>true</code> caso os dados sejam válidos ou false caso
   *         contrário.
   */
  protected abstract boolean hasValidFields();

}
