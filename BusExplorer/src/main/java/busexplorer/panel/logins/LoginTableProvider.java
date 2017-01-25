package busexplorer.panel.logins;

import busexplorer.utils.Utils;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Logins
 * 
 * @author Tecgraf
 */
public class LoginTableProvider implements ObjectTableProvider<LoginWrapper> {

  /** Índice da coluna Login */
  private static final int LOGIN_ID = 0;
  /** Índice da coluna Entidade */
  private static final int ENTITY_ID = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Utils.getString(this.getClass(), "login"),
      Utils.getString(this.getClass(), "entity") };
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
  public Object getCellValue(LoginWrapper row, int col) {
    switch (col) {
    case LOGIN_ID:
      return row.getId();

    case ENTITY_ID:
      return row.getEntityId();
    default:
      break;
    }
    return null;
  }
}
