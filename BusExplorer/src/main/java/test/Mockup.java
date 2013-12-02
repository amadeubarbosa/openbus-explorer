package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Mockup {

  private JFrame frame;
  private JTextField txtFiltro;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          Mockup window = new Mockup();
          window.frame.setVisible(true);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public Mockup() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();

    frame.setSize(640, 480);
    frame.setLocationByPlatform(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel contentPane = new JPanel(new BorderLayout(0, 0));
    contentPane.setBorder(null);

    JMenuBar menuBar = new JMenuBar();
    contentPane.add(menuBar, BorderLayout.NORTH);

    JMenu mnRecarregar = new JMenu("Recarregar");
    menuBar.add(mnRecarregar);

    JMenuItem mntmRecarregarAgora = new JMenuItem("Recarregar agora");
    mnRecarregar.add(mntmRecarregarAgora);

    JSeparator separator = new JSeparator();
    mnRecarregar.add(separator);

    JCheckBoxMenuItem chckbxmntmRecarregarAutomaticamente =
      new JCheckBoxMenuItem("Recarregar automaticamente");
    mnRecarregar.add(chckbxmntmRecarregarAutomaticamente);

    JMenu mnNovoLogin = new JMenu("Novo login...");
    menuBar.add(mnNovoLogin);

    JMenu mnSair = new JMenu("Sair");
    menuBar.add(mnSair);

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    JPanel panelCategories = new JPanel();
    tabbedPane.addTab("Categorias", null, panelCategories, null);
    panelCategories.setLayout(new BoxLayout(panelCategories, BoxLayout.X_AXIS));

    JPanel crudPanel = new JPanel();

    panelCategories.add(crudPanel);
    crudPanel.setLayout(new BorderLayout(0, 0));

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    crudPanel.add(scrollPane, BorderLayout.CENTER);

    JTable table = new JTable();
    table.setFillsViewportHeight(true);
    table.setModel(new DefaultTableModel(new Object[][] { { null, null },
        { null, null }, { null, null }, }, new String[] { "ID Categoria",
        "Nome" }));
    scrollPane.setViewportView(table);

    Box verticalBox = Box.createVerticalBox();
    crudPanel.add(verticalBox, BorderLayout.SOUTH);

    Component verticalStrut = Box.createVerticalStrut(5);
    verticalBox.add(verticalStrut);

    Box horizontalBox = Box.createHorizontalBox();
    horizontalBox.setAlignmentY(Component.CENTER_ALIGNMENT);
    verticalBox.add(horizontalBox);

    Component horizontalStrut = Box.createHorizontalStrut(10);
    horizontalBox.add(horizontalStrut);

    txtFiltro = new JTextField();
    txtFiltro.setForeground(Color.GRAY);
    txtFiltro.setText("Filtro...");
    horizontalBox.add(txtFiltro);
    txtFiltro.setColumns(10);

    Component horizontalGlue = Box.createHorizontalGlue();
    horizontalBox.add(horizontalGlue);

    Component horizontalStrut_1 = Box.createHorizontalStrut(10);
    horizontalBox.add(horizontalStrut_1);

    JButton btnAtualizar = new JButton("Editar");
    horizontalBox.add(btnAtualizar);

    Component horizontalStrut_2 = Box.createHorizontalStrut(5);
    horizontalBox.add(horizontalStrut_2);

    JButton btnAdicionar = new JButton("Adicionar");
    horizontalBox.add(btnAdicionar);

    Component horizontalStrut_3 = Box.createHorizontalStrut(5);
    horizontalBox.add(horizontalStrut_3);

    JButton btnRemover = new JButton("Remover");
    horizontalBox.add(btnRemover);

    Component horizontalStrut_4 = Box.createHorizontalStrut(10);
    horizontalBox.add(horizontalStrut_4);

    Component verticalStrut_1 = Box.createVerticalStrut(10);
    verticalBox.add(verticalStrut_1);

    JPanel panelEntities = new JPanel();
    tabbedPane.addTab("Entidades", null, panelEntities, null);

    JPanel panelCertificates = new JPanel();
    tabbedPane.addTab("Certificados", null, panelCertificates, null);

    JPanel panelInterfaces = new JPanel();
    tabbedPane.addTab("Interfaces", null, panelInterfaces, null);

    JPanel panelAuthorizations = new JPanel();
    tabbedPane.addTab("Autorizações", null, panelAuthorizations, null);

    JPanel panelOffers = new JPanel();
    tabbedPane.addTab("Ofertas", null, panelOffers, null);

    JPanel panelLogins = new JPanel();
    tabbedPane.addTab("Logins", null, panelLogins, null);

    contentPane.add(tabbedPane, BorderLayout.CENTER);
    frame.setContentPane(contentPane);
  }

}