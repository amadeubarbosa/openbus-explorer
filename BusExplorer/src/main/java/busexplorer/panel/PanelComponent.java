package busexplorer.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.GUIUtils;
import tecgraf.javautils.gui.crud.RegistrationImages;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.javautils.gui.table.SortableTable;
import busexplorer.utils.Utils;
import busexplorer.utils.VectorCellRenderer;

/**
 * Componente que define um painel com uma {@link SortableTable} e modulariza o
 * uso da mesma (inclusão, edição e remoção de elementos da tabela).
 * Opcionalmente é possível configurar ações relacionadas ao componente.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado a tabela do componente
 */
public class PanelComponent<T> extends JPanel {

  /** Table */
  private SortableTable table;
  /** Painel de scroll da tabela */
  private JScrollPane scrollPane;
  /** Painel de botões */
  private JPanel buttonsPanel;
  /** Botão de adição */
  private JButton addBtn;
  /** Botão de edição */
  private JButton editBtn;
  /** Botão de remoção */
  private JButton removeBtn;
  /** Conjunto de ações a serem incluídas no botão "outros" */
  private List<PanelActionInterface<T>> othersActions =
    new ArrayList<PanelActionInterface<T>>();
  /** Indicador se possui algum botão a ser incluído na GUI */
  private boolean hasBtns = false;
  /** Ação de Refresh */
  private PanelActionInterface<T> refreshAction;
  /** Campo de filtro */
  private JTextField filterText;
  /** Botão de limpar texto de filtro */
  private JButton filterButton;

  /**
   * Construtor
   * 
   * @param pInfo Lista com os dados da tabela.
   * @param pTableProvider Provedor de dados da tabela.
   * @param actions Conjunto de ações relacionadas ao componente.
   */
  public PanelComponent(List<T> pInfo, ObjectTableProvider<T> pTableProvider,
    List<? extends PanelActionInterface<T>> actions) {
    this(new ObjectTableModel<T>(pInfo, pTableProvider), actions);
  }

  /**
   * Construtor.
   * 
   * @param pTableModel Modelo da tabela.
   * @param actions Conjunto de ações relacionadas ao componente.
   */
  public PanelComponent(ObjectTableModel<T> pTableModel,
    List<? extends PanelActionInterface<T>> actions) {
    createTable(pTableModel);
    processActions(actions);
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
    table.setDefaultRenderer(Vector.class, new VectorCellRenderer());
    table.getSelectionModel().addListSelectionListener(
      new TableSelectionListener(table));

    DirectActionsListener l = new DirectActionsListener();
    table.addMouseListener(l);
    table.addKeyListener(l);
  }

  /**
   * Processa o conjunto de ações associadas ao componente.
   * 
   * @param actions
   */
  private void processActions(List<? extends PanelActionInterface<T>> actions) {
    for (PanelActionInterface<T> action : actions) {
      action.setPanelComponent(this);
      switch (action.getActionType()) {
        case ADD:
          addBtn = new JButton(action);
          addBtn
            .setToolTipText(Utils.getString(this.getClass(), "add.tooltip"));
          addBtn.setIcon(RegistrationImages.ICON_ADD_16);
          hasBtns = true;
          break;

        case REMOVE:
          removeBtn = new JButton(action);
          removeBtn.setToolTipText(Utils.getString(this.getClass(),
            "remove.tooltip"));
          removeBtn.setEnabled(false);
          removeBtn.setIcon(RegistrationImages.ICON_DELETE_16);
          hasBtns = true;
          break;

        case EDIT:
          editBtn = new JButton(action);
          editBtn.setToolTipText(Utils.getString(this.getClass(),
            "edit.tooltip"));
          editBtn.setEnabled(false);
          editBtn.setIcon(RegistrationImages.ICON_EDIT_16);
          hasBtns = true;
          break;

        case REFRESH:
          this.refreshAction = action;
          break;

        case OTHER_ONLY_MULTI_SELECTION:
        case OTHER_MULTI_SELECTION:
          // informações como tooltip e icones devem ser configurados na ação
          othersActions.add(action);
          action.setEnabled(false);
          break;

        default:
          // informações como tooltip e icones devem ser configurados na ação
          othersActions.add(action);
          break;
      }
    }
  }

  /** Inicialização interna */
  private void init() {
    // Define BorderLayout como o gerenciador padrão
    this.setLayout(new BorderLayout());
    this.add(getFilterPanel(), BorderLayout.NORTH);
    this.add(getScrollPane(), BorderLayout.CENTER);
    this.add(getButtonsPanel(), BorderLayout.SOUTH);
    if (!hasBtns) {
      getButtonsPanel().setVisible(false);
    }
    this.validate();
    this.setVisible(true);
  }

