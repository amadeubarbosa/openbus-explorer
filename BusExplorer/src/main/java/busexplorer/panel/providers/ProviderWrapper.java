package busexplorer.panel.providers;

import busexplorer.utils.Language;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Provider;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class ProviderWrapper {
  private final Provider remote;

  private String name;
  private String code;
  private String supportoffice;
  private String manageroffice;
  private List<String> support;
  private List<String> manager;
  private String busquery;
  private List<String> contracts;

  public ProviderWrapper(Provider remote) {
    this.remote = remote;
    this.name = remote.name();
    this.code = remote.code();
    this.supportoffice = remote.supportoffice();
    this.manageroffice = remote.manageroffice();
    this.support = new ArrayList<>(Arrays.asList(remote.support()));
    this.manager = new ArrayList<>(Arrays.asList(remote.manager()));
    this.busquery = remote.busquery();
    this.contracts = new ArrayList<>();
    for (Contract contract : remote.contracts()) {
      contracts.add(contract.name());
    }
  }

  public static String describe(Provider provider) {
    String space = " ";
    String separator = ";";
    StringBuilder sb = new StringBuilder();
    for (String field : new String[]{"name", "code", "contracts"}) {
      sb.append(Language.get(ProviderInputDialog.class, field + ".label"));
      sb.append(space);
      try {
        Object result = Provider.class.getMethod(field).invoke(provider);
        if (result instanceof String) {
          sb.append((String) result);
          sb.append(separator + space);
        } else if (result instanceof Contract[]) {
          Contract[] contracts = (Contract[]) result;
          for (int i = 0; i < contracts.length; i++) {
            sb.append(space);
            sb.append(contracts[i].name());
            if ((i+1) < contracts.length) sb.append(",");
          }
        }

      } catch (NoSuchMethodException e) {
        throw new IllegalStateException(e);
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      } catch (InvocationTargetException e) {
        throw new IllegalStateException(e);
      }
    }
    return sb.toString();
  }

  public String name() {
    return this.name;
  }
  public void name(String updated) {
    if (!this.remote.name().equals(updated)) {
      this.remote.name(updated);
      this.name = updated;
    }
  }
  public String code () {
    return this.code;
  }

  public void code(String updated) {
    if (!this.remote.code().equals(updated)) {
      this.remote.code(updated);
      this.code = updated;
    }
  }

  public String manageroffice() {
    return manageroffice;
  }

  public void manageroffice(String updated) {
    if (!this.remote.manageroffice().equals(updated)) {
      this.remote.manageroffice(updated);
      this.manageroffice = updated;
    }
  }

  public String supportoffice() {
    return supportoffice;
  }

  public void supportoffice(String updated) {
    if (!this.remote.supportoffice().equals(updated)) {
      this.remote.supportoffice(updated);
      this.supportoffice = updated;
    }
  }

  public List<String> support() {
    return Collections.unmodifiableList(support);
  }

  public void support(List<String> updated) {
    this.remote.support((String[]) updated.toArray());
    this.support = updated;
  }

  public List<String> manager() {
    return Collections.unmodifiableList(manager);
  }

  public void manager(List<String> updated) {
    this.remote.manager((String[]) updated.toArray());
    this.manager = updated;
  }

  public String busquery() {
    return busquery;
  }

  public void busquery(String updated) {
    if (!this.remote.busquery().equals(updated)) {
      this.remote.busquery(updated);
      this.busquery = updated;
    }
  }
  public List<String> contracts() {
    return Collections.unmodifiableList(contracts);
  }
  public void contracts(List<String> updated) {
    // add new interfaces
    for (String iface : updated) {
      if (!this.contracts.contains(iface)) {
        this.remote.addContract(iface);
        this.contracts.add(iface);
      }
    }
    // remove old ones
    ListIterator<String> iterator = this.contracts.listIterator();
    while (iterator.hasNext()) {
      String iface = iterator.next();
      if (!updated.contains(iface)) {
        this.remote.removeContract(iface);
        iterator.remove();
      }
    }
  }
  public Provider remote() {
    return this.remote;
  }
  /**
   * Compara um objeto à instância de {@link ProviderWrapper}.
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
    if (!(o instanceof ProviderWrapper)) {
      return false;
    }
    ProviderWrapper other = (ProviderWrapper) o;
    return name.equals(other.name);
  }

  public static List<ProviderWrapper> convertToInfo(
    List<Provider> entities) {
    List<ProviderWrapper> list = new ArrayList<>();
    for (Provider provider: entities) {
      list.add(new ProviderWrapper(provider));
    }
    return list;
  }

}
