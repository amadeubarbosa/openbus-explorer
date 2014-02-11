package busexplorer.panel.logins;

import tecgraf.javautils.gui.table.ObjectTableProvider;

import busexplorer.utils.Utils;

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
    String[] colNames = { Utils.getString(this.getClass(), "login"),
      Utils.getString(this.getClass(), "entity") };
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
  public Object getCellValue(LoginWrapper row, int col) {
    final LoginWrapper login = row;
    switch (col) {
    case LOGIN_ID:
      return login.getId();

    case ENTITY_ID:
      return login.getEntityId();

    default:
      break;
    }
    return null;
  }
}