  /**
   * Constrói o painel de filtro da tabela
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

    filterButton =
      new JButton(Utils.getString(this.getClass(), "filter.clear"));
    gbc = new GBC(2, 0).east().insets(10, 0, 10, 10);
    panel.add(filterButton, gbc);

    setupFilterControls();
    return panel;
  }

  /**
   * Configura os controles (textfield + botão) para filtragem da tabela com os
   * usuários.
   */
  private void setupFilterControls() {
    // Implementamos um DocumentListener para ativar o filtro quando o conteúdo
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

    // Queremos que o conteúdo do filtro seja todo selecionado quando o campo
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

    // ação do botão "limpar"
    filterButton.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        filterText.setText("");
        filterTableContent();
      }
    });
  }

  /**
   * Filtra o conteúdo da tabela a partir do conteúdo do campo "filtro". O texto
   * do campo é usado como <code>".*texto.*"</code>.
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
   * Retorna a tabela construído dentro de um JScrollPane.
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
   * Constrói o painel de botões.
   * 
   * @return o painel.
   */
  private JPanel getButtonsPanel() {
    List<JButton> toMatch = new ArrayList<JButton>();
    int idx = 0;
    buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridBagLayout());
    // separador para alinhar botões a direita
    buttonsPanel.add(new JPanel(), new GBC(idx, 0).horizontal().insets(2));
    idx++;
    if (addBtn != null) {
      buttonsPanel.add(addBtn, new GBC(idx, 0).center().none().insets(2));
      addBtn.setText(null);
      idx++;
      toMatch.add(addBtn);
    }
    if (editBtn != null) {
      buttonsPanel.add(editBtn, new GBC(idx, 0).center().none().insets(2));
      editBtn.setText(null);
      idx++;
      toMatch.add(editBtn);
    }
    if (removeBtn != null) {
      buttonsPanel.add(removeBtn, new GBC(idx, 0).center().none().insets(2));
      removeBtn.setText(null);
      idx++;
      toMatch.add(removeBtn);
    }
    if (!othersActions.isEmpty()) {
      buttonsPanel.add(getOthersButton(), new GBC(idx, 0).center().none()
        .insets(2));
      idx++;
    }
    GUIUtils.matchPreferredSizes(toMatch.toArray(new JButton[toMatch.size()]));
    return buttonsPanel;
  }

  /**
   * Inicialização do botão: others.
   * 
   * @return o botão
   */
  private JButton getOthersButton() {
    final JButton others = new JButton();
    others.setText(Utils.getString(this.getClass(), "button.others"));
    others.setIcon(RegistrationImages.ICON_DOWN_4);
    others.setHorizontalTextPosition(SwingConstants.RIGHT);
    others.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final T selectedObject = getSelectedElement();
        if (selectedObject == null) {
          return;
        }
        if (othersActions.isEmpty()) {
          return;
        }
        final JPopupMenu menu = new JPopupMenu();
        for (PanelActionInterface<T> action : othersActions) {
          menu.add(action);
        }
        final int y = others.getHeight();
        menu.show(others, 0, y);
      }
    });
    return others;
  }

  /**
   * Configura a lista de elementos associados à tabela.
   * 
   * @param objects a lista de elementos.
   */
  public void setElements(List<T> objects) {
    ObjectTableModel<T> model = getTableModel();
    model.setRows(objects);
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
   * Realiza uma atualização do conteúdo apresentado pelo painel, possivelmente
   * realizando uma chamada remota.
   * 
   * @param event o evento que dispara a ação.
   */
  public void refresh(ActionEvent event) {
    if (this.refreshAction != null) {
      this.refreshAction.actionPerformed(event);
    }
    else {
      // do nothing
    }
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
   * Listener de seleção da tabela.
   * 
   * @author Tecgraf
   */
  class TableSelectionListener implements ListSelectionListener {

    /**
     * Referência para a tabela do componente.
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
      int[] selectedRows = table.getSelectedRows();
      int length = selectedRows.length;

      switch (length) {
        case 0:
          if (removeBtn != null) {
            removeBtn.setEnabled(false);
          }
          if (editBtn != null) {
            editBtn.setEnabled(false);
          }
          for (PanelActionInterface<T> action : othersActions) {
            if (!action.getActionType().equals(ActionType.OTHER)) {
              action.setEnabled(false);
            }
          }
          break;

        case 1:
          if (removeBtn != null) {
            removeBtn.setEnabled(true);
          }
          if (editBtn != null) {
            editBtn.setEnabled(true);
          }
          for (PanelActionInterface<T> action : othersActions) {
            switch (action.getActionType()) {
              case OTHER_SINGLE_SELECTION:
              case OTHER_MULTI_SELECTION:
                action.setEnabled(true);
                break;
              case OTHER_ONLY_MULTI_SELECTION:
                action.setEnabled(false);
                break;
              default:
                action.setEnabled(true);
                break;
            }
          }
          break;

        default:
          // length > 1
          if (removeBtn != null) {
            removeBtn.setEnabled(false);
          }
          if (editBtn != null) {
            editBtn.setEnabled(false);
          }
          for (PanelActionInterface<T> action : othersActions) {
            switch (action.getActionType()) {
              case OTHER_SINGLE_SELECTION:
                action.setEnabled(false);
                break;
              case OTHER_MULTI_SELECTION:
              case OTHER_ONLY_MULTI_SELECTION:
                action.setEnabled(true);
                break;
              default:
                action.setEnabled(true);
                break;
            }
          }
          break;
      }
    }
  }

  /**
   * Listener para eventos de mouse e teclado que acionam as ações fornecidas
   * pelo CRUD, sem que o usuário tenha que utilizar seus botões
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
          /* Caso a célula seja editável, inibe a ação de edição via botão */
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
          /* Caso a célula seja editável, inibe a ação de edição via teclado */
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
