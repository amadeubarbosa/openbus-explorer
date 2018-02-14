package busexplorer.desktop.dialog;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.function.Consumer;

import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.table.ObjectTableModel;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.BusExplorerLogin;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.RefreshDelegate;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelActionInterface;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationAddAction;
import busexplorer.panel.authorizations.AuthorizationDeleteAction;
import busexplorer.panel.authorizations.AuthorizationRefreshAction;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.categories.CategoryAddAction;
import busexplorer.panel.categories.CategoryDeleteAction;
import busexplorer.panel.categories.CategoryEditAction;
import busexplorer.panel.categories.CategoryRefreshAction;
import busexplorer.panel.categories.CategoryTableProvider;
import busexplorer.panel.categories.CategoryWrapper;
import busexplorer.panel.certificates.CertificateAddAction;
import busexplorer.panel.certificates.CertificateDeleteAction;
import busexplorer.panel.certificates.CertificateEditAction;
import busexplorer.panel.certificates.CertificateRefreshAction;
import busexplorer.panel.certificates.CertificateTableProvider;
import busexplorer.panel.certificates.CertificateWrapper;
import busexplorer.panel.configuration.admins.AdminAddAction;
import busexplorer.panel.configuration.admins.AdminDeleteAction;
import busexplorer.panel.configuration.admins.AdminEditAction;
import busexplorer.panel.configuration.admins.AdminRefreshAction;
import busexplorer.panel.configuration.admins.AdminTableProvider;
import busexplorer.panel.configuration.admins.AdminWrapper;
import busexplorer.panel.configuration.validators.ValidatorDeleteAction;
import busexplorer.panel.configuration.validators.ValidatorRefreshAction;
import busexplorer.panel.configuration.validators.ValidatorRestartAction;
import busexplorer.panel.configuration.validators.ValidatorTableProvider;
import busexplorer.panel.configuration.validators.ValidatorWrapper;
import busexplorer.panel.consumers.ConsumerAddAction;
import busexplorer.panel.consumers.ConsumerDeleteAction;
import busexplorer.panel.consumers.ConsumerEditAction;
import busexplorer.panel.consumers.ConsumerRefreshAction;
import busexplorer.panel.consumers.ConsumerTableProvider;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractAddAction;
import busexplorer.panel.contracts.ContractDeleteAction;
import busexplorer.panel.contracts.ContractEditAction;
import busexplorer.panel.contracts.ContractRefreshAction;
import busexplorer.panel.contracts.ContractTableProvider;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.entities.EntityAddAction;
import busexplorer.panel.entities.EntityDeleteAction;
import busexplorer.panel.entities.EntityEditAction;
import busexplorer.panel.entities.EntityRefreshAction;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.healing.ConsistencyReportPanel;
import busexplorer.panel.integrations.IntegrationAddAction;
import busexplorer.panel.integrations.IntegrationDeleteAction;
import busexplorer.panel.integrations.IntegrationEditAction;
import busexplorer.panel.integrations.IntegrationExportToXLSAction;
import busexplorer.panel.integrations.IntegrationRefreshAction;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.interfaces.InterfaceAddAction;
import busexplorer.panel.interfaces.InterfaceDeleteAction;
import busexplorer.panel.interfaces.InterfaceRefreshAction;
import busexplorer.panel.interfaces.InterfaceTableProvider;
import busexplorer.panel.interfaces.InterfaceWrapper;
import busexplorer.panel.logins.LoginDeleteAction;
import busexplorer.panel.logins.LoginRefreshAction;
import busexplorer.panel.logins.LoginTableProvider;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.offers.OfferDeleteAction;
import busexplorer.panel.offers.OfferPropertiesAction;
import busexplorer.panel.offers.OfferRefreshAction;
import busexplorer.panel.offers.OfferStatusAction;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.panel.providers.ProviderAddAction;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderEditAction;
import busexplorer.panel.providers.ProviderRefreshAction;
import busexplorer.panel.providers.ProviderTableProvider;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusAddress;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import static busexplorer.Application.APPLICATION_LOGIN;

