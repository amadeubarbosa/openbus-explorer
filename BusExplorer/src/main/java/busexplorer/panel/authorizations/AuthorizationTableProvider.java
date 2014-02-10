package busexplorer.panel.authorizations;

import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Autorizações
 * 
 * @author Tecgraf
 */
public class AuthorizationTableProvider implements
  ObjectTableProvider<AuthorizationWrapper> {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna Interface */
  private static final int INTERFACE = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Entidade", "Interface" };
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
  public Object getCellValue(AuthorizationWrapper row, int col) {
    final AuthorizationWrapper authorization = row;
    switch (col) {
      case ENTITY_ID:
        return authorization.getEntityId();

      case INTERFACE:
        return authorization.getInterface();

      default:
        break;
    }
    return null;
  }
}
