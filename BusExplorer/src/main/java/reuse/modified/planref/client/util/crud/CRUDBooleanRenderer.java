package reuse.modified.planref.client.util.crud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Representa um boolean a ser renderizado como checkbox na tabela. Específico
 * para o Planref, pois está conforme as especificações de cores dos modifiable
 * table models.
 * 
 * @author Tecgraf
 */
public class CRUDBooleanRenderer extends CRUDCellRenderer {

  /** CheckBox que representa o valor booleano */
  private JCheckBox checkBox;

  /**
   * Painel que engloba os componentes.
   */
  private JPanel panel;

  /**
   * Construtor.
   */
  public CRUDBooleanRenderer() {
    super();
    setHorizontalAlignment(SwingConstants.CENTER);
    getPanel().add(this, BorderLayout.WEST);
    getPanel().add(getCheckBox(), BorderLayout.CENTER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
      row, column);
    if (value == null) {
      return null;
    }
    if (!table.isCellEditable(row, column)) {
      checkBox.setSelected((Boolean) value);
    }
    else {
      checkBox.setEnabled(table.isEnabled());
      checkBox.setSelected((Boolean) value);
    }
    setText(null);
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setToolTipText(String text) {
    super.setToolTipText(text);
    checkBox.setToolTipText(text);
    panel.setToolTipText(text);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color c) {
    super.setBackground(c);
    getPanel().setBackground(c);
    getCheckBox().setBackground(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForeground(Color c) {
    super.setForeground(c);
    getPanel().setForeground(c);
    getCheckBox().setForeground(c);
  }

  /**
   * Recupera o painel que contém o ícone e o checkBox
   * 
   * @return o painel.
   */
  private JPanel getPanel() {
    if (panel == null) {
      panel = new JPanel(new BorderLayout());
    }
    return panel;
  }

  /**
   * Recupera o check box
   * 
   * @return o check box
   */
  private JCheckBox getCheckBox() {
    if (checkBox == null) {
      checkBox = new JCheckBox();
      checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }
    return checkBox;
  }
}
