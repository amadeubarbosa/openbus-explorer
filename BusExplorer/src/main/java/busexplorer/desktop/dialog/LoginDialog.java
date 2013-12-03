package busexplorer.desktop.dialog;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import scs.core.IComponent;
import tecgraf.diagnostic.addons.openbus.v20.OpenBusMonitor;
import tecgraf.diagnostic.commom.StatusCode;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;
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

  /** Botão de confirmação do login */
  JButton loginButton = null;
  /** Label de endereço do barramento */
  JLabel addressLabel = null;
  /** Label de porta do barramento */
  JLabel portLabel = null;
  /** Label de chave */
  JLabel userLabel = null;
  /** Label de senha */
  JLabel passwordLabel = null;
  /** Diálogo */
  private JFrame loginDialog;
  /** Campo de texto para o endereço do barramento */
  private JTextField addressField;
  /** Campo de texto para a porta do barramento */
  private JTextField portField;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField userField;
  /** Campo de texto onde é digitada a senha do usuário. */
  private JTextField passwordField;
  /** Nome do host do barramento */
  private String host;
  /** Porta do barramento */
  private short port;
  /** Acessa os serviços de administração do barramento */
  private Assistant assistant;

  /** Construtor do diálogo. */
  public LoginDialog() {
    createDialog();
  }

  /** Cria e inicializa o diálogo de login. */
  private void createDialog() {
    loginDialog = new JFrame(getDialogTitle());
    JPanel mainPane = new JPanel(new GridBagLayout());

    mainPane.add(createLoginPanel(), new GBC(0, 1).north().horizontal().insets(
      new Insets(6, 10, 11, 11)));

    loginDialog.getContentPane().add(mainPane);
    loginDialog.pack();

    loginDialog.setResizable(false);
    GUIUtils.centerOnScreen(loginDialog);
    loginDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        shutdownLoginDialog();
      }

    });
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
   * Cria um painel para login.
   * 
   * @return o painel de login.
   */
  private JPanel createLoginPanel() {
    JPanel loginPane = new JPanel(new GridBagLayout());

    //Label de configurações de acesso ao barramento
    portLabel = new JLabel(LNG.get("LoginDialog.config.label"));
    loginPane.add(portLabel, new GBC(0, 0).none());

    //Label de endereço
    addressLabel = new JLabel(LNG.get("LoginDialog.address.label"));
    loginPane.add(addressLabel, new GBC(0, 1).west().insets(
      new Insets(20, 0, 0, 0)).none());

    //Campo de endereço
    addressField = new JTextField("localhost", 1);
    addressField.setName("address");
    addressField.setToolTipText(LNG.get("LoginDialog.address.help"));
    loginPane.add(addressField, new GBC(0, 2).west().insets(
      new Insets(0, 0, 0, 0)).horizontal());

    //Label de porta
    portLabel = new JLabel(LNG.get("LoginDialog.port.label"));
    loginPane.add(portLabel, new GBC(1, 1).southwest().insets(
      new Insets(0, 6, 0, 0)));

    //Campo de porta
    portField = new JTextField("2089", 1);
    portField.setName("port");
    portField.setToolTipText(LNG.get("LoginDialog.port.help"));
    loginPane.add(portField, new GBC(1, 2).insets(new Insets(0, 6, 0, 0))
      .horizontal());

    //Label de usuário
    userLabel = new JLabel(LNG.get("LoginDialog.user.label"));
    loginPane.add(userLabel, new GBC(0, 3).west());

    //Campo de usuário
    userField = new JTextField(1);
    userField.setName("username");
    userField.setToolTipText(LNG.get("LoginDialog.user.help"));
    loginPane.add(userField, new GBC(0, 4).west().horizontal());

    //Label de senha
    passwordLabel = new JLabel(LNG.get("LoginDialog.password.label"));
    loginPane.add(passwordLabel, new GBC(1, 3).west().insets(
      new Insets(0, 6, 0, 0)).right(70));

    //Campo de senha
    passwordField = new JPasswordField(1);
    passwordField.setName("password");
    passwordField.setToolTipText(LNG.get("LoginDialog.password.help"));
    loginPane.add(passwordField, new GBC(1, 4).west().horizontal().insets(
      new Insets(0, 6, 0, 0)));

    //Botão de login
    loginButton = new JButton(LNG.get("LoginDialog.confirm.button"));
    loginButton.setToolTipText(LNG.get("LoginDialog.confirm.help"));
    loginPane.add(loginButton, new GBC(2, 2).insets(new Insets(0, 6, 0, 0)));

    loginDialog.getRootPane().setDefaultButton(loginButton);

    loginButton.addActionListener(new LoginAction());

    return loginPane;
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
          host = addressField.getText();
          port = Short.valueOf(portField.getText());

          String entity = userField.getText();
          String password = passwordField.getText();
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
            MainDialog mainDialog = new MainDialog(host, port, assistant);
            mainDialog.show();
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

}
