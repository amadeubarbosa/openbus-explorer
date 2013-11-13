package busexplorer.wrapper;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;

public class InterfaceWrapper implements Identifiable<InterfaceWrapper> {
  private String interfaceName;

  public InterfaceWrapper(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  @Override
  public Code<InterfaceWrapper> getId() {
    return new Code<InterfaceWrapper>(interfaceName);
  }

  public String getInterface() {
    return interfaceName;
  }

}
