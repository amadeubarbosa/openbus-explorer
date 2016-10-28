package busexplorer.panel.entities;

import tecgraf.javautils.gui.table.ObjectTableProvider;

import busexplorer.utils.Utils;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class EntityTableProvider implements ObjectTableProvider<EntityWrapper> {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna ID da Categoria */
  private static final int CATEGORY_ID = 1;
  /** Índice da coluna Nome */
  private static final int ENTITY_NAME = 2;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Utils.getString(this.getClass(), "entity"),
      Utils.getString(this.getClass(), "category"),
      Utils.getString(this.getClass(), "description") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class, String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(EntityWrapper row, int col) {
    switch (col) {
      case ENTITY_ID:
        return row.getId();

      case CATEGORY_ID:
        return row.getCategory();

      case ENTITY_NAME:
        return row.getName();
      default:
        break;
    }
    return null;
  }
}
