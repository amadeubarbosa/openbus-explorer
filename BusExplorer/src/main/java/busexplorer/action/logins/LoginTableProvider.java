package busexplorer.action.logins;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.wrapper.LoginInfoInfo;

/**
 * Provedor de dados para a tabela de Logins
 * 
 * @author Tecgraf
 */
public class LoginTableProvider implements ObjectTableProvider<LoginInfoInfo> {

  /** Índice da coluna Login */
  private static final int LOGIN_ID = 0;
  /** Índice da coluna Entidade */
  private static final int ENTITY_ID = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Login", "Entidade" };
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
  public Object getCellValue(LoginInfoInfo row, int col) {
    final LoginInfoInfo login = row;
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
