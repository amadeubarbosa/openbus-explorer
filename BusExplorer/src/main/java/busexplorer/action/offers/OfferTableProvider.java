package busexplorer.action.offers;

import tecgraf.javautils.gui.table.ObjectTableProvider;
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
  /** Índice da coluna Hora */
  private static final int TIME = 3;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Entidade", "Interface", "Data", "Hora" };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses =
      { String.class, String.class, String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(OfferInfo row, int col) {
    final OfferInfo offer = (OfferInfo) row;
    switch (col) {
      case ENTITY_ID:
        return offer.getEntityId();

      case INTERFACE:
        return offer.getInterfaceName();

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
