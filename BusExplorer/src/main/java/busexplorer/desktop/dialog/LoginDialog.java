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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.BusExplorerLogin;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * Diálogo que obtém os dados do usuário e do barramento para efetuar login.
 *
 * @author Tecgraf
 */
public class LoginDialog extends JDialog {

  /** Label de endereço do barramento */
  JLabel labelHost = null;
  /** Label de porta do barramento */
  JLabel labelPort = null;
  /** Campo de texto para o endereço do barramento */
  private JTextField fieldHost;
  /** Campo de texto para a porta do barramento */
  private JTextField fieldPort;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField fieldUser;
  /** Campo de texto onde é digitada a senha do usuário. */
  private JPasswordField fieldPassword;
  /** Botão que executa a ação de login. */
  private JButton buttonLogin;
  /** Informações de login */
  private BusExplorerLogin login;
  /** Referência para a biblioteca de administração */
  private BusAdmin admin;

  /**
   * Construtor do diálogo.
   * 
   * @param owner janela pai.
   * @param admin biblioteca de administração
   */
  public LoginDialog(Window owner, BusAdmin admin) {
    super(owner, LNG.get("LoginDialog.title") + " - " +
      LNG.get("Application.title"), JDialog.ModalityType.APPLICATION_MODAL);
    this.admin = admin;
    buildDialog();
  }

  /**
   * Constrói o diálogo de login.
   */
  private void buildDialog() {
    setResizable(false);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        getOwner().dispose();
        System.exit(0);
      }
    });

    buildLoginPane();
    pack();
    setLocationRelativeTo(getOwner());
  }

  /**
   * Recupera as informações de login.
   *
   * @return Informações de login.
   */
  public BusExplorerLogin getLogin() {
    return login;
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

    TitledBorder configBorder =
      new TitledBorder(null, LNG.get("LoginDialog.config.label"));
    configPanel.setBorder(configBorder);

    // TODO ComboBox de barramentos pré-configurados; foi por isso que as
    // GridBagConstraints "pularam" para a linha 3. --tmartins

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    EnableLoginListener enableLoginListener = new EnableLoginListener();
    SelectAllTextListener selectAllTextListener = new SelectAllTextListener();

    labelHost = new JLabel(LNG.get("LoginDialog.host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, new GBC(0, 3).west().insets(6, 6, 3, 6));

    fieldHost = new JTextField();
    fieldHost.setFocusable(true);
    fieldHost.setToolTipText(LNG.get("LoginDialog.host.help"));
    fieldHost.addFocusListener(selectAllTextListener);
    fieldHost.getDocument().addDocumentListener(enableLoginListener);
    configPanel.add(fieldHost, new GBC(0, 4).horizontal().insets(0, 6, 6, 9));

    labelPort = new JLabel(LNG.get("LoginDialog.port.label"));
    labelPort.setFont(FONT_LABEL);
    configPanel.add(labelPort, new GBC(1, 3).west().insets(6, 0, 3, 6));

    fieldPort = new JTextField();
    fieldPort.setToolTipText(LNG.get("LoginDialog.port.help"));
    configPanel.add(fieldPort, new GBC(1, 4).horizontal().insets(0, 0, 6, 6));
    fieldPort.setFocusable(true);
    fieldPort.addFocusListener(selectAllTextListener);
    fieldPort.getDocument().addDocumentListener(enableLoginListener);

    JLabel labelUser = new JLabel(LNG.get("LoginDialog.user.label"));
    labelUser.setFont(FONT_LABEL);
    configPanel.add(labelUser, new GBC(0, 5).west().insets(6, 6, 3, 6));

    fieldUser = new JTextField();
    fieldUser.setToolTipText(LNG.get("LoginDialog.user.help"));
    configPanel.add(fieldUser, new GBC(0, 6).horizontal().insets(0, 6, 6, 9));
    fieldUser.setFocusable(true);
    fieldUser.addFocusListener(selectAllTextListener);
    fieldUser.getDocument().addDocumentListener(enableLoginListener);

    JLabel labelPassword = new JLabel(LNG.get("LoginDialog.password.label"));
    labelPassword.setFont(FONT_LABEL);
    configPanel.add(labelPassword, new GBC(1, 5).west().insets(6, 0, 3, 6));

    fieldPassword = new JPasswordField();
    fieldPassword.setToolTipText(LNG.get("LoginDialog.password.help"));
    configPanel.add(fieldPassword, new GBC(1, 6).horizontal()
      .insets(0, 0, 6, 6));
    fieldPassword.setFocusable(true);
    fieldPassword.addFocusListener(selectAllTextListener);

    loginPanel.add(configPanel, BorderLayout.CENTER);

    buttonLogin = new JButton(LNG.get("LoginDialog.confirm.button"));
    buttonLogin.setToolTipText(LNG.get("LoginDialog.confirm.help"));
    buttonLogin.setEnabled(false);
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

    getRootPane().setDefaultButton(buttonLogin);

    setContentPane(loginPanel);

    fieldHost.requestFocusInWindow();
  }

  /**
   * Listener de seleção de texto em um JTextField.
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
   * Ação que executa login no barramento
   * 
   * @author Tecgraf
   */
  private class LoginAction implements ActionListener {
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event) {

      BusExplorerTask<Object> task =
         new BusExplorerTask<Object>(Application.exceptionHandler(),
           ExceptionContext.LoginByPassword) {

        BusExplorerLogin theLogin;

        @Override
        protected void performTask() throws Exception {
          String host = fieldHost.getText().trim();
          int port = Integer.parseInt(fieldPort.getText().trim());
          String entity = fieldUser.getText().trim();
          String password = new String(fieldPassword.getPassword());

          theLogin = new BusExplorerLogin(admin, entity, host, port);
          BusExplorerLogin.doLogin(theLogin, password);
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            LoginDialog.this.dispose();
            login = theLogin;
          } else {
            if (theLogin != null && theLogin.getAssistant() != null) {
              theLogin.getAssistant().shutdown();
            }
            fieldUser.requestFocus();
          }
        }
      };

      task.execute(LoginDialog.this, LNG.get("LoginDialog.waiting.title"), LNG
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
     *
     * @param event Evento.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
      getOwner().dispose();
      System.exit(0);
    }
  }

  /**
   * Listener que ajusta a habilitação do botão de login de acordo com uma
   * validação inicial.
   *
   * @author Tecgraf
   */
  private class EnableLoginListener implements DocumentListener {
    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
      validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
      validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
      validate();
    }

    /**
     * Realiza a validação.
     */
    private void validate() {
      try {
        Integer.parseInt(fieldPort.getText().trim());
      } catch (NumberFormatException e) {
        buttonLogin.setEnabled(false);
        return;
      }

      if (fieldHost.getText().trim().length() > 0 &&
        fieldPort.getText().trim().length() > 0 &&
        fieldUser.getText().trim().length() > 0) {
          buttonLogin.setEnabled(true);
      }
      else {
          buttonLogin.setEnabled(false);
      }
    }
  }
}
