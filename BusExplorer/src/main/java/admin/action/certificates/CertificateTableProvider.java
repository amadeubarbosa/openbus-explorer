package admin.action.certificates;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import admin.wrapper.IdentifierWrapper;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CertificateTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses = { String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {
    IdentifierWrapper identifierWrapper = (IdentifierWrapper) row;

    String identifier = identifierWrapper.getIdentifier();

    switch (colIndex) {
      case ENTITY_ID:
        identifier = (String) newValue;
        break;
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
    String identifier = ((IdentifierWrapper) row).getIdentifier();

    if (!identifier.isEmpty()) {
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
    final IdentifierWrapper identifierWrapper = (IdentifierWrapper) row;

    final String identifier = identifierWrapper.getIdentifier();
    return new Object[] { identifier };
  }
}
