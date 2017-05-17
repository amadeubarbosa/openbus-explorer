package busexplorer.panel.contracts;

import tecgraf.openbus.services.governance.v1_0.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class ContractWrapper {
  private final Contract remote;

  private String name;
  private List<String> interfaces;

  public ContractWrapper(Contract remote) {
    this.remote = remote;
    this.name = remote.name();
    this.interfaces = new ArrayList<>(Arrays.asList(remote.interfaces()));
  }

  public String name() {
    return this.name;
  }

  public List<String> interfaces() {
    return Collections.unmodifiableList(interfaces);
  }

  public void interfaces(List<String> updated) {
    // add new interfaces
    for (String iface : updated) {
      if (!this.interfaces.contains(iface)) {
        this.remote.addInterface(iface);
        this.interfaces.add(iface);
      }
    }
    // remove old ones
    ListIterator<String> iterator = this.interfaces.listIterator();
    while (iterator.hasNext()) {
      String iface = iterator.next();
      if (!updated.contains(iface)) {
        this.remote.removeInterface(iface);
        iterator.remove();
      }
    }
  }

  public Contract remote() {
    return this.remote;
  }
  /**
   * Compara um objeto à instância de {@link ContractWrapper}.
   *
   * O método não leva em consideração o objeto remoto.
   * 
   * @param o Objeto a ser comparado.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ContractWrapper)) {
      return false;
    }
    ContractWrapper other = (ContractWrapper) o;
    return name.equals(other.name);
  }

  /**
   * Método utilitário para converter lista de {@link Contract} para
   * {@link ContractWrapper}
   * 
   * @param contracts a lista de {@link Contract}
   * @return a lista de {@link ContractWrapper}
   */
  public static List<ContractWrapper> convertToInfo(
    List<Contract> contracts) {
    List<ContractWrapper> list = new ArrayList<>();
    for (Contract contract : contracts) {
      list.add(new ContractWrapper(contract));
    }
    return list;
  }

}
