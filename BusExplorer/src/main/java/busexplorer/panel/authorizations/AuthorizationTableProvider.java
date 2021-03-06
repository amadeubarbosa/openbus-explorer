package busexplorer.panel.authorizations;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Autoriza��es
 * 
 * @author Tecgraf
 */
public class AuthorizationTableProvider implements
  ObjectTableProvider<AuthorizationWrapper> {

  /** �ndice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** �ndice da coluna Interface */
  private static final int INTERFACE = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Language.get(this.getClass(), "entity"),
      Language.get(this.getClass(), "interface") };
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
