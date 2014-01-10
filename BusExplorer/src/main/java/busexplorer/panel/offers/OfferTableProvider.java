package busexplorer.panel.offers;

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

  /** �ndice da coluna ID da entidade */
  private static final int ENTITY_ID = 0;
  /** �ndice da coluna Interface */
  private static final int INTERFACE = 1;
  /** �ndice da coluna Data */
  private static final int DATE = 2;
  /** �ndice da coluna Hora */
  private static final int TIME = 3;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames =
      { Utils.getString(this.getClass(), "entity"),
          Utils.getString(this.getClass(), "interface"),
          Utils.getString(this.getClass(), "date"),
          Utils.getString(this.getClass(), "time") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses =
      { String.class, Vector.class, String.class, String.class };
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

      case TIME:
        return offer.getTime();

      default:
        break;
    }
    return null;

  }
}