/**
 * Diálogo principal da aplicação.
 *
 * @author Tecgraf
 */
public class MainDialog extends JFrame implements PropertyChangeListener {
  public static final String TABBED_PANE_DISABLED_TEXT = "TabbedPane.disabledText";
  public static final String TABBED_PANE_FOREGROUND = "TabbedPane.foreground";
  public static final String COMPATIBILITY_FOREGROUND = "Label.disabledForeground";

  /**
   * Lista de consumidores a serem notificados da mudança da propriedade
   * {@link Application#APPLICATION_LOGIN} em outras partes deste diálogo.
   */
  private ArrayList<Consumer<Boolean>> notifiers = new ArrayList<>();

  /**
   * Painel de recursos de gerência do barramento.
   */
  private JTabbedPane featuresPane;
  /**
   * Botão de desconexão.
   */
  private JButton disconnect;
  /**
   * Barra de status.
   */
  private JLabel status;
  /**
   * Propriedades da aplicação.
   */
  private Properties properties;

  /**
   * Construtor.
   *  @param properties proprieades da aplicação.
   *
   */
  public MainDialog(Properties properties) {
    this.properties = properties;
    setIconImages(Arrays.asList(ApplicationIcons.BUSEXPLORER_LIST));
    buildDialog();
  }

  /**
   * Obtém as propriedades da aplicação.
   *
   * @return propriedades da aplicação.
   */
  public Properties getProperties() {
    return this.properties;
  }

  /**
   * Desconecta-se do barramento e libera os recursos da janela.
   */
  @Override
  public void dispose() {
    if (Application.login() != null) {
      Application.login().logout();
    }
    super.dispose();
  }

  /**
   * Atualiza membros dependentes de novas informações de login.
   */
  @Override
  public void propertyChange(PropertyChangeEvent e) {
    String propertyName = e.getPropertyName();
    BusExplorerLogin busExplorerLogin = (BusExplorerLogin) e.getNewValue();
    if (APPLICATION_LOGIN.equals(propertyName)) {
      String bus;
      BusAddress address = busExplorerLogin.address;
      if (address.getDescription() != null) {
        bus = address.getDescription();
      }
      else {
        bus = address.toString();
      }
      setDialogTitle(bus);
      busExplorerLogin.onRelogin((connection, oldLogin) -> { // atualização dinâmica a cada relogin
        // status
        status.setText(Language.get(this.getClass(), "connected.as",
          busExplorerLogin.info.entity, busExplorerLogin.domain, busExplorerLogin.info.id));
        // notificação para os controles
        notifiers.forEach(booleanConsumer -> booleanConsumer.accept(busExplorerLogin.hasAdminRights()));
      });
      status.setText(Language.get(this.getClass(), "connected.as",
        busExplorerLogin.info.entity, busExplorerLogin.domain, busExplorerLogin.info.id));
      status.setEnabled(true);
      disconnect.setEnabled(true);
      notifiers.forEach(booleanConsumer -> booleanConsumer.accept(busExplorerLogin.hasAdminRights()));
    }
  }

