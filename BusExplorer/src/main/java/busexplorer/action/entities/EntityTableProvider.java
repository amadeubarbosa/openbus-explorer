package busexplorer.action.entities;

import busexplorer.wrapper.RegisteredEntityDescWrapper;
import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class EntityTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna ID da Categoria */
  private static final int CATEGORY_ID = 1;
  /** Índice da coluna Nome */
  private static final int ENTITY_NAME = 2;

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade", "ID Categoria", "Nome" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class, String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
    RegisteredEntityDescWrapper entityWrapper =
      (RegisteredEntityDescWrapper) row;

    RegisteredEntityDesc entityDesc = entityWrapper.getRegisteredEntityDesc();
    String categoryID = entityWrapper.getCategoryID();

    switch (colIndex) {
      case ENTITY_ID:
        entityDesc.id = (String) newValue;
        break;
      case CATEGORY_ID:
        categoryID = (String) newValue;
        break;
      case ENTITY_NAME:
        entityDesc.name = (String) newValue;
      default:
        break;
    }

  }

  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public boolean isValid(Object row, int columnIndex) {
    RegisteredEntityDescWrapper rowWrapper = (RegisteredEntityDescWrapper) row;
    RegisteredEntityDesc entity =
      ((RegisteredEntityDescWrapper) row).getRegisteredEntityDesc();

    if (!entity.id.isEmpty() && !rowWrapper.getCategoryID().isEmpty()
      && !entity.name.isEmpty()) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] getCellValues(Object row) {
    final RegisteredEntityDescWrapper entityWrapper =
      (RegisteredEntityDescWrapper) row;

    final RegisteredEntityDesc entityDesc =
      entityWrapper.getRegisteredEntityDesc();
    return new Object[] { entityDesc.id, entityWrapper.getCategoryID(),
        entityDesc.name };
  }
}
