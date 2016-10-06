package busexplorer.desktop.dialog;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.RefreshDelegate;
import busexplorer.panel.RefreshablePanel;
import busexplorer.ApplicationIcons;
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
import busexplorer.panel.entities.EntityAddAction;
import busexplorer.panel.entities.EntityDeleteAction;
import busexplorer.panel.entities.EntityEditAction;
import busexplorer.panel.entities.EntityRefreshAction;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.entities.EntityWrapper;
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
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import exception.handling.ExceptionContext;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.table.ObjectTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * Diálogo principal da aplicação.
 * 
 * @author Tecgraf
 */
public class MainDialog extends JFrame implements PropertyChangeListener {
  /**
   * Acessa os serviços barramento relacionados à administração.
   */
  private BusAdmin admin;
  /**
   * Pane de recursos de gerência do barramento.
   */
  private JTabbedPane featuresPane;
  /**
   * Botão de desconexão.
   */
  private JButton disconnect;
  /**
   * Propriedades da aplicação.
   */
  private Properties properties;

  /**
   * Construtor.
   * 
   * @param properties proprieades da aplicação.
   * @param admin instância de administração do barramento.
   */
  public MainDialog(Properties properties, BusAdmin admin) {
    this.admin = admin;
    this.properties = properties;
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
    if ("Application.login".equals(propertyName)) {
      disconnect.setEnabled(true);
      setDialogTitle(Application.login().entity + "@" + Application.login().host +
        ":" + Application.login().port);
      updateAdminFeatures(Application.login().hasAdminRights());
    }
  }

