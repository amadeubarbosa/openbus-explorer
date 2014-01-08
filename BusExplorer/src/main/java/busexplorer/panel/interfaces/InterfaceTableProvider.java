package busexplorer.panel.interfaces;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.wrapper.InterfaceInfo;

/**
 * Provedor de dados para a tabela de Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceTableProvider implements 
  ObjectTableProvider<InterfaceInfo> {

  /** Índice da coluna Nome da entidade. */
  private static final int INTERFACE_NAME = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Interface" };
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
  public Object getCellValue(InterfaceInfo row, int col) {
    final InterfaceInfo interfaceInfo = row;

    switch (col) {
      case INTERFACE_NAME:
        return interfaceInfo.getName();

      default:
        break;
    }
    return null;
  }
}
