package admin.wrapper;

import logistic.logic.common.Code;
import logistic.logic.common.Identifiable;

public class IdentifierWrapper implements Identifiable<IdentifierWrapper> {
  private String identifier;

  public IdentifierWrapper(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public Code<IdentifierWrapper> getId() {
    return new Code<IdentifierWrapper>(identifier);
  }

  public String getIdentifier() {
    return identifier;
  }
}
