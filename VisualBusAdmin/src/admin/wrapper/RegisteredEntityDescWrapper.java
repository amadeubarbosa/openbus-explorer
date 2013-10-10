package admin.wrapper;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

public class RegisteredEntityDescWrapper implements
  Identifiable<RegisteredEntityDescWrapper> {
  private RegisteredEntityDesc registeredEntityDesc;
  private String categoryID;

  public RegisteredEntityDescWrapper(RegisteredEntityDesc registeredEntityDesc,
    String categoryID) {
    this.registeredEntityDesc = registeredEntityDesc;
    this.categoryID = categoryID;
  }

  @Override
  public Code<RegisteredEntityDescWrapper> getId() {
    return new Code<RegisteredEntityDescWrapper>(registeredEntityDesc.id);
  }

  public RegisteredEntityDesc getRegisteredEntityDesc() {
    return registeredEntityDesc;
  }

  public String getCategoryID() {
    return categoryID;
  }

}
