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
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.BusExplorerLogin;
import busexplorer.utils.BusAddress;
import busexplorer.utils.BusAddress.AddressType;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.ConfigurationProperties;
import exception.handling.ExceptionContext;

/**
 * Diálogo que obtém os dados do usuário e do barramento para efetuar login.
 *
 * @author Tecgraf
 */
public class LoginDialog extends JDialog {
  /** Combo box de barramentos pré-configurados */
  private JComboBox comboBus;
  /** Campo de texto para o endereço do barramento */
  private JTextField fieldAddress;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField fieldUser;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField fieldDomain;
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
    super(owner, LNG.get("LoginDialog.title") + " - "
      + LNG.get("Application.title"), JDialog.ModalityType.APPLICATION_MODAL);
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
    configLayout.columnWidths = new int[] { 300 };
    configPanel.setLayout(configLayout);

    TitledBorder configBorder =
      new TitledBorder(null, LNG.get("LoginDialog.config.label"));
    configPanel.setBorder(configBorder);

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    EnableLoginListener enableLoginListener = new EnableLoginListener();
    SelectAllTextListener selectAllTextListener = new SelectAllTextListener();

    ConfigurationProperties configProps = new ConfigurationProperties();
    final Vector<BusAddress> busVector = new Vector<>();

    for (int i = 1;; i++) {
      String busPrefix = "bus" + i + ".";
      String description = configProps.getProperty(busPrefix + "description");
      String address = configProps.getProperty(busPrefix + "address");
      if (description == null || address == null) {
        break;
      }
      busVector.add(BusAddress.toAddress(description, address));
    }

    fieldAddress = new JTextField();

    if (!busVector.isEmpty()) {
      busVector.add(BusAddress.UNSPECIFIED_ADDRESS);

      JLabel labelBus = new JLabel(LNG.get("LoginDialog.bus.label"));
      labelBus.setFont(FONT_LABEL);
      configPanel.add(labelBus, new GBC(0, 0).horizontal().insets(6, 6, 3, 6));

      comboBus = new JComboBox<>(busVector);
      comboBus.addItemListener(e -> {
        BusAddress selectedBus = (BusAddress) comboBus.getSelectedItem();
        updateAddress(selectedBus);
        if (selectedBus.getType().equals(AddressType.Unspecified)) {
          fieldAddress.setEnabled(true);
          fieldAddress.requestFocus();
        }
        else {
          fieldAddress.setEnabled(false);
          fieldUser.requestFocus();
        }
      });
      configPanel.add(comboBus, new GBC(0, 1).horizontal().insets(0, 6, 6, 9));
    }

