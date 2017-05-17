package tecgraf.openbus.extension;

import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.services.governance.v1_0.Consumer;
import tecgraf.openbus.services.governance.v1_0.ConsumerRegistry;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.ContractRegistry;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.IntegrationRegistry;
import tecgraf.openbus.services.governance.v1_0.Provider;
import tecgraf.openbus.services.governance.v1_0.ProviderRegistry;

import java.util.List;

/**
 * Essa interface representa os comandos para utilizar o
 * Serviço de Extensão à Governança do barramento.
 *
 * @author Tecgraf
 */
public interface BusExtensionFacade {
  //test if the service is offered
  boolean isExtensionCapable();

  //collections
  List<Contract> getContracts() throws ServiceFailure;
  List<Provider> getProviders() throws ServiceFailure;
  List<Consumer> getConsumers() throws ServiceFailure;
  List<Integration> getIntegrations() throws ServiceFailure;

  //services
  public ContractRegistry getContractRegistry() throws ServiceFailure;
  public ProviderRegistry getProviderRegistry() throws ServiceFailure;
  public ConsumerRegistry getConsumerRegistry() throws ServiceFailure;
  public IntegrationRegistry getIntegrationRegistry() throws ServiceFailure;
}
