package admin.action.authorizations;

import java.util.List;
import java.util.Map;

import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.wrapper.AuthorizationWrapper;

/**
 * Provedor de dados para a tabela de Autorizações
 * 
 * @author Tecgraf
 */
public class AuthorizationTableProvider extends
  VerifiableModifiableObjectTableProvider {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;
  /** Índice da coluna Interfaces */
  private static final int INTERFACES = 1;

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Entidade", "Interface" };
    return colNames;
  }

  @Override
  public Class[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class };
    return colClasses;
  }

  @Override
  public void setValueAt(Object row, Object newValue, int colIndex) {

  }

  @Override
  public boolean isCellEditable(Object row, int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public boolean isValid(Object row, int columnIndex) {
    return true;
  }

  @Override
  public String getTooltip(int columnIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] getCellValues(Object row) {
    final AuthorizationWrapper authorizationWrapper =
      (AuthorizationWrapper) row;

    RegisteredEntityDesc entity = authorizationWrapper.getEntity();
    String interfaceName = authorizationWrapper.getInterface();

    return new Object[] { entity.id, interfaceName };
  }
}
