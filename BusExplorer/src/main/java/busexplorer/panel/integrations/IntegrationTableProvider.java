package busexplorer.panel.integrations;

import busexplorer.utils.Utils;
import tecgraf.javautils.gui.table.ObjectTableProvider;

import java.util.Vector;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class IntegrationTableProvider implements ObjectTableProvider<IntegrationWrapper> {

  private static final int CONSUMER = 0;
  private static final int PROVIDER = 1;
  private static final int CONTRACTS = 2;
  private static final int ACTIVATED = 3;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Utils.getString(this.getClass(), "consumer"),
      Utils.getString(this.getClass(), "provider"),
      Utils.getString(this.getClass(), "contracts"),
      Utils.getString(this.getClass(), "activated")};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class, Vector.class, Boolean.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(IntegrationWrapper row, int col) {
    switch (col) {
      case CONSUMER:
        return row.consumer();

      case PROVIDER:
        return row.provider();

      case CONTRACTS:
        return row.contracts();

      case ACTIVATED:
        return row.isActivated();
      default:
        break;
    }
    return null;
  }
}
