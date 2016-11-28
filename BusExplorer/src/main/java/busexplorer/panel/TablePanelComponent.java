package busexplorer.panel;

import busexplorer.ApplicationIcons;
import busexplorer.utils.DateTimeRenderer;
import busexplorer.utils.StringVectorRenderer;
import busexplorer.utils.Utils;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.javautils.gui.table.SortableTable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Componente que define um painel com uma {@link SortableTable} e modulariza o
 * uso da mesma (inclus�o, edi��o e remo��o de elementos da tabela).
 * Opcionalmente � poss�vel configurar a��es relacionadas ao componente.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado a tabela do componente
 */
public class TablePanelComponent<T> extends RefreshablePanel {

  /** Table */
  private SortableTable table;
  /** Painel de scroll da tabela */
  private JScrollPane scrollPane;
  /** Painel de bot�es */
  private JPanel buttonsPanel;
  /** Bot�o de adi��o */
  private JButton addBtn;
  /** Bot�o de edi��o */
  private JButton editBtn;
  /** Bot�o de remo��o */
  private JButton removeBtn;
  /** Bot�es personalizados */
  private List<JButton> othersBtns = new ArrayList<JButton>();
  /** A��o de adi��o */
  private TablePanelActionInterface<T> addAction;
  /** A��o de edi��o */
  private TablePanelActionInterface<T> editAction;
  /** A��o de remo��o */
  private TablePanelActionInterface<T> removeAction;
  /** Conjunto de a��es a serem inclu�das como "outros" */
  private List<TablePanelActionInterface<T>> othersActions =
    new ArrayList<TablePanelActionInterface<T>>();
  /** Indicador se possui algum bot�o a ser inclu�do na GUI */
  private boolean hasBtns = false;
  /** Indicador se � necess�rio painel de filtro sobre a tabela */
  private boolean hasFilter = true;
  /** A��o de Refresh */
  private TablePanelActionInterface<T> refreshAction;
  /** Campo de filtro */
  private JTextField filterText;
  /** Bot�o de limpar texto de filtro */
  private JButton clearButton;

  /**
   * Construtor
   *
   * @param pInfo Lista com os dados da tabela.
   * @param pTableProvider Provedor de dados da tabela.
   * @param actions Conjunto de a��es relacionadas ao componente.
   */
  public TablePanelComponent(List<T> pInfo, ObjectTableProvider<T> pTableProvider,
                             List<? extends TablePanelActionInterface<T>> actions) {
    this(new ObjectTableModel<T>(pInfo, pTableProvider), actions, true);
  }

  /**
   * Construtor.
   *  @param pTableModel Modelo da tabela.
   * @param actions Conjunto de a��es relacionadas ao componente.
   * @param hasFilter
   */
  public TablePanelComponent(ObjectTableModel<T> pTableModel,
                             List<? extends TablePanelActionInterface<T>> actions, boolean hasFilter) {
    createTable(pTableModel);
    processActions(actions);
    this.hasFilter = hasFilter;
    init();
  }

  /**
   * Cria a tabela do componente.
   * 
   * @param model o modelo da tabela.
   */
  private void createTable(ObjectTableModel<T> model) {
    table = new SortableTable(true);
    table.setModel(model);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.sort(0, SortOrder.ASCENDING);
    table.setDefaultRenderer(Vector.class, new StringVectorRenderer());
    table.setDefaultRenderer(Date.class, new DateTimeRenderer());
    table.getSelectionModel().addListSelectionListener(
      new TableSelectionListener(table));

    DirectActionsListener l = new DirectActionsListener();
    table.addMouseListener(l);
    table.addKeyListener(l);
    table.adjustSize();
  }

