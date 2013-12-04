package busexplorer.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import admin.BusAdmin;
import admin.BusAdminImpl;
import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import reuse.modified.planref.client.util.crud.ModifiableObjectTableModel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.assistant.Assistant;
import test.PanelActionInterface;
import test.PanelComponent;
import admin.BusAdmin;
import admin.BusAdminImpl;
// TODO Implementar logout. --tmartins
// import busexplorer.action.LogoutAction;
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
import busexplorer.wrapper.AuthorizationWrapper;
import busexplorer.wrapper.EntityCategoryDescWrapper;
import busexplorer.wrapper.EntityInfo;
import busexplorer.wrapper.IdentifierWrapper;
import busexplorer.wrapper.InterfaceWrapper;
import busexplorer.wrapper.LoginInfoWrapper;
import busexplorer.wrapper.OfferWrapper;

/**
 * Di�logo principal da aplica��o
 * 
 * @author Tecgraf
 */
public class MainDialog {
  /**
   * Assistente do barramento
   */
  private Assistant assistant = null;
  /**
   * Acessa os servi�os barramento relacionados � administra��o
   */
  private BusAdmin admin = null;
  /**
   * Informa se o usu�rio que efetuou login no barramento possui permiss�o de
   * administra��o
   */
  private boolean isCurrentUserAdmin;

  /** A janela principal da aplica��o */
  private JFrame mainDialog;
  /** Pane de recursos de ger�ncia do barramento */
  private JTabbedPane featuresPane;
  /** Painel de ger�ncia das categorias no barramento */
  private CRUDPanel<EntityCategoryDescWrapper> panelCategory;
  /** Painel de ger�ncia das entidades registradas no barramento */
  private PanelComponent<EntityInfo> panelEntity;
  /** Painel de ger�ncia dos certificados registrados no barramento */
  private CRUDPanel<IdentifierWrapper> panelCertificate;
  /** Painel de ger�ncia das interfaces registradas no barramento */
  private CRUDPanel<InterfaceWrapper> panelInterface;
  /** Painel de ger�ncia das autoriza��es concedidas no barramento */
  private CRUDPanel<AuthorizationWrapper> panelAuthorization;
  /** Painel de ger�ncia das ofertas registradas no barramento */
  private CRUDPanel<OfferWrapper> panelOffer;
  /** Painel de ger�ncia dos logins ativos no barramento */
  private CRUDPanel<LoginInfoWrapper> panelLogin;

  /**
   * Construtor
   */
  public MainDialog() {
    buildDialog();
  }

