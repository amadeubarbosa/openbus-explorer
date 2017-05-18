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
 * Fachada para as principais funcionalidades do Servi�o de Extens�o � Governan�a.
 *
 * @author Tecgraf
 */
public interface BusExtensionFacade {
  /**
   * Verifica se o Servi�o de Extens�o � Governan�a est� dispon�vel.
   *
   * @return {@code true} caso o servi�o esteja dispon�vel e {@code false}
   * caso n�o esteja dispon�vel ou aconte�a alguma exce��o que impe�a essa verifica��o
   */
  boolean isExtensionCapable();

  /**
   * Obt�m uma lista de todas refer�ncias remotas para {@link Contract}
   * cadastrados no Servi�o de Extens�o � Governan�a.
   *
   * @return a lista de todos contratos, a lista pode ter tamanho zero caso n�o haja nenhum contrato
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  List<Contract> getContracts() throws ServiceFailure;

  /**
   * Obt�m uma lista de todas refer�ncias remotas para {@link Provider}
   * cadastrados no Servi�o de Extens�o � Governan�a.
   *
   * @return a lista de todos provedores, a lista pode ter tamanho zero caso n�o haja nenhum provedor
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  List<Provider> getProviders() throws ServiceFailure;

  /**
   * Obt�m uma lista de todas refer�ncias remotas para {@link Consumer}
   * cadastrados no Servi�o de Extens�o � Governan�a.
   *
   * @return a lista de todos consumidores, a lista pode ter tamanho zero caso n�o haja nenhum consumidor
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  List<Consumer> getConsumers() throws ServiceFailure;

  /**
   * Obt�m uma lista de todas refer�ncias remotas para {@link Integration}
   * cadastrados no Servi�o de Extens�o � Governan�a.
   *
   * @return a lista de todas integra��es, a lista pode ter tamanho zero caso n�o haja nenhuma integra��o
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  List<Integration> getIntegrations() throws ServiceFailure;

  /**
   * Obt�m a refer�ncia remota da faceta do registro de contratos
   * (i.e: {@link ContractRegistry}) do Servi�o de Extens�o � Governan�a.
   *
   * @return a refer�ncia remota para o registro de contratos
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  ContractRegistry getContractRegistry() throws ServiceFailure;

  /**
   * Obt�m a refer�ncia remota da faceta do registro de provedores
   * (i.e: {@link ProviderRegistry}) do Servi�o de Extens�o � Governan�a.
   *
   * @return a refer�ncia remota para o registro de provedores
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  ProviderRegistry getProviderRegistry() throws ServiceFailure;

  /**
   * Obt�m a refer�ncia remota da faceta do registro de consumidores
   * (i.e: {@link ConsumerRegistry}) do Servi�o de Extens�o � Governan�a.
   *
   * @return a refer�ncia remota para o registro de consumidores
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  ConsumerRegistry getConsumerRegistry() throws ServiceFailure;

  /**
   * Obt�m a refer�ncia remota da faceta do registro de integra��es
   * (i.e: {@link IntegrationRegistry}) do Servi�o de Extens�o � Governan�a.
   *
   * @return a refer�ncia remota para o registro de integra��es
   * @throws ServiceFailure caso seja imposs�vel acessar o servi�o
   */
  IntegrationRegistry getIntegrationRegistry() throws ServiceFailure;
}
