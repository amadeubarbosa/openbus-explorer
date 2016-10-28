package busexplorer.panel.authorizations;

import tecgraf.javautils.gui.table.ObjectTableProvider;

import busexplorer.utils.Utils;

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
    return new String[] { Utils.getString(this.getClass(), "entity"),
      Utils.getString(this.getClass(), "interface") };
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
  public Object getCellValue(AuthorizationWrapper row, int col) {
    switch (col) {
      case ENTITY_ID:
        return row.getEntityId();

      case INTERFACE:
        return row.getInterface();

      default:
        break;
    }
    return null;
  }
}
