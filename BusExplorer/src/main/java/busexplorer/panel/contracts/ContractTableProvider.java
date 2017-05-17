package busexplorer.panel.contracts;

import busexplorer.utils.Utils;
import tecgraf.javautils.gui.table.ObjectTableProvider;

import java.util.Vector;

/**
 * Provedor de dados para a tabela de Entidades
 * 
 * @author Tecgraf
 */
public class ContractTableProvider implements ObjectTableProvider<ContractWrapper> {

  private static final int NAME = 0;
  private static final int INTERFACES = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Utils.getString(this.getClass(), "name"),
      Utils.getString(this.getClass(), "interfaces") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, Vector.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(ContractWrapper row, int col) {
    switch (col) {
      case NAME:
        return row.name();

      case INTERFACES:
        return row.interfaces();

      default:
        break;
    }
    return null;
  }
}
