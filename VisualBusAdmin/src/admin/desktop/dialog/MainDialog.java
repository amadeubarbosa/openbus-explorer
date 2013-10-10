package admin.desktop.dialog;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.assistant.Assistant;
import admin.BusAdmin;
import admin.action.LogoutAction;
import admin.action.authorizations.AuthorizationAddAction;
import admin.action.authorizations.AuthorizationDeleteAction;
import admin.action.authorizations.AuthorizationRefreshAction;
import admin.action.authorizations.AuthorizationTableProvider;
import admin.action.categories.CategoryAddAction;
import admin.action.categories.CategoryDeleteAction;
import admin.action.categories.CategoryRefreshAction;
import admin.action.categories.CategoryTableProvider;
import admin.action.certificates.CertificateDeleteAction;
import admin.action.certificates.CertificateRefreshAction;
import admin.action.certificates.CertificateTableProvider;
import admin.action.entities.EntityAddAction;
import admin.action.entities.EntityDeleteAction;
import admin.action.entities.EntityRefreshAction;
import admin.action.entities.EntityTableProvider;
import admin.action.interfaces.InterfaceAddAction;
import admin.action.interfaces.InterfaceDeleteAction;
import admin.action.interfaces.InterfaceRefreshAction;
import admin.action.interfaces.InterfaceTableProvider;
import admin.action.logins.LoginRefreshAction;
import admin.action.logins.LoginDeleteAction;
import admin.action.logins.LoginTableProvider;
import admin.action.offers.OfferRefreshAction;
import admin.action.offers.OfferDeleteAction;
import admin.action.offers.OffersTableProvider;
import admin.wrapper.AuthorizationWrapper;
import admin.wrapper.EntityCategoryDescWrapper;
import admin.wrapper.IdentifierWrapper;
import admin.wrapper.InterfaceWrapper;
import admin.wrapper.LoginInfoWrapper;
import admin.wrapper.OfferWrapper;
import admin.wrapper.RegisteredEntityDescWrapper;

/**
 * Diálogo principal da aplicação
 * 
 * @author Tecgraf
 */
public class MainDialog {
  /**
   * Acessa os serviços barramento relacionados à administração
   */
  private BusAdmin admin;
  /**
   * Assistente do barramento
   */
  private Assistant assistant;
  /**
   * O diálogo principal
   */
  private JFrame mainDialog;
  /**
   * Informa se o usuário que efetuou login no barramento possui permissão de
   * administração
   */
  private boolean isCurrentUserAdmin;

  /**
   * Painel das funcionalidades de gerência do barramento
   */
  private JPanel featuresPanel;

  /** ID do painel de categorias */
  private final String CATEGORY_PANEL_ID = "Category";
  /** ID do painel de entidades */
  private final String ENTITY_PANEL_ID = "Entity";
  /** ID do painel de certificados */
  private final String CERTIFICATE_PANEL_ID = "Certificate";
  /** ID do painel de interfaces */
  private final String INTERFACE_PANEL_ID = "Interface";
  /** ID do painel de autorizações */
  private final String AUTHORIZATION_PANEL_ID = "Authorization";
  /** ID do painel de ofertas */
  private final String OFFER_PANEL_ID = "Offer";
  /** ID do painel de logins */
  private final String LOGIN_PANEL_ID = "Login";

  /** Painel de gerência das categorias no barramento */
  private CRUDPanel<EntityCategoryDescWrapper> panelCategory;
  /** Painel de gerência das entidades registradas no barramento */
  private CRUDPanel<RegisteredEntityDescWrapper> panelEntity;
  /** Painel de gerência dos certificados registrados no barramento */
  private CRUDPanel<IdentifierWrapper> panelCertificate;
  /** Painel de gerência das interfaces registradas no barramento */
  private CRUDPanel<InterfaceWrapper> panelInterface;
  /** Painel de gerência das autorizações concedidas no barramento */
  private CRUDPanel<AuthorizationWrapper> panelAuthorization;
  /** Painel de gerência das ofertas registradas no barramento */
  private CRUDPanel<OfferWrapper> panelOffer;
  /** Painel de gerência dos logins ativos no barramento */
  private CRUDPanel<LoginInfoWrapper> panelLogin;

  /**
   * Construtor
   * 
   * @param admin acessa os serviços de administração do barramento
   * @param assistant assistente do barramento
   * @param isCurrentUserAdmin informa se o usuário logado possui permissão de
   *        administração
   */
  public MainDialog(BusAdmin admin, Assistant assistant,
    boolean isCurrentUserAdmin) {
    this.assistant = assistant;
    this.admin = admin;
    this.isCurrentUserAdmin = isCurrentUserAdmin;
    buildDialog();

  }

