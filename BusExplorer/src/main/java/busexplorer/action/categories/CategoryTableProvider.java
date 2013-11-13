package busexplorer.action.categories;

import busexplorer.wrapper.EntityCategoryDescWrapper;
import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CategoryTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /** Índice da coluna ID da Categoria */
  private static final int CATEGORY_ID = 0;
  /** Índice da coluna Nome */
  private static final int CATEGORY_NAME = 1;

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Categoria", "Nome" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
    EntityCategoryDescWrapper categoryWrapper = (EntityCategoryDescWrapper) row;

    EntityCategoryDesc categoryDesc = categoryWrapper.getEntityCategoryDesc();

    switch (colIndex) {
      case CATEGORY_ID:
        categoryDesc.id = (String) newValue;
        break;
      case CATEGORY_NAME:
        categoryDesc.name = (String) newValue;
        break;
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
    EntityCategoryDesc category =
      ((EntityCategoryDescWrapper) row).getEntityCategoryDesc();

    if (!category.id.isEmpty() && !category.name.isEmpty()) {
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
    final EntityCategoryDescWrapper categoryWrapper =
      (EntityCategoryDescWrapper) row;

    final EntityCategoryDesc categoryDesc =
      categoryWrapper.getEntityCategoryDesc();
    return new Object[] { categoryDesc.id, categoryDesc.name };
  }
}
