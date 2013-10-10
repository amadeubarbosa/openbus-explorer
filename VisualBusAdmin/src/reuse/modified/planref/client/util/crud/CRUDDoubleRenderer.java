package reuse.modified.planref.client.util.crud;

import java.text.NumberFormat;

/**
 * Renderer para números de ponto flutuante.
 */
public class CRUDDoubleRenderer extends CRUDNumberRenderer {
  /** Formatador */
  protected NumberFormat formatter;

  /**
   * Construtor.
   */
  public CRUDDoubleRenderer() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    if (formatter == null) {
      formatter = NumberFormat.getInstance();
      formatter.setMaximumFractionDigits(2);
    }
    setText((value == null) ? "" : formatter.format(value));
  }
}
