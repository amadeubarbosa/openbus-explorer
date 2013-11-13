package busexplorer.action.logins;

import busexplorer.wrapper.LoginInfoWrapper;
import reuse.modified.planref.client.util.crud.VerifiableModifiableObjectTableProvider;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;

/**
 * Provedor de dados para a tabela de Logins
 * 
 * @author Tecgraf
 */
public class LoginTableProvider extends VerifiableModifiableObjectTableProvider {

  @Override
  public String[] getColumnNames() {
    String[] colNames = { "ID Login", "ID Entidade" };
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
    return false;
  }

  @Override
  public String getTooltip(int columnIndex) {
    return null;
  }

  @Override
  public Object[] getCellValues(Object row) {
    final LoginInfoWrapper loginInfoWrapper = (LoginInfoWrapper) row;

    final LoginInfo loginInfo = loginInfoWrapper.getLoginInfo();
    return new Object[] { loginInfo.id, loginInfo.entity };
  }
}
