package reuse.modified.planref.client.util.crud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import reuse.modified.planref.client.util.PlanrefUI;
import reuse.modified.planref.client.util.TransparentPanel;
import reuse.modified.planref.client.util.crud.celleditors.CRUDNumberEditor;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GUIUtils;
import tecgraf.javautils.gui.MenuButton;
import tecgraf.javautils.gui.MenuButton.PopupPosition;
import tecgraf.javautils.gui.table.ColumnGroup;
import tecgraf.javautils.gui.table.GroupableTableHeader;
import tecgraf.javautils.gui.table.MultiLineLabelHeaderRenderer;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.javautils.gui.table.ObjectTableProvider;

import com.vlsolutions.swing.table.FilterModel;
import com.vlsolutions.swing.table.VLJTable;
import com.vlsolutions.swing.table.filters.RegExpFilter;

/**
 * Listener do Popup menu.
 * 
 * @author Tecgraf.
 */
class PopupListener extends MouseAdapter {

  /** O menu a ser exibido. */
  final JPopupMenu popup;

  /**
   * Construtor b�sico com o menu a ser exibido.
   * 
   * @param popupMenu
   */
  PopupListener(final JPopupMenu popupMenu) {
    popup = popupMenu;
  }

  /** {@inheritDoc} */
  @Override
  public void mousePressed(final MouseEvent e) {
  }

  /** {@inheritDoc} */
  @Override
  public void mouseReleased(final MouseEvent e) {
    if (e.isPopupTrigger()) {
      popup.show(e.getComponent(), e.getX(), e.getY());
    }
  }
}

/**
 * Monta um Painel CRUD - Create Retrieve Update Delete Passando um Vector de
 * AbstractActions, bot�es s�o mostrados.
 * 
 * @author Tecgraf
 * @param <T> O tipo do dado associado ao painel.
 */
public class CRUDPanel<T> extends TransparentPanel implements ActionListener {

  /** Tabela usada na visualiza��a das informa��es */
  private VLJTable table;

  /** Provedor de informa��es da tabela */
  ObjectTableProvider<T> tableProvider;

  /** JScroll panel da tabela */
  private JScrollPane scrollPane;

  /** Panel com os bot�es de a��o */
  private JPanel pnlActionButtons;

  /** Vector com as a��es do bot�es */
  final private Vector<CRUDbleActionInterface> actions =
    new Vector<CRUDbleActionInterface>();

  /** Indicativo do bot�o de adi��o */
  private boolean hasAddButton;

  /** Indicativo do bot�o de edi��o */
  private boolean hasEditButton;

  /** Indicativo do bot�o de remo��o */
  private boolean hasRemoveButton;

  /** A��es recebida pelo bot�o de adi��o */
  private CRUDbleActionInterface crudActionAdd;

  /** A��es recebida pelo bot�o de edi��o */
  private CRUDbleActionInterface crudActionEdit;

  /**
   * Habilita a a��o de editar da tabela no double clique das c�lulas n�o
   * edit�veis.
   */
  private boolean editActionOnUneditableCellDoubleClick = true;

  /** N�mero de a��es que ser�o inclu�das no bot�o de menu. */
  private int nmMenuBtnAction = 0;

  /** A��es recebidas pelo bot�o de remo��o */
  private CRUDbleActionInterface crudActionRemove;

  /** Bot�o de edi��o */
  private JButton editButton;
  /** Bot�o de remo��o */
  private JButton removeButton;

  /** A��es que requerem apenas um item selecionado */
  private List<CRUDbleActionInterface> singleSelectionActionList;

  /** A��es que requerem pelo menos um item selecionado */
  private List<CRUDbleActionInterface> multiSelectionActionList;

  /** A��es que requerem mais de um item selecionado */
  private List<CRUDbleActionInterface> onlyMultiSelectionActionList;

  /**
   * Construtor.
   * 
   * @param pInfo Lista com as informa��es da tabela.
   * @param pTableProvider Provedor de dados da tabela.
   */
  public CRUDPanel(List<T> pInfo, ObjectTableProvider<T> pTableProvider) {
    tableProvider = pTableProvider;
    this.createTable(pInfo, new ObjectTableModel<T>(pInfo, pTableProvider));
    initialize();
  }

  /**
   * Construtor.
   * 
   * @param pTableModel Modelo da tabela.
   */
  public CRUDPanel(ObjectTableModel<T> pTableModel) {
    tableProvider = pTableModel.getProvider();
    this.createTable(pTableModel.getRows(), pTableModel);
    initialize();
  }

