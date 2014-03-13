package busexplorer.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;

import busexplorer.utils.Utils;

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
   * Conjunto de informa��es adicionais que ser�o mostradas na janela de erro e
   * inclu�das em um poss�vel email ao administrador.
   */
  protected String[] additionalInfo;

  /**
   * Construtor auxiliar com <code>JDialog</code>.
   * 
   * @param owner a janela pai
   * @param title o t�tulo
   * @param throwable o erro ou exce��o
   */
  protected ExceptionDialog(Window owner, String title, Throwable throwable) {
    this(owner, title, throwable, null);
  }

  /**
   * Construtor auxiliar com <code>JFrame</code>.
   * 
   * @param owner a janela pai
   * @param title o t�tulo
   * @param throwable o erro ou exce��o
   * @param additionalInfo - lista de informa��es adicionais que ser�o mostradas
   *        na janela de erro e inclu�das em um poss�vel email.
   */
  protected ExceptionDialog(Window owner, String title, Throwable throwable,
    String[] additionalInfo) {
    super(owner, title);
    this._throwable = throwable;
    this.additionalInfo = additionalInfo;
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
    return createDialog(window, title, throwable, (String[]) null);
  }

  /**
   * M�todo est�tico de cria��o de um di�logo com base em par�metros passados
   * pelo m�dulo-cliente para uso sem construtor.<br>
   * � criado um ExceptionDialog <b>simplificado</b> pois o foco deste �
   * apresentar a mensagem ao usu�rio. Caso o usu�rio queira saber mais sobre o
   * erro/exce��o, este di�logo apresenta uma op��o para detalhar.
   * 
   * @param window a janela-m�e.
   * @param title o t�tulo.
   * @param throwable a exce��o.
   * @param message mensagem a ser apresentada ao usu�rio
   * @return um dialogo de erro.
   */
  public static ExceptionDialog createDialog(final Window window,
    final String title, final Throwable throwable, String message) {
    return createDialog(window, title, throwable, message, null);
  }

  /**
   * M�todo est�tico de cria��o de um di�logo <b>detalhado</b> com base em
   * par�metros passados pelo m�dulo-cliente para uso sem construtor.
   * 
   * @param window a janela-m�e.
   * @param title o t�tulo.
   * @param throwable a exce��o.
   * @param additionalInfo - lista de informa��es adicionais que ser�o mostradas
   *        na janela de erro e inclu�das em um poss�vel email.
   * @return um dialogo de erro.
   */
  public static ExceptionDialog createDialog(final Window window,
    final String title, final Throwable throwable, String[] additionalInfo) {
    return new DetailedExceptionDialog(window, title, throwable, additionalInfo);
  }

  /**
   * M�todo est�tico de cria��o de um di�logo com base em par�metros passados
   * pelo m�dulo-cliente para uso sem construtor.<br>
   * � criado um ExceptionDialog <b>simplificado</b> pois o foco deste �
   * apresentar a mensagem ao usu�rio. Caso o usu�rio queira saber mais sobre o
   * erro/exce��o, este di�logo apresenta uma op��o para detalhar.
   * 
   * @param window a janela-m�e.
   * @param title o t�tulo.
   * @param throwable a exce��o.
   * @param message mensagem a ser apresentada ao usu�rio
   * @param additionalInfo - lista de informa��es adicionais que ser�o mostradas
   *        na janela de erro e inclu�das em um poss�vel email.
   * @return um dialogo de erro.
   */
  public static ExceptionDialog createDialog(final Window window,
    final String title, final Throwable throwable, String message,
    String[] additionalInfo) {
    return new SimpleExceptionDialog(window, title, throwable, message,
      additionalInfo);
  }

  /**
   * Ajuste de um conjunto de elementos de interface para o mesmo tamanho.
   * 
   * @param comps os widgets que ser�o ajustados.
   */
  public static void adjustEqualSizes(JComponent... comps) {
    final Dimension dim = new Dimension(0, 0);
    for (int i = 0; i < comps.length; i++) {
      final Dimension pref = comps[i].getPreferredSize();
      final double h = Math.max(dim.getHeight(), pref.getHeight());
      final double w = Math.max(dim.getWidth(), pref.getWidth());
      dim.setSize(w, h);
    }
    for (int i = 0; i < comps.length; i++) {
      comps[i].setPreferredSize(dim);
    }
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
     * @param additionalInfo - lista de informa��es adicionais que ser�o
     *        mostradas na janela de erro e inclu�das em um poss�vel email.
     */
    public DetailedExceptionDialog(final Window owner, final String title,
      final Throwable throwable, String[] additionalInfo) {
      super(owner, title, throwable, additionalInfo);
      createComponents();
    }

    /**
     * M�todo interno de constru��o da inteface gr�fica.
     */
    private void createComponents() {

      // Define a cor que ser� utilizada como cor de fundo para componentes
      // inativos
      Color inactiveColor =
        UIManager.getDefaults().getColor("TextField.inactiveBackground");

      // Cria uma �rvore de erros/exce��es
      final JLabel troubleTreeLabel = new
        JLabel(Utils.getString(this.getClass(), "exceptionTree"));
      this._throwableTree = new JTree(new ThrowableTreeNode(this._throwable));
      this._throwableTree.addTreeSelectionListener(new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
          updateFields();
        }
      });
      this._throwableTree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
      final JScrollPane scrollTroubleTree =
        new JScrollPane(this._throwableTree);
      scrollTroubleTree.setPreferredSize(new Dimension(_TROUBLE_TREE_WIDTH,
        _TROUBLE_TREE_HEIGHT));

      // Cria a �rea p/ mostrar a mensagem da exce��o selecionada na �rvore
      final JLabel messageLabel = new
        JLabel(Utils.getString(this.getClass(), "exceptionMessage"));
      this._messageTextArea =
        new JTextArea(this._throwable.getLocalizedMessage());
      this._messageTextArea.setColumns(_MESSAGE_COLUMNS);
      this._messageTextArea.setRows(_MESSAGE_ROWS);
      this._messageTextArea.setEditable(false);
      this._messageTextArea.setBackground(inactiveColor);

      // Cria a �rea p/ mostrar a stack da exce��o
      final JLabel stackTraceLabel = new
        JLabel(Utils.getString(this.getClass(), "stackTrace"));
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

      adjustEqualSizes(new JButton[] { closeButton });

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
      for (int i = 0; i < stackTrace.length; i++) {
        text += stackTrace[i] + "\n";
      }
      return text;
    }
  }

  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * Classe que modela um di�logo <b>simplificado</b> de exibi��o de um
   * erro/exce��o detectado no sistema. Este di�logo apresenta uma mensagem do
   * sistema para o usu�rio e d� a possibilidade de abrir um
   * DetailedExceptionDialog.
   * 
   * @see ExceptionDialog di�logo que apresenta um erro/exce��o, detectado no
   *      sistema, para o usu�rio.
   * @see DetailedExceptionDialog di�logo que detalha o erro/exce��o
   */
  private static final class SimpleExceptionDialog extends ExceptionDialog {

    /**
     * Construtor auxiliar com <code>JDialog</code>.
     * 
     * @param owner a janela pai
     * @param title o t�tulo
     * @param message as mensagens de erro
     * @param throwable o erro ou exce��o
     * @param additionalInfo - lista de informa��es adicionais que ser�o
     *        mostradas na janela de erro e inclu�das em um poss�vel email.
     */
    public SimpleExceptionDialog(final Window owner, final String title,
      final Throwable throwable, final String message,
      final String[] additionalInfo) {
      super(owner, title, throwable, additionalInfo);
      buildGui(message);
    }

    /**
     * Inicializa��o interna do di�logo.
     * 
     * @param message mensagem que deve aparecer no di�logo.
     */
    private void buildGui(String message) {
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(makeIconPanel(), BorderLayout.WEST);
      getContentPane().add(makeMainPanel(message), BorderLayout.CENTER);
      pack();
      center();
    }

    /**
     * Cria o painel que cont�m o �cone de erro
     * 
     * @return o painel
     */
    private JPanel makeIconPanel() {
      JPanel panel = new JPanel();
      panel.add(new JLabel(UIManager.getIcon("OptionPane.errorIcon")));

      return panel;
    }

    /**
     * Cria o painel principal
     * 
     * @param message mensagem a ser exibida
     * @return o painel
     */
    private JPanel makeMainPanel(String message) {

      JPanel mainPanel = new JPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(12, 12, 11, 11);
      c.anchor = GridBagConstraints.CENTER;
      c.weightx = 1;
      c.weighty = 1;
      mainPanel.add(makeInfoPanel(message), c);
      c.gridy = 1;
      c.anchor = GridBagConstraints.SOUTHEAST;
      c.insets = new Insets(17, 12, 11, 11);
      mainPanel.add(makeButtonsPanel(), c);

      return mainPanel;
    }

    /**
     * Cria o painel que cont�m a mensagem de erro
     * 
     * @param message mensagem a ser exibida
     * @return o painel
     */
    private JPanel makeInfoPanel(String message) {

      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(0, 0, 0, 0);

      c.fill = GridBagConstraints.HORIZONTAL;
      if (message == null || message.isEmpty() || message.startsWith("<html>")) {
        JLabel label = new JLabel(message);
        panel.add(label, c);
      }
      else {
        JMultilineLabel label = new JMultilineLabel();
        label.setMaxWidth(550);
        label.setJustified(false);
        label.setText(message);
        panel.add(label, c);
      }

      c.gridy = 1;
      c.insets = new Insets(12, 0, 0, 0);
      JLabel jwhere = new JLabel(Utils.getString(this.getClass(),
        "executionError"));
      panel.add(jwhere, c);
      c.gridy = 2;
      c.insets = new Insets(0, 0, 0, 0);
      panel.add(new JLabel(Utils.getString(this.getClass(), "contactError")),
        c);
      return panel;
    }

    /**
     * Cria o painel com os bot�es
     * 
     * @return o painel
     */
    private JPanel makeButtonsPanel() {
      final JPanel panel = new JPanel();
      final JButton detailButton = new JButton(new DetailThrowableAction(this));
      final JButton closeButton = new JButton(new CloseAction(this));

      adjustEqualSizes(new JButton[] { detailButton, closeButton });

      panel.add(detailButton);
      panel.add(closeButton);
      return panel;
    }
  }

  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * A��o que ao ser requisitada fecha a janela corrente.
   */
  class CloseAction extends AbstractAction {

    /**
     * Janela corrente que cont�m esta a��o amarrada a um de seus componentes.
     */
    protected ExceptionDialog owner;

    /**
     * Construtor
     * 
     * @param owner janela corrente
     */
    protected CloseAction(ExceptionDialog owner) {
      this(owner, LNG.get(ExceptionDialog.class.getSimpleName() + ".close"));
    }

    /**
     * Construtor
     * 
     * @param owner janela corrente
     * @param text texto.
     */
    protected CloseAction(ExceptionDialog owner, String text) {
      super(text);
      this.owner = owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      this.owner.dispose();
    }
  }

  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * A��o que ao ser requisitada abre um di�logo detalhando o erro/exce��o
   * apresentado pela janela corrente. Em seguida fecha a janela corrente.
   */
  final class DetailThrowableAction extends CloseAction {

    /**
     * Construtor
     * 
     * @param owner janela corrente
     */
    protected DetailThrowableAction(ExceptionDialog owner) {
      super(owner, LNG.get(ExceptionDialog.class.getSimpleName() +
        ".errorDetails"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      super.actionPerformed(e);
      ExceptionDialog dialog =
        ExceptionDialog.createDialog(owner, owner.getTitle(), owner._throwable,
          owner.additionalInfo);
      dialog.setVisible(true);
    }
  }
}