  /**
   * Constrói os componentes da janela.
   */
  private void buildDialog() {
    setMinimumSize(new Dimension(800, 600));
    setLocationByPlatform(true);
    setLayout(new BorderLayout(0, 0));
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });

    buildTopPanel();
    buildFeaturesComponent();
    pack();

    setDialogTitle(LNG.get("MainDialog.title.disconnected"));
  }

  /**
   * Constrói a barra de menu da janela.
   */
  private void buildTopPanel() {
    JPanel panel = new JPanel(new GridBagLayout());

    disconnect = new JButton(LNG.get("MainDialog.disconnect"));
    disconnect.setEnabled(false);
    disconnect.setIcon(ApplicationIcons.ICON_LOGOUT_16);
    disconnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        BusExplorerTask<Object> task =
          new BusExplorerTask<Object>(Application.exceptionHandler(),
            ExceptionContext.Service) {
          @Override
          protected void performTask() throws Exception {
            Application.login().logout();
            setDialogTitle(LNG.get("MainDialog.title.disconnected"));
            disconnect.setEnabled(false);
          }

          @Override
          protected void afterTaskUI() {
            Application.loginProcess(MainDialog.this);
          }
        };

        int option =
          JOptionPane.showConfirmDialog(MainDialog.this,
            Utils.getString(MainDialog.class, "disconnect.confirm.msg"),
            Utils.getString(MainDialog.class, "disconnect.confirm.title"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
          task.execute(MainDialog.this,
            Utils.getString(MainDialog.class, "logout.waiting.title"),
            Utils.getString(MainDialog.class, "logout.waiting.msg"));
        }
      }
    });

    panel.add(disconnect, new GBC(0,0).insets(5));
    panel.add(new JLabel(), new GBC(1,0).horizontal());

    add(panel, BorderLayout.SOUTH);
  }

  /**
   * Constrói o painel das funcionalidades.
   */
  private void buildFeaturesComponent() {
    featuresPane = new JTabbedPane(JTabbedPane.TOP);

    featuresPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    // A primeira aba não deve depender de permissões administrativas; vide
    // método updateAdminFeatures().
    String[] featureNames =
      { "category", "entity", "certificate", "interface", "authorization",
          "offer", "login", "conf" };
    for (String featureName : featureNames) {
      featuresPane.addTab(LNG.get("MainDialog." + featureName + ".title"),
        null, null, LNG.get("MainDialog." + featureName + ".toolTip"));
    }
    initFeaturePanels();

    featuresPane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent event) {
        ((RefreshDelegate) featuresPane.getSelectedComponent()).refresh(null);
      }
    });

    add(featuresPane, BorderLayout.CENTER);
  }

  /**
   * Inicializa o painel de CRUD de categorias.
   */
  private void initPanelCategory() {
    ObjectTableModel<CategoryWrapper> model =
      new ObjectTableModel<CategoryWrapper>(new LinkedList<CategoryWrapper>(),
        new CategoryTableProvider());

    List<TablePanelActionInterface<CategoryWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<CategoryWrapper>>(3);
    actionsVector.add(new CategoryRefreshAction(this, admin));
    actionsVector.add(new CategoryAddAction(this, admin));
    actionsVector.add(new CategoryEditAction(this, admin));
    actionsVector.add(new CategoryDeleteAction(this, admin));

    TablePanelComponent<CategoryWrapper> panelCategory =
      new TablePanelComponent<CategoryWrapper>(model, actionsVector, true);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.category.title"));
    featuresPane.setComponentAt(index, panelCategory);
  }

  /**
   * Inicializa o painel de CRUD de entidades.
   */
  private void initPanelEntity() {
    ObjectTableModel<EntityWrapper> model =
      new ObjectTableModel<EntityWrapper>(new ArrayList<EntityWrapper>(),
        new EntityTableProvider());

    List<TablePanelActionInterface<EntityWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<EntityWrapper>>(3);
    actionsVector.add(new EntityRefreshAction(this, admin));
    actionsVector.add(new EntityAddAction(this, admin));
    actionsVector.add(new EntityEditAction(this, admin));
    actionsVector.add(new EntityDeleteAction(this, admin));

    TablePanelComponent<EntityWrapper> panelEntity =
      new TablePanelComponent<EntityWrapper>(model, actionsVector, true);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.entity.title"));
    featuresPane.setComponentAt(index, panelEntity);
  }

  /**
   * Inicializa o painel de CRUD de certificados.
   */
  private void initPanelCertificate() {
    ObjectTableModel<CertificateWrapper> model =
      new ObjectTableModel<CertificateWrapper>(new LinkedList<CertificateWrapper>(),
        new CertificateTableProvider());

    List<TablePanelActionInterface<CertificateWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<CertificateWrapper>>(3);
    actionsVector.add(new CertificateRefreshAction(this, admin));
    actionsVector.add(new CertificateAddAction(this, admin));
    actionsVector.add(new CertificateEditAction(this, admin));
    actionsVector.add(new CertificateDeleteAction(this, admin));

    TablePanelComponent<CertificateWrapper> panelCertificate =
      new TablePanelComponent<CertificateWrapper>(model, actionsVector, true);

    int index =
      featuresPane.indexOfTab(LNG.get("MainDialog.certificate.title"));
    featuresPane.setComponentAt(index, panelCertificate);
  }

  /**
   * Inicializa o painel de CRUD de interfaces.
   */
  private void initPanelInterface() {
    ObjectTableModel<InterfaceWrapper> model =
      new ObjectTableModel<InterfaceWrapper>(new LinkedList<InterfaceWrapper>(),
        new InterfaceTableProvider());

    List<TablePanelActionInterface<InterfaceWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<InterfaceWrapper>>(3);
    actionsVector.add(new InterfaceRefreshAction(this, admin));
    actionsVector.add(new InterfaceAddAction(this, admin));
    actionsVector.add(new InterfaceDeleteAction(this, admin));

    TablePanelComponent<InterfaceWrapper> panelInterface =
      new TablePanelComponent<InterfaceWrapper>(model, actionsVector, true);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.interface.title"));
    featuresPane.setComponentAt(index, panelInterface);
  }

  /**
   * Inicializa o painel de CRUD de autorizações.
   */
  private void initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> model =
      new ObjectTableModel<AuthorizationWrapper>(
        new LinkedList<AuthorizationWrapper>(), new AuthorizationTableProvider());

    List<TablePanelActionInterface<AuthorizationWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<AuthorizationWrapper>>(3);
    actionsVector.add(new AuthorizationRefreshAction(this, admin));
    actionsVector.add(new AuthorizationAddAction(this, admin));
    actionsVector.add(new AuthorizationDeleteAction(this, admin));

    TablePanelComponent<AuthorizationWrapper> panelAuthorization =
      new TablePanelComponent<AuthorizationWrapper>(model, actionsVector, true);

    int index =
      featuresPane.indexOfTab(LNG.get("MainDialog.authorization.title"));
    featuresPane.setComponentAt(index, panelAuthorization);
  }

  /**
   * Inicializa o painel de CRUD de ofertas.
   */
  private void initPanelOffer() {
    ObjectTableModel<OfferWrapper> model =
      new ObjectTableModel<OfferWrapper>(new LinkedList<OfferWrapper>(),
        new OfferTableProvider());

    List<TablePanelActionInterface<OfferWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<OfferWrapper>>(2);
    actionsVector.add(new OfferRefreshAction(this, admin));
    actionsVector.add(new OfferDeleteAction(this, admin));
    final OfferPropertiesAction propertiesAction =
      new OfferPropertiesAction(this, admin);
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
    int index =
      featuresPane.indexOfTab(Utils.getString(this.getClass(), "offer.title"));
    featuresPane.setComponentAt(index, panelOffer);
  }

  /**
   * Inicializa o painel CRUD de logins.
   */
  private void initPanelLogin() {
    ObjectTableModel<LoginWrapper> model =
      new ObjectTableModel<LoginWrapper>(new LinkedList<LoginWrapper>(),
        new LoginTableProvider());

    List<TablePanelActionInterface<LoginWrapper>> actionsVector =
      new Vector<TablePanelActionInterface<LoginWrapper>>(2);
    actionsVector.add(new LoginRefreshAction(this, admin));
    actionsVector.add(new LoginDeleteAction(this, admin));

    TablePanelComponent<LoginWrapper> panelLogin =
      new TablePanelComponent<LoginWrapper>(model, actionsVector, true);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.login.title"));
    featuresPane.setComponentAt(index, panelLogin);
  }

  /**
   * Inicializa o painel de CRUD de administradores.
   */
  private void initPanelConfiguration() {
    List<TablePanelActionInterface<AdminWrapper>> adminActionsVector =
      new Vector<TablePanelActionInterface<AdminWrapper>>(3);
    adminActionsVector.add(new AdminRefreshAction(this, admin));
    adminActionsVector.add(new AdminAddAction(this, admin));
    adminActionsVector.add(new AdminEditAction(this, admin));
    adminActionsVector.add(new AdminDeleteAction(this, admin));

    final TablePanelComponent<AdminWrapper> adminsPanel =
      new TablePanelComponent<AdminWrapper>(new ObjectTableModel<AdminWrapper>(new LinkedList<AdminWrapper>(),
              new AdminTableProvider()), adminActionsVector, false);

    List<TablePanelActionInterface<ValidatorWrapper>> validatorActionsVector =
            new Vector<TablePanelActionInterface<ValidatorWrapper>>(3);
    validatorActionsVector.add(new ValidatorRefreshAction(this, admin));
    validatorActionsVector.add(new ValidatorRestartAction(this, admin));
    validatorActionsVector.add(new ValidatorDeleteAction(this, admin));

    final TablePanelComponent<ValidatorWrapper> validatorsPanel =
            new TablePanelComponent<ValidatorWrapper>(new ObjectTableModel<ValidatorWrapper>(new LinkedList<ValidatorWrapper>(),
                    new ValidatorTableProvider()), validatorActionsVector, false);

    JPanel settingsPanel = new JPanel(new MigLayout("wrap 2","[grow][]", "[][][][]"));
    settingsPanel.add(new JLabel(LNG.get("MainDialog.conf.busloglevel")), "grow");
    final JSpinner busLogLevelSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
    busLogLevelSpinner.setToolTipText(LNG.get("MainDialog.conf.busloglevel.tooltip"));
    settingsPanel.add(busLogLevelSpinner,"grow");

    settingsPanel.add(new JLabel(LNG.get("MainDialog.conf.oilloglevel")),"grow");
    final JSpinner oilLogLevelSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
    oilLogLevelSpinner.setToolTipText(LNG.get("MainDialog.conf.oilloglevel.tooltip"));
    settingsPanel.add(oilLogLevelSpinner,"grow");

    settingsPanel.add(new JLabel(LNG.get("MainDialog.conf.maxchannels")),"grow");
    final JSpinner maxChannelsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
    maxChannelsSpinner.setToolTipText(LNG.get("MainDialog.conf.maxchannels.tooltip"));
    settingsPanel.add(maxChannelsSpinner,"grow");

    final JButton cancelButton = new JButton(LNG.get("MainDialog.conf.cancel"));
    cancelButton.setToolTipText(LNG.get("MainDialog.conf.cancel.tooltip"));
    cancelButton.setIcon(ApplicationIcons.ICON_CANCEL_16);
    cancelButton.setEnabled(false);

    final JButton applyButton = new JButton(LNG.get("MainDialog.conf.apply"));
    applyButton.setIcon(ApplicationIcons.ICON_VALIDATE_16);
    applyButton.setEnabled(false);
    applyButton.setToolTipText(LNG.get("MainDialog.conf.apply.tooltip"));

    settingsPanel.add(cancelButton,"gapleft push");
    settingsPanel.add(applyButton,"gapleft push");

    final BusExplorerTask<Object> getBasicConfFromBusTask =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        int maxChannels = 0;
        int busLogLevel = 0;
        int oilLogLevel = 0;

        @Override
        protected void performTask() throws Exception {
          maxChannels = admin.getMaxChannels();
          busLogLevel = admin.getLogLevel();
          oilLogLevel = admin.getOilLogLevel();
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            busLogLevelSpinner.setValue(busLogLevel);
            oilLogLevelSpinner.setValue(oilLogLevel);
            maxChannelsSpinner.setValue(maxChannels);
            applyButton.setEnabled(false);
            cancelButton.setEnabled(false);
          }
        }
      };
    final BusExplorerTask<Object> sendBasicConfToBusTask =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          admin.setMaxChannels(((SpinnerNumberModel) maxChannelsSpinner.getModel()).getNumber().intValue());
          admin.setLogLevel(((SpinnerNumberModel) busLogLevelSpinner.getModel()).getNumber().shortValue());
          admin.setOilLogLevel(((SpinnerNumberModel) oilLogLevelSpinner.getModel()).getNumber().shortValue());
        }

        @Override
        protected void afterTaskUI() {
          applyButton.setEnabled(false);
          cancelButton.setEnabled(false);
        }
      };
    applyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        sendBasicConfToBusTask.execute(MainDialog.this, LNG.get("MainDialog.conf.apply.waiting.title"),
          LNG.get("MainDialog.conf.apply.waiting.msg"));
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        getBasicConfFromBusTask.execute(MainDialog.this, LNG.get("MainDialog.conf.apply.waiting.title"),
          LNG.get("MainDialog.conf.apply.waiting.msg"));
      }
    });
    ChangeListener activateButtons = new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
        applyButton.setEnabled(true);
        cancelButton.setEnabled(true);
      }
    };
    maxChannelsSpinner.addChangeListener(activateButtons);
    busLogLevelSpinner.addChangeListener(activateButtons);
    oilLogLevelSpinner.addChangeListener(activateButtons);

    final RefreshablePanel customPanel = new RefreshablePanel() {
      @Override
      public void refresh(ActionEvent event) {
      adminsPanel.refresh(event);
      validatorsPanel.refresh(event);
      getBasicConfFromBusTask.execute(MainDialog.this, LNG.get("MainDialog.conf.waiting.title"),
        LNG.get("MainDialog.conf.waiting.msg"));
      }
    };

    JPanel restoreDefaultsPanel = new JPanel(new MigLayout("align center"));
    JButton restoreDefaultsButton = new JButton(LNG.get("MainDialog.conf.restoredefaults.label"));
    restoreDefaultsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        BusExplorerTask<Object> task =
          new BusExplorerTask<Object>(Application.exceptionHandler(),
            ExceptionContext.BusCore) {

            @Override
            protected void performTask() throws Exception {
              admin.reloadConfigsFile();
            }

            @Override
            protected void afterTaskUI() {
              if (getStatus()) {
                customPanel.refresh(null);
              }
            }
          };

        task.execute(MainDialog.this, LNG.get("MainDialog.conf.waiting.title"),
          LNG.get("MainDialog.conf.waiting.msg"));
      }
    });
    restoreDefaultsPanel.add(restoreDefaultsButton);

    Border loweredBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    settingsPanel.setBorder(BorderFactory.createTitledBorder(loweredBorder, LNG.get("MainDialog.conf.settings.label")));
    adminsPanel.setBorder(BorderFactory.createTitledBorder(loweredBorder, LNG.get("MainDialog.conf.admins.label")));
    validatorsPanel.setBorder(BorderFactory.createTitledBorder(loweredBorder, LNG.get("MainDialog.conf.validators.label")));

    customPanel.setLayout(new MigLayout("wrap 2, fill, insets 10","[]10[]","[][grow][]"));
    customPanel.add(settingsPanel, "growx");
    customPanel.add(adminsPanel, "spany 2, grow");
    customPanel.add(validatorsPanel, "grow");
    customPanel.add(restoreDefaultsPanel, "spanx 2, grow");

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.conf.title"));
    featuresPane.setComponentAt(index, customPanel);
  }

  /**
   * Inicializa os painéis das funcionalidades.
   */
  private void initFeaturePanels() {
    initPanelCategory();
    initPanelEntity();
    initPanelCertificate();
    initPanelInterface();
    initPanelAuthorization();
    initPanelLogin();
    initPanelOffer();
    initPanelConfiguration();
  }

  /**
   * Atualiza as funcionalidades administrativas da aplicação.
   * 
   * @param isAdmin indicador se o usuário possui permissão de administração.
   */
  private void updateAdminFeatures(boolean isAdmin) {
    String[] featureNames = { "certificate", "login" };
    for (String featureName : featureNames) {
      int index =
        featuresPane
          .indexOfTab(LNG.get("MainDialog." + featureName + ".title"));
      featuresPane.setEnabledAt(index, isAdmin);
    }

    // A ativação da aba de configurações segue uma regra
    // diferenciada para suportar busservices < 2.0.0.9
    featuresPane.setEnabledAt(featuresPane
            .indexOfTab(LNG.get("MainDialog.conf.title")),
            isAdmin && admin.isReconfigurationCapable());

    // Seleciona a primeira aba do pane de funcionalidades.
    featuresPane.setSelectedIndex(0);
    // A atualização explícita é necessária porque, como esperado, o
    // ChangeListener da pane só é ativado se a aba corrente for modificada.
    Component component = featuresPane.getSelectedComponent();
    ((RefreshDelegate) component).refresh(null);
  }

  /**
   * Ajusta o título do diálogo.
   * 
   * @param title Título do diálogo.
   */
  private void setDialogTitle(String title) {
    setTitle(Utils.getString(Application.class, "title") + " - " +
      title);
  }
}
