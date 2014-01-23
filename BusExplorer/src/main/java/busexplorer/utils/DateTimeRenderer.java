package busexplorer.utils;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderizador de célula que é uma data ({@link Date}) com hora.
 * 
 * @author Tecgraf
 */
public class DateTimeRenderer extends DefaultTableCellRenderer {

  /** Formatador de data */
  DateFormat formatter;

  /**
   * Construtor.
   */
  public DateTimeRenderer() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setValue(Object value) {
    if (formatter == null) {
      formatter = DateFormat.getDateTimeInstance();
    }
    setText((value == null) ? "" : formatter.format(value));
  }
}