  /**
   * Construtor
   * 
   * @param pTableModel Modelo da tabela.
   * @param nmMenuActions n�mero de a��es que ser�o inclu�das no bot�o de menu.
   */
  public CRUDPanel(ObjectTableModel<T> pTableModel, int nmMenuActions) {
    tableProvider = pTableModel.getProvider();
    nmMenuBtnAction = nmMenuActions;
    this.createTable(pTableModel.getRows(), pTableModel);
    initialize();
  }

  /**
   * Construtor
   * 
   * @param pTableModel Modelo da tabela.
   * @param pButtonActions Bot�es que ser�o usados.
   * @param nmMenuActions n�mero de a��es que ser�o inclu�das no bot�o de menu.
   */
  public CRUDPanel(ObjectTableModel<T> pTableModel,
    Vector<? extends CRUDbleActionInterface> pButtonActions, int nmMenuActions) {
    tableProvider = pTableModel.getProvider();
    nmMenuBtnAction = nmMenuActions;
    this.createTable(pTableModel.getRows(), pTableModel);
    setButtonsPane(pButtonActions);
    initialize();
  }

  /**
   * Construtor
   * 
   * @param pInfo Lista com os dados da tabela.
   * @param pTableProvider Provedor de dados da tabela.
   * @param pButtonActions Vetor com os bot�es que ser�o utilizados.
   */
  public CRUDPanel(List<T> pInfo, ObjectTableProvider<T> pTableProvider,
    Vector<? extends CRUDbleActionInterface> pButtonActions) {
    this.tableProvider = pTableProvider;
    this.createTable(pInfo, new ObjectTableModel<T>(pInfo, pTableProvider));
    setButtonsPane(pButtonActions);
    initialize();
  }

  /**
   * @return True, se � necess�rio ativar a a��o de editar nos double cliques
   *         das c�lulas n�o edit�veis. False, caso contr�rio.
   */
  public boolean isEditActionOnUneditableCellDoubleClick() {
    return editActionOnUneditableCellDoubleClick;
  }

  /**
   * Atribui a a��o de editar tamb�m para o double click das c�lulas n�o
   * edit�veis.
   * 
   * @param editActionOnUneditableCellDoubleClick
   */
  public void setEditActionOnUneditableCellDoubleClick(
    boolean editActionOnUneditableCellDoubleClick) {
    this.editActionOnUneditableCellDoubleClick =
      editActionOnUneditableCellDoubleClick;
  }

  /** Inicializa��o interna */
  protected void initialize() {
    // Define BorderLayout como o gerenciador padr�o
    this.setLayout(new BorderLayout());
    this.add(createPopupMenu());
    this.add(getScrollPane(), BorderLayout.CENTER);
    this.add(getButtonsPane(), BorderLayout.SOUTH);
    if (actions.isEmpty()) {
      getButtonsPane().setVisible(false);
    }
    PlanrefUI.setLowBorder(this);
    this.validate();
    this.setVisible(true);
  }

  /**
   * Atualiza a tabela do painel.
   * 
   * @param table A nova tabela do painel.
   */
  public void setTable(VLJTable table) {
    this.table = table;
  }

  /**
   * Cria um novo agrupamento de colunas a partir das colunas fornecidas e o
   * adiciona ao cabe�alho da tabela do painel.
   * 
   * @param columns Colunas do novo agrupamento.
   * @param groupTitle O nome do novo agrupamento de colunas.
   */
  public void addColumnGroup(int[] columns, String groupTitle) {
    TableColumnModel columnModel = this.table.getColumnModel();
    GroupableTableHeader header =
      (GroupableTableHeader) this.table.getTableHeader();
    ColumnGroup columnGroup = new ColumnGroup(groupTitle);
    for (int i = 0; i < columns.length; i++) {
      columnGroup.add(columnModel.getColumn(columns[i]));
    }
    header.addColumnGroup(columnGroup);
  }

  /**
   * @return Retorna o modelo de colunas do crud.
   */
  public TableColumnModel getColumnModel() {
    return this.table.getColumnModel();
  }

  /**
   * @return Retorna o cabe�alho da tabela.
   */
  public GroupableTableHeader getTableHeader() {
    return (GroupableTableHeader) this.table.getTableHeader();
  }