  /**
   * Constrói os componentes da janela
   */
  private void buildDialog() {
    mainDialog = new JFrame(getDialogTitle());

    mainDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        shutdownMainDialog();
      }

    });

    mainDialog.setLayout(new GridBagLayout());

    featuresPanel = buildFeaturesPanel();
    mainDialog.add(buildMenuPanel(), new GBC(0, 0).northwest());
    mainDialog.add(featuresPanel, new GBC(1, 0).both());
    mainDialog.pack();

    GUIUtils.centerOnScreen(mainDialog);

  }

  /**
   * Exibe a janela
   */
  public void show() {
    mainDialog.setVisible(true);
  }

  /**
   * Constrói o painel de menu das funcionalidades
   * 
   * @return o painel contendo o menu
   */
  private JPanel buildMenuPanel() {
    JPanel panel = new JPanel(new GridBagLayout());

    ButtonGroup buttons = new ButtonGroup();

    JToggleButton btnCategory =
      new JToggleButton(LNG.get("MainDialog.category.button"));
    JToggleButton btnEntity =
      new JToggleButton(LNG.get("MainDialog.entity.button"));
    JToggleButton btnInterface =
      new JToggleButton(LNG.get("MainDialog.interface.button"));
    JToggleButton btnAutorization =
      new JToggleButton(LNG.get("MainDialog.authorization.button"));
    JToggleButton btnOffer =
      new JToggleButton(LNG.get("MainDialog.offer.button"));
    JToggleButton btnLogout =
      new JToggleButton(LNG.get("MainDialog.logout.button"));

    btnCategory.setPreferredSize(new Dimension(130, 80));
    btnEntity.setPreferredSize(new Dimension(130, 80));
    btnInterface.setPreferredSize(new Dimension(130, 80));
    btnAutorization.setPreferredSize(new Dimension(130, 80));
    btnOffer.setPreferredSize(new Dimension(130, 80));
    btnLogout.setPreferredSize(new Dimension(130, 80));

    btnCategory.setToolTipText(LNG.get("MainDialog.category.help"));
    btnEntity.setToolTipText(LNG.get("MainDialog.entity.help"));
    btnInterface.setToolTipText(LNG.get("MainDialog.interface.help"));
    btnAutorization.setToolTipText(LNG.get("MainDialog.authorization.help"));
    btnOffer.setToolTipText(LNG.get("MainDialog.offer.help"));
    btnLogout.setToolTipText(LNG.get("MainDialog.logout.help"));

    btnCategory.addActionListener(new CategoryFeatureAction(mainDialog,
      panelCategory.getTable(), admin));
    btnEntity.addActionListener(new EntityFeatureAction(mainDialog, panelEntity
      .getTable(), admin));
    btnInterface.addActionListener(new InterfaceFeatureAction(mainDialog,
      panelInterface.getTable(), admin));
    btnOffer.addActionListener(new OfferFeatureAction(mainDialog, panelOffer
      .getTable(), admin));
    btnAutorization.addActionListener(new AuthorizationFeatureAction(
      mainDialog, panelAuthorization.getTable(), admin));
    btnLogout.addActionListener(new LogoutAction(assistant, mainDialog));

    buttons.add(btnCategory);
    btnCategory.setSelected(true);
    buttons.add(btnEntity);
    buttons.add(btnInterface);
    buttons.add(btnAutorization);
    buttons.add(btnOffer);
    buttons.add(btnLogout);

    panel.add(btnCategory, new GBC(0, 0));
    panel.add(btnEntity, new GBC(0, 1));
    panel.add(btnInterface, new GBC(0, 3));
    panel.add(btnAutorization, new GBC(0, 4));
    panel.add(btnOffer, new GBC(0, 5));
    panel.add(btnLogout, new GBC(0, 7));

    if (isCurrentUserAdmin) {
      JToggleButton btnCertificate =
        new JToggleButton(LNG.get("MainDialog.certificate.button"));
      JToggleButton btnLogin =
        new JToggleButton(LNG.get("MainDialog.login.button"));

      btnCertificate.setPreferredSize(new Dimension(130, 80));
      btnLogin.setPreferredSize(new Dimension(130, 80));

      btnCertificate.addActionListener(new CertificateFeatureAction(mainDialog,
        panelCertificate.getTable(), admin));
      btnLogin.addActionListener(new LoginFeatureAction(mainDialog, panelLogin
        .getTable(), admin));

      btnCertificate.setToolTipText(LNG.get("MainDialog.certificate.help"));
      btnLogin.setToolTipText(LNG.get("MainDialog.login.help"));

      buttons.add(btnCertificate);
      buttons.add(btnLogin);

      panel.add(btnCertificate, new GBC(0, 2));
      panel.add(btnLogin, new GBC(0, 6));
    }

    return panel;
  }

  /** Inicializa os painéis das funcionalidades */
  private void initFeaturePanels() {
    initPanelCategory();
    initPanelEntity();
    initPanelInterface();
    initPanelAuthorization();
    initPanelOffer();

    if (isCurrentUserAdmin) {
      initPanelCertificate();
      initPanelLogin();
    }
  }

  /**
   * Constrói o painel das funcionalidades
   * 
   * @return painel contendo as tabelas e botões
   */
  private JPanel buildFeaturesPanel() {
    JPanel panel = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

    initFeaturePanels();

    featuresPanel = new JPanel(new CardLayout());

    featuresPanel.add(panelCategory, CATEGORY_PANEL_ID);
    featuresPanel.add(panelEntity, ENTITY_PANEL_ID);
    featuresPanel.add(panelInterface, INTERFACE_PANEL_ID);
    featuresPanel.add(panelAuthorization, AUTHORIZATION_PANEL_ID);
    featuresPanel.add(panelOffer, OFFER_PANEL_ID);

    if (isCurrentUserAdmin) {
      featuresPanel.add(panelCertificate, CERTIFICATE_PANEL_ID);
      featuresPanel.add(panelLogin, LOGIN_PANEL_ID);
    }

    return featuresPanel;
  }

  /** Inicializa o painel de CRUD de categorias */
  private void initPanelCategory() {
    ObjectTableModel<EntityCategoryDescWrapper> m =
      new ModifiableObjectTableModel<EntityCategoryDescWrapper>(
        new LinkedList<EntityCategoryDescWrapper>(),
        new CategoryTableProvider());

    panelCategory = new CRUDPanel<EntityCategoryDescWrapper>(m, 0);

    new CategoryRefreshAction(mainDialog, panelCategory.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new CategoryRefreshAction(mainDialog, panelCategory
      .getTable(), admin));
    actionsVector.add(new CategoryAddAction(mainDialog, panelCategory, admin));
    actionsVector.add(new CategoryDeleteAction(mainDialog, panelCategory, admin));

    panelCategory.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de entidades */
  private void initPanelEntity() {
    ObjectTableModel<RegisteredEntityDescWrapper> m =
      new ModifiableObjectTableModel<RegisteredEntityDescWrapper>(
        new LinkedList<RegisteredEntityDescWrapper>(),
        new EntityTableProvider());

    panelEntity = new CRUDPanel<RegisteredEntityDescWrapper>(m, 0);

    new EntityRefreshAction(mainDialog, panelEntity.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new EntityRefreshAction(mainDialog, panelEntity
      .getTable(), admin));
    actionsVector.add(new EntityAddAction(mainDialog, panelEntity, admin));
    actionsVector.add(new EntityDeleteAction(mainDialog, panelEntity, admin));

    panelEntity.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de certificados */
  private void initPanelCertificate() {
    ObjectTableModel<IdentifierWrapper> m =
      new ModifiableObjectTableModel<IdentifierWrapper>(
        new LinkedList<IdentifierWrapper>(),
        new CertificateTableProvider());

    panelCertificate = new CRUDPanel<IdentifierWrapper>(m, 0);

    new CertificateRefreshAction(mainDialog, panelCertificate.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new CertificateRefreshAction(mainDialog, panelCertificate
      .getTable(), admin));
    actionsVector.add(new CertificateDeleteAction(mainDialog, panelCertificate, admin));

    panelCertificate.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de interfaces */
  private void initPanelInterface() {

    ObjectTableModel<InterfaceWrapper> m =
      new ModifiableObjectTableModel<InterfaceWrapper>(
        new LinkedList<InterfaceWrapper>(), new InterfaceTableProvider());

    panelInterface = new CRUDPanel<InterfaceWrapper>(m, 0);

    new InterfaceRefreshAction(mainDialog, panelInterface.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new InterfaceRefreshAction(mainDialog, panelInterface
      .getTable(), admin));
    actionsVector
      .add(new InterfaceAddAction(mainDialog, panelInterface, admin));
    actionsVector
      .add(new InterfaceDeleteAction(mainDialog, panelInterface, admin));

    panelInterface.setButtonsPane(actionsVector);

  }

  /** Inicializa o painel de CRUD de autorizações */
  private void initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> m =
      new ModifiableObjectTableModel<AuthorizationWrapper>(
        new LinkedList<AuthorizationWrapper>(),
        new AuthorizationTableProvider());

    panelAuthorization = new CRUDPanel<AuthorizationWrapper>(m, 0);

    new AuthorizationRefreshAction(mainDialog, panelAuthorization.getTable(),
      admin).actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new AuthorizationRefreshAction(mainDialog,
      panelAuthorization.getTable(), admin));
    actionsVector.add(new AuthorizationAddAction(mainDialog,
      panelAuthorization, admin));
    actionsVector.add(new AuthorizationDeleteAction(mainDialog,
      panelAuthorization, admin));

    panelAuthorization.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de ofertas */
  private void initPanelOffer() {
    ObjectTableModel<OfferWrapper> m =
      new ModifiableObjectTableModel<OfferWrapper>(
        new LinkedList<OfferWrapper>(), new OffersTableProvider());

    panelOffer = new CRUDPanel<OfferWrapper>(m, 0);

    new OfferRefreshAction(mainDialog, panelOffer.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new OfferRefreshAction(mainDialog, panelOffer.getTable(),
      admin));
    actionsVector.add(new OfferDeleteAction(mainDialog, panelOffer, admin));

    panelOffer.setButtonsPane(actionsVector);

  }

  /** Inicializa o painel CRUD de logins */
  private void initPanelLogin() {
    ObjectTableModel<LoginInfoWrapper> m =
      new ModifiableObjectTableModel<LoginInfoWrapper>(
        new LinkedList<LoginInfoWrapper>(), new LoginTableProvider());

    panelLogin = new CRUDPanel<LoginInfoWrapper>(m, 0);

    new LoginRefreshAction(mainDialog, panelLogin.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new LoginRefreshAction(mainDialog, panelLogin.getTable(),
      admin));
    actionsVector.add(new LoginDeleteAction(mainDialog, panelLogin, admin));

    panelLogin.setButtonsPane(actionsVector);

  }

  /** Altera o painel exibido */
  private void changeCardPanel(String feature) {
    CardLayout cl = (CardLayout) (featuresPanel.getLayout());
    cl.show(featuresPanel, feature);
  }

  /**
   * Faz shutdown do diálogo principal.
   */
  private void shutdownMainDialog() {
    mainDialog.dispose();
    try {
      assistant.shutdown();
    }
    catch (Exception e) {
    }
  }

  /**
   * @return Título do diálogo
   */
  private String getDialogTitle() {
    return LNG.get("MainDialog.title") + " - " + "BusAdmin";
  }

  /**
   * Ação que exibe o painel de CRUD de categorias e atualiza o conteúdo da
   * tabela de categorias
   */
  private class CategoryFeatureAction extends CategoryRefreshAction {

    public CategoryFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(CATEGORY_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de entidades e atualiza o conteúdo da
   * tabela de entidades
   */
  private class EntityFeatureAction extends EntityRefreshAction {

    public EntityFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(ENTITY_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de certificados e atualiza o conteúdo da
   * tabela de certificados
   */
  private class CertificateFeatureAction extends CertificateRefreshAction {

    public CertificateFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(CERTIFICATE_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de autorizações e atualiza o conteúdo da
   * tabela de autorizações
   */
  private class AuthorizationFeatureAction extends AuthorizationRefreshAction {

    public AuthorizationFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(AUTHORIZATION_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de interfaces e atualiza o conteúdo da
   * tabela de interfaces
   */
  private class InterfaceFeatureAction extends InterfaceRefreshAction {

    public InterfaceFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(INTERFACE_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de ofertas e atualiza o conteúdo da tabela
   * de ofertas
   */
  private class OfferFeatureAction extends OfferRefreshAction {

    public OfferFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(OFFER_PANEL_ID);
      super.actionPerformed(event);
    }
  }

  /**
   * Ação que exibe o painel de CRUD de logins e atualiza o conteúdo da tabela
   * de logins
   */
  private class LoginFeatureAction extends LoginRefreshAction {

    public LoginFeatureAction(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      super(parentWindow, table, admin);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      changeCardPanel(LOGIN_PANEL_ID);
      super.actionPerformed(event);
    }
  }

}
