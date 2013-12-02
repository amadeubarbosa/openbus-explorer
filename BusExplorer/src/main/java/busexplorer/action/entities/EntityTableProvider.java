package busexplorer.action.entities;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.wrapper.EntityInfo;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class EntityTableProvider implements ObjectTableProvider<EntityInfo> {

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
    String[] colNames = { "Entidade", "Categoria", "Descrição" };
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
  public Object getCellValue(EntityInfo row, int col) {
    final EntityInfo entity = row;
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