  /**
   * Atualiza o modelo da tabela.
   * 
   * @param tableModel O novo modelo da tabela.
   */
  public void setTableModel(ObjectTableModel<T> tableModel) {
    getTable().setModel(tableModel);
  }

  /**
   * @return Retorna o modelo da tabela.
   */
  @SuppressWarnings("unchecked")
  public ObjectTableModel<T> getTableModel() {
    FilterModel filterModel = (FilterModel) getTable().getModel();
    return (ObjectTableModel<T>) filterModel.getModel();
  }

  /**
   * Atualiza a informa��o nas linhas do modelo.
   * 
   * @param info A nova informa��o.
   */
  public void setInfo(List<T> info) {

    ObjectTableModel<T> model = getTableModel();
    model.setRows(info);
  }

  /**
   * Seleciona a linha com uma determinada informa��o.
   * 
   * @param info A informa��o da linha que ser� selecionada.
   */
  public void setSelectedInfo(T info) {
    if (info != null) {
      getTable().clearSelection();
      List<T> infoList = getTableModel().getRows();
      int indexToSelect = infoList.indexOf(info);
      if (indexToSelect > -1) {
        getTable().addRowSelectionInterval(indexToSelect, indexToSelect);
      }
    }
  }

  /**
   * @return Uma lista com todos os objetos do tipo T contidos na tabela.
   */
  public List<T> getAllInfos() {
    return getTableModel().getRows();
  }

  /**
   * @return Um array com todas as linhas selecionadas na tabela.
   */
  public int[] getSelectedRows() {
    return this.table.getSelectedRows();
  }

  /**
   * @return O objeto do tipo T selecionado na tabela.
   */
  public T getSelectedInfo() {
    return getElementAt(table.getSelectedRow());
  }

  /**
   * Retorna uma lista com todos os objetos do tipo T selecionados na tabela.
   * 
   * @return List<T> com os objetos relativos as linhas selecionadas.
   */
  public List<T> getSelectedInfos() {
    List<T> selectedInfos = new ArrayList<T>();
    final int[] selectedRows = this.table.getSelectedRows();

    for (int i = 0; i < selectedRows.length; i++) {
      selectedInfos.add(getElementAt(selectedRows[i]));
    }
    return selectedInfos;
  }

  /**
   * Retorna o elemento no �ndice fornecido.
   * 
   * @param index O �ndice.
   * @return O elemento no �ndice passado.
   */
  private T getElementAt(int index) {
    // Pega o �ndice da linha no modelo base (ObjectTableModel)
    int selectedModelRowIndex = getBaseModelIndex(index);
    if (selectedModelRowIndex == -1) {
      return null;
    }
    @SuppressWarnings("unchecked")
    ObjectTableModel<T> model = (ObjectTableModel<T>) table.getBaseModel();
    // Recupera a Info no modelo original
    T info1 = model.getRow(selectedModelRowIndex);

    return info1;
  }

  /**
   * Retorna o �ndice da linha no modelo base da tabela.
   * 
   * @param tableIndex O �ndice da tabela.
   * @return O �ndice da linha no modelo base da tabela.
   */
  private int getBaseModelIndex(int tableIndex) {
    // Pega a linha selecionada na tabela
    int selectedRow = tableIndex;

    // Se nenhuma linha � selecionada retorna null
    if (selectedRow == -1) {
      return -1;
    }

    FilterModel filterModel = (FilterModel) table.getModel();

    // Pega o �ndice da linha no modelo base (ObjectTableModel)
    int selectedModelRowIndex = filterModel.getSourceRow(selectedRow);

    return selectedModelRowIndex;
  }

  /**
   * Adiciona/Altera uma informa��o na tabela.
   * 
   * @param row Informa��o a ser adicionada/alterada.
   */
  public void updateInfo(T row) {
    int[] rows = getSelectedRows();
    getTableModel().add(row);
    if (rows.length > 0) {
      for (int item : rows) {
        getSelectionModel().addSelectionInterval(item, item);
      }
    }
  }

  /**
   * Remove uma informa��o da tabela.
   * 
   * @param row Informa��o a ser removida.
   */
  public void removeInfo(T row) {
    getTableModel().remove(row);
  }

  /** Remove todas as informa��es da tabela. */
  public void removeAllInfo() {
    getTableModel().removeAll();
  }

