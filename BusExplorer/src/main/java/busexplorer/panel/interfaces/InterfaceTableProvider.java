package busexplorer.panel.interfaces;

import tecgraf.javautils.gui.table.ObjectTableProvider;

import busexplorer.utils.Utils;

/**
 * Provedor de dados para a tabela de Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceTableProvider implements 
  ObjectTableProvider<InterfaceWrapper> {

  /** Índice da coluna Nome da entidade. */
  private static final int INTERFACE_NAME = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { Utils.getString(this.getClass(), "interface") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(InterfaceWrapper row, int col) {
    final InterfaceWrapper interfaceInfo = row;

    switch (col) {
      case INTERFACE_NAME:
        return interfaceInfo.getName();

      default:
        break;
    }
    return null;
  }
}
