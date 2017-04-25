package busexplorer.utils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * Renderizador de célula que é um {@link Availability} da conectividade da oferta
 *
 * @author Tecgraf
 */
public class AvailabilityRenderer extends DefaultTableCellRenderer {

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

    setFont(table.getFont());
    Availability availability = (Availability) value;
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    switch (availability.status) {
      case ONLINE:
        component.setForeground(Color.WHITE);
        component.setBackground(new Color(40, 155, 44));
        builder.append(Utils.getString(Availability.class, "online"));
        break;
      case UNREACHABLE:
        component.setBackground(new Color(188,20,48));
        component.setForeground(Color.WHITE);
        builder.append(Utils.getString(Availability.class, "unreachable"));
        break;
      case FAILURE:
        component.setBackground(new Color(255,212,43));
        component.setForeground(Color.BLACK);
        builder.append(Utils.getString(Availability.class, "failure"));
        break;
      case UNEXPECTED:
        component.setBackground(new Color(221, 138, 28));
        component.setForeground(Color.BLACK);
        builder.append(Utils.getString(Availability.class, "unexpected"));
        break;
      default:
        if (isSelected) {
          component.setBackground(UIManager.getColor("Table.selectionBackground"));
          component.setForeground(UIManager.getColor("Table.selectionForeground"));
        } else {
          component.setBackground(UIManager.getColor("Table.background"));
          component.setForeground(UIManager.getColor("Table.foreground"));
        }
        builder.append(Utils.getString(Availability.class, "unknown"));
        break;
    }
    if (!availability.detail.isEmpty()) {
      builder.append("<br>");
      builder.append(availability.detail);
    }
    builder.append("<html>");
    ((JLabel) component).setText(builder.toString());
    ((JLabel) component).setVerticalAlignment(CENTER);
    ((JLabel) component).setHorizontalAlignment(CENTER);
    return component;
  }
}
