package reuse.modified.planref.client.util.crud;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import reuse.modified.logistic.client.util.UI;
import tecgraf.javautils.gui.table.ObjectTableModel;

import com.vlsolutions.swing.table.FilterModel;
import com.vlsolutions.swing.table.VLJTable;

/**
 * Renderer Básico de celulas para a VLJTable e CRUDPanel.
 * 
 * @author Tecgraf
 */
public class CRUDCellRenderer extends DefaultTableCellRenderer.UIResource {
  /**
   * 
   */
  private static final Color SELECTED_BLUE_BG = new Color(10, 36, 106);

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
      row, column);
    TableModel model = table.getModel();
    TableModel realModel;
    if (model instanceof FilterModel) {
      FilterModel filterModel = (FilterModel) model;
      realModel = filterModel.getModel();
    }
    else {
      realModel = model;
    }

    if (realModel instanceof ObjectTableModel<?>) {
      int realRow;
      if (table instanceof VLJTable) {
        VLJTable vljTable = (VLJTable) table;
        realRow = vljTable.getBaseRow(row);
      }
      else {
        realRow = row;
      }

      ObjectTableModel<?> objetcTableModel = (ObjectTableModel<?>) realModel;
      ModifiableObjectTableModel<?> modifiableObjetcTableModel = null;
      if (objetcTableModel instanceof ModifiableObjectTableModel<?>) {
        modifiableObjetcTableModel =
          (ModifiableObjectTableModel<?>) objetcTableModel;
      }

      /* Atribuindo o ícone da célula e tooltip */
      setIcon(UI.BLANK_BUTTON_ICON);
      setToolTipText(null);
      if (objetcTableModel.isCellEditable(realRow, column)) {
        setIcon(UI.EDITABLE_BUTTON_ICON);
        if (modifiableObjetcTableModel != null) {
          if (modifiableObjetcTableModel.isValueAtInvalid(realRow, column)) {
            setIcon(UI.WARNING_VALIDATION_ICON);
            /* Verifica se valor da célula está inválido */
            if (modifiableObjetcTableModel.getProvider() instanceof VerifiableModifiableObjectTableProvider) {
              VerifiableModifiableObjectTableProvider provider =
                (VerifiableModifiableObjectTableProvider) modifiableObjetcTableModel
                  .getProvider();
              setToolTipText(provider.getTooltip(column));
            }
          }
        }
      }

      /* Atribuindo as cores das linhas */
      if (isSelected) {
        /* Critério mais alto: linha selecionada */
        setBackground(SELECTED_BLUE_BG);
        setForeground(Color.WHITE);
      }
      else if (modifiableObjetcTableModel != null
        && modifiableObjetcTableModel.isValueAtNew(realRow, column)) {
        /* Valor é novo? */
        setBackground(UI.CHANGED_BACKGROUND_COLOR);
        setForeground(Color.black);
      }
      else if (modifiableObjetcTableModel != null
        && modifiableObjetcTableModel.wasValueAtEdited(realRow, column)) {
        /* Valor é editado? */
        setBackground(UI.EDITED_BACKGROUND_COLOR);
        setForeground(Color.black);
      }
      else {
        /* Não é selecionado nem editado, nem novo */
        setBackground(Color.WHITE);
        setForeground(Color.black);
      }
    }
    else {

    }

    return this;
  }
}