  /**
   * Remove uma lista de informa��es da tabela.
   * 
   * @return Os elementos removidos.
   */
  public Collection<T> removeSelectedInfos() {
    final int[] originalIndexes = table.getSelectedRows();
    final int[] indexesToRemove = new int[originalIndexes.length];
    for (int i = 0; i < originalIndexes.length; i++) {
      indexesToRemove[i] = getBaseModelIndex(originalIndexes[i]);
    }
    Collection<T> collection = getTableModel().removeAll(indexesToRemove);
    return collection;
  }

  /**
   * M�todo respons�vel por criar a tabela do CRUDPanel.
   * 
   * @param info Informa��es da tabela.
   * @param model O modelo da tabela.
   */
  private void createTable(List<T> info, ObjectTableModel<T> model) {
    table = new VLJTable();

    if (model == null) {
      model = new ObjectTableModel<T>(info, tableProvider);
    }

    table.setModel(model);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.setFilteringEnabled(true);
    table.setPopUpSelectorEnabled(false);

    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
      table.installFilter(i, new RegExpFilter(true));
      // Setando como identificador da coluna o nome da mesma
      String identifier = getTableModel().getColumnName(i);
      table.getColumnModel().getColumn(i).setIdentifier(identifier);
    }

    TableColumnModel columnModel = table.getColumnModel();
    GroupableTableHeader header = new GroupableTableHeader(columnModel);
    header.setDefaultRenderer(new MultiLineLabelHeaderRenderer());

    table.setTableHeader(header);
    table.setDefaultRenderer(Boolean.class, new CRUDBooleanRenderer());
    table.setDefaultRenderer(Date.class, new CRUDDateRenderer());
    table.setDefaultRenderer(Number.class, new CRUDNumberRenderer());
    table.setDefaultRenderer(Double.class, new CRUDDoubleRenderer());
    table.setDefaultRenderer(Float.class, new CRUDDoubleRenderer());
    table.setDefaultRenderer(String.class, new CRUDCellRenderer());

    table.setDefaultEditor(Number.class, new CRUDNumberEditor(false,
      CRUDNumberEditor.VALUE_TYPE.NUMBER));
    table.setDefaultEditor(Double.class, new CRUDNumberEditor(false,
      CRUDNumberEditor.VALUE_TYPE.DOUBLE));
    table.setDefaultEditor(Float.class, new CRUDNumberEditor(false,
      CRUDNumberEditor.VALUE_TYPE.FLOAT));

