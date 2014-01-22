package busexplorer.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import scs.core.IComponent;
import tecgraf.diagnostic.addons.openbus.v20.OpenBusMonitor;
import tecgraf.diagnostic.commom.StatusCode;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.assistant.Assistant;
import tecgraf.openbus.assistant.AssistantParams;
import tecgraf.openbus.assistant.OnFailureCallback;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.AccessDenied;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;
import admin.BusAdminImpl;

/**
 * Di�logo que obt�m os dados do usu�rio e do barramento para efetuar login
 */
public class LoginDialog {

  /** Label de endere�o do barramento */
  JLabel labelHost = null;
  /** Label de porta do barramento */
  JLabel labelPort = null;
  /** Di�logo */
  private JDialog loginDialog;
  /** Campo de texto para o endere�o do barramento */
  private JTextField fieldHost;
  /** Campo de texto para a porta do barramento */
  private JTextField fieldPort;
  /** Campo de texto para o nome do usu�rio (entidade do barramento). */
  private JTextField fieldUser;
  /** Campo de texto onde � digitada a senha do usu�rio. */
  private JPasswordField fieldPassword;
  /** Nome do host do barramento */
  private String host;
  /** Porta do barramento */
  private short port;
  /** Acessa os servi�os de administra��o do barramento */
  private Assistant assistant;
  /** Indicador de sucesso na realiza��o de login pelo di�logo */
  private boolean success = false;
  /** Refer�ncia para a biblioteca de administra��o */
  private BusAdminImpl admin;
  /** Indicador se o usu�rio autenticado possui perfil de administra��o */
  boolean isAdmin = false;

  /**
   * Construtor do di�logo.
   * 
   * @param owner janela pai.
   * @param admin biblioteca de administra��o
   */
  public LoginDialog(Window owner, BusAdminImpl admin) {
    createDialog(owner);
    this.admin = admin;
  }

