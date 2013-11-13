package busexplorer.action.interfaces;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import busexplorer.wrapper.InterfaceWrapper;

/**
 * Provedor de dados para a tabela de Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceTableProvider extends
  VerifiableModifiableObjectTableProvider {

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
  public void setValueAt(Object row, Object newValue, int colIndex) {
    InterfaceWrapper interfaceWrapper = (InterfaceWrapper) row;

    String interfaceName = interfaceWrapper.getInterface();

    switch (colIndex) {
      case INTERFACE_NAME:
        interfaceName = (String) newValue;
      default:
        break;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(Object row, int columnIndex) {
    InterfaceWrapper interfaceWrapper = (InterfaceWrapper) row;
    String interfaceName = interfaceWrapper.getInterface();

    if (!interfaceName.isEmpty()) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getCellValues(Object row) {
    final InterfaceWrapper interfaceWrapper = (InterfaceWrapper) row;

    final String interfaceName = interfaceWrapper.getInterface();
    return new Object[] { interfaceName };
  }
}
