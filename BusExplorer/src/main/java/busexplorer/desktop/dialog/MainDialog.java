package busexplorer.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.PanelActionInterface;
import busexplorer.panel.PanelComponent;
import busexplorer.panel.authorizations.AuthorizationAddAction;
import busexplorer.panel.authorizations.AuthorizationDeleteAction;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.authorizations.AuthorizationRefreshAction;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.categories.CategoryAddAction;
import busexplorer.panel.categories.CategoryEditAction;
import busexplorer.panel.categories.CategoryDeleteAction;
import busexplorer.panel.categories.CategoryWrapper;
import busexplorer.panel.categories.CategoryRefreshAction;
import busexplorer.panel.categories.CategoryTableProvider;
import busexplorer.panel.certificates.CertificateAddAction;
import busexplorer.panel.certificates.CertificateEditAction;
import busexplorer.panel.certificates.CertificateDeleteAction;
import busexplorer.panel.certificates.CertificateWrapper;
import busexplorer.panel.certificates.CertificateRefreshAction;
import busexplorer.panel.certificates.CertificateTableProvider;
import busexplorer.panel.entities.EntityAddAction;
import busexplorer.panel.entities.EntityDeleteAction;
import busexplorer.panel.entities.EntityEditAction;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.entities.EntityRefreshAction;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.interfaces.InterfaceAddAction;
import busexplorer.panel.interfaces.InterfaceDeleteAction;
import busexplorer.panel.interfaces.InterfaceWrapper;
import busexplorer.panel.interfaces.InterfaceRefreshAction;
import busexplorer.panel.interfaces.InterfaceTableProvider;
import busexplorer.panel.logins.LoginDeleteAction;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.logins.LoginRefreshAction;
import busexplorer.panel.logins.LoginTableProvider;
import busexplorer.panel.offers.OfferDeleteAction;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.panel.offers.OfferPropertiesAction;
import busexplorer.panel.offers.OfferRefreshAction;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import exception.handling.ExceptionContext;

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
    if (Application.login() != null && Application.login().getAssistant() != null) {
      Application.login().getAssistant().shutdown();
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

    buildMenuBar();
    buildFeaturesComponent();
    pack();

    setDialogTitle(LNG.get("MainDialog.title.disconnected"));
  }

  /**
   * Constrói a barra de menu da janela.
   */
  private void buildMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menuConnection = new JMenu(LNG.get("MainDialog.menuBar.connection"));
    menuBar.add(menuConnection);

    JMenuItem itemDisconnect =
      new JMenuItem(LNG.get("MainDialog.menuBar.connection.disconnect"));
    itemDisconnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        BusExplorerTask<Object> task =
          new BusExplorerTask<Object>(Application.exceptionHandler(),
            ExceptionContext.Service) {
          @Override
          protected void performTask() throws Exception {
            if (Application.login().getAssistant() != null) {
              Application.login().getAssistant().shutdown();
            }
            setDialogTitle(LNG.get("MainDialog.title.disconnected"));
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
    menuConnection.add(itemDisconnect);

    menuConnection.add(new JSeparator());

    JMenuItem itemQuit =
      new JMenuItem(LNG.get("MainDialog.menuBar.connection.quit"));
    itemQuit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int option =
          JOptionPane.showConfirmDialog(MainDialog.this,
            Utils.getString(MainDialog.class, "quit.confirm.msg"),
            Utils.getString(MainDialog.class, "quit.confirm.title"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
          dispose();
          System.exit(0);
        }
      }
    });
    menuConnection.add(itemQuit);

    add(menuBar, BorderLayout.NORTH);
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
          "offer", "login" };
    for (String featureName : featureNames) {
      featuresPane.addTab(LNG.get("MainDialog." + featureName + ".title"),
        null, null, LNG.get("MainDialog." + featureName + ".toolTip"));
    }
    initFeaturePanels();

    featuresPane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent event) {
        PanelComponent<?> component =
          (PanelComponent<?>) featuresPane.getSelectedComponent();
        component.refresh(null);
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

    List<PanelActionInterface<CategoryWrapper>> actionsVector =
      new Vector<PanelActionInterface<CategoryWrapper>>(3);
    actionsVector.add(new CategoryRefreshAction(this, admin));
    actionsVector.add(new CategoryAddAction(this, admin));
    actionsVector.add(new CategoryEditAction(this, admin));
    actionsVector.add(new CategoryDeleteAction(this, admin));

    PanelComponent<CategoryWrapper> panelCategory =
      new PanelComponent<CategoryWrapper>(model, actionsVector);

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

    List<PanelActionInterface<EntityWrapper>> actionsVector =
      new Vector<PanelActionInterface<EntityWrapper>>(3);
    actionsVector.add(new EntityRefreshAction(this, admin));
    actionsVector.add(new EntityAddAction(this, admin));
    actionsVector.add(new EntityEditAction(this, admin));
    actionsVector.add(new EntityDeleteAction(this, admin));

    PanelComponent<EntityWrapper> panelEntity =
      new PanelComponent<EntityWrapper>(model, actionsVector);

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

    List<PanelActionInterface<CertificateWrapper>> actionsVector =
      new Vector<PanelActionInterface<CertificateWrapper>>(3);
    actionsVector.add(new CertificateRefreshAction(this, admin));
    actionsVector.add(new CertificateAddAction(this, admin));
    actionsVector.add(new CertificateEditAction(this, admin));
    actionsVector.add(new CertificateDeleteAction(this, admin));

    PanelComponent<CertificateWrapper> panelCertificate =
      new PanelComponent<CertificateWrapper>(model, actionsVector);

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

    List<PanelActionInterface<InterfaceWrapper>> actionsVector =
      new Vector<PanelActionInterface<InterfaceWrapper>>(3);
    actionsVector.add(new InterfaceRefreshAction(this, admin));
    actionsVector.add(new InterfaceAddAction(this, admin));
    actionsVector.add(new InterfaceDeleteAction(this, admin));

    PanelComponent<InterfaceWrapper> panelInterface =
      new PanelComponent<InterfaceWrapper>(model, actionsVector);

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

    List<PanelActionInterface<AuthorizationWrapper>> actionsVector =
      new Vector<PanelActionInterface<AuthorizationWrapper>>(3);
    actionsVector.add(new AuthorizationRefreshAction(this, admin));
    actionsVector.add(new AuthorizationAddAction(this, admin));
    actionsVector.add(new AuthorizationDeleteAction(this, admin));

    PanelComponent<AuthorizationWrapper> panelAuthorization =
      new PanelComponent<AuthorizationWrapper>(model, actionsVector);

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

    List<PanelActionInterface<OfferWrapper>> actionsVector =
      new Vector<PanelActionInterface<OfferWrapper>>(2);
    actionsVector.add(new OfferRefreshAction(this, admin));
    actionsVector.add(new OfferDeleteAction(this, admin));
    final OfferPropertiesAction propertiesAction =
      new OfferPropertiesAction(this, admin);
    actionsVector.add(propertiesAction);

    PanelComponent<OfferWrapper> panelOffer =
      new PanelComponent<OfferWrapper>(model, actionsVector);
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

    List<PanelActionInterface<LoginWrapper>> actionsVector =
      new Vector<PanelActionInterface<LoginWrapper>>(2);
    actionsVector.add(new LoginRefreshAction(this, admin));
    actionsVector.add(new LoginDeleteAction(this, admin));

    PanelComponent<LoginWrapper> panelLogin =
      new PanelComponent<LoginWrapper>(model, actionsVector);

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
    // Seleciona a primeira aba do pane de funcionalidades.
    featuresPane.setSelectedIndex(0);
    // A atualização explícita é necessária porque, como esperado, o
    // ChangeListener da pane só é ativado se a aba corrente for modificada.
    ((PanelComponent<?>) featuresPane.getSelectedComponent()).refresh(null);
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