  /**
   * Cria e inicializa o di�logo de login.
   * 
   * @param owner janela pai.
   */
  private void createDialog(Window owner) {
    loginDialog =
      new JDialog(owner, getDialogTitle(),
        JDialog.ModalityType.APPLICATION_MODAL);
    loginDialog.setResizable(false);
    loginDialog.setLocationRelativeTo(owner);
    loginDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        shutdownLoginDialog();
      }
    });

    buildLoginPane();
    loginDialog.pack();
  }

  /**
   * Faz shutdown do di�logo de login.
   */
  private void shutdownLoginDialog() {
    loginDialog.getOwner().dispose();
  }

  /**
   * Exibe o di�logo de login.
   * 
   * @return <code>true</code> se realizou um login com sucesso, e
   *         <code>false</code> caso contr�rio.
   */
  public boolean show() {
    fieldHost.requestFocusInWindow();
    loginDialog.setVisible(true);
    return success;
  }

  /**
   * @return T�tulo do di�logo
   */
  private String getDialogTitle() {
    return LNG.get("LoginDialog.title") + " - " + LNG.get("Application.title");
  }

  /**
   * Constr�i o pane de login.
   */
  private void buildLoginPane() {
    JPanel loginPanel = new JPanel(new BorderLayout());
    loginPanel.setBorder(new EmptyBorder(6, 6, 6, 6));

    JPanel configPanel = new JPanel();

    GridBagLayout configLayout = new GridBagLayout();
    configLayout.columnWidths = new int[] { 300, 120 };
    configPanel.setLayout(configLayout);

    TitledBorder configBorder =
      new TitledBorder(null, LNG.get("LoginDialog.config.label"));
    configPanel.setBorder(configBorder);

    // TODO ComboBox de barramentos pr�-configurados; foi por isso que as
    // GridBagConstraints "pularam" para a linha 3. --tmartins

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    labelHost = new JLabel(LNG.get("LoginDialog.host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, new GBC(0, 3).west().insets(6, 6, 3, 6));

    fieldHost = new JTextField();
    fieldHost.setFocusable(true);
    fieldHost.setToolTipText(LNG.get("LoginDialog.host.help"));
    fieldHost.addFocusListener(new SelectAllTextListener());
    configPanel.add(fieldHost, new GBC(0, 4).horizontal().insets(0, 6, 6, 9));

    labelPort = new JLabel(LNG.get("LoginDialog.port.label"));
    labelPort.setFont(FONT_LABEL);
    configPanel.add(labelPort, new GBC(1, 3).west().insets(6, 0, 3, 6));

    fieldPort = new JTextField();
    fieldPort.setToolTipText(LNG.get("LoginDialog.port.help"));
    configPanel.add(fieldPort, new GBC(1, 4).horizontal().insets(0, 0, 6, 6));
    fieldPort.setFocusable(true);
    fieldPort.addFocusListener(new SelectAllTextListener());

    JLabel labelUser = new JLabel(LNG.get("LoginDialog.user.label"));
    labelUser.setFont(FONT_LABEL);
    configPanel.add(labelUser, new GBC(0, 5).west().insets(6, 6, 3, 6));

    fieldUser = new JTextField();
    fieldUser.setToolTipText(LNG.get("LoginDialog.user.help"));
    configPanel.add(fieldUser, new GBC(0, 6).horizontal().insets(0, 6, 6, 9));
    fieldUser.setFocusable(true);
    fieldUser.addFocusListener(new SelectAllTextListener());

    JLabel labelPassword = new JLabel(LNG.get("LoginDialog.password.label"));
    labelPassword.setFont(FONT_LABEL);
    configPanel.add(labelPassword, new GBC(1, 5).west().insets(6, 0, 3, 6));

    fieldPassword = new JPasswordField();
    fieldPassword.setToolTipText(LNG.get("LoginDialog.password.help"));
    configPanel.add(fieldPassword, new GBC(1, 6).horizontal()
      .insets(0, 0, 6, 6));
    fieldPassword.setFocusable(true);
    fieldPassword.addFocusListener(new SelectAllTextListener());

    loginPanel.add(configPanel, BorderLayout.CENTER);

    JButton buttonLogin = new JButton(LNG.get("LoginDialog.confirm.button"));
    buttonLogin.setToolTipText(LNG.get("LoginDialog.confirm.help"));
    buttonLogin.addActionListener(new LoginAction());

    JButton buttonQuit = new JButton(LNG.get("LoginDialog.quit.button"));
    buttonQuit.setToolTipText(LNG.get("LoginDialog.quit.help"));
    buttonQuit.addActionListener(new QuitAction());

    Box buttonsBox = Box.createHorizontalBox();
    buttonsBox.setBorder(new EmptyBorder(9, 3, 3, 3));
    buttonsBox.add(Box.createHorizontalGlue());
    buttonsBox.add(buttonLogin);
    buttonsBox.add(Box.createHorizontalStrut(9));
    buttonsBox.add(buttonQuit);
    loginPanel.add(buttonsBox, BorderLayout.SOUTH);

    loginDialog.getRootPane().setDefaultButton(buttonLogin);

    loginDialog.setContentPane(loginPanel);
  }

  /**
   * Recupera o assistente do barramento.
   * 
   * @return o inst�ncia do assistente
   */
  public Assistant getAssistant() {
    return assistant;
  }

  /**
   * Recupera o nome do host do barramento.
   * 
   * @return o host ao qual estamos conectados.
   */
  public String getHost() {
    return host;
  }

  /**
   * Recupera a porta do barramento.
   * 
   * @return a porta � qual estamos conectados
   */
  public int getPort() {
    return port;
  }

  /**
   * Listener de sele��o de texto em um JTextField.
   * 
   * @author Tecgraf
   */
  private class SelectAllTextListener extends FocusAdapter {
    /**
     * Seleciona o texto do componente focado.
     */
    @Override
    public void focusGained(FocusEvent event) {
      ((JTextField) event.getComponent()).selectAll();
    }
  }

  /**
   * A��o que executa login no barramento
   * 
   * @author Tecgraf
   */
  private class LoginAction implements ActionListener {
    /** Intervalo de tempo para verificar se o login j� foi efetuado */
    final int LOGIN_CHECK_INTERVAL = 250;
    /** N�mero m�ximo de tentativas de login */
    final int MAX_LOGIN_FAILS = 3;
    /** �ltima exce��o lan�ada no login */
    Exception lastException = null;

    /**
     * Tenta logar no barramento at� conseguir, ou a tarefa remota ser
     * cancelada, ou receber uma exce��o AccessDenied, ou o n�mero de tentativas
     * ultrapassar o limite
     */
    @Override
    public void actionPerformed(ActionEvent event) {

      Task task = new Task() {
        volatile int failedAttempts = 0;
        volatile boolean accessDenied = false;

        @Override
        protected void performTask() throws Exception {
          host = fieldHost.getText();
          port = Short.valueOf(fieldPort.getText());

          String entity = fieldUser.getText();
          String password = new String(fieldPassword.getPassword());
          AssistantParams params = new AssistantParams();

          params.callback = new OnFailureCallback() {

            @Override
            public void onStartSharedAuthFailure(Assistant arg0, Throwable arg1) {
              // n�o iremos utilizar este recurso
            }

            @Override
            public void onRegisterFailure(Assistant arg0, IComponent arg1,
              ServiceProperty[] arg2, Throwable arg3) {
              // n�o iremos utilizar este recurso
            }

            @Override
            public void onLoginFailure(Assistant arg0, Throwable arg1) {
              lastException = (Exception) arg1;
              if (arg1 instanceof AccessDenied) {
                accessDenied = true;
              }
              failedAttempts++;
            }

            @Override
            public void onFindFailure(Assistant arg0, Throwable arg1) {
              // TODO precisamos realizar algum tratamento aqui?
            }
          };

          assistant =
            Assistant.createWithPassword(host, port, entity, password
              .getBytes(), params);

          OpenBusMonitor monitor =
            new OpenBusMonitor("openbus", (OpenBusContext) assistant.orb()
              .resolve_initial_references("OpenBusContext"));

          while (true) {
            if (wasCancelled()) {
              assistant.shutdown();
              break;
            }
            if (accessDenied || failedAttempts == MAX_LOGIN_FAILS) {
              assistant.shutdown();
              throw lastException;
            }
            if (monitor.checkResource().code == StatusCode.OK) {
              break;
            }

            try {
              Thread.sleep(LOGIN_CHECK_INTERVAL);
            }
            catch (InterruptedException e) {
            }
          }
          admin.connect(host, port, assistant.orb());
          isAdmin = isCurrentUserAdmin();
        }

        @Override
        protected void afterTaskUI() {
          if (getError() == null) {
            loginDialog.dispose();
            success = true;
          }
        }

        @Override
        protected void handleError(Exception exception) {
          // TODO Alterar tratamento de erros. Outros erros podem ocorrer
          if (exception != null) {
            if (accessDenied) {
              JOptionPane.showMessageDialog(loginDialog, LNG
                .get("LoginDialog.login.accessDenied.message"), LNG
                .get("ProgressDialog.error.title"), JOptionPane.ERROR_MESSAGE);
            }
            else {
              JOptionPane.showMessageDialog(loginDialog, LNG
                .get("LoginDialog.login.communicationError.message"), LNG
                .get("ProgressDialog.error.title"), JOptionPane.ERROR_MESSAGE);
            }
          }
          // Retorna o foco para o TextField de host (o primeiro).
          fieldHost.requestFocus();
          success = false;
          isAdmin = false;
        }
      };

      task.execute(loginDialog, LNG.get("LoginDialog.waiting.title"), LNG
        .get("LoginDialog.waiting.msg"));
    }
  }

  /**
   * Verifica se o usu�rio tem permiss�es para administrar o barramento.
   * 
   * @return Booleano que indica se o usu�rio � administrador ou n�o.
   * @throws ServiceFailure erro ao acessar registro de logins.
   */
  private boolean isCurrentUserAdmin() throws ServiceFailure {
    // Se o m�todo getLogins() n�o lan�ar exce��o, o usu�rio logado est�
    // cadastrado como administrador no barramento.
    try {
      admin.getLogins();
      return true;
    }
    catch (UnauthorizedOperation e) {
      return false;
    }
  }

  /**
   * Indica se o usu�rio autenticado no momento possui permiss�o de
   * administra��o.
   * 
   * @return <code>true</code> se possui permiss�o de administra��o e
   *         <code>false</code> caso contr�rio.
   */
  public boolean isAdmin() {
    return isAdmin;
  }

  /**
   * A��o que termina a aplica��o.
   * 
   * @author Tecgraf
   */
  private class QuitAction implements ActionListener {
    /**
     * Termina a aplica��o.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
      loginDialog.dispose();
      shutdownLoginDialog();
    }
  }
}
