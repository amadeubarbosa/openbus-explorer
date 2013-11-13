package busexplorer.wrapper;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;

public class EntityCategoryDescWrapper implements
  Identifiable<EntityCategoryDescWrapper> {
  private EntityCategoryDesc entityCategoryDesc;

  public EntityCategoryDescWrapper(EntityCategoryDesc entityCategoryDesc) {
    this.entityCategoryDesc = entityCategoryDesc;
  }

  @Override
  public Code<EntityCategoryDescWrapper> getId() {
    return new Code<EntityCategoryDescWrapper>(entityCategoryDesc.id);
  }

  public EntityCategoryDesc getEntityCategoryDesc() {
    return entityCategoryDesc;
  }

}
