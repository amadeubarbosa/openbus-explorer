package admin.wrapper;

import java.util.List;
import java.util.Map;

import logistic.logic.common.Code;
import logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;

public class AuthorizationWrapper implements Identifiable<AuthorizationWrapper> {
  private Map.Entry<RegisteredEntityDesc, List<String>> authorization;

  public AuthorizationWrapper(
    Map.Entry<RegisteredEntityDesc, List<String>> authorization) {
    this.authorization = authorization;
  }

  @Override
  public Code<AuthorizationWrapper> getId() {
    return new Code<AuthorizationWrapper>(authorization.getKey().id
      + authorization.getValue());
  }

  public Map.Entry<RegisteredEntityDesc, List<String>> getAuthorization() {
    return authorization;
  }

}
