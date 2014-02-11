package busexplorer.panel.categories;

import tecgraf.javautils.gui.table.ObjectTableProvider;

import busexplorer.utils.Utils;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CategoryTableProvider implements ObjectTableProvider<CategoryWrapper> {

  /** �ndice da coluna Categoria */
  private static final int CATEGORY_ID = 0;
  /** �ndice da coluna Descri��o */
  private static final int CATEGORY_NAME = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { Utils.getString(this.getClass(), "category"),
      Utils.getString(this.getClass(), "description") };
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
  public Object getCellValue(CategoryWrapper row, int col) {
    final CategoryWrapper category = row;
     
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
