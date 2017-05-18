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
 * Fachada para as principais funcionalidades de Governan�a e Configura��o do n�cleo do OpenBus.
 * 
 * @author Tecgraf
 */
public interface BusAdminFacade {

  /*
   * CATEGORIAS
   */

  /**
   * Retorna uma lista que cont�m descri��es de todas as categorias das
   * entidades do barramento.
   *
   * @return Lista com as descri��es das categorias das entidades.
   * 
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
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
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws EntityCategoryAlreadyExists caso a categoria j� exista
   */
  EntityCategory createCategory(String categoryID, String categoryName)
    throws ServiceFailure, UnauthorizedOperation, EntityCategoryAlreadyExists;

  /**
   * Remove uma categoria do barramento.
   * 
   * @param categoryID ID da categoria a ser removida.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws EntityCategoryInUse caso a categoria esteja em uso em alguma entidade {@link BusAdminFacade#removeEntity}
   */
  void removeCategory(String categoryID) throws ServiceFailure,
    UnauthorizedOperation, EntityCategoryInUse;

  /*
   * ENTIDADES
   */

  /**
   * Retorna uma lista que cont�m descri��es de todas as entidades registradas
   * no barramento.
   *
   * @return Lista com as descri��es de todas as entidades.
   * 
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  List<RegisteredEntityDesc> getEntities() throws ServiceFailure;

  /**
   * Cadastra uma nova entidade no barramento.
   *
   * @return Entidade cadastrada.
   * 
   * @param entityID ID da nova entidade.
   * @param entityName Nome da nova entidade.
   * @param categoryID ID de alguma categoria j� cadastrada.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws EntityAlreadyRegistered caso a entidade j� exista uma entidade registrada com o mesmo {@code entityID}
   */
  RegisteredEntity createEntity(String entityID, String entityName,
    String categoryID) throws ServiceFailure, UnauthorizedOperation,
    EntityAlreadyRegistered;

  /**
   * Remove o registro de uma entidade do barramento.
   * 
   * @return <code>true</code> caso a entidade foi removida, e
   *         <code>false</code> caso contr�rio.
   *
   * @param entityID ID da entidade a ser removida.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  boolean removeEntity(String entityID) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CERTIFICADOS
   */

  /**
   * Retorna uma lista que cont�m IDs de todas as etidades com certificado
   * registrado no barramento.
   *
   * @return Lista com as IDs de todas as entidades com certificado registrado.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  List<String> getEntitiesWithCertificate() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Registra um certificado no barramento.
   *
   * @param entityID ID da entidade.
   * @param certificate Certificado.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws InvalidCertificate caso o certificado seja incompat�vel com o padr�o do barramento
   */
  void registerCertificate(String entityID, byte[] certificate)
    throws ServiceFailure, UnauthorizedOperation, InvalidCertificate;

  /**
   * Remove o certificado vinculado a uma entidade do barramento.
   *
   * @param entityID ID da entidade a ser removida.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void removeCertificate(String entityID) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * INTERFACES
   */

  /**
   * Retorna uma lista que cont�m as IDs de todas as interfaces registradas no
   * barramento.
   *
   * @return Lista com as IDs de todas as interfaces registradas.
   * 
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  List<String> getInterfaces() throws ServiceFailure;

  /**
   * Cadastra uma nova interface no barramento.
   *
   * @return {@code true} se a interface cadastrada inexistia no barramento;
   *         {@code false} em caso contr�rio.
   * 
   * @param interfaceName Nome da nova interface.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador.
   * @throws InvalidInterface caso a interface n�o tenha um nome v�lido
   */
  boolean createInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InvalidInterface;

  /**
   * Remove uma interface do barramento.
   * 
   * @param interfaceName Nome da interface a ser removida.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws InterfaceInUse caso a interface esteja em uso em alguma autoriza��o {@link BusAdminFacade#revokeAuthorization}
   */
  void removeInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InterfaceInUse;

  /*
   * AUTORIZA��ES
   */