  /**
   * Constr�i os componentes da janela
   */
  private void buildDialog() {
    mainDialog = new JFrame(getDialogTitle());
    mainDialog.setSize(640, 480);
    mainDialog.setLocationByPlatform(true);
    mainDialog.setLayout(new BorderLayout(0, 0));
    mainDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        shutdownMainDialog();
      }
    });

    buildFeaturesComponent();
  }

  /**
   * Exibe a janela
   */
  public void show() {
    mainDialog.setVisible(true);

    if (assistant == null) {
      login();
    }
  }

  /**
   * Executa as a��es de login e, em caso de sucesso, atualiza membros dependentes das novas informa��es de login.
   */
  public void login() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        LoginDialog loginDialog = new LoginDialog(mainDialog);
        loginDialog.show();

        // TODO Os dados de login devem ficar na classe LoginDialog? Talvez
        // fosse melhor ter uma refer�ncia diferente que contivesse as
        // informa��es de login (assistente, host e porta). --tmartins
        assistant = loginDialog.getAssistant();
        admin = new BusAdminImpl(loginDialog.getHost(), loginDialog.getPort(),
          assistant.orb());
        updateAccessToAdminFeatures();
        updateFeatureActions();
      }
    });
  }

  /**
   * Constr�i o painel das funcionalidades
   */
  private void buildFeaturesComponent() {
    featuresPane = new JTabbedPane(JTabbedPane.TOP);

    featuresPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    String[] featureNames = { "category", "entity", "certificate", "interface",
      "authorization", "offer", "login" };
    for (String featureName : featureNames) {
      featuresPane.addTab(LNG.get("MainDialog." + featureName + ".title"), null,
        null, LNG.get("MainDialog." + featureName + ".toolTip"));
    }
    initFeaturePanels();
    updateAccessToAdminFeatures();

    mainDialog.add(featuresPane, BorderLayout.CENTER);
  }

  /**
   * Inicializa o painel de CRUD de categorias.
   */
  private void initPanelCategory() {
    ObjectTableModel<EntityCategoryDescWrapper> m =
      new ModifiableObjectTableModel<EntityCategoryDescWrapper>(
        new LinkedList<EntityCategoryDescWrapper>(),
        new CategoryTableProvider());

    panelCategory = new CRUDPanel<EntityCategoryDescWrapper>(m, 0);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.category.title"));
    featuresPane.setComponentAt(index, panelCategory);
  }

  /**
   * Inicializa o painel de CRUD de entidades.
   */
  private void initPanelEntity() {
    // TODO Separar inicializa��o e atualiza��o das a��es. --tmartins
    ObjectTableModel<EntityInfo> model =
      new ObjectTableModel<EntityInfo>(new ArrayList<EntityInfo>(),
        new EntityTableProvider());

    List<PanelActionInterface<EntityInfo>> actionsVector =
      new Vector<PanelActionInterface<EntityInfo>>();
    actionsVector.add(new EntityRefreshAction(mainDialog, admin));
    actionsVector.add(new EntityAddAction(mainDialog, admin));
    actionsVector.add(new EntityDeleteAction(mainDialog, admin));

    panelEntity = new PanelComponent<EntityInfo>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.entity.title"));
    featuresPane.setComponentAt(index, panelEntity);
  }

  /** 
   * Inicializa o painel de CRUD de certificados.
   */
  private void initPanelCertificate() {
    ObjectTableModel<IdentifierWrapper> m =
      new ModifiableObjectTableModel<IdentifierWrapper>(
        new LinkedList<IdentifierWrapper>(), new CertificateTableProvider());

    panelCertificate = new CRUDPanel<IdentifierWrapper>(m, 0);

    int index =
      featuresPane.indexOfTab(LNG.get("MainDialog.certificate.title"));
    featuresPane.setComponentAt(index, panelCertificate);
  }

  /**
   * Inicializa o painel de CRUD de interfaces.
   */
  private void initPanelInterface() {
    ObjectTableModel<InterfaceWrapper> m =
      new ModifiableObjectTableModel<InterfaceWrapper>(
        new LinkedList<InterfaceWrapper>(), new InterfaceTableProvider());

    panelInterface = new CRUDPanel<InterfaceWrapper>(m, 0);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.interface.title"));
    featuresPane.setComponentAt(index, panelInterface);
  }

  /**
   * Inicializa o painel de CRUD de autoriza��es.
   */
  private void initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> m =
      new ModifiableObjectTableModel<AuthorizationWrapper>(
        new LinkedList<AuthorizationWrapper>(),
        new AuthorizationTableProvider());

    panelAuthorization = new CRUDPanel<AuthorizationWrapper>(m, 0);

    int index =
      featuresPane.indexOfTab(LNG.get("MainDialog.authorization.title"));
    featuresPane.setComponentAt(index, panelAuthorization);
  }

  /**
   * Inicializa o painel de CRUD de ofertas.
   */
  private void initPanelOffer() {
    ObjectTableModel<OfferWrapper> m =
      new ModifiableObjectTableModel<OfferWrapper>(
        new LinkedList<OfferWrapper>(), new OffersTableProvider());

    panelOffer = new CRUDPanel<OfferWrapper>(m, 0);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.offer.title"));
    featuresPane.setComponentAt(index, panelOffer);
  }

  /**
   * Inicializa o painel CRUD de logins.
   */
  private void initPanelLogin() {
    ObjectTableModel<LoginInfoWrapper> m =
      new ModifiableObjectTableModel<LoginInfoWrapper>(
        new LinkedList<LoginInfoWrapper>(), new LoginTableProvider());

    panelLogin = new CRUDPanel<LoginInfoWrapper>(m, 0);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.login.title"));
    featuresPane.setComponentAt(index, panelLogin);
  }

  /**
   * Inicializa os pain�is das funcionalidades.
   */
  private void initFeaturePanels() {
    initPanelCategory();
    initPanelEntity();
    initPanelCertificate();
    initPanelInterface();
    initPanelAuthorization();
    initPanelLogin();
    initPanelOffer();
  }

  /**
   * Atualiza as a��es de categoria para que interajam com a inst�ncia corrente
   * dos servi�os de administra��o. 
   */
  private void updateCategoryActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(3);
    actionsVector.add(new CategoryRefreshAction(mainDialog, panelCategory
      .getTable(), admin));
    actionsVector.add(new CategoryAddAction(mainDialog, panelCategory, admin));
    actionsVector
      .add(new CategoryDeleteAction(mainDialog, panelCategory, admin));

    panelCategory.setButtonsPane(actionsVector);
  }

  /**
   * Atualiza as a��es de entidade para que interajam com a inst�ncia corrente
   * dos servi�os de administra��o. 
   */
  private void updateEntityActions() {
    // TODO Separar inicializa��o e atualiza��o das a��es. --tmartins
  }

  /**
   * Atualiza as a��es de certificado para que interajam com a inst�ncia
   * corrente dos servi�os de administra��o. 
   */
  private void updateCertificateActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(3);
    actionsVector.add(new CertificateRefreshAction(mainDialog, panelCertificate
      .getTable(), admin));
    actionsVector.add(new CertificateAddAction(mainDialog, panelCertificate,
      admin));
    actionsVector.add(new CertificateDeleteAction(mainDialog, panelCertificate,
      admin));

    panelCertificate.setButtonsPane(actionsVector);
  }

  /**
   * Atualiza as a��es de interface para que interajam com a inst�ncia corrente
   * dos servi�os de administra��o. 
   */
  private void updateInterfaceActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(3);
    actionsVector.add(new InterfaceRefreshAction(mainDialog, panelInterface
      .getTable(), admin));
    actionsVector
      .add(new InterfaceAddAction(mainDialog, panelInterface, admin));
    actionsVector.add(new InterfaceDeleteAction(mainDialog, panelInterface,
      admin));

    panelInterface.setButtonsPane(actionsVector);
  }

  /**
   * Atualiza as a��es de autoriza��o para que interajam com a inst�ncia
   * corrente dos servi�os de administra��o. 
   */
  private void updateAuthorizationActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(3);
    actionsVector.add(new AuthorizationRefreshAction(mainDialog,
      panelAuthorization.getTable(), admin));
    actionsVector.add(new AuthorizationAddAction(mainDialog,
      panelAuthorization, admin));
    actionsVector.add(new AuthorizationDeleteAction(mainDialog,
      panelAuthorization, admin));

    panelAuthorization.setButtonsPane(actionsVector);
  }

  /**
   * Atualiza as a��es de login para que interajam com a inst�ncia corrente dos
   * servi�os de administra��o. 
   */
  private void updateLoginActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(2);
    actionsVector.add(new LoginRefreshAction(mainDialog, panelLogin.getTable(),
      admin));
    actionsVector.add(new LoginDeleteAction(mainDialog, panelLogin, admin));

    panelLogin.setButtonsPane(actionsVector);
  }

  /**
   * Atualiza as a��es de oferta para que interajam com a inst�ncia corrente dos
   * servi�os de administra��o. 
   */
  private void updateOfferActions() {
    Vector<CRUDbleActionInterface> actionsVector =
      new Vector<CRUDbleActionInterface>(2);
    actionsVector.add(new OfferRefreshAction(mainDialog, panelOffer.getTable(),
      admin));
    actionsVector.add(new OfferDeleteAction(mainDialog, panelOffer, admin));

    panelOffer.setButtonsPane(actionsVector);
  }

  private void updateFeatureActions() {
    updateCategoryActions();
    // TODO Pensar em uma melhor forma de divis�o entre a inicializa��o do
    // painel e a atualiza��o de suas a��es ap�s um login. --tmartins
    initPanelEntity();
    updateInterfaceActions();
    updateAuthorizationActions();
    updateOfferActions();

    if (isCurrentUserAdmin) {
      updateCertificateActions();
      updateLoginActions();
    }
  }

  /**
   * Verifica se o usu�rio tem permiss�es para administrar o barramento.
   *
   * @return Booleano que indica se o usu�rio � administrador ou n�o.
   */
  private boolean isCurrentUserAdmin() {
    // Se o m�todo getLogins() n�o lan�ar exce��o, o usu�rio logado est�
    // cadastrado como administrador no barramento.
    try {
      admin.getLogins();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Atualiza o acesso aos recursos de ger�ncia que necessitam de permiss�o
   * administrativa.
   */
  private void updateAccessToAdminFeatures() {
    isCurrentUserAdmin = isCurrentUserAdmin();

    String[] featureNames = { "certificate", "login" };
    for (String featureName : featureNames) {
      int index = featuresPane.indexOfTab(LNG.get("MainDialog." + featureName +
        ".title"));
      featuresPane.setEnabledAt(index, isCurrentUserAdmin);
    }
  }

  /**
   * Faz shutdown do di�logo principal.
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
   * Obt�m o t�tulo do di�logo.
   * 
   * @return T�tulo do di�logo
   */
  private String getDialogTitle() {
    return LNG.get("MainDialog.title") + " - " + LNG.get("Application.title");
  }

}
