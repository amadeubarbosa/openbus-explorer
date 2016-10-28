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
    return new String[] { Utils.getString(this.getClass(), "category"),
      Utils.getString(this.getClass(), "description") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(CategoryWrapper row, int col) {
    switch (col) {
      case CATEGORY_ID:
        return row.getId();

      case CATEGORY_NAME:
        return row.getName();

      default:
        break;
    }
    return null;
  }
}
