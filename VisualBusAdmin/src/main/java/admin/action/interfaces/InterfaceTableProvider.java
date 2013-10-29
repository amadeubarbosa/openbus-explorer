package admin.action.interfaces;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import admin.wrapper.InterfaceWrapper;

/**
 * Provedor de dados para a tabela de Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceTableProvider extends
  VerifiableModifiableObjectTableProvider {

  private static final int ENTITY_NAME = 0;

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Interface" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses = { String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
    InterfaceWrapper interfaceWrapper = (InterfaceWrapper) row;

    String interfaceName = interfaceWrapper.getInterface();

    switch (colIndex) {
      case ENTITY_NAME:
        interfaceName = (String) newValue;
      default:
        break;
    }

  }

  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

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

  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] getCellValues(Object row) {
    final InterfaceWrapper interfaceWrapper = (InterfaceWrapper) row;

    final String interfaceName = interfaceWrapper.getInterface();
    return new Object[] { interfaceName };
  }
}
