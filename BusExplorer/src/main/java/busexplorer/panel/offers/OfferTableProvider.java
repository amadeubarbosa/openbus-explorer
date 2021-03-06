package busexplorer.panel.offers;

import busexplorer.utils.Availability;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

import java.util.Date;
import java.util.Vector;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class OfferTableProvider implements ObjectTableProvider<OfferWrapper> {

  /** �ndice da coluna ID da entidade */
  private static final int ENTITY_ID = 0;
  /** �ndice da coluna Nome */
  private static final int NAME = 1;
  /** �ndice da coluna Vers�o */
  private static final int VERSION = 2;
  /** �ndice da coluna Interface */
  private static final int INTERFACE = 3;
  /** �ndice da coluna Endpoints */
  private static final int ENDPOINTS = 4;
  /** �ndice da coluna Data */
  private static final int DATE = 5;
  /** �ndice da coluna Disponibilidade */
  private static final int STATUS = 6;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[]
      { Language.get(this.getClass(), "entity"),
          Language.get(this.getClass(), "name"),
          Language.get(this.getClass(), "version"),
          Language.get(this.getClass(), "interface"),
          Language.get(this.getClass(), "endpoints"),
          Language.get(this.getClass(), "date"),
          Language.get(this.getClass(), "status")
      };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = {
        String.class, String.class, String.class,
        Vector.class, Vector.class, Date.class, Availability.class
    };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(OfferWrapper row, int col) {
    switch (col) {
      case ENTITY_ID:
        return row.getEntityId();
      case INTERFACE:
        return row.getInterfaces();
      case DATE:
        return row.getDate();
      case NAME:
    	return row.getName();
      case VERSION:
      	return row.getVersion();
      case ENDPOINTS:
        return row.getEndpoints();
      case STATUS:
        return row.getStatus();
      default:
        break;
    }
    return null;
  }
}
