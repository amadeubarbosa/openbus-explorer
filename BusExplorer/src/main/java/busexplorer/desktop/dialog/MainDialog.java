package busexplorer.desktop.dialog;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import test.PanelActionInterface;
import test.PanelComponent;
import admin.BusAdmin;
import busexplorer.action.LogoutAction;
import busexplorer.action.authorizations.AuthorizationAddAction;
import busexplorer.action.authorizations.AuthorizationDeleteAction;
import busexplorer.action.authorizations.AuthorizationRefreshAction;
import busexplorer.action.authorizations.AuthorizationTableProvider;
import busexplorer.action.categories.CategoryAddAction;
import busexplorer.action.categories.CategoryDeleteAction;
import busexplorer.action.categories.CategoryRefreshAction;
import busexplorer.action.categories.CategoryTableProvider;
import busexplorer.action.certificates.CertificateAddAction;
import busexplorer.action.certificates.CertificateDeleteAction;
import busexplorer.action.certificates.CertificateRefreshAction;
import busexplorer.action.certificates.CertificateTableProvider;
import busexplorer.action.entities.EntityAddAction;
import busexplorer.action.entities.EntityDeleteAction;
import busexplorer.action.entities.EntityRefreshAction;
import busexplorer.action.entities.EntityTableProvider;
import busexplorer.action.interfaces.InterfaceAddAction;
import busexplorer.action.interfaces.InterfaceDeleteAction;
import busexplorer.action.interfaces.InterfaceRefreshAction;
import busexplorer.action.interfaces.InterfaceTableProvider;
import busexplorer.action.logins.LoginDeleteAction;
import busexplorer.action.logins.LoginRefreshAction;
import busexplorer.action.logins.LoginTableProvider;
import busexplorer.action.offers.OfferDeleteAction;
import busexplorer.action.offers.OfferRefreshAction;
import busexplorer.action.offers.OffersTableProvider;
import busexplorer.gui.RunnableList;
import busexplorer.wrapper.AuthorizationWrapper;
import busexplorer.wrapper.EntityCategoryDescWrapper;
import busexplorer.wrapper.EntityInfo;
import busexplorer.wrapper.IdentifierWrapper;
import busexplorer.wrapper.InterfaceWrapper;
import busexplorer.wrapper.LoginInfoWrapper;
import busexplorer.wrapper.OfferWrapper;

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
   * A janela principal da aplicação
   */
  private JFrame desktop;
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
  private PanelComponent<EntityInfo> panelEntity;
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
    desktop = new JFrame(getDialogTitle());

    desktop.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        shutdownMainDialog();
      }

    });

    desktop.setLayout(new GridBagLayout());

    featuresPanel = (JPanel) buildFeaturesComponent();
    desktop.add(buildMenuComponent(), new GBC(0, 0).west().filly());
    desktop.add(featuresPanel, new GBC(1, 0).both());
    desktop.pack();

    GUIUtils.centerOnScreen(desktop);

  }

  /**
   * Exibe a janela
   */
  public void show() {
    desktop.setVisible(true);
  }

  /**
   * Constrói o painel de menu das funcionalidades
   * 
   * @return o painel contendo o menu
   */
  private JComponent buildMenuComponent() {
    RunnableList.Item itemCategory =
      new RunnableList.Item(LNG.get("MainDialog.category.button"),
        new CategoryFeatureRunnable(desktop, panelCategory.getTable(), admin));
    RunnableList.Item itemEntity =
      new RunnableList.Item(LNG.get("MainDialog.entity.button"),
        new EntityFeatureRunnable(desktop, panelEntity, admin));
    RunnableList.Item itemInterface =
      new RunnableList.Item(LNG.get("MainDialog.interface.button"),
        new InterfaceFeatureRunnable(desktop, panelInterface.getTable(), admin));
    RunnableList.Item itemAuthorization =
      new RunnableList.Item(LNG.get("MainDialog.authorization.button"),
        new AuthorizationFeatureRunnable(desktop,
          panelAuthorization.getTable(), admin));
    RunnableList.Item itemOffer =
      new RunnableList.Item(LNG.get("MainDialog.offer.button"),
        new OfferFeatureRunnable(desktop, panelOffer.getTable(), admin));
    RunnableList.Item itemLogout =
      new RunnableList.Item(LNG.get("MainDialog.logout.button"),
        new LogoutFeatureRunnable(assistant, desktop));

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
          new CertificateFeatureRunnable(desktop, panelCertificate.getTable(),
            admin));
      RunnableList.Item itemLogin =
        new RunnableList.Item(LNG.get("MainDialog.login.button"),
          new LoginFeatureRunnable(desktop, panelLogin.getTable(), admin));

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
      @Override
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

    new CategoryRefreshAction(desktop, panelCategory.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new CategoryRefreshAction(desktop, panelCategory
      .getTable(), admin));
    actionsVector.add(new CategoryAddAction(desktop, panelCategory, admin));
    actionsVector.add(new CategoryDeleteAction(desktop, panelCategory, admin));

    panelCategory.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de entidades */
  private void initPanelEntity() {
    ObjectTableModel<EntityInfo> model =
      new ObjectTableModel<EntityInfo>(new ArrayList<EntityInfo>(),
        new EntityTableProvider());
    List<PanelActionInterface<EntityInfo>> actionsVector =
      new Vector<PanelActionInterface<EntityInfo>>();
    actionsVector.add(new EntityRefreshAction(desktop, admin));
    actionsVector.add(new EntityAddAction(desktop, admin));
    actionsVector.add(new EntityDeleteAction(desktop, admin));

    panelEntity = new PanelComponent<EntityInfo>(model, actionsVector);
  }

  /** Inicializa o painel de CRUD de certificados */
  private void initPanelCertificate() {
    ObjectTableModel<IdentifierWrapper> m =
      new ModifiableObjectTableModel<IdentifierWrapper>(
        new LinkedList<IdentifierWrapper>(), new CertificateTableProvider());

    panelCertificate = new CRUDPanel<IdentifierWrapper>(m, 0);

    new CertificateRefreshAction(desktop, panelCertificate.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new CertificateRefreshAction(desktop, panelCertificate
      .getTable(), admin));
    actionsVector
      .add(new CertificateAddAction(desktop, panelCertificate, admin));
    actionsVector.add(new CertificateDeleteAction(desktop, panelCertificate,
      admin));

    panelCertificate.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de interfaces */
  private void initPanelInterface() {

    ObjectTableModel<InterfaceWrapper> m =
      new ModifiableObjectTableModel<InterfaceWrapper>(
        new LinkedList<InterfaceWrapper>(), new InterfaceTableProvider());

    panelInterface = new CRUDPanel<InterfaceWrapper>(m, 0);

    new InterfaceRefreshAction(desktop, panelInterface.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new InterfaceRefreshAction(desktop, panelInterface
      .getTable(), admin));
    actionsVector.add(new InterfaceAddAction(desktop, panelInterface, admin));
    actionsVector
      .add(new InterfaceDeleteAction(desktop, panelInterface, admin));

    panelInterface.setButtonsPane(actionsVector);

  }

  /** Inicializa o painel de CRUD de autorizações */
  private void initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> m =
      new ModifiableObjectTableModel<AuthorizationWrapper>(
        new LinkedList<AuthorizationWrapper>(),
        new AuthorizationTableProvider());

    panelAuthorization = new CRUDPanel<AuthorizationWrapper>(m, 0);

    new AuthorizationRefreshAction(desktop, panelAuthorization.getTable(),
      admin).actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new AuthorizationRefreshAction(desktop,
      panelAuthorization.getTable(), admin));
    actionsVector.add(new AuthorizationAddAction(desktop, panelAuthorization,
      admin));
    actionsVector.add(new AuthorizationDeleteAction(desktop,
      panelAuthorization, admin));

    panelAuthorization.setButtonsPane(actionsVector);
  }

  /** Inicializa o painel de CRUD de ofertas */
  private void initPanelOffer() {
    ObjectTableModel<OfferWrapper> m =
      new ModifiableObjectTableModel<OfferWrapper>(
        new LinkedList<OfferWrapper>(), new OffersTableProvider());

    panelOffer = new CRUDPanel<OfferWrapper>(m, 0);

    new OfferRefreshAction(desktop, panelOffer.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new OfferRefreshAction(desktop, panelOffer.getTable(),
      admin));
    actionsVector.add(new OfferDeleteAction(desktop, panelOffer, admin));

    panelOffer.setButtonsPane(actionsVector);

  }

  /** Inicializa o painel CRUD de logins */
  private void initPanelLogin() {
    ObjectTableModel<LoginInfoWrapper> m =
      new ModifiableObjectTableModel<LoginInfoWrapper>(
        new LinkedList<LoginInfoWrapper>(), new LoginTableProvider());

    panelLogin = new CRUDPanel<LoginInfoWrapper>(m, 0);

    new LoginRefreshAction(desktop, panelLogin.getTable(), admin)
      .actionPerformed(null);

    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>();
    actionsVector.add(new LoginRefreshAction(desktop, panelLogin.getTable(),
      admin));
    actionsVector.add(new LoginDeleteAction(desktop, panelLogin, admin));

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
    desktop.dispose();
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
    return LNG.get("MainDialog.title") + " - " + LNG.get("Application.title");
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

    @Override
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

    PanelComponent<EntityInfo> panel;

    public EntityFeatureRunnable(JFrame parentWindow,
      PanelComponent<EntityInfo> panelEntity, BusAdmin admin) {
      this.panel = panelEntity;
    }

    @Override
    public void run() {
      changeCardPanel(ENTITY_PANEL_ID);
      this.panel.refresh(null);
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void run() {
      action.actionPerformed(null);
    }

  }

}