  /**
   * Processa o conjunto de a��es associadas ao componente.
   * 
   * @param actions
   */
  private void processActions(List<? extends TablePanelActionInterface<T>> actions) {
    for (TablePanelActionInterface<T> action : actions) {
      action.setTablePanelComponent(this);
      switch (action.getActionType()) {
        case ADD:
          addAction = action;
          addAction.setEnabled(addAction.abilityConditions());

          addBtn = new JButton(action);
          addBtn
            .setToolTipText(Utils.getString(this.getClass(), "add.tooltip"));
          addBtn.setIcon(ApplicationIcons.ICON_ADD_16);
          hasBtns = true;
          break;

        case REMOVE:
          removeAction = action;
          removeAction.setEnabled(false);

          removeBtn = new JButton(action);
          removeBtn.setToolTipText(Utils.getString(this.getClass(),
            "remove.tooltip"));
          removeBtn.setIcon(ApplicationIcons.ICON_DELETE_16);
          hasBtns = true;
          break;

        case EDIT:
          editAction = action;
          editAction.setEnabled(false);

          editBtn = new JButton(action);
          editBtn.setToolTipText(Utils.getString(this.getClass(),
            "edit.tooltip"));
          editBtn.setIcon(ApplicationIcons.ICON_EDIT_16);
          hasBtns = true;
          break;

        case REFRESH:
          this.refreshAction = action;
          break;

        case OTHER_ONLY_MULTI_SELECTION:
        case OTHER_MULTI_SELECTION:
        case OTHER_SINGLE_SELECTION:
          // informa��es como tooltip e icones devem ser configurados na a��o
          othersActions.add(action);
          action.setEnabled(false);
          othersBtns.add(new JButton(action));
          hasBtns = true;
          break;

        default:
          // informa��es como tooltip e icones devem ser configurados na a��o
          othersActions.add(action);
          othersBtns.add(new JButton(action));
          hasBtns = true;
          break;
      }
    }
  }

  /** Inicializa��o interna */
  private void init() {
    // Define BorderLayout como o gerenciador padr�o
    this.setLayout(new BorderLayout());
    if (hasFilter) {
      this.add(getFilterPanel(), BorderLayout.NORTH);
    }
    this.add(getScrollPane(), BorderLayout.CENTER);
    if (hasBtns) {
      this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }
    this.validate();
    this.setVisible(true);
  }

  /**
   * Constr�i o painel de filtro da tabela
   * 
   * @return o painel de filtro.
   */
  private JPanel getFilterPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    GBC gbc = new GBC(0, 0).west().insets(10, 10, 10, 0);
    final JLabel filterLabel =
      new JLabel(Utils.getString(this.getClass(), "filter.label"));
    panel.add(filterLabel, gbc);

    filterText = new JTextField();
    gbc = new GBC(1, 0).insets(10).horizontal().filly();
    panel.add(filterText, gbc);
    filterText.setToolTipText(Utils
      .getString(this.getClass(), "filter.tooltip"));

    clearButton = new JButton(Utils.getString(this.getClass(), "filter.clear"));
    gbc = new GBC(2, 0).east().insets(10, 0, 10, 10);
    clearButton.setToolTipText(Utils.getString(this.getClass(),
      "filter.clear.tooltip"));
    clearButton.setIcon(ApplicationIcons.ICON_CLEAR_16);
    panel.add(clearButton, gbc);

    JButton refreshButton =
      new JButton(Utils.getString(this.getClass(), "refresh"));
    refreshButton.setAction(refreshAction);
    gbc = new GBC(3, 0).east().insets(10, 0, 10, 10);
    refreshButton.setToolTipText(Utils.getString(this.getClass(),
      "refresh.tooltip"));
    refreshButton.setIcon(ApplicationIcons.ICON_REFRESH_16);
    panel.add(refreshButton, gbc);

