package busexplorer.desktop.dialog;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import tecgraf.openbus.core.v2_0.services.access_control.AccessDenied;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;

/**
 * Diálogo que obtém os dados do usuário e do barramento para efetuar login
 */
public class LoginDialog {

  /** Label de endereço do barramento */
  JLabel labelHost = null;
  /** Label de porta do barramento */
  JLabel labelPort = null;
  /** Diálogo */
  private JDialog loginDialog;
  /** Campo de texto para o endereço do barramento */
  private JTextField fieldHost;
  /** Campo de texto para a porta do barramento */
  private JTextField fieldPort;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField fieldUser;
  /** Campo de texto onde é digitada a senha do usuário. */
  private JPasswordField fieldPassword;
  /** Nome do host do barramento */
  private String host;
  /** Porta do barramento */
  private short port;
  /** Acessa os serviços de administração do barramento */
  private Assistant assistant;

  /**
   * Construtor do diálogo.
   */
  public LoginDialog(Window owner) {
    createDialog(owner);
  }

  /**
   * Cria e inicializa o diálogo de login.
   */
  private void createDialog(Window owner) {
    loginDialog = new JDialog(owner, getDialogTitle(),
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
   * Faz shutdown do diálogo de login.
   */
  private void shutdownLoginDialog() {
    loginDialog.dispose();
    try {
      System.exit(0);
    }
    catch (Exception e) {
    }
  }

  /**
   * Exibe o diálogo de login.
   */
  public void show() {
    loginDialog.setVisible(true);
  }

  /**
   * @return Título do diálogo
   */
  private String getDialogTitle() {
    return LNG.get("LoginDialog.title") + " - " + LNG.get("Application.title");
  }

  /**
   * Constrói o pane de login.
   */
  private void buildLoginPane() {
    JPanel loginPanel = new JPanel(new BorderLayout());
    loginPanel.setBorder(new EmptyBorder(6, 6, 6, 6));

    JPanel configPanel = new JPanel();

    GridBagLayout configLayout = new GridBagLayout();
    configLayout.columnWidths = new int[] { 300, 120 };
    configPanel.setLayout(configLayout);

    TitledBorder configBorder = new TitledBorder(null, 
      LNG.get("LoginDialog.config.label"));
    configPanel.setBorder(configBorder);

    // TODO ComboBox de barramentos pré-configurados; foi por isso que as
    // GridBagConstraints "pularam" para a linha 3. --tmartins

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    labelHost = new JLabel(LNG.get("LoginDialog.host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, new GBC(0, 3).west().insets(6, 6, 3, 6));

    fieldHost = new JTextField();
    fieldHost.setToolTipText(LNG.get("LoginDialog.host.help"));
    configPanel.add(fieldHost, new GBC(0, 4).horizontal().insets(0, 6, 6, 9));

    labelPort = new JLabel(LNG.get("LoginDialog.port.label"));
    labelPort.setFont(FONT_LABEL);
    configPanel.add(labelPort, new GBC(1, 3).west().insets(6, 0, 3, 6));

    fieldPort = new JTextField();
    fieldPort.setToolTipText(LNG.get("LoginDialog.port.help"));
    configPanel.add(fieldPort, new GBC(1, 4).horizontal().insets(0, 0, 6, 6));

    JLabel labelUser = new JLabel(LNG.get("LoginDialog.user.label"));
    labelUser.setFont(FONT_LABEL);
    configPanel.add(labelUser, new GBC(0, 5).west().insets(6, 6, 3, 6));

    fieldUser = new JTextField();
    fieldUser.setToolTipText(LNG.get("LoginDialog.user.help"));
    configPanel.add(fieldUser, new GBC(0, 6).horizontal().insets(0, 6, 6, 9));

    JLabel labelPassword = new JLabel(LNG.get("LoginDialog.password.label"));
    labelPassword.setFont(FONT_LABEL);
    configPanel.add(labelPassword, new GBC(1, 5).west().insets(6, 0, 3, 6));

    fieldPassword = new JPasswordField();
    fieldPassword.setToolTipText(LNG.get("LoginDialog.password.help"));
    configPanel.add(fieldPassword, new
      GBC(1, 6).horizontal().insets(0, 0, 6, 6));

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
   */
  public Assistant getAssistant() {
    return assistant;
  }

  /**
   * Recupera o nome do host do barramento.
   */
  public String getHost() {
    return host;
  }

  /**
   * Recupera a porta do barramento.
   */
  public short getPort() {
    return port;
  }

  /**
   * Ação que executa login no barramento
   * 
   * @author Tecgraf
   */
  private class LoginAction implements ActionListener {
    /** Intervalo de tempo para verificar se o login já foi efetuado */
    final int LOGIN_CHECK_INTERVAL = 250;
    /** Número máximo de tentativas de login */
    final int MAX_LOGIN_FAILS = 3;
    /** Última exceção lançada no login */
    Exception lastException = null;

    /**
     * Tenta logar no barramento até conseguir, ou a tarefa remota ser
     * cancelada, ou receber uma exceção AccessDenied, ou o número de tentativas
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
          String password = fieldPassword.getText();
          AssistantParams params = new AssistantParams();

          params.callback = new OnFailureCallback() {

            @Override
            public void onStartSharedAuthFailure(Assistant arg0, Throwable arg1) {
            }

            @Override
            public void onRegisterFailure(Assistant arg0, IComponent arg1,
              ServiceProperty[] arg2, Throwable arg3) {
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
              return;
            }
            if (accessDenied || failedAttempts == MAX_LOGIN_FAILS) {
              assistant.shutdown();
              throw lastException;
            }
            if (monitor.checkResource().code == StatusCode.OK) {
              return;
            }

            try {
              Thread.sleep(LOGIN_CHECK_INTERVAL);
            }
            catch (InterruptedException e) {
            }
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getError() == null) {
            loginDialog.dispose();
          }
        }

        @Override
        protected void handleError(Exception exception) {
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
        }
      };

      task.execute(loginDialog, LNG.get("LoginDialog.waiting.title"), LNG
        .get("LoginDialog.waiting.msg"));
    }
  }

  /**
   * Ação que termina a aplicação.
   *
   * @author Tecgraf
   */
  private class QuitAction implements ActionListener {
    /**
     * Termina a aplicação.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
      shutdownLoginDialog();
    }
  }
}
