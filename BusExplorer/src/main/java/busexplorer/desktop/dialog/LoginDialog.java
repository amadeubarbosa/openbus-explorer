package busexplorer.desktop.dialog;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.BusExplorerLogin;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.utils.BusAddress;
import busexplorer.utils.BusAddress.AddressType;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.preferences.ApplicationPreferences;
import busexplorer.utils.Language;
import busexplorer.utils.preferences.BusExplorerPrefs;
import busexplorer.utils.preferences.PrefName;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.GBC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Diálogo que obtém os dados do usuário e do barramento para efetuar login.
 *
 * @author Tecgraf
 */
public class LoginDialog extends JDialog {
  /** Combo box de domínios pré-configurados */
  private JComboBox comboDomain;
  /** Campo de texto para o endereço do barramento */
  private JComboBox fieldAddress;
  /** Campo de texto para o nome do usuário (entidade do barramento). */
  private JTextField fieldUser;
  /** Campo de texto onde é digitada a senha do usuário. */
  private JPasswordField fieldPassword;
  /** Botão que executa a ação de login. */
  private JButton buttonLogin;
  /** Informações de login */
  private BusExplorerLogin login;
  /** Preferências da aplicação */
  private ApplicationPreferences prefs = BusExplorerPrefs.instance();

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

    final Font FONT_LABEL = new Font("Dialog", Font.PLAIN, 12);

    EnableLoginListener enableLoginListener = new EnableLoginListener();
    SelectAllTextListener selectAllTextListener = new SelectAllTextListener();

    // Carregando das preferências os endereços já logados com sucesso
    String[] addresses = prefs.readCollection(PrefName.ADDRESSES).toArray(new String[]{});


    JLabel labelHost = new JLabel(Language.get(this.getClass(),"host.label"));
    labelHost.setFont(FONT_LABEL);
    configPanel.add(labelHost, "grow");

    fieldAddress = new JComboBox<>(new DefaultComboBoxModel<>(addresses));
    fieldAddress.setEditable(true);
    new JComboBoxKeyAdapterMaxLength(fieldAddress, 30);
    fieldAddress.setToolTipText(Language.get(this.getClass(),"host.help"));
    fieldAddress.addFocusListener(selectAllTextListener);
    fieldAddress.addItemListener(enableLoginListener);
    fieldAddress.addActionListener(al -> {
      if ("comboBoxChanged".equals(al.getActionCommand())) {
        JComboBox jComboBox = ((JComboBox)al.getSource());
        String host = (String)jComboBox.getSelectedItem();
        fieldUser.setText(prefs.read(host, PrefName.LAST_USER));
        comboDomain.removeAllItems();
        prefs.readCollection(host, PrefName.DOMAINS).forEach(item -> comboDomain.addItem(item));
        if (comboDomain.getItemCount() > 0) {
          comboDomain.setSelectedIndex(0);
        }
      }
    });
    fieldAddress.setFocusable(true);
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

    comboDomain = new JComboBox<String>();
    new JComboBoxKeyAdapterMaxLength(comboDomain, 30);
    comboDomain.setEditable(true);
    comboDomain.setToolTipText(Language.get(this.getClass(),"domain.help"));
    comboDomain.addFocusListener(selectAllTextListener);
    comboDomain.setFocusable(true);
    domainPanel.add(comboDomain, "grow, push");
    configPanel.add(domainPanel, "grow");

    loginPanel.add(configPanel, BorderLayout.CENTER);

    buttonLogin = new JButton(Language.get(this.getClass(),"confirm.button"));
    buttonLogin.setMnemonic(Language.get(this.getClass(),"confirm.button.mnemonic").charAt(0));
    buttonLogin.setToolTipText(Language.get(this.getClass(),"confirm.help"));
    buttonLogin.addActionListener(new LoginAction());

    buttonLogin.setIcon(ApplicationIcons.ICON_LOGIN_16);
    buttonLogin.setEnabled(false);

    // Caso alguma preferência já tenha sido carregada, seleciona o primeiro item
    if (fieldAddress.getItemCount() > 0) {
      fieldAddress.setSelectedIndex(0);
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
      fieldAddress.requestFocus();
    }
    else {
      fieldAddress.getEditor().setItem(propertyHost);

      if (propertyHost == null) {
        fieldAddress.requestFocus();
      }
      else {
        fieldUser.requestFocus();
      }
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
      if (event.getComponent() instanceof JTextField) {
        ((JTextField) event.getComponent()).selectAll();
      }
      else if (event.getComponent() instanceof JComboBox) {
        ((JComboBox) event.getComponent()).getEditor().selectAll();
      }
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
            String host = fieldAddress.getEditor().getItem().toString().trim();
            BusAddress address = BusAddress.toAddress(null, host);

            String entity = fieldUser.getText().trim();
            String domain = comboDomain.getSelectedItem() == null ? "" : comboDomain.getSelectedItem().toString().trim();
            String password = new String(fieldPassword.getPassword());

            theLogin = new BusExplorerLogin(address, entity, domain);
            theLogin.doLogin(password);
            // Escreve as preferências: elas serão escritas de acordo com os endereços informados
            prefs.addToCollection(PrefName.ADDRESSES, host, false);
            prefs.write(host, PrefName.LAST_USER, entity);
            prefs.addToCollection(host, PrefName.DOMAINS, domain, false);
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
  private class EnableLoginListener implements DocumentListener, ItemListener {
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
      String text = ((String)fieldAddress.getEditor().getItem()).trim();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e.getItem() != null) {
        validate();
      }
    }

  }

}
