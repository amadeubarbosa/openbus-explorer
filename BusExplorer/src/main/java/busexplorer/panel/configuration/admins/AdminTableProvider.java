package busexplorer.panel.configuration.admins;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Administradores
 * 
 * @author Tecgraf
 */
public class AdminTableProvider implements ObjectTableProvider<AdminWrapper> {

  /** Índice da coluna Descrição */
  private static final int ADMIN_NAME = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[]{ Language.get(this.getClass(), "administrator") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[]{ String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(AdminWrapper row, int col) {
    switch (col) {
      case ADMIN_NAME:
        return row.getAdmin();

      default:
        break;
    }
    return null;
  }
}
