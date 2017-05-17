package tecgraf.openbus.extension;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import scs.core.IComponent;
import tecgraf.openbus.OfferRegistry;
import tecgraf.openbus.RemoteOffer;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.services.governance.v1_0.Consumer;
import tecgraf.openbus.services.governance.v1_0.ConsumerRegistry;
import tecgraf.openbus.services.governance.v1_0.ConsumerRegistryHelper;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.ContractRegistry;
import tecgraf.openbus.services.governance.v1_0.ContractRegistryHelper;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.IntegrationRegistry;
import tecgraf.openbus.services.governance.v1_0.IntegrationRegistryHelper;
import tecgraf.openbus.services.governance.v1_0.Provider;
import tecgraf.openbus.services.governance.v1_0.ProviderRegistry;
import tecgraf.openbus.services.governance.v1_0.ProviderRegistryHelper;
import tecgraf.openbus.services.governance.v1_0.ServiceName;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Tecgraf
 */
public class BusExtensionImpl implements BusExtensionFacade {

  private OfferRegistry offers;

  private Cache<String, IComponent> cachedReferences;

  public BusExtensionImpl(OfferRegistry offers) {
    cachedReferences = CacheBuilder.newBuilder()
      .maximumSize(10)
      .build();
    this.offers = offers;
  }

  private IComponent service() throws ServiceFailure {
    try {
      return cachedReferences.get(ServiceName.value, () -> {
        ArrayListMultimap<String, String> props = ArrayListMultimap.create();
        props.put("openbus.component.name", ServiceName.value);

        List<RemoteOffer> available = offers.findServices(props);
        available.removeIf(remoteOffer -> {
          try {
            remoteOffer.service().getComponentId();
          } catch (Exception e) {
            return true;
          }
          return false;
        });
        return available.get(0).service();
      });
    } catch (ExecutionException e) {
      throw new ServiceFailure(e.getMessage());
    }
  }

  public boolean isExtensionCapable() {
    try {
      getConsumerRegistry();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  public ContractRegistry getContractRegistry() throws ServiceFailure {
    return ContractRegistryHelper.narrow(service().getFacet(ContractRegistryHelper.id()));
  }
  public ProviderRegistry getProviderRegistry() throws ServiceFailure {
    return ProviderRegistryHelper.narrow(service().getFacet(ProviderRegistryHelper.id()));
  }
  public ConsumerRegistry getConsumerRegistry() throws ServiceFailure {
    return ConsumerRegistryHelper.narrow(service().getFacet(ConsumerRegistryHelper.id()));
  }
  public IntegrationRegistry getIntegrationRegistry() throws ServiceFailure {
    return IntegrationRegistryHelper.narrow(service().getFacet(IntegrationRegistryHelper.id()));
  }

  @Override
  public List<Contract> getContracts() throws ServiceFailure {
      return Arrays.asList(getContractRegistry().contracts());
  }

  @Override
  public List<Provider> getProviders() throws ServiceFailure {
    return Arrays.asList(getProviderRegistry().providers());
  }

  @Override
  public List<Consumer> getConsumers() throws ServiceFailure {
    return Arrays.asList(getConsumerRegistry().consumers());
  }

  @Override
  public List<Integration> getIntegrations() throws ServiceFailure {
    return Arrays.asList(getIntegrationRegistry().integrations());
  }
}
