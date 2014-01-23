package busexplorer.panel.offers;

import java.util.Date;
import java.util.Vector;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.utils.Utils;
import busexplorer.wrapper.OfferInfo;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class OfferTableProvider implements ObjectTableProvider<OfferInfo> {

  /** Índice da coluna ID da entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna Interface */
  private static final int INTERFACE = 1;
  /** Índice da coluna Data */
  private static final int DATE = 2;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames =
      { Utils.getString(this.getClass(), "entity"),
          Utils.getString(this.getClass(), "interface"),
          Utils.getString(this.getClass(), "date") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, Vector.class, Date.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(OfferInfo row, int col) {
    final OfferInfo offer = row;
    switch (col) {
      case ENTITY_ID:
        return offer.getEntityId();

      case INTERFACE:
        return offer.getInterfaces();

      case DATE:
        return offer.getDate();

      default:
        break;
    }
    return null;

  }
}
