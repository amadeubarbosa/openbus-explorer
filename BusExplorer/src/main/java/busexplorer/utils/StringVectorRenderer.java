package busexplorer.utils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Renderizador de célula que é um {@link Vector} de {@link String}
 * 
 * @author Tecgraf
 */
public class StringVectorRenderer extends DefaultTableCellRenderer {

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

    List<String> values = new ArrayList((List<String>) value);
    Collections.sort(values);
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    for (int i = 0; i < values.size(); i++) {
      builder.append(values.get(i));
      if (i < values.size() - 1) {
        builder.append("<br>");
      }
    }
    builder.append("</html>");
    ((JLabel) component).setText(builder.toString());
    return component;
  }
}
