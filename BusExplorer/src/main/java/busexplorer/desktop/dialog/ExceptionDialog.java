package busexplorer.desktop.dialog;

import busexplorer.ApplicationIcons;
import busexplorer.utils.Language;
import busexplorer.utils.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Janela que exibe o detalhamento de uma exce��o.
 *
 * @author Tecgraf PUC-Rio
 */
public abstract class ExceptionDialog extends JDialog {

  /**
   * Erro ou exce��o a ser visualizada na janela
   */
  protected final Throwable _throwable;

  /**
   * Construtor auxiliar com <code>JFrame</code>.
   *
   * @param owner a janela pai
   * @param title o t�tulo
   * @param throwable o erro ou exce��o
   */
  protected ExceptionDialog(Window owner, String title, Throwable throwable) {
    super(owner, title);
    this._throwable = throwable;
    setWindowClosingMethod();
  }

  /**
   * M�todo chamado pelos construtores para alterar o comportamento do di�logo
   * clicando no bot�o marcado por um <b>X</b> localizado no canto superior
   * direito. Por padr�o, a super classe ignora esse evento, ao chamar este
   * m�todo, o construtor ir� garantir que a janela seja fechada.
   */
  private void setWindowClosingMethod() {
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });
  }

  /**
   * M�todo est�tico de cria��o de um di�logo com base em par�metros passados
   * pelo m�dulo-cliente para uso sem construtor.<br>
   * � criado um {@link JOptionPane} com mensagem do tipo {@link JOptionPane#ERROR_MESSAGE}
   * e bot�o adicional para o usu�rio ter mais detalhes sobre o erro/exce��o.<br>
   * O uso de {@link JOptionPane} garante a apresenta��o adequada nas diferentes plataformas.
   *
   * @param window a janela-m�e.
   * @param title o t�tulo.
   * @param throwable a exce��o
   * @param message mensagem a ser apresentada ao usu�rio
   * @return um dialogo de erro.
   */
  public static JDialog createDialog(final Window window,
    final String title, final Throwable throwable, String message) {
    JOptionPane optionPane = new JOptionPane();
    optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
    optionPane.setMessage(createComponents(message));

    JDialog dialog = optionPane.createDialog(window, title);
    Vector<JButton> buttons = new Vector<>();
    if (throwable != null) {
      buttons.add(new JButton(new DetailThrowableAction(dialog, throwable)));
    }
    if (message != null && !message.isEmpty()) {
      JButton copy = new JButton(ApplicationIcons.ICON_COPY_16);
      copy.setToolTipText(Language.get(ExceptionDialog.class, "copy.tooltip"));
      copy.setText(Language.get(ExceptionDialog.class, "copy.name"));
      copy.setMnemonic(Language.get(ExceptionDialog.class, "copy.mnemonic").charAt(0));
      copy.addActionListener(al -> {
        StringSelection stringSelection = new StringSelection(message);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
      });
      buttons.add(copy);
    }
    buttons.add(new JButton(new CloseAction(dialog)));
    JButton[] options = buttons.toArray(new JButton[]{});
    SwingUtilities.equalizeComponentSize(options);
    optionPane.setOptions(options);
    dialog.pack();
    dialog.getRootPane().setDefaultButton(options[0]);
    return dialog;
  }

  /**
   * M�todo est�tico de cria��o de um di�logo <b>detalhado</b> com base em
   * par�metros passados pelo m�dulo-cliente para uso sem construtor.
   *
   * @param window a janela-m�e.
   * @param title o t�tulo.
   * @param throwable a exce��o.
   * @return um dialogo de erro.
   */
  public static ExceptionDialog createDialog(final Window window,
    final String title, final Throwable throwable) {
    return new DetailedExceptionDialog(window, title, throwable);
  }

  private static JPanel createComponents(String message) {
    JPanel mainPanel = new JPanel(new MigLayout("fill, flowy"));
    if (message == null || message.isEmpty()) {
      mainPanel.add(new JLabel(Language.get(ExceptionDialog.class, "executionError")));
      mainPanel.add(new JLabel(Language.get(ExceptionDialog.class, "contactError")));
    } else {
      if (message.startsWith("<html>")) {
        JLabel label = new JLabel(message);
        mainPanel.add(label, "grow");
      } else {
        JMultilineLabel label = new JMultilineLabel();
        label.setMaxWidth(400);
        label.setJustified(false);
        label.setText(message);
        JScrollPane js = new JScrollPane(label);
        js.setViewportBorder(null);
        js.setBorder(null);
        mainPanel.add(js, "grow");
      }
    }
    return mainPanel;
  }

  /**
   * Centraliza��o
   */
  public void center() {
    this.center(getOwner());
  }

  /**
   * Centraliza��o
   *
   * @param window a janela de refer�ncia para centraliza��o
   */
  public void center(Window window) {
    if (window == null) {
      GUIUtils.centerOnScreen(this);
      return;
    }

    if (window instanceof JFrame) {
      // getSize(), getX() e getY() n�o levam em considera��o o fato da janela
      // de refer�ncia estar maximizada; neste caso, centralizamos na tela.
      JFrame jframe = (JFrame) window;
      final int windowState = jframe.getExtendedState();
      if ((windowState & Frame.MAXIMIZED_BOTH) != 0) {
        GUIUtils.centerOnScreen(this);
        return;
      }
    }
    Dimension currentSize = this.getSize();
    Dimension windowSize = window.getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int minX = window.getX() + (windowSize.width - currentSize.width) / 2;
    if ((minX + currentSize.width) > screenSize.width) {
      minX = screenSize.width - currentSize.width;
    }
    if (minX < 0) {
      minX = 0;
    }
    int minY = window.getY() + (windowSize.height - currentSize.height) / 2;
    if ((minY + currentSize.height) > screenSize.height) {
      minY = screenSize.height - currentSize.height;
    }
    if (minY < 0) {
      minY = 0;
    }
    this.setLocation(minX, minY);
  }

  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * Classe que modela um di�logo <b>detalhado</b> de exibi��o de um
   * erro/exce��o detectado no sistema. Este di�logo apresenta a pilha do
   * erro/exce��o para o usu�rio.
   *
   * @see ExceptionDialog di�logo que apresenta um erro/exce��o, detectado no
   *      sistema, para o usu�rio.
   */
  private static final class DetailedExceptionDialog extends ExceptionDialog {

    /** N�mero de colunas */
    private static final int _MESSAGE_COLUMNS = 80;

    /** N�mero de linhas */
    private static final int _MESSAGE_ROWS = 4;

    /** N�mero de colunas */
    private static final int _STACK_TRACE_COLUMNS = _MESSAGE_COLUMNS;

    /** N�mero de linhas */
    private static final int _STACK_TRACE_ROWS = 14;

    /** Largura da �rea da �rvore */
    private static final int _TROUBLE_TREE_WIDTH = 800;

    /** Altura da �rea da �rvore */
    private static final int _TROUBLE_TREE_HEIGHT = 100;

    /** �rea de texto de mensagens */
    private JTextArea _messageTextArea;

    /** �rea de texto de mensagens da pilha. */
    private JTextArea _stackTraceTextArea;

    /** �rvore de exce��es aninhadas */
    private JTree _throwableTree;

    /**
     * Construtor auxiliar com <code>JDialog</code>.
     *
     * @param owner a janela pai
     * @param title o t�tulo
     * @param throwable o erro ou exce��o
     */
    public DetailedExceptionDialog(final Window owner, final String title,
      final Throwable throwable) {
      super(owner, title, throwable);
      setModal(true);
      createComponents();
    }

    /**
     * M�todo interno de constru��o da inteface gr�fica.
     */
    private void createComponents() {
      setMinimumSize(new Dimension(750, 550));
      // Define a cor que ser� utilizada como cor de fundo para componentes
      // inativos
      Color inactiveColor =
        UIManager.getDefaults().getColor("TextField.inactiveBackground");

      // Cria uma �rvore de erros/exce��es
      final JLabel troubleTreeLabel = new
        JLabel(Language.get(this.getClass(), "exceptionTree"));
      this._throwableTree = new JTree(new ThrowableTreeNode(this._throwable));
      this._throwableTree.addTreeSelectionListener(e -> updateFields());
      this._throwableTree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
      final JScrollPane scrollTroubleTree =
        new JScrollPane(this._throwableTree);
      scrollTroubleTree.setPreferredSize(new Dimension(_TROUBLE_TREE_WIDTH,
        _TROUBLE_TREE_HEIGHT));

      // Cria a �rea p/ mostrar a mensagem da exce��o selecionada na �rvore
      final JLabel messageLabel = new
        JLabel(Language.get(this.getClass(), "exceptionMessage"));
      this._messageTextArea =
        new JTextArea(this._throwable.getLocalizedMessage());
      this._messageTextArea.setColumns(_MESSAGE_COLUMNS);
      this._messageTextArea.setRows(_MESSAGE_ROWS);
      this._messageTextArea.setEditable(false);
      this._messageTextArea.setBackground(inactiveColor);

      // Cria a �rea p/ mostrar a stack da exce��o
      final JLabel stackTraceLabel = new
        JLabel(Language.get(this.getClass(), "stackTrace"));
      this._stackTraceTextArea =
        new JTextArea(getStackTraceText(this._throwable.getStackTrace()));
      this._stackTraceTextArea.setColumns(_STACK_TRACE_COLUMNS);
      this._stackTraceTextArea.setRows(_STACK_TRACE_ROWS);
      this._stackTraceTextArea.setEditable(false);
      this._stackTraceTextArea.setBackground(inactiveColor);

      // Cria o painel com os bot�es do di�logo.
      JPanel buttonsPanel = createButtonPanel();

      setLayout(new GridBagLayout());
      JScrollPane msgScrollPane = new JScrollPane(this._messageTextArea);
      JScrollPane stackTraceScroll = new JScrollPane(this._stackTraceTextArea);

      Insets li = new Insets(5, 6, 0, 10);
      Insets fi = new Insets(3, 10, 5, 10);

      int y = 0;

      // �rvore
      add(troubleTreeLabel, new GBC(0, y++).west().insets(li));
      add(scrollTroubleTree, new GBC(0, y++).both().insets(fi));

      // mensagem
      add(messageLabel, new GBC(0, y++).west().insets(li));
      add(msgScrollPane, new GBC(0, y++).both().insets(fi));

      // pilha de execu��o
      add(stackTraceLabel, new GBC(0, y++).west().insets(li));
      add(stackTraceScroll, new GBC(0, y++).both().insets(fi));

      add(buttonsPanel, new GBC(0, y++).center().insets(7));

      pack();
      center();
    }

    /**
     * Cria��o do painel de bot�es.
     *
     * @return o painel.
     */
    private JPanel createButtonPanel() {
      final JPanel panel = new JPanel();
      final JButton closeButton = new JButton(new CloseAction(this));

      SwingUtilities.equalizeComponentSize(new JButton[] { closeButton });

      panel.add(closeButton);
      return panel;
    }

    /**
     * Atualiza��o dos campos relativos a exce��o selecionada.
     */
    private void updateFields() {
      ThrowableTreeNode node =
        (ThrowableTreeNode) this._throwableTree.getSelectionPath()
          .getLastPathComponent();

      if (node == null) {
        node = (ThrowableTreeNode) this._throwableTree.getModel().getRoot();
      }
      this._messageTextArea.setText(node.getThrowable().getLocalizedMessage());
      this._stackTraceTextArea.setText(getStackTraceText(node.getThrowable()
        .getStackTrace()));
    }

    /**
     * Busca de uma string que representa a pilha de execu��o.
     *
     * @param stackTrace a pilha.
     * @return uma string.
     */
    private String getStackTraceText(final StackTraceElement[] stackTrace) {
      String text = "";
      for (StackTraceElement aStackTrace : stackTrace) {
        text += aStackTrace + "\n";
      }
      return text;
    }
  }
}
