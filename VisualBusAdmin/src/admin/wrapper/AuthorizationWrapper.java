package admin.wrapper;

import logistic.logic.common.Code;
import logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;

public class AuthorizationWrapper implements Identifiable<AuthorizationWrapper> {
  private RegisteredEntityDesc entity;

  private String interfaceName;

  public AuthorizationWrapper(
    RegisteredEntityDesc entity, String interfaceName) {
    this.entity = entity;
    this.interfaceName = interfaceName;
  }

  @Override
  public Code<AuthorizationWrapper> getId() {
    return new Code<AuthorizationWrapper>(entity.id + interfaceName);
  }

  public RegisteredEntityDesc getEntity() {
    return entity;
  }

  public String getInterface() {
    return interfaceName;
  }
}
