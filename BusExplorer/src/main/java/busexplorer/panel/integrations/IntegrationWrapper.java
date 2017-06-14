package busexplorer.panel.integrations;

import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class IntegrationWrapper {
  private final List<String> contracts;
  private final Integration remote;

  private boolean activated;
  private String consumer;
  private String provider;

  public IntegrationWrapper(Integration remote) {
    this.remote = remote;
    this.consumer = remote.consumer() == null ? "" : remote.consumer().name();
    this.provider = remote.provider() == null ? "" : remote.provider().name();
    this.contracts = new ArrayList<>();
    for (Contract contract : remote.contracts()) {
      contracts.add(contract.name());
    }
    this.activated = remote.activated();
  }

  public String consumer() {
    return consumer;
  }
  public String provider() {
    return provider;
  }
  public void consumer(ConsumerWrapper consumer) {
    if (!consumer.name().equals(this.consumer)) {
      this.consumer = consumer.name();
      this.remote.consumer(consumer.remote());
    }
  }
  public void provider(ProviderWrapper provider) {
    if (!provider.name().equals(this.provider)) {
      this.provider = provider.name();
      this.remote.provider(provider.remote());
    }
  }
  public Boolean isActivated() {
    return this.activated;
  }
  public void activate(Boolean b) {
    if (b != this.activated) {
      this.activated = b;
      this.remote.activated(b);
    }
  }
  public List<String> contracts() {
    return Collections.unmodifiableList(contracts);
  }
  public void contracts(List<String> updated, boolean shouldAddContractToProvider) {
    ArrayList<String> rollback = new ArrayList<>();
    // add new contracts
    for (String contract : updated) {
      if (!this.contracts.contains(contract)) {
        if (this.remote.addContract(contract) == false) {
          if (shouldAddContractToProvider) {
            if (this.remote.provider().addContract(contract) == false) {
              for (String revert : rollback) {
                this.remote.provider().removeContract(revert);
              }
              throw new IllegalArgumentException(contract);
            }
            rollback.add(contract);
            if (this.remote.addContract(contract) == false) {
              throw new IllegalStateException(contract);
            }
          } else {
            throw new IllegalArgumentException(contract);
          }
        }
        this.contracts.add(contract);
      }
    }
    // remove old ones
    ListIterator<String> iterator = this.contracts.listIterator();
    while (iterator.hasNext()) {
      String contract = iterator.next();
      if (!updated.contains(contract)) {
        this.remote.removeContract(contract);
        iterator.remove();
      }
    }
  }

  public Integration remote() {
    return this.remote;
  }
  /**
   * Compara um objeto à instância de {@link IntegrationWrapper}.
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
    if (!(o instanceof IntegrationWrapper)) {
      return false;
    }
    IntegrationWrapper other = (IntegrationWrapper) o;
    return consumer.equals(other.consumer) &&
      provider.equals(other.provider) && (activated == other.activated) &&
      contracts.equals(other.contracts);
  }

  public static List<IntegrationWrapper> convertToInfo(
    List<Integration> integrations) {
    List<IntegrationWrapper> list = new ArrayList<>();
    for (Integration integration : integrations) {
      list.add(new IntegrationWrapper(integration));
    }
    return list;
  }
}
