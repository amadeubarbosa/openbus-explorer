package busexplorer.action.categories;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.wrapper.CategoryInfo;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CategoryTableProvider implements ObjectTableProvider<CategoryInfo> {

  /** Índice da coluna Categoria */
  private static final int CATEGORY_ID = 0;
  /** Índice da coluna Descrição */
  private static final int CATEGORY_NAME = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Categoria", "Descrição" };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(CategoryInfo row, int col) {
    final CategoryInfo category = row;
     
    switch (col) {
      case CATEGORY_ID:
        return category.getId();

      case CATEGORY_NAME:
        return category.getName();

      default:
        break;
    }
    return null;
  }
}
