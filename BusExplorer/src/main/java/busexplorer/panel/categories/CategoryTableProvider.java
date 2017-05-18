package busexplorer.panel.categories;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CategoryTableProvider implements ObjectTableProvider<CategoryWrapper> {

  /** Índice da coluna Categoria */
  private static final int CATEGORY_ID = 0;
  /** Índice da coluna Descrição */
  private static final int CATEGORY_NAME = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Language.get(this.getClass(), "category"),
      Language.get(this.getClass(), "description") };
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
