package tecgraf.openbus.admin;

import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.AuthorizationInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityAlreadyRegistered;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InvalidInterface;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import java.util.List;
import java.util.Map;

/**
 * Fachada para as principais funcionalidades de Governança e Configuração do núcleo do OpenBus.
 * 
 * @author Tecgraf
 */
public interface BusAdminFacade {

  /*
   * CATEGORIAS
   */

  /**
   * Retorna uma lista que contém descrições de todas as categorias das
   * entidades do barramento.
   *
   * @return Lista com as descrições das categorias das entidades.
   * 
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<EntityCategoryDesc> getCategories() throws ServiceFailure;

  /**
   * Cadastra uma nova categoria no barramento.
   *
   * @return Categoria cadastrada.
   * 
   * @param categoryID ID da nova categoria.
   * @param categoryName Nome da nova categoria.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws EntityCategoryAlreadyExists caso a categoria já exista
   */
  EntityCategory createCategory(String categoryID, String categoryName)
    throws ServiceFailure, UnauthorizedOperation, EntityCategoryAlreadyExists;

  /**
   * Remove uma categoria do barramento.
   * 
   * @param categoryID ID da categoria a ser removida.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws EntityCategoryInUse caso a categoria esteja em uso em alguma entidade {@link BusAdminFacade#removeEntity}
   */
  void removeCategory(String categoryID) throws ServiceFailure,
    UnauthorizedOperation, EntityCategoryInUse;

  /*
   * ENTIDADES
   */

  /**
   * Retorna uma lista que contém descrições de todas as entidades registradas
   * no barramento.
   *
   * @return Lista com as descrições de todas as entidades.
   * 
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<RegisteredEntityDesc> getEntities() throws ServiceFailure;

  /**
   * Cadastra uma nova entidade no barramento.
   *
   * @return Entidade cadastrada.
   * 
   * @param entityID ID da nova entidade.
   * @param entityName Nome da nova entidade.
   * @param categoryID ID de alguma categoria já cadastrada.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws EntityAlreadyRegistered caso a entidade já exista uma entidade registrada com o mesmo {@code entityID}
   */
  RegisteredEntity createEntity(String entityID, String entityName,
    String categoryID) throws ServiceFailure, UnauthorizedOperation,
    EntityAlreadyRegistered;

  /**
   * Remove o registro de uma entidade do barramento.
   * 
   * @return <code>true</code> caso a entidade foi removida, e
   *         <code>false</code> caso contrário.
   *
   * @param entityID ID da entidade a ser removida.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  boolean removeEntity(String entityID) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CERTIFICADOS
   */

  /**
   * Retorna uma lista que contém IDs de todas as etidades com certificado
   * registrado no barramento.
   *
   * @return Lista com as IDs de todas as entidades com certificado registrado.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  List<String> getEntitiesWithCertificate() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Registra um certificado no barramento.
   *
   * @param entityID ID da entidade.
   * @param certificate Certificado.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws InvalidCertificate caso o certificado seja incompatível com o padrão do barramento
   */
  void registerCertificate(String entityID, byte[] certificate)
    throws ServiceFailure, UnauthorizedOperation, InvalidCertificate;

  /**
   * Remove o certificado vinculado a uma entidade do barramento.
   *
   * @param entityID ID da entidade a ser removida.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void removeCertificate(String entityID) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * INTERFACES
   */

  /**
   * Retorna uma lista que contém as IDs de todas as interfaces registradas no
   * barramento.
   *
   * @return Lista com as IDs de todas as interfaces registradas.
   * 
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<String> getInterfaces() throws ServiceFailure;

  /**
   * Cadastra uma nova interface no barramento.
   *
   * @return {@code true} se a interface cadastrada inexistia no barramento;
   *         {@code false} em caso contrário.
   * 
   * @param interfaceName Nome da nova interface.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador.
   * @throws InvalidInterface caso a interface não tenha um nome válido
   */
  boolean createInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InvalidInterface;

  /**
   * Remove uma interface do barramento.
   * 
   * @param interfaceName Nome da interface a ser removida.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws InterfaceInUse caso a interface esteja em uso em alguma autorização {@link BusAdminFacade#revokeAuthorization}
   */
  void removeInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InterfaceInUse;

  /*
   * AUTORIZAÇÕES
   */

