package busexplorer.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderizador de célula que é um {@link Vector} de {@link String}
 * 
 * @author Tecgraf
 */
public class VectorCellRenderer extends DefaultTableCellRenderer {

  /** Espaçamento vertical entre as bordas e o texto */
  private int verticalGap = 8;
  /** Espaço entre as linhas. */
  private int lineSpace = 2;

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
    Component component =
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
        row, column);

    if (value == null) {
      return component;
    }

    Vector<String> interfaces = (Vector<String>) value;
    Collections.sort(interfaces);
    setFont(table.getFont());
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    for (int i = 0; i < interfaces.size(); i++) {
      builder.append(interfaces.get(i));
      if (i < interfaces.size() - 1) {
        builder.append("<br>");
      }
    }
    builder.append("</html>");
    ((JLabel) component).setText(builder.toString());

    Insets insets = getInsets();
    FontMetrics metrics = getFontMetrics(getFont());
    // Calcula as dimensões do texto.
    int width = 0;
    for (String aLine : interfaces) {
      width = Math.max(width, metrics.stringWidth(aLine));
    }
    int height = +((metrics.getHeight() + lineSpace) * interfaces.size());

    // Inclui o gap e os insets
    width += insets.left + insets.right;
    height += verticalGap + insets.top + insets.bottom;

    Dimension size = new Dimension(width, height);
    component.setPreferredSize(size);
    component.setMinimumSize(size);
    // FIXME esta linha parece que esta gerando um problema de processamento exagerado.
    //table.setRowHeight(row, height);

    return component;
  }
}