  /**
   * Retorna um mapa que cont�m as descri��es de todas as entidades registradas
   * no barramento, associadas �s suas respectivas autoriza��es.
   * 
   * @return mapa com par chave-valor: descri��o da entidade registrada no
   *         barramento e lista de autoriza��es associadas a essa entidade,
   *         respectivamente.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure;

  /**
   * Concede autoriza��o para uma interface a uma entidade.
   * 
   * @return {@code true} se a autoriza��o cadastrada inexistia no barramento;
   *         {@code false} em caso contr�rio.
   *
   * @param entityID ID da entidade a receber autoriza��o.
   * @param interfaceName Interface a ser autorizada.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws InvalidInterface caso a interface n�o tenha um nome v�lido
   */
  boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autoriza��o para uma interface de uma entidade.
   * 
   * @param entityID ID da entidade a ter autoriza��o removida.
   * @param interfaceName Interface a ser autorizada.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   * @throws InvalidInterface caso a interface n�o tenha um nome v�lido
   * @throws AuthorizationInUse caso a autoriza��o esteja em uso em alguma oferta {@link BusAdminFacade#getOffers}
   */
  void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface,
    AuthorizationInUse;

  /*
   * OFERTAS
   */

  /**
   * Retorna uma lista que cont�m todas as descri��es de ofertas de servi�os do
   * barramento.
   * 
   * @return Lista de descri��es de ofertas de servi�os.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  List<ServiceOfferDesc> getOffers() throws ServiceFailure;

  /**
   * Remove uma oferta de servi�o.
   * 
   * @param desc Descri��o da oferta.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void removeOffer(ServiceOfferDesc desc) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * LOGINS
   */

  /**
   * Retorna lista que cont�m as informa��es de todos os logins ativos no
   * barramento.
   * 
   * @return Lista contendo informa��es de login das entidades.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  List<LoginInfo> getLogins() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Invalida um login ativo no barramento.
   * 
   * @param loginInfo Informa��o de login.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CONFIGURA��O DIN�MICA
   */

  /**
   * Indica se o barramento disponibiliza a interface de reconfigura��o din�mica.
   *
   * @return {@code true} caso o barramento forne�a a interface de reconfigura��o
   * din�mica e {@code false} caso contr�rio.
   */
  boolean isReconfigurationCapable();

  /**
   * Recarrega o arquivo de configura��es no barramento.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void reloadConfigsFile() throws ServiceFailure, UnauthorizedOperation;

  /**
   * Confere direitos de administra��o a uma lista de entidades.
   *
   * @param users Lista de entidades que receber� direitos de administra��o.
   * @throws ServiceFailure  caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void grantAdminTo(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Revoga direitos de administra��o de uma lista de entidades.
   *
   * @param users Lista de entidades que perder� direitos de administra��o.
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void revokeAdminFrom(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista das entidades administradoras atuais.
   *
   * @return Lista contendo os nomes das entidades administradoras.
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  List<String> getAdmins() throws ServiceFailure;

  /**
   * Adiciona um validador no barramento.
   *
   * @param validator Identifica��o do validador.
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void addPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Remove um validador do barramento.
   *
   * @param validator Identifica��o do validador.
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void delPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista dos validadores atualmente dispon�veis no barramento.
   *
   * @return Lista contendo as identifica��es dos validadores.
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  List<String> getPasswordValidators() throws ServiceFailure;

  /**
   * Configura a propriedade maxchannels do barramento.
   *
   * @param maxchannels N�mero m�ximo de canais a serem utilizados.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void setMaxChannels(int maxchannels) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade maxchannels do barramento.
   *
   * @return N�mero m�ximo de canais configurado atualmente.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  int getMaxChannels() throws ServiceFailure;

  /**
   * Configura o tamanho m�ximo das caches LRU de profiles IOR, sess�es de entrada e de sa�da.
   *
   * @param maxcachesize Tamanho m�ximo das caches LRU.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void setMaxCacheSize(int maxcachesize) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o tamanho m�ximo das caches LRU de profiles IOR, sess�es de entrada e de sa�da.
   *
   * @return Tamanho m�ximo das caches LRU configurado atualmente.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  int getMaxCacheSize() throws ServiceFailure;

  /**
   * Configura o tempo de espera por respostas nas chamadas do barramento.
   *
   * @param timeout Tempo de espera em segundos.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void setCallsTimeout(int timeout) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o tamanho de espera por respostas nas chamadas do barramento.
   *
   * @return Tamanho de espera atual. Tempo 0 significa que o barramento espera
   *         indefinidamente por respostas.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  int getCallsTimeout() throws ServiceFailure;

  /**
   * Configura a propriedade loglevel do barramento.
   *
   * @param loglevel N�vel de log do barramento.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void setLogLevel(short loglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade loglevel do barramento.
   *
   * @return N�vel de log do barramento configurado atualmente.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  short getLogLevel() throws ServiceFailure;

  /**
   * Configura a propriedade oilloglevel do barramento.
   *
   * @param oilLoglevel N�vel de log do middleware OiL no barramento.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   * @throws UnauthorizedOperation caso o usu�rio n�o seja um administrador
   */
  void setOilLogLevel(short oilLoglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade oilloglevel do barramento.
   *
   * @return N�vel de log do middleware OiL configurado atualmente.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
   */
  short getOilLogLevel() throws ServiceFailure;
}
