package busexplorer.desktop.dialog;

import org.omg.CORBA.ORB;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import admin.BusAdminImpl;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.assistant.Assistant;
import test.PanelActionInterface;
import test.PanelComponent;
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
import busexplorer.wrapper.CategoryInfo;
import busexplorer.wrapper.CertificateInfo;
import busexplorer.wrapper.EntityInfo;
import busexplorer.wrapper.InterfaceInfo;
import busexplorer.wrapper.LoginInfoWrapper;
import busexplorer.wrapper.OfferWrapper;

/**
 * Diálogo principal da aplicação
 * 
 * @author Tecgraf
 */
public class MainDialog {
  /**
   * Assistente do barramento.
   */
  private Assistant assistant = null;
  /**
   * Acessa os serviços barramento relacionados à administração.
   */
  private BusAdminImpl admin;
  /**
   * A janela principal da aplicação.
   */
  private JFrame mainDialog;
  /**
   * Pane de recursos de gerência do barramento.
   */
  private JTabbedPane featuresPane;

  /**
   * Construtor.
   */
  public MainDialog() {
    admin = new BusAdminImpl();
    buildDialog();
  }

  /**
   * Exibe a janela.
   */
  public void show() {
    mainDialog.setVisible(true);
    login();
  }

  /**
   * Constrói os componentes da janela.
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

    buildMenuBar();
    buildFeaturesComponent();
  }

  /**
   * Constrói a barra de menu da janela.
   */
  private void buildMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menuConnection = new JMenu(LNG.get("MainDialog.menuBar.connection"));
    menuBar.add(menuConnection);

    JMenuItem itemDisconnect = new
      JMenuItem(LNG.get("MainDialog.menuBar.connection.disconnect"));
    itemDisconnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Task task = new Task() {
          @Override
          protected void performTask() throws Exception {
            if (assistant != null) {
              assistant.shutdown();
            }
          }

          @Override
          protected void afterTaskUI() {
            login();
          }
        };

        task.execute(mainDialog, LNG.get("MainDialog.logout.waiting.title"), LNG
          .get("MainDialog.logout.waiting.msg"));
      }
    });
    menuConnection.add(itemDisconnect);

    menuConnection.add(new JSeparator());

    JMenuItem itemQuit = new
      JMenuItem(LNG.get("MainDialog.menuBar.connection.quit"));
    itemQuit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        shutdownMainDialog();
      }
    });
    menuConnection.add(itemQuit);

    mainDialog.add(menuBar, BorderLayout.NORTH);
  }

  /**
   * Constrói o painel das funcionalidades.
   */
  private void buildFeaturesComponent() {
    featuresPane = new JTabbedPane(JTabbedPane.TOP);

    featuresPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    // A primeira aba não deve depender de permissões administrativas; vide
    // método updateAdminFeatures().
    String[] featureNames = { "category", "entity", "certificate", "interface",
      "authorization", "offer", "login" };
    for (String featureName : featureNames) {
      featuresPane.addTab(LNG.get("MainDialog." + featureName + ".title"), null,
        null, LNG.get("MainDialog." + featureName + ".toolTip"));
    }
    initFeaturePanels();

    featuresPane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent event) {
        PanelComponent<?> component =
          (PanelComponent<?>)featuresPane.getSelectedComponent();
        // TODO Corrigir parâmetro do método refresh, ou sobrecarregá-lo.
        // --tmartins
        component.refresh(null);
      }
    });

    mainDialog.add(featuresPane, BorderLayout.CENTER);
  }
  /**
   * Inicializa o painel de CRUD de categorias.
   */
  private void initPanelCategory() {
    ObjectTableModel<CategoryInfo> model = new ObjectTableModel<CategoryInfo>(
      new LinkedList<CategoryInfo>(),
      new CategoryTableProvider());

    List<PanelActionInterface<CategoryInfo>> actionsVector =
      new Vector<PanelActionInterface<CategoryInfo>>(3);
    actionsVector.add(new CategoryRefreshAction(mainDialog, admin));
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new CategoryAddAction(mainDialog, admin));
    actionsVector.add(new CategoryDeleteAction(mainDialog, admin));
*/

    PanelComponent<CategoryInfo> panelCategory = new
      PanelComponent<CategoryInfo>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.category.title"));
    featuresPane.setComponentAt(index, panelCategory);
  }

  /**
   * Inicializa o painel de CRUD de entidades.
   */
  private void initPanelEntity() {
    ObjectTableModel<EntityInfo> model =
      new ObjectTableModel<EntityInfo>(new ArrayList<EntityInfo>(),
        new EntityTableProvider());

    List<PanelActionInterface<EntityInfo>> actionsVector =
      new Vector<PanelActionInterface<EntityInfo>>(3);
    actionsVector.add(new EntityRefreshAction(mainDialog, admin));
    actionsVector.add(new EntityAddAction(mainDialog, admin));
    actionsVector.add(new EntityDeleteAction(mainDialog, admin));

    PanelComponent<EntityInfo> panelEntity = new
      PanelComponent<EntityInfo>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.entity.title"));
    featuresPane.setComponentAt(index, panelEntity);
  }

  /** 
   * Inicializa o painel de CRUD de certificados.
   */
  private void initPanelCertificate() {
    ObjectTableModel<CertificateInfo> model =
      new ObjectTableModel<CertificateInfo>(new LinkedList<CertificateInfo>(),
        new CertificateTableProvider());

    List<PanelActionInterface<CertificateInfo>> actionsVector =
      new Vector<PanelActionInterface<CertificateInfo>>(3);
    actionsVector.add(new CertificateRefreshAction(mainDialog, admin));
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new CertificateAddAction(mainDialog, admin));
    actionsVector.add(new CertificateDeleteAction(mainDialog, admin));
