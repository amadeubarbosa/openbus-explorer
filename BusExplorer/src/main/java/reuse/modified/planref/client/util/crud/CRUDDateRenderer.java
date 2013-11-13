package reuse.modified.planref.client.util.crud;

import java.text.DateFormat;
import java.util.Date;

import reuse.modified.planref.client.util.crud.CRUDCellRenderer;
import reuse.modified.planref.client.util.datetime.DateTimeUtil;

/**
 * Renderer para datas.
 */
public class CRUDDateRenderer extends CRUDCellRenderer {
  /** Formatador */
  DateFormat formatter;

  /**
   * Construtor.
   */
  public CRUDDateRenderer() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    setText((value == null) ? "" : DateTimeUtil.getDateString((Date) value));
  }
}
