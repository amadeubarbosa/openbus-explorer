package busexplorer.wrapper;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;

public class LoginInfoWrapper implements Identifiable<LoginInfoWrapper> {
  private LoginInfo loginInfo;

  public LoginInfoWrapper(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  @Override
  public Code<LoginInfoWrapper> getId() {
    return new Code<LoginInfoWrapper>(loginInfo.id);
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

}