    ComponentAdapter adapter = new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent e) {
        Container tableParent = table.getParent();
        if (tableParent instanceof JViewport) {
          int oldMode = table.getAutoResizeMode();
          if (tableParent.getSize().getWidth() < table.getPreferredSize()
            .getWidth()) {
            if (oldMode != JTable.AUTO_RESIZE_OFF) {
              table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
          }
          else {
            if (oldMode != JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS) {
              table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            }
          }
        }
      }
    };

    table.getModel().addTableModelListener(new TableModelListener() {
      /** {@inheritDoc} */
      @Override
      public void tableChanged(TableModelEvent e) {
      }
    });

    table.addComponentListener(adapter);
    table.getTableHeader().addComponentListener(adapter);

    DirectActionsListener l = new DirectActionsListener();
    table.addMouseListener(l);
    table.addKeyListener(l);
    table.setOpaque(false);
    table.setBackground(Color.white);
  }

  /**
   * Retorna a tabela principal
   * 
   * @return JTable
   */
  public JTable getTable() {
    return table;
  }

  /**
   * Retorna o CRUD constru�do dentro de um JScrollPane.
   * 
   * @return JScrollPane com todos os componentes do CRUD.
   */
  private JScrollPane getScrollPane() {
    if (scrollPane != null) {
      return scrollPane;
    }
    scrollPane = new JScrollPane(getTable());
    scrollPane.setViewportView(getTable());
    scrollPane.setAutoscrolls(true);
    setFilterHeaderVisible(false);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    return scrollPane;
  }

  /**
   * Atualiza a visiblidade dos filtros da tabela.
   * 
   * @param isVisible Caso esse valor seja verdadeiro, uma linha de componentes
   *        edit�veis � adicionada embaixo do cabe�alho da tabela.
   */
  public void setFilterHeaderVisible(boolean isVisible) {
    ((VLJTable) getTable()).setFilterHeaderVisible(isVisible);
  }

  /**
   * Define o mode de ordena��o de uma coluna do CRUD.
   * 
   * @param col O �ndice da coluna da tabela.
   * @param mode O modo de ordena��o desejado. 0 - N�o ordenado; 1 - ordena��o
   *        crescente; igual ou maior a 2 - ordena��o decrescente.
   */
  public void setSortMode(int col, int mode) {
    final VLJTable theTable = ((VLJTable) getTable());
    switch (mode) {
      case 0:
        theTable.setSortMode(col, FilterModel.SORT_NONE);
        break;
      case 1:
        theTable.setSortMode(col, FilterModel.SORT_ASCENDING);
        break;
      default:
        theTable.setSortMode(col, FilterModel.SORT_DESCENDING);
        break;
    }
    theTable.setSortMode(col, mode);
  }

  /**
   * Ordena todas as colunas da tabela de acordo com o modo fornecido.
   * 
   * @param mode O modo de ordena��o desejado. 0 - N�o ordenado; 1 - ordena��o
   *        crescente; igual ou maior a 2 - ordena��o decrescente.
   */
  public void setSortMode(int mode) {
    for (int i = 0; i < table.getColumnCount(); i++) {
      setSortMode(i, mode);
    }
  }

  /**
   * Nesse m�todo escondemos ou mostramos colunas da tabela e o filtro de busca.
   * 
   * @param e Evento disparado.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

    // Verifica se o comando acionado foi o de Habilitar Filtro
    if (e.getActionCommand().equals(LNG.get("CRUDPanel.table.enable.filter"))) {
      setFilterHeaderVisible(((JCheckBoxMenuItem) e.getSource()).isSelected());
    }

    // Verifica se a coluna selecionada/deselecionada deve aparecer ou n�o
    // na visualiza��o
    try {
      final int selectedColumn = Integer.parseInt(e.getActionCommand());
      if (((JCheckBoxMenuItem) e.getSource()).isSelected()) {
        // Torna a coluna visivel
        table.getColumnModel().getColumn(selectedColumn)
          .setMaxWidth(2147483647);
        table.getColumnModel().getColumn(selectedColumn).setMinWidth(15);
        // Torna o header visivel
        table.getTableHeader().getColumnModel().getColumn(selectedColumn)
          .setMaxWidth(2147483647);
        table.getTableHeader().getColumnModel().getColumn(selectedColumn)
          .setMinWidth(15);
      }
      else {
        // Torna a coluna invisivel
        table.getColumnModel().getColumn(selectedColumn).setMaxWidth(0);
        table.getColumnModel().getColumn(selectedColumn).setMinWidth(0);
        // Torna header invisivel (para evitar os "..." 3 pontinhos que
        // ficam se zerar s� o Width)
        table.getTableHeader().getColumnModel().getColumn(selectedColumn)
          .setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(selectedColumn)
          .setMinWidth(0);
      }
    }
    catch (NumberFormatException n) {
    }
  }

  /**
   * Cria o menu popup da tabela do painel
   * 
   * @return O menu.
   */
  private JPopupMenu createPopupMenu() {
    JCheckBoxMenuItem cbMenuItem;

    // Create the popup menu.
    JPopupMenu popup = new JPopupMenu();
    cbMenuItem =
      new JCheckBoxMenuItem(LNG.get("CRUDPanel.table.enable.filter"));
    cbMenuItem.addActionListener(this);
    popup.add(cbMenuItem);

    popup.addSeparator();

    for (int i = 0; i < this.tableProvider.getColumnNames().length; i++) {
      cbMenuItem =
        new JCheckBoxMenuItem(this.tableProvider.getColumnNames()[i], true);
      cbMenuItem.addActionListener(this);
      cbMenuItem.setActionCommand(Integer.toString(i));
      popup.add(cbMenuItem);
    }

    cbMenuItem.addActionListener(this);

    MouseListener popupListener = new PopupListener(popup);
    // Adiciona um listener ao header da tabela para que ela pop o menu
    getTable().getTableHeader().addMouseListener(popupListener);

    return popup;
  }

  /**
   * Definine os bot�es que acompanham o painel
   * 
   * @param pButtonActions Vetor com os bot�es
   */
  public void setButtonsPane(
    Vector<? extends CRUDbleActionInterface> pButtonActions) {
    actions.clear();
    actions.addAll(pButtonActions);
    int sz = pButtonActions.size();
    int menuBtnIdx = sz - nmMenuBtnAction;
    if (sz > 0) {
      if (nmMenuBtnAction > 0) {
        // atualizando o valor do tamanho do array necessario.
        sz = sz - nmMenuBtnAction + 1;
      }
      final AbstractButton[] btns = new AbstractButton[sz];
      int idx = 0;
      getButtonsPane().removeAll();
      editButton = null;
      removeButton = null;
      for (CRUDbleActionInterface action : pButtonActions) {
        final AbstractButton bt;
        if (idx == menuBtnIdx) {
          // Cria o bot�o de menu
          bt = new MenuButton(LNG.get("Planref.others"), PopupPosition.BOTTOM);
          ((MenuButton) bt).add(action);
          btns[idx++] = bt;
          getButtonsPane().add(bt);
        }
        else if (idx > menuBtnIdx) {
          // adiciona uma a��o ao bot�o de menu
          bt = btns[menuBtnIdx];
          ((MenuButton) bt).add(action);
          idx++;
        }
        else {
          // cria um novo bot�o.
          bt = new JButton(action);
          btns[idx++] = bt;
          getButtonsPane().add(bt);
        }
        switch (action.crudActionType()) {
          case ADD:
            hasAddButton = true;
            crudActionAdd = action;
            break;
          case EDIT:
            hasEditButton = true;
            action.setEnabled(false);
            crudActionEdit = action;
            editButton = (JButton) bt;
            break;
          case REMOVE:
            hasRemoveButton = true;
            action.setEnabled(false);
            crudActionRemove = action;
            removeButton = (JButton) bt;
            break;
          case OTHER_SINGLE_SELECTION:
            addSingleSelectAction(action);
            action.setEnabled(false);
            break;
          case OTHER_MULTI_SELECTION:
            addMultiSelectAction(action);
            action.setEnabled(false);
            break;
          case OTHER_ONLY_MULTI_SELECTION:
            addOnlyMultiSelectAction(action);
            action.setEnabled(false);
            break;
          case OTHER:
          default:
            break;
        }
      }

      // Adiciona um listener para tratar com os bot�es
      this.getTable().getSelectionModel().addListSelectionListener(
        new CRUDListSelectionListener());

      GUIUtils.matchPreferredSizes(btns);
      getButtonsPane().validate();
      getButtonsPane().setVisible(true);
      this.validate();
    }
  }

  /**
   * Adiciona a a��o na lista de a��es que requerem multi sele��o.
   * 
   * @param action A��o a ser adicionada.
   */
  private void addMultiSelectAction(CRUDbleActionInterface action) {
    if (multiSelectionActionList == null) {
      multiSelectionActionList = new ArrayList<CRUDbleActionInterface>();
    }
    multiSelectionActionList.add(action);
  }

  /**
   * Adiciona a a��o na lista de a��es que requerem apenas um item selecionado.
   * 
   * @param action A��o a ser adicionada.
   */
  private void addSingleSelectAction(CRUDbleActionInterface action) {
    if (singleSelectionActionList == null) {
      singleSelectionActionList = new ArrayList<CRUDbleActionInterface>();
    }
    singleSelectionActionList.add(action);
  }

  /**
   * Adiciona a a��o na lista de a��es que requerem multi sele��o.
   * 
   * @param action A��o a ser adicionada.
   */
  private void addOnlyMultiSelectAction(CRUDbleActionInterface action) {
    if (onlyMultiSelectionActionList == null) {
      onlyMultiSelectionActionList = new ArrayList<CRUDbleActionInterface>();
    }
    onlyMultiSelectionActionList.add(action);
  }

  /**
   * Habilita as a��es que requerem m�ltiplos itens selecionados.
   * 
   * @param multiSelection Caso esse valor seja verdadeiro, as a��es que
   *        requerem m�ltiplos itens selecionados s�o habilitadas.
   */
  private void enableOnlyMultiSelectionActions(boolean multiSelection) {
    if (onlyMultiSelectionActionList == null) {
      return;
    }
    for (CRUDbleActionInterface action : onlyMultiSelectionActionList) {
      action.setEnabled(multiSelection);
    }
  }

  /**
   * Habilita as a��es que requerem pelo menos um item selecionado.
   * 
   * @param multiSelection Caso esse valor seja verdadeiro, as a��es que
   *        requerem pelo menos um item selecionado s�o habilitadas.
   */
  private void enableMultiSelectionActions(boolean multiSelection) {
    if (multiSelectionActionList == null) {
      return;
    }
    for (CRUDbleActionInterface action : multiSelectionActionList) {
      action.setEnabled(multiSelection);
    }
  }

  /**
   * Habilita as a��es que requerem apenas um item selecionado.
   * 
   * @param singleSelection Caso esse valor seja verdadeiro, as a��es que
   *        requerem apenas um item selecionado s�o habilitadas.
   */
  private void enableSingleSelectionActions(boolean singleSelection) {
    if (singleSelectionActionList == null) {
      return;
    }
    for (CRUDbleActionInterface action : singleSelectionActionList) {
      action.setEnabled(singleSelection);
    }
  }

  /**
   * Retorna o painel contendo os bot�es necess�rios.
   * 
   * @return JPanel com o bot�es.
   */
  public JPanel getButtonsPane() {
    if (pnlActionButtons == null) {
      final FlowLayout layout = new FlowLayout();
      layout.setAlignment(FlowLayout.RIGHT);
      pnlActionButtons = new TransparentPanel(layout);
    }
    return pnlActionButtons;
  }

  /**
   * Altera o editor da coluna.
   * 
   * @param columnName O nome da coluna.
   * @param cellEditor O editor de c�lula para a coluna.
   */
  public void setEditor(String columnName, TableCellEditor cellEditor) {
    TableColumn column = table.getColumn(columnName);
    column.setCellEditor(cellEditor);
  }

  /**
   * M�todo que habilita ou desabilita a ordena��o dos dados quando se clica em
   * uma determinada coluna.
   * 
   * @param enable True para habilitar e false para desabilitar.
   */
  public void setSortEnabled(boolean enable) {
    table.setSortEnabled(enable);
  }

  /**
   * Define um renderer default para ser usado se nenhum renderer tiver sido
   * definido para uma coluna. Se o renderer for nulo, remove o renderer para a
   * classe passada.
   * 
   * @param columnClass A classe para o qual o renderer est� sendo definido.
   * @param renderer O renderer default.
   */
  public void setDefaultRenderer(Class<?> columnClass,
    TableCellRenderer renderer) {
    table.setDefaultRenderer(columnClass, renderer);
  }

  /**
   * Adiciona o listener fornecido.
   * 
   * @param tblModelListener
   */
  public void addTableModelListener(TableModelListener tblModelListener) {
    getTableModel().addTableModelListener(tblModelListener);
  }

  /**
   * Remove o listener fornecido.
   * 
   * @param tblModelListener
   */
  public void removeTableModelListener(TableModelListener tblModelListener) {
    getTableModel().removeTableModelListener(tblModelListener);
  }

  /**
   * Adiciona um listener de sele��o.
   * 
   * @param listener
   */
  public void addListSelectionListener(ListSelectionListener listener) {
    this.getTable().getSelectionModel().addListSelectionListener(listener);
  }

  /**
   * Retorna o modelo de sele��o
   * 
   * @return ListSelectionModel
   */
  public ListSelectionModel getSelectionModel() {
    return this.getTable().getSelectionModel();
  }

  /**
   * Define o modo de sele��o das linhas. Pode ser MULTIPLO ou SIMPLES.
   * 
   * @param option O modo de sele��o das linhas.
   * @see ListSelectionModel
   */
  public void setSelectionMode(int option) {
    getTable().setSelectionMode(option);
  }

  /**
   * Habilita/Desabilita as a��es de edi��o e remo��o.
   * 
   * @param addAction A��o de adicionar.
   * @param editAction A��o de editar.
   * @param removeAction A��o de remover.
   * @param singleSelection Indica se as a��es que requerem apenas um item
   *        selecionado devem ser habilitadas.
   * @param multiSelection Indica se as a��es que requerem pelo menos um item
   *        selecionado devem ser habilitadas.
   * @param onlyMultiSelection Indica se as a��es que requerem m�ltiplos itens
   *        selecionado devem ser habilitadas.
   */
  public void enableActions(boolean addAction, boolean editAction,
    boolean removeAction, boolean singleSelection, boolean multiSelection,
    boolean onlyMultiSelection) {
    if (hasAddButton) {
      crudActionAdd.setEnabled(addAction);
    }
    if (hasEditButton) {
      crudActionEdit.setEnabled(editAction);
    }
    if (hasRemoveButton) {
      crudActionRemove.setEnabled(removeAction);
    }
    if (singleSelectionActionList != null) {
      enableSingleSelectionActions(singleSelection);
    }
    if (multiSelectionActionList != null) {
      enableMultiSelectionActions(multiSelection);
    }
    if (onlyMultiSelectionActionList != null) {
      enableOnlyMultiSelectionActions(onlyMultiSelection);
    }
  }

  /**
   * Retorna a combo para sele��o da penalidade.
   * 
   * @return combo para sele��o da penalidade. private JComboBox
   *         getPenaltyCombo() { List<Penalty> penaltieslist = new
   *         ArrayList<Penalty>(); Penalty[] penalties = Penalty.values(); for
   *         (int i = 0; i < penalties.length; i++) {
   *         penaltieslist.add(penalties[i]); } Collections.sort(penaltieslist);
   *         JComboBox penaltyCombo = new JComboBox();
   *         penaltyCombo.setModel(ListComboBoxModel.get(penaltieslist)); return
   *         penaltyCombo; }
   */

  /**
   * Retorna a combo para sele��o dos tipos de ponto de entrega.
   * 
   * @return combo para sele��o dos tipos de ponto de entrega. private JComboBox
   *         getDeliberyPointTypeCombo() { List<DeliveryPointType> typeslist =
   *         new ArrayList<DeliveryPointType>(); DeliveryPointType[] types =
   *         DeliveryPointType.values(); for (int i = 0; i < types.length; i++)
   *         { typeslist.add(types[i]); } Collections.sort(typeslist); JComboBox
   *         penaltyCombo = new JComboBox();
   *         penaltyCombo.setModel(ListComboBoxModel.get(typeslist)); return
   *         penaltyCombo; }
   */

  /**
   * Implementa um listener que � notificado quando um valor da lista de eventos
   * � modificado.
   * 
   * @author Tecgraf
   */
  class CRUDListSelectionListener implements ListSelectionListener {
    /** {@inheritDoc} */
    @Override
    public void valueChanged(ListSelectionEvent e) {
      final ListSelectionModel listSelectionModel =
        (ListSelectionModel) e.getSource();
      if (listSelectionModel.isSelectionEmpty()) {
        enableActions(true, false, false, false, false, false);

      }
      else if ((listSelectionModel.getMaxSelectionIndex() == listSelectionModel
        .getMinSelectionIndex())) {
        enableActions(true, true, true, true, true, false);
      }
      else if ((listSelectionModel.getMaxSelectionIndex() != listSelectionModel
        .getMinSelectionIndex())) {
        enableActions(true, false, true, false, true, true);
      }
    }
  }

  /**
   * Listener para eventos de mouse e teclado que acionam as a��es fornecidas
   * pelo CRUD, sem que o usu�rio tenha que utilizar seus bot�es corrspondentes.
   */
  class DirectActionsListener extends MouseAdapter implements KeyListener {

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        if (hasEditButton && crudActionEdit.isEnabled()) {
          JTable table = getTable();
          int selectedRow = table.getSelectedRow();
          int selectedColumn = table.getSelectedColumn();
          if (table.isCellEditable(selectedRow, selectedColumn)) {
            /* Caso a c�lula seja edit�vel, inibe a a��o de edi��o via bot�o */
          }
          else if (editActionOnUneditableCellDoubleClick) {
            /* Se a c�lula n�o for edit�vel, aciona o bot�o de edit */
            CRUDPanel.this.crudActionEdit.actionPerformed(new ActionEvent(
              editButton, ActionEvent.ACTION_PERFORMED, ""));
          }
        }
      }
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
      /** Tecla Delete */
      if (e.getKeyCode() == KeyEvent.VK_DELETE) {
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();
        if (table.isCellEditable(selectedRow, selectedColumn)) {
          /* Caso a c�lula seja edit�vel, inibe a a��o de edi��o via teclado */
        }
        else if (hasRemoveButton && crudActionRemove.isEnabled()) {
          crudActionRemove.actionPerformed(new ActionEvent(removeButton,
            ActionEvent.ACTION_PERFORMED, ""));
        }
      }
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
    }

  }

  /**
   * Adaptador para controlar o redimensionamento da tabela
   * 
   * @author Tecgraf
   */
  abstract class ComponentAdapter implements ComponentListener {

    /** {@inheritDoc} */
    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void componentMoved(ComponentEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void componentShown(ComponentEvent e) {
    }
  }

}