  /**
   * Retorna um mapa que contém as descrições de todas as entidades registradas
   * no barramento, associadas às suas respectivas autorizações.
   * 
   * @return mapa com par chave-valor: descrição da entidade registrada no
   *         barramento e lista de autorizações associadas a essa entidade,
   *         respectivamente.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure;

  /**
   * Concede autorização para uma interface a uma entidade.
   * 
   * @return {@code true} se a autorização cadastrada inexistia no barramento;
   *         {@code false} em caso contrário.
   *
   * @param entityID ID da entidade a receber autorização.
   * @param interfaceName Interface a ser autorizada.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws InvalidInterface caso a interface não tenha um nome válido
   */
  boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autorização para uma interface de uma entidade.
   * 
   * @param entityID ID da entidade a ter autorização removida.
   * @param interfaceName Interface a ser autorizada.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   * @throws InvalidInterface caso a interface não tenha um nome válido
   * @throws AuthorizationInUse caso a autorização esteja em uso em alguma oferta {@link BusAdminFacade#getOffers}
   */
  void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface,
    AuthorizationInUse;

  /*
   * OFERTAS
   */

  /**
   * Retorna uma lista que contém todas as descrições de ofertas de serviços do
   * barramento.
   * 
   * @return Lista de descrições de ofertas de serviços.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<ServiceOfferDesc> getOffers() throws ServiceFailure;

  /**
   * Remove uma oferta de serviço.
   * 
   * @param desc Descrição da oferta.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void removeOffer(ServiceOfferDesc desc) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * LOGINS
   */

  /**
   * Retorna lista que contém as informações de todos os logins ativos no
   * barramento.
   * 
   * @return Lista contendo informações de login das entidades.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  List<LoginInfo> getLogins() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Invalida um login ativo no barramento.
   * 
   * @param loginInfo Informação de login.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CONFIGURAÇÃO DINÂMICA
   */

  /**
   * Indica se o barramento disponibiliza a interface de reconfiguração dinâmica.
   *
   * @return {@code true} caso o barramento forneça a interface de reconfiguração
   * dinâmica e {@code false} caso contrário.
   */
  boolean isReconfigurationCapable();

  /**
   * Recarrega o arquivo de configurações no barramento.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void reloadConfigsFile() throws ServiceFailure, UnauthorizedOperation;

  /**
   * Confere direitos de administração a uma lista de entidades.
   *
   * @param users Lista de entidades que receberá direitos de administração.
   * @throws ServiceFailure  caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void grantAdminTo(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Revoga direitos de administração de uma lista de entidades.
   *
   * @param users Lista de entidades que perderá direitos de administração.
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void revokeAdminFrom(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista das entidades administradoras atuais.
   *
   * @return Lista contendo os nomes das entidades administradoras.
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<String> getAdmins() throws ServiceFailure;

  /**
   * Adiciona um validador no barramento.
   *
   * @param validator Identificação do validador.
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void addPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Remove um validador do barramento.
   *
   * @param validator Identificação do validador.
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void delPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista dos validadores atualmente disponíveis no barramento.
   *
   * @return Lista contendo as identificações dos validadores.
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  List<String> getPasswordValidators() throws ServiceFailure;

  /**
   * Configura a propriedade maxchannels do barramento.
   *
   * @param maxchannels Número máximo de canais a serem utilizados.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void setMaxChannels(int maxchannels) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade maxchannels do barramento.
   *
   * @return Número máximo de canais configurado atualmente.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  int getMaxChannels() throws ServiceFailure;

  /**
   * Configura o tamanho máximo das caches LRU de profiles IOR, sessões de entrada e de saída.
   *
   * @param maxcachesize Tamanho máximo das caches LRU.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void setMaxCacheSize(int maxcachesize) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o tamanho máximo das caches LRU de profiles IOR, sessões de entrada e de saída.
   *
   * @return Tamanho máximo das caches LRU configurado atualmente.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  int getMaxCacheSize() throws ServiceFailure;

  /**
   * Configura o tempo de espera por respostas nas chamadas do barramento.
   *
   * @param timeout Tempo de espera em segundos.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void setCallsTimeout(int timeout) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o tamanho de espera por respostas nas chamadas do barramento.
   *
   * @return Tamanho de espera atual. Tempo 0 significa que o barramento espera
   *         indefinidamente por respostas.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  int getCallsTimeout() throws ServiceFailure;

  /**
   * Configura a propriedade loglevel do barramento.
   *
   * @param loglevel Nível de log do barramento.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void setLogLevel(short loglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade loglevel do barramento.
   *
   * @return Nível de log do barramento configurado atualmente.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  short getLogLevel() throws ServiceFailure;

  /**
   * Configura a propriedade oilloglevel do barramento.
   *
   * @param oilLoglevel Nível de log do middleware OiL no barramento.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   * @throws UnauthorizedOperation caso o usuário não seja um administrador
   */
  void setOilLogLevel(short oilLoglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade oilloglevel do barramento.
   *
   * @return Nível de log do middleware OiL configurado atualmente.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  short getOilLogLevel() throws ServiceFailure;
}
