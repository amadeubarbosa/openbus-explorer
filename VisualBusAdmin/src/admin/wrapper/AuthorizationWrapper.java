package admin.wrapper;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

public class AuthorizationWrapper implements Identifiable<AuthorizationWrapper> {
  private RegisteredEntityDesc entity;

  private String interfaceName;

  public AuthorizationWrapper(
    RegisteredEntityDesc entity, String interfaceName) {
    this.entity = entity;
    this.interfaceName = interfaceName;
  }

  public boolean equals(AuthorizationWrapper w) {
    if (entity.id.equals(w.entity.id)
      && interfaceName.equals(w.interfaceName)) {
      return true;
    }
    return false;
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