*/

    PanelComponent<CertificateInfo> panelCertificate = new
      PanelComponent<CertificateInfo>(model, actionsVector);

    int index =
      featuresPane.indexOfTab(LNG.get("MainDialog.certificate.title"));
    featuresPane.setComponentAt(index, panelCertificate);
  }

  /**
   * Inicializa o painel de CRUD de interfaces.
   */
  private void initPanelInterface() {
    ObjectTableModel<InterfaceInfo> model =
      new ObjectTableModel<InterfaceInfo>(new LinkedList<InterfaceInfo>(),
        new InterfaceTableProvider());

    List<PanelActionInterface<InterfaceInfo>> actionsVector =
      new Vector<PanelActionInterface<InterfaceInfo>>(3);
    actionsVector.add(new InterfaceRefreshAction(mainDialog, admin));
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new InterfaceAddAction(mainDialog, admin));
    actionsVector.add(new InterfaceDeleteAction(mainDialog, admin));
*/

    PanelComponent<InterfaceInfo> panelInterface = new
      PanelComponent<InterfaceInfo>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.interface.title"));
    featuresPane.setComponentAt(index, panelInterface);
  }

  /**
   * Inicializa o painel de CRUD de autorizações.
   */
  private void initPanelAuthorization() {
    ObjectTableModel<AuthorizationWrapper> model =
      new ObjectTableModel<AuthorizationWrapper>(
        new LinkedList<AuthorizationWrapper>(),
        new AuthorizationTableProvider());

    List<PanelActionInterface<AuthorizationWrapper>> actionsVector =
      new Vector<PanelActionInterface<AuthorizationWrapper>>(3);
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new AuthorizationRefreshAction(mainDialog, admin));
    actionsVector.add(new AuthorizationAddAction(mainDialog, admin));
    actionsVector.add(new AuthorizationDeleteAction(mainDialog, admin));
*/

    PanelComponent<AuthorizationWrapper> panelAuthorization = new
      PanelComponent<AuthorizationWrapper>(model, actionsVector);

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
        new OffersTableProvider());

    List<PanelActionInterface<OfferWrapper>> actionsVector =
      new Vector<PanelActionInterface<OfferWrapper>>(2);
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new OfferRefreshAction(mainDialog, admin));
    actionsVector.add(new OfferDeleteAction(mainDialog, admin));
*/

    PanelComponent<OfferWrapper> panelOffer = new
      PanelComponent<OfferWrapper>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.offer.title"));
    featuresPane.setComponentAt(index, panelOffer);
  }

  /**
   * Inicializa o painel CRUD de logins.
   */
  private void initPanelLogin() {
    ObjectTableModel<LoginInfoWrapper> model =
      new ObjectTableModel<LoginInfoWrapper>(new LinkedList<LoginInfoWrapper>(),
        new LoginTableProvider());

    List<PanelActionInterface<LoginInfoWrapper>> actionsVector =
      new Vector<PanelActionInterface<LoginInfoWrapper>>(2);
    // TODO Refatorar implementação das ações. --tmartins
/*
    actionsVector.add(new LoginRefreshAction(mainDialog, admin));
    actionsVector.add(new LoginDeleteAction(mainDialog, admin));
*/

    PanelComponent<LoginInfoWrapper> panelLogin = new
      PanelComponent<LoginInfoWrapper>(model, actionsVector);

    int index = featuresPane.indexOfTab(LNG.get("MainDialog.login.title"));
    featuresPane.setComponentAt(index, panelLogin);
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
  }

  /**
   * Executa as ações de login e, em caso de sucesso, atualiza membros
   * dependentes das novas informações de login.
   */
  private void login() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        LoginDialog loginDialog = new LoginDialog(mainDialog);
        loginDialog.show();

        assistant = loginDialog.getAssistant();
        updateAdminFeatures(loginDialog.getHost(), loginDialog.getPort(),
          assistant.orb());
      }
    });
  }

  /**
   * Atualiza as funcionalidades administrativas da aplicação.
   *
   * @param host Host do barramento a ser administrado
   * @param port Porta do barramento a ser administrado
   * @param orb ORB que contém o contexto OpenBus
   */
  private void updateAdminFeatures(String host, short port, ORB orb) {
    admin.connect(host, port, orb);

    String[] featureNames = { "certificate", "login" };
    for (String featureName : featureNames) {
      boolean isCurrentUserAdmin = isCurrentUserAdmin();
      int index = featuresPane.indexOfTab(LNG.get("MainDialog." +
        featureName + ".title"));
      featuresPane.setEnabledAt(index, isCurrentUserAdmin);
    }
    // Seleciona e atualiza a primeira aba do pane de funcionalidades.
    featuresPane.setSelectedIndex(0);
    // A atualização explícita é necessária porque, como esperado, o
    // ChangeListener da pane só é ativado se a aba corrente for modificada.
    // TODO Corrigir parâmetro do método refresh, ou sobrecarregá-lo. --tmartins
    ((PanelComponent<?>)featuresPane.getSelectedComponent()).refresh(null);
  }

  /**
   * Verifica se o usuário tem permissões para administrar o barramento.
   *
   * @return Booleano que indica se o usuário é administrador ou não.
   */
  private boolean isCurrentUserAdmin() {
    // Se o método getLogins() não lançar exceção, o usuário logado está
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
   * Faz shutdown do diálogo principal.
   */
  private void shutdownMainDialog() {
    mainDialog.dispose();
    assistant.shutdown();
  }

  /**
   * Obtém o título do diálogo.
   * 
   * @return Título do diálogo
   */
  private String getDialogTitle() {
    return LNG.get("MainDialog.title") + " - " + LNG.get("Application.title");
  }

}
