package tecgraf.openbus.extension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;

import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.OfferObserver;
import tecgraf.openbus.OfferRegistry;
import tecgraf.openbus.RemoteOffer;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.ServiceFailureHelper;
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

import scs.core.IComponent;

/**
 * Implementação da fachada para o Serviço de Extensão à Governança.
 * <p>
 * A {@link BusExtensionImpl} utiliza um {@link Cache} de referências para as interfaces
 * {@link ContractRegistry}, {@link ProviderRegistry}, {@link ConsumerRegistry} e {@link IntegrationRegistry}.
 * Quando a referência remota dessas interfaces não está responsiva acontece um falta no cache e será realizada
 * uma nova busca do Serviço de Extensão à Governança para atualizar as referências cacheadas.
 * Caso essa nova busca não tenha sucesso os métodos de acesso a serviço lançarão {@link ServiceFailure}.
 * <p>
 * Para realizar as buscas do Serviço de Extensão à Governança são usadas as constantes
 * {@link BusExtensionImpl#SEARCH_CRITERIA_KEY} e {@link BusExtensionImpl#SEARCH_CRITERIA_VALUE}.
 *
 * @author Tecgraf
 */
public class BusExtensionImpl implements BusExtensionFacade {

  public static final String SEARCH_CRITERIA_KEY = "openbus.component.name";
  public static final String SEARCH_CRITERIA_VALUE = ServiceName.value;
  private OfferRegistry offers;
  private Cache<String, IComponent> cachedReferences;

  /**
   * Construtor da fachada a partir do registro de ofertas {@link OfferRegistry} fornecido pela
   * biblioteca do OpenBus SDK Java para buscar a referência para o Serviço de Extensão à Governança.
   *
   * @param offers Referência para o Registro de Oferta fornecido pela biblioteca do OpenBus SDK Java
   */
  public BusExtensionImpl(OfferRegistry offers) {
    cachedReferences = CacheBuilder.newBuilder()
      .maximumSize(10)
      .build();
    this.offers = offers;
  }

  private IComponent service() throws ServiceFailure {
    try {
      return cachedReferences.get(SEARCH_CRITERIA_VALUE, () -> {
        ArrayListMultimap<String, String> props = ArrayListMultimap.create();
        props.put(SEARCH_CRITERIA_KEY, SEARCH_CRITERIA_VALUE);

        List<RemoteOffer> available = offers.findServices(props);
        available.removeIf(remoteOffer -> {
          try {
            remoteOffer.service().getComponentId();
          } catch (Exception e) {
            return true;
          }
          return false;
        });
        if (available.size() == 0) {
          throw new ServiceFailure(LNG.get("ServiceFailure.not.found",
            new String[]{SEARCH_CRITERIA_VALUE,
              offers.connection().busId(),
              SEARCH_CRITERIA_KEY
            }));
        }
        RemoteOffer firstAvailable = available.get(0);
        firstAvailable.subscribeObserver(new OfferObserver() {
          @Override
          public void propertiesChanged(RemoteOffer offer) {
          }

          @Override
          public void removed(RemoteOffer offer) {
            cachedReferences.invalidateAll();
          }
        });
        return firstAvailable.service();
      });
    } catch (ExecutionException e) {
      if (e.getCause() instanceof ServiceFailure) {
        throw (ServiceFailure) e.getCause();
      } else {
        throw new ServiceFailure(ServiceFailureHelper.id(), e.getMessage());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean isExtensionCapable() {
    try {
      getConsumerRegistry();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  public ContractRegistry getContractRegistry() throws ServiceFailure {
    return ContractRegistryHelper.narrow(service().getFacet(ContractRegistryHelper.id()));
  }

  /**
   * {@inheritDoc}
   */
  public ProviderRegistry getProviderRegistry() throws ServiceFailure {
    return ProviderRegistryHelper.narrow(service().getFacet(ProviderRegistryHelper.id()));
  }

  /**
   * {@inheritDoc}
   */
  public ConsumerRegistry getConsumerRegistry() throws ServiceFailure {
    return ConsumerRegistryHelper.narrow(service().getFacet(ConsumerRegistryHelper.id()));
  }

  /**
   * {@inheritDoc}
   */
  public IntegrationRegistry getIntegrationRegistry() throws ServiceFailure {
    return IntegrationRegistryHelper.narrow(service().getFacet(IntegrationRegistryHelper.id()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Contract> getContracts() throws ServiceFailure {
    return Arrays.asList(getContractRegistry().contracts());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Provider> getProviders() throws ServiceFailure {
    return Arrays.asList(getProviderRegistry().providers());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Consumer> getConsumers() throws ServiceFailure {
    return Arrays.asList(getConsumerRegistry().consumers());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integration> getIntegrations() throws ServiceFailure {
    return Arrays.asList(getIntegrationRegistry().integrations());
  }
}
