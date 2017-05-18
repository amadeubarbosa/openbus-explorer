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
 * Fachada para as principais funcionalidades do Serviço de Extensão à Governança.
 *
 * @author Tecgraf
 */
public interface BusExtensionFacade {
  /**
   * Verifica se o Serviço de Extensão à Governança está disponível.
   *
   * @return {@code true} caso o serviço esteja disponível e {@code false}
   * caso não esteja disponível ou aconteça alguma exceção que impeça essa verificação
   */
  boolean isExtensionCapable();

  /**
   * Obtém uma lista de todas referências remotas para {@link Contract}
   * cadastrados no Serviço de Extensão à Governança.
   *
   * @return a lista de todos contratos, a lista pode ter tamanho zero caso não haja nenhum contrato
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  List<Contract> getContracts() throws ServiceFailure;

  /**
   * Obtém uma lista de todas referências remotas para {@link Provider}
   * cadastrados no Serviço de Extensão à Governança.
   *
   * @return a lista de todos provedores, a lista pode ter tamanho zero caso não haja nenhum provedor
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  List<Provider> getProviders() throws ServiceFailure;

  /**
   * Obtém uma lista de todas referências remotas para {@link Consumer}
   * cadastrados no Serviço de Extensão à Governança.
   *
   * @return a lista de todos consumidores, a lista pode ter tamanho zero caso não haja nenhum consumidor
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  List<Consumer> getConsumers() throws ServiceFailure;

  /**
   * Obtém uma lista de todas referências remotas para {@link Integration}
   * cadastrados no Serviço de Extensão à Governança.
   *
   * @return a lista de todas integrações, a lista pode ter tamanho zero caso não haja nenhuma integração
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  List<Integration> getIntegrations() throws ServiceFailure;

  /**
   * Obtém a referência remota da faceta do registro de contratos
   * (i.e: {@link ContractRegistry}) do Serviço de Extensão à Governança.
   *
   * @return a referência remota para o registro de contratos
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  ContractRegistry getContractRegistry() throws ServiceFailure;

  /**
   * Obtém a referência remota da faceta do registro de provedores
   * (i.e: {@link ProviderRegistry}) do Serviço de Extensão à Governança.
   *
   * @return a referência remota para o registro de provedores
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  ProviderRegistry getProviderRegistry() throws ServiceFailure;

  /**
   * Obtém a referência remota da faceta do registro de consumidores
   * (i.e: {@link ConsumerRegistry}) do Serviço de Extensão à Governança.
   *
   * @return a referência remota para o registro de consumidores
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  ConsumerRegistry getConsumerRegistry() throws ServiceFailure;

  /**
   * Obtém a referência remota da faceta do registro de integrações
   * (i.e: {@link IntegrationRegistry}) do Serviço de Extensão à Governança.
   *
   * @return a referência remota para o registro de integrações
   * @throws ServiceFailure caso seja impossível acessar o serviço
   */
  IntegrationRegistry getIntegrationRegistry() throws ServiceFailure;
}
