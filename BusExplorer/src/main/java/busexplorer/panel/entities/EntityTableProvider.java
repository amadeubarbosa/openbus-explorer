package busexplorer.panel.entities;

import busexplorer.utils.Utils;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class EntityTableProvider implements ObjectTableProvider<EntityWrapper> {

  /** �ndice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** �ndice da coluna ID da Categoria */
  private static final int CATEGORY_ID = 1;
  /** �ndice da coluna Nome */
  private static final int ENTITY_NAME = 2;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { Utils.getString(this.getClass(), "entity"),
      Utils.getString(this.getClass(), "category"),
      Utils.getString(this.getClass(), "description") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(EntityWrapper row, int col) {
    final EntityWrapper entity = row;
    switch (col) {
      case ENTITY_ID:
        return entity.getId();

      case CATEGORY_ID:
        return entity.getCategory();

      case ENTITY_NAME:
        return entity.getName();

      default:
        break;
    }
    return null;
  }
}