    setupFilterControls();
    return panel;
  }

  /**
   * Configura os controles (textfield + bot�o) para filtragem da tabela com os
   * usu�rios.
   */
  private void setupFilterControls() {
    // Implementamos um DocumentListener para ativar o filtro quando o conte�do
    // do campo for alterado de qualquer forma
    final Document document = filterText.getDocument();
    document.addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        filterTableContent();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        filterTableContent();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        filterTableContent();
      }
    });

    // Queremos que o conte�do do filtro seja todo selecionado quando o campo
    // ganhar o foco
    filterText.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        filterText.selectAll();
      }

      @Override
      public void focusLost(FocusEvent e) {
        filterText.select(0, 0);
      }
    });

    // a��o do bot�o "limpar"
    clearButton.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        filterText.setText("");
        filterTableContent();
      }
    });
  }

  /**
   * Filtra o conte�do da tabela a partir do conte�do do campo "filtro". O texto
   * do campo � usado como <code>".*texto.*"</code>.
   */
  private void filterTableContent() {
    final String text = filterText.getText();
    if (text.length() > 0) {
      table.setRowFilter(RowFilter.regexFilter(".*" + text + ".*"));
    }
    else {
      table.setRowFilter(null);
    }
  }

  /**
   * Retorna a tabela constru�do dentro de um JScrollPane.
   * 
   * @return JScrollPane com todos os componentes do CRUD.
   */
  private JScrollPane getScrollPane() {
    if (scrollPane != null) {
      return scrollPane;
    }
    scrollPane = new JScrollPane(table);
    scrollPane.setViewportView(table);
    scrollPane.setAutoscrolls(true);
    return scrollPane;
  }

  /**
   * Constr�i o painel de bot�es.
   * 
   * @return o painel.
   */
  private JPanel getButtonsPanel() {
    List<JButton> toMatch = new ArrayList<JButton>();
    int idx = 0;
    buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridBagLayout());
    // separador para alinhar bot�es a direita
    buttonsPanel.add(new JPanel(), new GBC(idx, 0).horizontal().insets(2));
    idx++;
    if (addBtn != null) {
      buttonsPanel.add(addBtn, new GBC(idx, 0).center().none().insets(2));
      //addBtn.setText(null);
      idx++;
      toMatch.add(addBtn);
    }
    if (editBtn != null) {
      buttonsPanel.add(editBtn, new GBC(idx, 0).center().none().insets(2));
      //editBtn.setText(null);
      idx++;
      toMatch.add(editBtn);
    }
    if (removeBtn != null) {
      buttonsPanel.add(removeBtn, new GBC(idx, 0).center().none().insets(2));
      //removeBtn.setText(null);
      idx++;
      toMatch.add(removeBtn);
    }
    if (!othersBtns.isEmpty()) {
      for (JButton otherBtn : othersBtns) {
        buttonsPanel.add(otherBtn, new GBC(idx, 0).center().none().insets(2));
        idx++;
        toMatch.add(otherBtn);
      }

    }
    GUIUtils.matchPreferredSizes(toMatch.toArray(new JButton[toMatch.size()]));
    return buttonsPanel;
  }

  /**
   * Atualiza as habilita��es das a��es cadastradas.
   */
  private void updateActionsAbilities() {
    int[] selectedRows = table.getSelectedRows();
    int length = selectedRows.length;

    if (addAction != null) {
      addAction.setEnabled(addAction.abilityConditions());
    }

    switch (length) {
      case 0:
        if (removeAction != null) {
          removeAction.setEnabled(false);
        }
        if (editAction != null) {
          editAction.setEnabled(false);
        }
        for (TablePanelActionInterface<T> action : othersActions) {
          if (!action.getActionType().equals(ActionType.OTHER)) {
            action.setEnabled(false);
          }
          else {
            action.setEnabled(action.abilityConditions());
          }
        }
        break;

      case 1:
        if (removeAction != null) {
          removeAction.setEnabled(removeAction.abilityConditions());
        }
        if (editBtn != null) {
          editAction.setEnabled(editAction.abilityConditions());
        }
        for (TablePanelActionInterface<T> action : othersActions) {
          switch (action.getActionType()) {
            case OTHER_SINGLE_SELECTION:
            case OTHER_MULTI_SELECTION:
              action.setEnabled(action.abilityConditions());
              break;
            case OTHER_ONLY_MULTI_SELECTION:
              action.setEnabled(false);
              break;
            default:
              action.setEnabled(action.abilityConditions());
              break;
          }
        }
        break;

      default:
        // length > 1
        if (removeAction != null) {
          removeAction.setEnabled(removeAction.abilityConditions());
        }
        if (editAction != null) {
          editAction.setEnabled(false);
        }
        for (TablePanelActionInterface<T> action : othersActions) {
          switch (action.getActionType()) {
            case OTHER_SINGLE_SELECTION:
              action.setEnabled(false);
              break;
            case OTHER_MULTI_SELECTION:
            case OTHER_ONLY_MULTI_SELECTION:
              action.setEnabled(action.abilityConditions());
              break;
            default:
              action.setEnabled(action.abilityConditions());
              break;
          }
        }
        break;
    }
  }

  /**
   * Configura a lista de elementos associados � tabela.
   * 
   * @param objects a lista de elementos.
   */
  public void setElements(List<T> objects) {
    ObjectTableModel<T> model = getTableModel();
    model.setRows(objects);
    table.adjustSize();
  }

  /**
   * Recupera o elemento selecionado.
   * 
   * @return o elemento.
   */
  public T getSelectedElement() {
    final int row = table.getSelectedRow();
    if (row < 0) {
      return null;
    }
    int modelRow = table.convertRowIndexToModel(row);
    return getTableModel().getRow(modelRow);
  }

  /**
   * Recupera o conjunto de elementos selecionados.
   * 
   * @return o conjunto de elementos.
   */
  public List<T> getSelectedElements() {
    List<T> selections = new ArrayList<T>();
    for (int row : table.getSelectedRows()) {
      int modelRow = table.convertRowIndexToModel(row);
      T object = getTableModel().getRow(modelRow);
      selections.add(object);
    }
    return selections;
  }

  /**
   * Remove o elemento selecionado.
   * 
   * @return o elemento removido.
   */
  public T removeSelectedElement() {
    final int row = table.getSelectedRow();
    if (row < 0) {
      return null;
    }
    int modelRow = table.convertRowIndexToModel(row);
    T object = getTableModel().remove(modelRow);
    return object;
  }

  /**
   * Remove o conjunto de elementos selecionados.
   * 
   * @return os elementos removidos.
   */
  public List<T> removeSelectedElements() {
    List<T> removed = new ArrayList<T>();
    int[] rows = table.getSelectedRows();
    if (rows.length <= 0) {
      return new ArrayList<T>();
    }
    int[] modelRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    removed.addAll(getTableModel().removeAll(modelRows));
    return removed;
  }

  /**
   * Seleciona elemento especificado pelo objeto, e, opcionalmente, o exibe.
   *
   * @param object Objeto a ser selecionado.
   * @param show Indica se o elemento selecionado deve ser exibido.
   */
  public void selectElement(T object, boolean show) {
    int index = this.getTableModel().getRows().indexOf(object);
    if (index != -1) {
      int vIndex = table.convertRowIndexToView(index);
      this.table.getSelectionModel().setSelectionInterval(vIndex, vIndex);
      if (!show) {
        return;
      }
      JViewport viewport = (JViewport)table.getParent();
      Rectangle rectangle = table.getCellRect(vIndex, vIndex, true);
      Point point = viewport.getViewPosition();
      rectangle.setLocation(rectangle.x - point.x, rectangle.y - point.y);
      viewport.scrollRectToVisible(rectangle);
    }
  }

  /**
   * Realiza uma atualiza��o do conte�do apresentado pelo painel, possivelmente
   * realizando uma chamada remota.
   *
   * @param event o evento que dispara a a��o.
   */
  @Override
  public void refresh(ActionEvent event) {
    if (this.refreshAction != null) {
      this.refreshAction.actionPerformed(event);
    }
    updateActionsAbilities();
  }

  /**
   * Recupera o modelo da tabela.
   * 
   * @return o modelo da tabela.
   */
  protected ObjectTableModel<T> getTableModel() {
    return (ObjectTableModel<T>) this.table.getModel();
  }

  /**
   * Recupera a tabela deste painel.
   * 
   * @return a tabela do painel.
   */
  protected SortableTable getTable() {
    return this.table;
  }

  /**
   * Listener de sele��o da tabela.
   * 
   * @author Tecgraf
   */
  class TableSelectionListener implements ListSelectionListener {

    /**
     * Refer�ncia para a tabela do componente.
     */
    private JTable table;

    /**
     * Construtor.
     * 
     * @param table a tabela sendo observada.
     */
    public TableSelectionListener(JTable table) {
      this.table = table;
    }

    /** {@inheritDoc} */
    @Override
    public void valueChanged(ListSelectionEvent e) {
      updateActionsAbilities();
    }
  }

  /**
   * Inclui listener de mouse na tabela do painel.
   * 
   * @param listener o listener a ser inclu�do.
   */
  public void addTableMouseListener(MouseListener listener) {
    table.addMouseListener(listener);
  }

  /**
   * Inclui listener de teclado na tabela do painel.
   * 
   * @param listener o listener a ser inclu�do.
   */
  public void addTableKeyListener(KeyListener listener) {
    table.addKeyListener(listener);
  }

  /**
   * Listener para eventos de mouse e teclado que acionam as a��es fornecidas
   * pelo CRUD, sem que o usu�rio tenha que utilizar seus bot�es
   * correspondentes.
   */
  class DirectActionsListener extends MouseAdapter implements KeyListener {

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();
        if (table.isCellEditable(selectedRow, selectedColumn)) {
          /* Caso a c�lula seja edit�vel, inibe a a��o de edi��o via bot�o */
        }
        else if (editBtn != null && editBtn.isEnabled()) {
          editBtn.doClick();
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
        else if (removeBtn != null && removeBtn.isEnabled()) {
          removeBtn.doClick();
        }
      }
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
    }

  }

}
