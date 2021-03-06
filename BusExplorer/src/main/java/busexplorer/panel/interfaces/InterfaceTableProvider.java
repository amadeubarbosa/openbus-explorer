package busexplorer.panel.interfaces;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceTableProvider implements 
  ObjectTableProvider<InterfaceWrapper> {

  /** �ndice da coluna Nome da entidade. */
  private static final int INTERFACE_NAME = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Language.get(this.getClass(), "interface") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(InterfaceWrapper row, int col) {
    switch (col) {
      case INTERFACE_NAME:
        return row.getName();
      default:
        break;
    }
    return null;
  }
}
