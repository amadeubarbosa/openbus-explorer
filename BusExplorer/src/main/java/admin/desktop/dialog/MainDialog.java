package admin.desktop.dialog;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
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
import admin.action.certificates.CertificateAddAction;
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
import admin.gui.RunnableList;
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

    featuresPanel = (JPanel) buildFeaturesComponent();
    mainDialog.add(buildMenuComponent(), new GBC(0, 0).west().filly());
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
  private JComponent buildMenuComponent() {
    RunnableList.Item itemCategory =
      new RunnableList.Item(
        LNG.get("MainDialog.category.button"),
        new CategoryFeatureRunnable(mainDialog, panelCategory.getTable(), admin));
    RunnableList.Item itemEntity =
      new RunnableList.Item(LNG.get("MainDialog.entity.button"),
        new EntityFeatureRunnable(mainDialog, panelEntity.getTable(), admin));
    RunnableList.Item itemInterface =
      new RunnableList.Item(LNG.get("MainDialog.interface.button"),
        new InterfaceFeatureRunnable(mainDialog, panelInterface.getTable(),
          admin));
    RunnableList.Item itemAuthorization =
      new RunnableList.Item(LNG.get("MainDialog.authorization.button"),
        new AuthorizationFeatureRunnable(mainDialog, panelAuthorization
          .getTable(), admin));
    RunnableList.Item itemOffer =
      new RunnableList.Item(LNG.get("MainDialog.offer.button"),
        new OfferFeatureRunnable(mainDialog, panelOffer.getTable(), admin));
    RunnableList.Item itemLogout =
      new RunnableList.Item(LNG.get("MainDialog.logout.button"),
        new LogoutFeatureRunnable(assistant, mainDialog));

    Vector<RunnableList.Item> featuresVector = new Vector<RunnableList.Item>();
    Vector<String> toolTipTextVector = new Vector<String>();

    featuresVector.add(itemCategory);
    toolTipTextVector.add(LNG.get("MainDialog.category.help"));

    featuresVector.add(itemEntity);
    toolTipTextVector.add(LNG.get("MainDialog.entity.help"));

    featuresVector.add(itemInterface);
    toolTipTextVector.add(LNG.get("MainDialog.interface.help"));

    featuresVector.add(itemAuthorization);
    toolTipTextVector.add(LNG.get("MainDialog.authorization.help"));

    featuresVector.add(itemOffer);
    toolTipTextVector.add(LNG.get("MainDialog.offer.help"));

    featuresVector.add(itemLogout);
    toolTipTextVector.add(LNG.get("MainDialog.logout.help"));

    if (isCurrentUserAdmin) {
      RunnableList.Item itemCertificate =
        new RunnableList.Item(LNG.get("MainDialog.certificate.button"),
          new CertificateFeatureRunnable(mainDialog, panelCertificate
            .getTable(), admin));
      RunnableList.Item itemLogin =
        new RunnableList.Item(LNG.get("MainDialog.login.button"),
          new LoginFeatureRunnable(mainDialog, panelLogin.getTable(), admin));

      featuresVector.add(2, itemCertificate);
      toolTipTextVector.add(2, LNG.get("MainDialog.certificate.help"));

      featuresVector.add(6, itemLogin);
      toolTipTextVector.add(6, LNG.get("MainDialog.login.help"));
    }

    RunnableList.Item[] featuresArray =
      new RunnableList.Item[featuresVector.size()];
    RunnableList list = new RunnableList(featuresVector.toArray(featuresArray));

    String[] toolTipTextArray = new String[toolTipTextVector.size()];
    list.setToolTipTextArray(toolTipTextVector.toArray(toolTipTextArray));

    list.setFixedCellHeight(70);
    list.setCellRenderer(new DefaultListCellRenderer() {
      public int getHorizontalAlignment() {
        return DefaultListCellRenderer.CENTER;
      }
    });
    list.setSelectedValue(featuresArray[0], true);

    JScrollPane scrollPane = new JScrollPane(list);
    scrollPane.setMinimumSize(new Dimension(120, list.getFixedCellHeight()
      * featuresArray.length + 6));
    scrollPane.setPreferredSize(new Dimension(120, list.getFixedCellHeight()
      * featuresArray.length + 6));

    return scrollPane;
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
  private JComponent buildFeaturesComponent() {
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
    actionsVector
      .add(new CategoryDeleteAction(mainDialog, panelCategory, admin));

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
        new LinkedList<IdentifierWrapper>(), new CertificateTableProvider());

    panelCertificate = new CRUDPanel<IdentifierWrapper>(m, 0);

    new CertificateRefreshAction(mainDialog, panelCertificate.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new CertificateRefreshAction(mainDialog, panelCertificate
      .getTable(), admin));
    actionsVector.add(new CertificateAddAction(mainDialog, panelCertificate,
      admin));
    actionsVector.add(new CertificateDeleteAction(mainDialog, panelCertificate,
      admin));

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
    actionsVector.add(new InterfaceDeleteAction(mainDialog, panelInterface,
      admin));

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
   * Runnable que exibe o painel de CRUD de categorias e atualiza o conteúdo da
   * tabela de categorias
   */
  private class CategoryFeatureRunnable implements Runnable {

    private CategoryRefreshAction action;

    public CategoryFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new CategoryRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(CATEGORY_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de entidades e atualiza o conteúdo da
   * tabela de entidades
   */
  private class EntityFeatureRunnable implements Runnable {

    private EntityRefreshAction action;

    public EntityFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new EntityRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(ENTITY_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de certificados e atualiza o conteúdo
   * da tabela de certificados
   */
  private class CertificateFeatureRunnable implements Runnable {

    private CertificateRefreshAction action;

    public CertificateFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new CertificateRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(CERTIFICATE_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de autorizações e atualiza o conteúdo
   * da tabela de autorizações
   */
  private class AuthorizationFeatureRunnable implements Runnable {

    private AuthorizationRefreshAction action;

    public AuthorizationFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new AuthorizationRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(AUTHORIZATION_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de interfaces e atualiza o conteúdo da
   * tabela de interfaces
   */
  private class InterfaceFeatureRunnable implements Runnable {

    private InterfaceRefreshAction action;

    public InterfaceFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new InterfaceRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(INTERFACE_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de ofertas e atualiza o conteúdo da
   * tabela de ofertas
   */
  private class OfferFeatureRunnable implements Runnable {

    private OfferRefreshAction action;

    public OfferFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new OfferRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(OFFER_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que exibe o painel de CRUD de logins e atualiza o conteúdo da
   * tabela de logins
   */
  private class LoginFeatureRunnable implements Runnable {

    private LoginRefreshAction action;

    public LoginFeatureRunnable(JFrame parentWindow, JTable table,
      BusAdmin admin) {
      action = new LoginRefreshAction(parentWindow, table, admin);
    }

    public void run() {
      changeCardPanel(LOGIN_PANEL_ID);
      action.actionPerformed(null);
    }
  }

  /**
   * Runnable que realiza o logout do barramento
   */
  private class LogoutFeatureRunnable implements Runnable {

    private LogoutAction action;

    private LogoutFeatureRunnable(Assistant assistant, JFrame parentWindow) {
      action = new LogoutAction(assistant, parentWindow);
    }

    public void run() {
      action.actionPerformed(null);
    }

  }

}
