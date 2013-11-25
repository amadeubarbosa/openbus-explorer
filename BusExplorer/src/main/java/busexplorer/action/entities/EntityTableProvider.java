package busexplorer.action.entities;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class EntityTableProvider implements
  ObjectTableProvider<RegisteredEntityDesc> {

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
    String[] colNames = { "Entidade", "Categoria", "Nome" };
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
  public Object getCellValue(RegisteredEntityDesc row, int col) {
    final RegisteredEntityDesc entityDesc = row;
    switch (col) {
      case ENTITY_ID:
        return entityDesc.id;

      case CATEGORY_ID:
        // FIXME: chamada remota na EDT. Precisa mudar o tipo de elemento
        return entityDesc.category.id();

      case ENTITY_NAME:
        return entityDesc.name;

      default:
        break;
    }
    return null;
  }
}
