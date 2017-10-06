package busexplorer.desktop.dialog;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.BusExplorerLogin;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.utils.BusAddress;
import busexplorer.utils.BusAddress.AddressType;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.ConfigurationProperties;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.GBC;

import javax.swing.Box;
import javax.swing.ImageIcon;
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

/**
 * Diálogo que obtém os dados do usuário e do barramento para efetuar login.
 *
 * @author Tecgraf
 */
public class LoginDialog extends JDialog {
  /** Combo box de barramentos pré-configurados */
  private JComboBox comboBus;
  /** Combo box de domínios pré-configurados */
  private JComboBox comboDomain;
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

  /**
   * Construtor do diálogo.
   *  @param owner janela pai.
   *
   */
  public LoginDialog(Window owner) {
    super(owner, Language.get(LoginDialog.class, "title"), JDialog.ModalityType.APPLICATION_MODAL);
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

    JPanel configPanel = new JPanel(new MigLayout("fill, flowy"));

    TitledBorder configBorder =
      new TitledBorder(null, Language.get(this.getClass(),"config.label"));
    configPanel.setBorder(configBorder);

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    EnableLoginListener enableLoginListener = new EnableLoginListener();
    SelectAllTextListener selectAllTextListener = new SelectAllTextListener();

    ConfigurationProperties configProps = new ConfigurationProperties();
    final Vector<BusAddress> busVector = new Vector<>();
    final Vector<Vector<String>> busDomain = new Vector<>();

    for (int i = 1;; i++) {
      String busPrefix = "bus" + i + ".";
      String description = configProps.getProperty(busPrefix + "description");
      String address = configProps.getProperty(busPrefix + "address");
      if (description == null || address == null) {
        break;
      }
      busDomain.add(new Vector<>());
      for (int j = 1;; j++) {
        String domain = configProps.getProperty(busPrefix + "domain" + j);
        if (domain == null) {
          break;
        }
        busDomain.get(i-1).add(domain);
      }
      busVector.add(BusAddress.toAddress(description, address));
    }

    fieldAddress = new JTextField(30);

    if (!busVector.isEmpty()) {
      busVector.add(BusAddress.UNSPECIFIED_ADDRESS);
      busDomain.add(new Vector<>());

      JLabel labelBus = new JLabel(Language.get(this.getClass(),"bus.label"));
      labelBus.setFont(FONT_LABEL);
      configPanel.add(labelBus, "grow");

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
      configPanel.add(comboBus, "grow");
    }

    JLabel labelHost = new JLabel(Language.get(this.getClass(),"host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, "grow");

    fieldAddress.setToolTipText(Language.get(this.getClass(),"host.help"));
    fieldAddress.addFocusListener(selectAllTextListener);
    fieldAddress.getDocument().addDocumentListener(enableLoginListener);
    fieldAddress.setFocusable(true);
    if (!busVector.isEmpty()) {
      fieldAddress.setEnabled(false);
    }
    configPanel
      .add(fieldAddress, "grow");

    JLabel labelUser = new JLabel(Language.get(this.getClass(),"user.label"));
    labelUser.setFont(FONT_LABEL);
    configPanel.add(labelUser, "grow");

    fieldUser = new JTextField();
    fieldUser.setToolTipText(Language.get(this.getClass(),"user.help"));
    fieldUser.addFocusListener(selectAllTextListener);
    fieldUser.getDocument().addDocumentListener(enableLoginListener);
    fieldUser.setFocusable(true);
    configPanel.add(fieldUser, "grow");

    JLabel labelPassword = new JLabel(Language.get(this.getClass(),"password.label"));
    labelPassword.setFont(FONT_LABEL);
    configPanel.add(labelPassword, "grow");

    fieldPassword = new JPasswordField();
    fieldPassword.setToolTipText(Language.get(this.getClass(),"password.help"));
    fieldPassword.addFocusListener(selectAllTextListener);
    fieldPassword.setFocusable(true);
    configPanel.add(fieldPassword, "grow");

    JPanel domainPanel = new JPanel(new MigLayout("ins 0, fill","[][]","[]"));
    JLabel labelDomain = new JLabel(Language.get(this.getClass(),"domain.label"));
    labelDomain.setFont(FONT_LABEL);
    domainPanel.add(labelDomain, "grow, wrap");

    fieldDomain = new JTextField();
    fieldDomain.setToolTipText(Language.get(this.getClass(),"domain.help"));
    fieldDomain.addFocusListener(selectAllTextListener);
    fieldDomain.setFocusable(true);
    domainPanel.add(fieldDomain, "grow, push");
    comboDomain = new JComboBox<String>();
    comboDomain.setEnabled(false);
    domainPanel.add(comboDomain, "grow");
    configPanel.add(domainPanel, "grow");

    loginPanel.add(configPanel, BorderLayout.CENTER);

    buttonLogin = new JButton(Language.get(this.getClass(),"confirm.button"));
    buttonLogin.setMnemonic(Language.get(this.getClass(),"confirm.button.mnemonic").charAt(0));
    buttonLogin.setToolTipText(Language.get(this.getClass(),"confirm.help"));
    buttonLogin.addActionListener(new LoginAction());

    buttonLogin.setIcon(ApplicationIcons.ICON_LOGIN_16);
    buttonLogin.setEnabled(false);

    if (!busDomain.isEmpty()) {
      comboBus.addItemListener(listener -> {
        comboDomain.removeAllItems();
        BusAddress selectedBus = (BusAddress) comboBus.getSelectedItem();
        for (String d : busDomain.get(busVector.indexOf(selectedBus))) {
          comboDomain.addItem(d);
        }
        if (comboDomain.getItemCount() == 0) {
          comboDomain.setEnabled(false);
          fieldDomain.setEnabled(true);
        } else {
          comboDomain.setEnabled(true);
        }
      });
      comboDomain.addItemListener(listener -> {
        String selected = (String) comboDomain.getSelectedItem();
        fieldDomain.setText(selected);
        fieldDomain.setEnabled(false);
        buttonLogin.requestFocus();
      });
      // first presentation
      for (String d : busDomain.get(busVector.indexOf(busVector.get(0)))) {
        comboDomain.addItem(d);
      }
      if (comboDomain.getItemCount() == 0) {
        comboDomain.setEnabled(false);
        fieldDomain.setEnabled(true);
      } else {
        comboDomain.setEnabled(true);
      }
    }

    Box buttonsBox = Box.createHorizontalBox();
    buttonsBox.setBorder(new EmptyBorder(9, 3, 3, 3));
    buttonsBox.add(Box.createHorizontalGlue());
    buttonsBox.add(buttonLogin);
    loginPanel.add(buttonsBox, BorderLayout.SOUTH);

    JPanel leftBox = new JPanel(new GridBagLayout());
    leftBox.add(new JLabel(new ImageIcon(ApplicationIcons.BUSEXPLORER_LIST[ApplicationIcons.BUSEXPLORER_LIST.length-1])));
    String[] version = Application.version();
    JLabel apiVersion = new JLabel(version[0]);
    apiVersion.setFont(new Font(apiVersion.getFont().getFontName(), Font.PLAIN, (int)(apiVersion.getFont().getSize()*1.5)));
    leftBox.add(apiVersion, new GBC(0,1).insets(10));
    JLabel appVersion = new JLabel(version[1]);
    appVersion.setFont(new Font(appVersion.getFont().getFontName(), Font.PLAIN, (int)(appVersion.getFont().getSize()*1.2)));
    leftBox.add(appVersion, new GBC(0,2).insets(10));
    loginPanel.add(leftBox, BorderLayout.WEST);
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

      BusExplorerTask<Void> task =
        new BusExplorerTask<Void>(ExceptionContext.LoginByPassword) {

          BusExplorerLogin theLogin;

          @Override
          protected void doPerformTask() throws Exception {
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
            String domain = fieldDomain.getText().trim();
            String password = new String(fieldPassword.getPassword());

            theLogin = new BusExplorerLogin(address, entity, domain);
            theLogin.doLogin(password);
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

      task.execute(LoginDialog.this,
        Language.get(LoginDialog.class,"waiting.title"),
        Language.get(LoginDialog.class, "waiting.msg"), 2, 0);
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