    JLabel labelHost = new JLabel(LNG.get("LoginDialog.host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, new GBC(0, 2).west().insets(6, 6, 3, 6));

    fieldAddress.setToolTipText(LNG.get("LoginDialog.host.help"));
    fieldAddress.addFocusListener(selectAllTextListener);
    fieldAddress.getDocument().addDocumentListener(enableLoginListener);
    fieldAddress.setFocusable(true);
    if (!busVector.isEmpty()) {
      fieldAddress.setEnabled(false);
    }
    configPanel
      .add(fieldAddress, new GBC(0, 3).horizontal().insets(0, 6, 6, 9));

    JLabel labelUser = new JLabel(LNG.get("LoginDialog.user.label"));
    labelUser.setFont(FONT_LABEL);
    configPanel.add(labelUser, new GBC(0, 4).west().insets(6, 6, 3, 6));

    fieldUser = new JTextField();
    fieldUser.setToolTipText(LNG.get("LoginDialog.user.help"));
    fieldUser.addFocusListener(selectAllTextListener);
    fieldUser.getDocument().addDocumentListener(enableLoginListener);
    fieldUser.setFocusable(true);
    configPanel.add(fieldUser, new GBC(0, 5).horizontal().insets(0, 6, 6, 9));

    JLabel labelPassword = new JLabel(LNG.get("LoginDialog.password.label"));
    labelPassword.setFont(FONT_LABEL);
    configPanel.add(labelPassword, new GBC(0, 6).west().insets(6, 6, 3, 6));

    fieldPassword = new JPasswordField();
    fieldPassword.setToolTipText(LNG.get("LoginDialog.password.help"));
    fieldPassword.addFocusListener(selectAllTextListener);
    fieldPassword.setFocusable(true);
    configPanel.add(fieldPassword, new GBC(0, 7).horizontal()
      .insets(0, 6, 6, 9));

    JLabel labelDomain = new JLabel(LNG.get("LoginDialog.domain.label"));
    labelDomain.setFont(FONT_LABEL);
    configPanel.add(labelDomain, new GBC(0, 8).west().insets(6, 6, 3, 6));

    fieldDomain = new JTextField();
    fieldDomain.setToolTipText(LNG.get("LoginDialog.domain.help"));
    fieldDomain.addFocusListener(selectAllTextListener);
    fieldDomain.setFocusable(true);
    configPanel.add(fieldDomain, new GBC(0, 9).horizontal().insets(0, 6, 6, 9));

    loginPanel.add(configPanel, BorderLayout.CENTER);

    buttonLogin = new JButton(LNG.get("LoginDialog.confirm.button"));
    buttonLogin.setToolTipText(LNG.get("LoginDialog.confirm.help"));
    buttonLogin.addActionListener(new LoginAction());
    buttonLogin.setEnabled(false);

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

    setContentPane(loginPanel);
    getRootPane().setDefaultButton(buttonLogin);

    pack();

    fillLoginForm();
  }

  /**
   * Preenche os campos do formulário de login e ajusta o foco adequadamente.
   */
  private void fillLoginForm() {
    String propertyHost = System.getProperty("host");
    String propertyPort = System.getProperty("port");

    try {
      Integer.parseInt(propertyPort);
    }
    catch (NumberFormatException e) {
      propertyPort = null;
    }

    if (propertyHost == null && propertyPort == null) {
      if (comboBus != null) {
        updateAddress((BusAddress) comboBus.getSelectedItem());
        fieldUser.requestFocus();
      }
      else {
        fieldAddress.requestFocus();
      }
    }
    else {
      if (comboBus != null) {
        comboBus.setSelectedItem(BusAddress.UNSPECIFIED_ADDRESS);
      }
      fieldAddress.setText(propertyHost);

      if (propertyHost == null) {
        fieldAddress.requestFocus();
      }
      else {
        fieldUser.requestFocus();
      }
    }
  }

  /**
   * Atualiza os campos de endereço de acordo com a manipulação da combo box de
   * barramentos.
   * 
   * @param selectedBus o barramento selecionado
   */
  private void updateAddress(BusAddress selectedBus) {
    switch (selectedBus.getType()) {
      case Address:
        fieldAddress.setText(String.format("%s:%d", selectedBus.getHost(),
          selectedBus.getPort()));
        break;
      case Reference:
        fieldAddress.setText(selectedBus.getIOR());
        break;
      case Unspecified:
        fieldAddress.setText("");
        break;
    }
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
            BusAddress address;
            if (comboBus == null) {
              address =
                      BusAddress.toAddress(null, fieldAddress.getText().trim());
            } else {
              address = (BusAddress) comboBus.getSelectedItem();
              if (address.getType().equals(AddressType.Unspecified)) {
                address =
                        BusAddress.toAddress(null, fieldAddress.getText().trim());
              }
            }

            String entity = fieldUser.getText().trim();
            String password = new String(fieldPassword.getPassword());
            String domain = fieldDomain.getText().trim();

            theLogin = new BusExplorerLogin(admin, entity, address);
            BusExplorerLogin.doLogin(theLogin, password, domain);
          }

          @Override
          protected void afterTaskUI() {
            if (getStatus()) {
              LoginDialog.this.dispose();
              login = theLogin;
            }
            else {
              if (theLogin != null) {
                theLogin.logout();
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
      String text = fieldAddress.getText().trim();
      BusAddress address = BusAddress.toAddress(null, text);
      if (text.length() > 0
        && !address.getType().equals(AddressType.Unspecified)
        && fieldUser.getText().trim().length() > 0) {
        buttonLogin.setEnabled(true);
      }
      else {
        buttonLogin.setEnabled(false);
      }
    }
  }
}