  /**
   * Constrói os componentes da janela.
   */
  private void buildDialog() {
    setMinimumSize(new Dimension(980, 700));
    setLocationByPlatform(true);
    setLayout(new BorderLayout(0, 0));
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });

    buildFeaturesComponent();
    buildBottomPanel();
    pack();

    setDialogTitle("");
  }

  /**
   * Constrói a barra de menu da janela.
   */
  private void buildBottomPanel() {
    JPanel panel = new JPanel(new GridBagLayout());

    disconnect = new JButton(Language.get(this.getClass(),"disconnect"));
    disconnect.setEnabled(false);
    disconnect.setIcon(ApplicationIcons.ICON_LOGOUT_16);
    disconnect.setMnemonic(Language.get(this.getClass(),"disconnect.mnemonic").charAt(0));
    disconnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        BusExplorerTask<Void> task =
          new BusExplorerTask<Void>(ExceptionContext.Service) {

          @Override
          protected void doPerformTask() throws Exception {
            Application.login().logout();
            setDialogTitle("");
            status.setText(Language.get(MainDialog.class,"title.disconnected"));
            status.setEnabled(false);
            disconnect.setEnabled(false);
          }

          @Override
          protected void afterTaskUI() {
            Application.showLoginDialog(MainDialog.this);
          }
        };

      if (InputDialog.showConfirmDialog(MainDialog.this,
        Language.get(MainDialog.class, "disconnect.confirm.msg"),
        Language.get(MainDialog.class, "disconnect.confirm.title")) == JOptionPane.YES_OPTION) {
        task.execute(MainDialog.this, Language.get(MainDialog.class,
          "logout.waiting.title"), Language.get(MainDialog.class,
          "logout.waiting.msg"));
      }
    }});

    panel.add(disconnect, new GBC(0,0).insets(5));
    status = new JLabel();
    status.setEnabled(false);
    status.setText(Language.get(MainDialog.class,"title.disconnected"));
    status.setHorizontalAlignment(JTextField.RIGHT);
    panel.add(status, new GBC(1,0).insets(5).east().both());

    add(panel, BorderLayout.SOUTH);
  }

  /**
   * Constrói o painel das funcionalidades.
   */
  private void buildFeaturesComponent() {
    featuresPane = new JTabbedPane(JTabbedPane.TOP);

    featuresPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    HashMap<String, JComponent> conceptsPanels = new LinkedHashMap<>();
    conceptsPanels.put("offer",  initPanelOffer());
    conceptsPanels.put("login",  initPanelLogin());
    conceptsPanels.put("conf",   initPanelConfiguration());
    conceptsPanels.put("editor", initPanelEditor());
    for (String concept : conceptsPanels.keySet()) {
      String tabTitle = Language.get(MainDialog.class, concept + ".title");
      String tabTooltip = Language.get(MainDialog.class, concept + ".tooltip");
      featuresPane.addTab(tabTitle, null,  conceptsPanels.get(concept), tabTooltip);
    }

    featuresPane.addChangeListener(changeEvent -> {
      Component selected = featuresPane.getSelectedComponent();
      if (selected instanceof RefreshDelegate) {
        ((RefreshDelegate) selected).refresh(null);
      }
    });

    notifiers.add(isAdmin -> {
      disableTab(featuresPane, conceptsPanels.get("login"), isAdmin);

      // A ativação da aba de configurações segue uma regra
      // diferenciada para suportar busservices < 2.0.0.9
      disableTab(featuresPane, conceptsPanels.get("conf"), Application.login().admin.isReconfigurationCapable());

      // Seleciona a primeira aba do pane de funcionalidades.
      featuresPane.setSelectedIndex(0);
      // A atualização explícita é necessária porque, como esperado, o
      // ChangeListener da pane só é ativado se a aba corrente for modificada.
      Component component = featuresPane.getSelectedComponent();
      ((RefreshDelegate) component).refresh(null);
    });

    add(featuresPane, BorderLayout.CENTER);
  }

  private static void disableTab(JTabbedPane pane, JComponent component, Boolean enabled) {
    int index = pane.indexOfComponent(component);
    pane.setEnabledAt(index, enabled);
    if (!enabled) {
      pane.setForegroundAt(index, UIManager.getColor(TABBED_PANE_DISABLED_TEXT));
    } else {
      pane.setForegroundAt(index, UIManager.getColor(TABBED_PANE_FOREGROUND));
    }
  }

  private JComponent initPanelEditor() {
    JTabbedPane editorPane = new JTabbedPane(JTabbedPane.LEFT);
    HashMap<String, JComponent> conceptsPanels = new LinkedHashMap<>();
    conceptsPanels.put("category",      initPanelCategory());
    conceptsPanels.put("entity",        initPanelEntity());
    conceptsPanels.put("certificate",   initPanelCertificate());
    conceptsPanels.put("interface",     initPanelInterface());
    conceptsPanels.put("authorization", initPanelAuthorization());
    conceptsPanels.put("integration",   initExtensionEditor());
    conceptsPanels.put("pending",       initPendingChecks());
    for (String concept : conceptsPanels.keySet()) {
      String tabTitle = Language.get(MainDialog.class,concept + ".title");
      String tabTooltip = Language.get(MainDialog.class, concept + ".tooltip");
      editorPane.addTab(tabTitle, null,  conceptsPanels.get(concept), tabTooltip);
    }
    editorPane.addChangeListener(changeEvent -> {
      Component selected = editorPane.getSelectedComponent();
      if (selected instanceof RefreshDelegate) {
        ((RefreshDelegate) selected).refresh(null);
      }
    });
    notifiers.add(isAdmin -> {
      editorPane.setSelectedIndex(0);
      ((RefreshablePanel) editorPane.getSelectedComponent()).refresh(null);
      disableTab(editorPane, conceptsPanels.get("certificate"), isAdmin);
      disableTab(editorPane, conceptsPanels.get("integration"),
        Application.login().extension.isExtensionCapable());
    });
    return editorPane;
  }

  private JComponent initExtensionEditor() {
    JTabbedPane editorPane = new JTabbedPane(JTabbedPane.LEFT);
    HashMap<String, RefreshablePanel> conceptsPanels = new LinkedHashMap<>();
    conceptsPanels.put("extension.overview", initPanelIntegrationOverview());
    conceptsPanels.put("extension.consumer", initPanelIntegrationConsumer());
    conceptsPanels.put("extension.provider", initPanelIntegrationProvider());
    conceptsPanels.put("extension.contract", initPanelIntegrationContract());
    for (String concept : conceptsPanels.keySet()) {
      String tabTitle = Language.get(MainDialog.class, concept + ".title");
      String tabTooltip = Language.get(MainDialog.class, concept + ".tooltip");
      editorPane.addTab(tabTitle, null,  conceptsPanels.get(concept), tabTooltip);
    }
    editorPane.addChangeListener(changeEvent -> {
      Component selected = editorPane.getSelectedComponent();
      if (selected instanceof RefreshDelegate) {
          ((RefreshDelegate) selected).refresh(null);
      }
    });
    notifiers.add(isAdmin -> {
      if (Application.login().extension.isExtensionCapable()) {
        editorPane.setSelectedIndex(0);
        ((RefreshablePanel) editorPane.getSelectedComponent()).refresh(null);
      }
    });

    return editorPane;
  }

  private JComponent initPendingChecks() {
    return new ConsistencyReportPanel(this);
  }

  private RefreshablePanel initPanelIntegrationContract() {
    ObjectTableModel<ContractWrapper> model = new ObjectTableModel<>(new
      ArrayList<>(), new ContractTableProvider());

    List<TablePanelActionInterface<ContractWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<ContractWrapper>>(3);
    actionsVector.add(new ContractRefreshAction(this));
    actionsVector.add(new ContractAddAction(this));
    actionsVector.add(new ContractEditAction(this));
    actionsVector.add(new ContractDeleteAction(this));

    return
      new TablePanelComponent<ContractWrapper>(model, actionsVector, true);
  }

  private RefreshablePanel initPanelIntegrationProvider() {
    ObjectTableModel<ProviderWrapper> model = new ObjectTableModel<>(new
      ArrayList<>(), new ProviderTableProvider());

    List<TablePanelActionInterface<ProviderWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<ProviderWrapper>>(3);
    actionsVector.add(new ProviderRefreshAction(this));
    actionsVector.add(new ProviderAddAction(this));
    actionsVector.add(new ProviderEditAction(this));
    actionsVector.add(new ProviderDeleteAction(this));

    return
      new TablePanelComponent<ProviderWrapper>(model, actionsVector, true);
  }

  private RefreshablePanel initPanelIntegrationConsumer() {
    ObjectTableModel<ConsumerWrapper> model = new ObjectTableModel<>(new
      ArrayList<>(), new ConsumerTableProvider());

    List<TablePanelActionInterface<ConsumerWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<ConsumerWrapper>>(3);
    actionsVector.add(new ConsumerRefreshAction(this));
    actionsVector.add(new ConsumerAddAction(this));
    actionsVector.add(new ConsumerEditAction(this));
    actionsVector.add(new ConsumerDeleteAction(this));

    return
      new TablePanelComponent<ConsumerWrapper>(model, actionsVector, true);
  }

  private RefreshablePanel initPanelIntegrationOverview() {
      ObjectTableModel<IntegrationWrapper> model = new ObjectTableModel<>(new
        ArrayList<>(), new IntegrationTableProvider());

      List<TablePanelActionInterface<IntegrationWrapper>> actionsVector =
        new Vector<TablePanelActionInterface<IntegrationWrapper>>(5);
      actionsVector.add(new IntegrationExportToXLSAction(this));
      actionsVector.add(new IntegrationRefreshAction(this));
      actionsVector.add(new IntegrationAddAction(this));
      actionsVector.add(new IntegrationEditAction(this));
      actionsVector.add(new IntegrationDeleteAction(this));

      return
        new TablePanelComponent<IntegrationWrapper>(model, actionsVector, true);
    }

    /**
   * Inicializa o painel de CRUD de categorias.
   * @return painel contendo dados e ações relativas a categorias
   */
  private RefreshablePanel initPanelCategory() {
    ObjectTableModel<CategoryWrapper> model =
      new ObjectTableModel<>(new LinkedList<>(), new CategoryTableProvider());

    List<TablePanelActionInterface<CategoryWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<CategoryWrapper>>(3);
    actionsVector.add(new CategoryRefreshAction(this));
    actionsVector.add(new CategoryAddAction(this));
    actionsVector.add(new CategoryEditAction(this));
    actionsVector.add(new CategoryDeleteAction(this));

    return
      new TablePanelComponent<CategoryWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de entidades.
   * @return painel contendo os dados e as ações relativas a entidades
   */
  private RefreshablePanel initPanelEntity() {
    ObjectTableModel<EntityWrapper> model = new ObjectTableModel<>(new
      ArrayList<>(), new EntityTableProvider());

    List<TablePanelActionInterface<EntityWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<EntityWrapper>>(3);
    actionsVector.add(new EntityRefreshAction(this));
    actionsVector.add(new EntityAddAction(this));
    actionsVector.add(new EntityEditAction(this));
    actionsVector.add(new EntityDeleteAction(this));

    return
      new TablePanelComponent<EntityWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de certificados.
   * @return panel contendo os dados e as ações relativas a certificados
   */
  private RefreshablePanel initPanelCertificate() {
    ObjectTableModel<CertificateWrapper> model = new ObjectTableModel<> (new
      LinkedList<>(), new CertificateTableProvider());

    List<TablePanelActionInterface<CertificateWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<CertificateWrapper>>(3);
    actionsVector.add(new CertificateRefreshAction(this));
    actionsVector.add(new CertificateAddAction(this));
    actionsVector.add(new CertificateEditAction(this));
    actionsVector.add(new CertificateDeleteAction(this));

    return new TablePanelComponent<CertificateWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de interfaces.
   * @return painel contendo os dados e ações relativas a interfaces
   */
  private RefreshablePanel initPanelInterface() {
    ObjectTableModel<InterfaceWrapper> model = new ObjectTableModel<>(new
      LinkedList<>(), new InterfaceTableProvider());

    List<TablePanelActionInterface<InterfaceWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<InterfaceWrapper>>(3);
    actionsVector.add(new InterfaceRefreshAction(this));
    actionsVector.add(new InterfaceAddAction(this));
    actionsVector.add(new InterfaceDeleteAction(this));

    return
      new TablePanelComponent<InterfaceWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de autorizações.
   * @return painel contendo os dados e ações relativas a autorizações
   */
  private RefreshablePanel initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> model = new ObjectTableModel<>(new
      LinkedList<>(), new AuthorizationTableProvider());

    List<TablePanelActionInterface<AuthorizationWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<AuthorizationWrapper>>(3);
    actionsVector.add(new AuthorizationRefreshAction(this));
    actionsVector.add(new AuthorizationAddAction(this));
    actionsVector.add(new AuthorizationDeleteAction(this));

    return
      new TablePanelComponent<AuthorizationWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de ofertas.
   * @return painel com os dados e ações das ofertas.
   */
  private RefreshablePanel initPanelOffer() {
    ObjectTableModel<OfferWrapper> model = new ObjectTableModel<>(new
      LinkedList<>(), new OfferTableProvider());

    List<TablePanelActionInterface<OfferWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<OfferWrapper>>(2);
    actionsVector.add(new OfferRefreshAction(this));
    actionsVector.add(new OfferDeleteAction(this));
    actionsVector.add(new OfferStatusAction(this));
    final OfferPropertiesAction propertiesAction =
      new OfferPropertiesAction(this);
    actionsVector.add(propertiesAction);

    TablePanelComponent<OfferWrapper> panelOffer =
      new TablePanelComponent<OfferWrapper>(model, actionsVector, true);
    /*
     * Inclui listener de duplo clique para disparar ação de visualizar
     * propriedades da oferta, dado que não temos ação de edição neste painel.
     */
    panelOffer.addTableMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          propertiesAction.actionPerformed(null);
        }
      }
    });

    return panelOffer;
  }

  /**
   * Inicializa o painel CRUD de logins.
   * @return painel com os dados e ações dos logins
   */
  private RefreshablePanel initPanelLogin() {
    ObjectTableModel<LoginWrapper> model = new ObjectTableModel<>(new
      LinkedList<>(), new LoginTableProvider());

    List<TablePanelActionInterface<LoginWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<LoginWrapper>>(2);
    actionsVector.add(new LoginRefreshAction(this));
    actionsVector.add(new LoginDeleteAction(this));

    return
      new TablePanelComponent<LoginWrapper>(model, actionsVector, true);
  }

  /**
   * Inicializa o painel de CRUD de administradores.
   * @return painel com os dados e ações das configurações
   */
  private RefreshablePanel initPanelConfiguration() {
    List<TablePanelActionInterface<AdminWrapper>> adminActionsVector =
      new Vector<TablePanelActionInterface<AdminWrapper>>(4);
    adminActionsVector.add(new AdminRefreshAction(this));
    adminActionsVector.add(new AdminAddAction(this));
    adminActionsVector.add(new AdminEditAction(this));
    adminActionsVector.add(new AdminDeleteAction(this));

    final TablePanelComponent<AdminWrapper> adminsPanel =
      new TablePanelComponent<AdminWrapper>(new ObjectTableModel<AdminWrapper>(new LinkedList<AdminWrapper>(),
              new AdminTableProvider()), adminActionsVector, false);

    List<TablePanelActionInterface<ValidatorWrapper>> validatorActionsVector =
            new Vector<TablePanelActionInterface<ValidatorWrapper>>(2);
    validatorActionsVector.add(new ValidatorRefreshAction(this));
    validatorActionsVector.add(new ValidatorRestartAction(this));
    validatorActionsVector.add(new ValidatorDeleteAction(this));

    final TablePanelComponent<ValidatorWrapper> validatorsPanel =
            new TablePanelComponent<ValidatorWrapper>(new ObjectTableModel<ValidatorWrapper>(new LinkedList<ValidatorWrapper>(),
                    new ValidatorTableProvider()), validatorActionsVector, false);

    BasicSettingsPanel busSettings = BasicSettingsPanel.create(this);
    AuditSettingsPanel auditSettings = AuditSettingsPanel.create(this);

    JPanel restoreDefaultsPanel = new JPanel(new MigLayout("align center, insets 5"));
    final JButton defaultsButton = new JButton(Language.get(MainDialog.class,"conf.restoredefaults"));
    defaultsButton.setIcon(ApplicationIcons.ICON_RESTORE_16);
    defaultsButton.setMnemonic(Language.get(MainDialog.class, "conf.restoredefaults.mnemonic").charAt(0));
    defaultsButton.setToolTipText(Language.get(MainDialog.class, "conf.restoredefaults.tooltip"));

    final JButton refreshButton = new JButton(Language.get(MainDialog.class,"conf.refresh"));
    refreshButton.setMnemonic(Language.get(MainDialog.class, "conf.refresh.mnemonic").charAt(0));
    refreshButton.setToolTipText(Language.get(MainDialog.class, "conf.refresh.tooltip"));
    refreshButton.setIcon(ApplicationIcons.ICON_REFRESH_16);

    restoreDefaultsPanel.add(refreshButton);
    restoreDefaultsPanel.add(defaultsButton);

    final RefreshablePanel customPanel =
      new RefreshablePanel(new MigLayout("flowx, fill, insets 0","[]5[]")) {
        @Override
      public void refresh(ActionEvent event) {
        busSettings.getRetrieveTask().execute(MainDialog.this,
          Language.get(MainDialog.class,"conf.waiting.title"),
          Language.get(MainDialog.class,"conf.waiting.msg"));
        auditSettings.getRetrieveTask().execute(MainDialog.this,
          Language.get(MainDialog.class,"conf.audit.waiting.title"),
          Language.get(MainDialog.class,"conf.audit.waiting.msg"));
        adminsPanel.refresh(event);
        validatorsPanel.refresh(event);
        }
    };

    refreshButton.addActionListener(e -> customPanel.refresh(null));
    defaultsButton.addActionListener(actionEvent -> {
      BusExplorerTask<Void> task =
        new BusExplorerTask<Void>(ExceptionContext.BusCore) {

          @Override
          protected void doPerformTask() throws Exception {
            Application.login().admin.reloadConfigsFile();
          }

          @Override
          protected void afterTaskUI() {
            if (getStatus()) {
              customPanel.refresh(null);
            }
          }
        };

      task.execute(MainDialog.this,
        Language.get(MainDialog.class,"conf.waiting.title"),
        Language.get(MainDialog.class,"conf.waiting.msg"));
    });

    Border loweredBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    busSettings.panel().setBorder(BorderFactory.createTitledBorder(loweredBorder,
      Language.get(MainDialog.class,"conf.settings.label")));
    auditSettings.panel().setBorder(BorderFactory.createTitledBorder(loweredBorder,
      Language.get(MainDialog.class,"conf.audit.label")));
    adminsPanel.setBorder(BorderFactory.createTitledBorder(loweredBorder,
      Language.get(MainDialog.class,"conf.admins.label")));
    validatorsPanel.setBorder(BorderFactory.createTitledBorder(loweredBorder,
      Language.get(MainDialog.class,"conf.validators.label")));

    JPanel leftPanel = new JPanel(new MigLayout("fill, insets 0, flowy"));
    leftPanel.add(busSettings.panel(), "north");
    leftPanel.add(auditSettings.panel(), "grow");

    JPanel rightPanel = new JPanel(new MigLayout("fill, insets 0, flowy"));
    rightPanel.add(adminsPanel, "grow");
    rightPanel.add(validatorsPanel, "growx");
    rightPanel.add(restoreDefaultsPanel, "growx");

    customPanel.add(leftPanel, "grow");
    customPanel.add(rightPanel, "grow");

    // trigger to enable controls if user is admin executed by BusExplorer login completion task
    notifiers.add(isAdmin -> {
      if (Application.login().admin.isReconfigurationCapable()) {
        defaultsButton.setEnabled(isAdmin);
      }
      busSettings.activate(Application.login().admin.isReconfigurationCapable() && isAdmin);
      auditSettings.activate(Application.login().audit.isAuditCapable() && isAdmin);
    });

    return customPanel;
  }

  /**
   * Ajusta o título do diálogo.
   *
   * @param message Mensagem extra ser adicionada no título do diálogo.
   */
  private void setDialogTitle(String message) {
    String title = Language.get(Application.class, "title");
    if (!message.isEmpty()) {
      title += " - " + message;
    }
    setTitle(title);
  }
}
