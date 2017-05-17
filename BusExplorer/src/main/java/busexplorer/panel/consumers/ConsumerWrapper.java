package busexplorer.panel.consumers;

import tecgraf.openbus.services.governance.v1_0.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConsumerWrapper {
  private final Consumer remote;

  private String name;
  private String code;
  private String office;
  private List<String> support;
  private List<String> manager;
  private String busquery;

  public ConsumerWrapper(Consumer remote) {
    this.remote = remote;
    this.name = remote.name();
    this.code = remote.code();
    this.office = remote.office();
    this.manager = new ArrayList<>(Arrays.asList(remote.manager()));
    this.support = new ArrayList<>(Arrays.asList(remote.support()));
    this.busquery = remote.busquery();
  }

  public String name() {
    return this.name;
  }

  public void name(String updated) {
    this.remote.name(updated);
    this.name = updated;
  }

  public String code () {
    return this.code;
  }

  public void code(String updated) {
    this.remote.code(updated);
    this.code = updated;
  }

  public String office() {
    return office;
  }

  public void office(String updated) {
    this.remote.office(updated);
    this.office = updated;
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
    this.remote.busquery(updated);
    this.busquery = updated;
  }

  public Consumer remote() {
    return this.remote;
  }

  /**
   * Compara um objeto à instância de {@link ConsumerWrapper}.
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
    if (!(o instanceof ConsumerWrapper)) {
      return false;
    }
    ConsumerWrapper other = (ConsumerWrapper) o;
    return name.equals(other.name);
  }

  /**
   * Método utilitário para converter lista de {@link Consumer} para
   * {@link ConsumerWrapper}
   * 
   * @param consumers a lista de {@link Consumer}
   * @return a lista de {@link ConsumerWrapper}
   */
  public static List<ConsumerWrapper> convertToInfo(
    List<Consumer> consumers) {
    List<ConsumerWrapper> list = new ArrayList<>();
    for (Consumer consumer : consumers) {
      list.add(new ConsumerWrapper(consumer));
    }
    return list;
  }

}
