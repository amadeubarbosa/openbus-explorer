package tecgraf.openbus.admin;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.TRANSIENT;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.AuthorizationInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityAlreadyRegistered;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InvalidInterface;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import java.util.List;
import java.util.Map;

/**
 * Essa interface representa os comandos básicos utilizados para acessar os
 * serviços do barramento, necessários para sua administração.
 * 
 * @author Tecgraf
 */
public interface BusAdmin {

  /*
   * CATEGORIAS
   */

  /**
   * Retorna uma lista que contém descrições de todas as categorias das
   * entidades do barramento.
   *
   * @return Lista com as descrições das categorias das entidades.
   * 
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  List<EntityCategoryDesc> getCategories() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Cadastra uma nova categoria no barramento.
   *
   * @return Categoria cadastrada.
   * 
   * @param categoryID ID da nova categoria.
   * @param categoryName Nome da nova categoria.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws EntityCategoryAlreadyExists
   */
  EntityCategory createCategory(String categoryID, String categoryName)
    throws ServiceFailure, UnauthorizedOperation, EntityCategoryAlreadyExists;

  /**
   * Remove uma categoria do barramento.
   * 
   * @param categoryID ID da categoria a ser removida.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws EntityCategoryInUse
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
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  List<RegisteredEntityDesc> getEntities() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Cadastra uma nova entidade no barramento.
   *
   * @return Entidade cadastrada.
   * 
   * @param entityID ID da nova entidade.
   * @param entityName Nome da nova entidade.
   * @param categoryID ID de alguma categoria já cadastrada.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws EntityAlreadyRegistered
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
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
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
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  List<String> getEntitiesWithCertificate() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Registra um certificado no barramento.
   *
   * @param entityID ID da entidade.
   * @param certificate Certificado.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InvalidCertificate
   */
  void registerCertificate(String entityID, byte[] certificate)
    throws ServiceFailure, UnauthorizedOperation, InvalidCertificate;

  /**
   * Remove o certificado vinculado a uma entidade do barramento.
   *
   * @param entityID ID da entidade a ser removida.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
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
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  List<String> getInterfaces() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Cadastra uma nova interface no barramento.
   *
   * @return {@code true} se a interface cadastrada inexistia no barramento;
   *         {@code false} em caso contrário.
   * 
   * @param interfaceName Nome da nova interface.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InvalidInterface
   */
  boolean createInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InvalidInterface;

  /**
   * Remove uma interface do barramento.
   * 
   * @param interfaceName Nome da interface a ser removida.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InterfaceInUse
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
   * @throws ServiceFailure
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
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InvalidInterface
   */
  boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autorização para uma interface de uma entidade.
   * 
   * @param entityID ID da entidade a ter autorização removida.
   * @param interfaceName Interface a ser autorizada.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InvalidInterface
   * @throws AuthorizationInUse
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
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  List<ServiceOfferDesc> getOffers() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Remove uma oferta de serviço.
   * 
   * @param desc Descrição da oferta.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
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
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  List<LoginInfo> getLogins() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Invalida um login ativo no barramento.
   * 
   * @param loginInfo Informação de login.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CONFIGURAÇÃO DINÂMICA
   */

  /**
   * Indica se o barramento disponibiliza a interface de reconfiguração dinâmica.
   *
   * @return <code>true</code> caso o barramento forneça a interface de reconfiguração
   * dinâmica e <code>false</code> caso contrário.
   */
  boolean isReconfigurationCapable();

  /**
   * Recarrega o arquivo de configurações no barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void reloadConfigsFile() throws ServiceFailure, UnauthorizedOperation;

  /**
   * Confere direitos de administração a uma lista de entidades.
   *
   * @param users Lista de entidades que receberá direitos de administração.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void grantAdminTo(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Revoga direitos de administração de uma lista de entidades.
   *
   * @param users Lista de entidades que perderá direitos de administração.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void revokeAdminFrom(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista das entidades administradoras atuais.
   *
   * @return Lista contendo os nomes das entidades administradoras.
   * @throws ServiceFailure
   */
  List<String> getAdmins() throws ServiceFailure;

  /**
   * Adiciona um validador no barramento.
   *
   * @param validator Identificação do validador.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void addValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Remove um validador do barramento.
   *
   * @param validator Identificação do validador.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void delValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista dos validadores atualmente disponíveis no barramento.
   *
   * @return Lista contendo as identificações dos validadores.
   * @throws ServiceFailure
   */
  List<String> getValidators() throws ServiceFailure;

  /**
   * Configura a propriedade maxchannels do barramento.
   *
   * @param maxchannels Número máximo de canais a serem utilizados.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setMaxChannels(int maxchannels) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade maxchannels do barramento.
   *
   * @return Número máximo de canais configurado atualmente.
   *
   * @throws ServiceFailure
   */
  int getMaxChannels() throws ServiceFailure;

  /**
   * Configura a propriedade loglevel do barramento.
   *
   * @param loglevel Nível de log do barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setLogLevel(short loglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade loglevel do barramento.
   *
   * @return Nível de log do barramento configurado atualmente.
   *
   * @throws ServiceFailure
   */
  short getLogLevel() throws ServiceFailure;

  /**
   * Configura a propriedade oilloglevel do barramento.
   *
   * @param oilLoglevel Nível de log do middleware OiL no barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setOilLogLevel(short oilLoglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade oilloglevel do barramento.
   *
   * @return Nível de log do middleware OiL configurado atualmente.
   *
   * @throws ServiceFailure
   */
  short getOilLogLevel() throws ServiceFailure;
}
