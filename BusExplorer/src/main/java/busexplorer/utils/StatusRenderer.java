package busexplorer.utils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

import static busexplorer.utils.Status.FAILURE;
import static busexplorer.utils.Status.ONLINE;
import static busexplorer.utils.Status.UNEXPECTED;
import static busexplorer.utils.Status.UNREACHABLE;

/**
 * Renderizador de célula que é um {@link Status} da conectividade da oferta
 * 
 * @author Tecgraf
 */
public class StatusRenderer extends DefaultTableCellRenderer {

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
    StringBuilder builder = new StringBuilder();
    builder.append("<html><center>");
    switch ((Integer) value) {
      case ONLINE:
        builder.append(Utils.getString(Status.class, "online"));
        component.setForeground(Color.WHITE);
        component.setBackground(new Color(40, 155, 44));
        break;
      case UNREACHABLE:
        component.setBackground(new Color(188,20,48));
        component.setForeground(Color.WHITE);
        builder.append(Utils.getString(Status.class, "unreachable"));
        break;
      case FAILURE:
        component.setBackground(new Color(255,212,43));
        component.setForeground(Color.BLACK);
        builder.append(Utils.getString(Status.class, "failure"));
        break;
      case UNEXPECTED:
        component.setBackground(new Color(221, 138, 28));
        component.setForeground(Color.BLACK);
        builder.append(Utils.getString(Status.class, "unexpected"));
        break;
      default:
        if (isSelected) {
          component.setBackground(UIManager.getColor("Table.selectionBackground"));
          component.setForeground(UIManager.getColor("Table.selectionForeground"));
        } else {
          component.setBackground(UIManager.getColor("Table.background"));
          component.setForeground(UIManager.getColor("Table.foreground"));
        }
        builder.append(Utils.getString(Status.class, "unknown"));
        break;
    }
    builder.append("</center><html>");
    ((JLabel) component).setText(builder.toString());
    return component;
  }
}
