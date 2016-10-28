package busexplorer.panel.offers;

import java.util.Date;
import java.util.Vector;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.utils.Utils;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class OfferTableProvider implements ObjectTableProvider<OfferWrapper> {

  /** Índice da coluna ID da entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna Nome */
  private static final int NAME = 1;
  /** Índice da coluna Versão */
  private static final int VERSION = 2;
  /** Índice da coluna Interface */
  private static final int INTERFACE = 3;
  /** Índice da coluna Data */
  private static final int DATE = 4;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[]
      { Utils.getString(this.getClass(), "entity"),
          Utils.getString(this.getClass(), "name"),
          Utils.getString(this.getClass(), "version"),
          Utils.getString(this.getClass(), "interface"),
          Utils.getString(this.getClass(), "date") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class, String.class, Vector
      .class, Date.class };
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
      default:
        break;
    }
    return null;
  }
}
