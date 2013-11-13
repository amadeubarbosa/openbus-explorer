package busexplorer.action.authorizations;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import busexplorer.wrapper.AuthorizationWrapper;

/**
 * Provedor de dados para a tabela de Autorizações
 * 
 * @author Tecgraf
 */
public class AuthorizationTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade", "Interface" };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {

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
    return true;
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
    final AuthorizationWrapper authorizationWrapper =
      (AuthorizationWrapper) row;

    RegisteredEntityDesc entity = authorizationWrapper.getEntity();
    String interfaceName = authorizationWrapper.getInterface();

    return new Object[] { entity.id, interfaceName };
  }
}
