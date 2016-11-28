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
 * Essa interface representa os comandos b�sicos utilizados para acessar os
 * servi�os do barramento, necess�rios para sua administra��o.
 * 
 * @author Tecgraf
 */
public interface BusAdmin {

  /*
   * CATEGORIAS
   */

  /**
   * Retorna uma lista que cont�m descri��es de todas as categorias das
   * entidades do barramento.
   *
   * @return Lista com as descri��es das categorias das entidades.
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
   * Retorna uma lista que cont�m descri��es de todas as entidades registradas
   * no barramento.
   *
   * @return Lista com as descri��es de todas as entidades.
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
   * @param categoryID ID de alguma categoria j� cadastrada.
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
   *         <code>false</code> caso contr�rio.
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
   * Retorna uma lista que cont�m IDs de todas as etidades com certificado
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
   * Retorna uma lista que cont�m as IDs de todas as interfaces registradas no
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
   *         {@code false} em caso contr�rio.
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
   * @throws ServiceFailure
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
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   * @throws InvalidInterface
   */
  boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autoriza��o para uma interface de uma entidade.
   * 
   * @param entityID ID da entidade a ter autoriza��o removida.
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
   * Retorna uma lista que cont�m todas as descri��es de ofertas de servi�os do
   * barramento.
   * 
   * @return Lista de descri��es de ofertas de servi�os.
   *
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  List<ServiceOfferDesc> getOffers() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Remove uma oferta de servi�o.
   * 
   * @param desc Descri��o da oferta.
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
   * Retorna lista que cont�m as informa��es de todos os logins ativos no
   * barramento.
   * 
   * @return Lista contendo informa��es de login das entidades.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  List<LoginInfo> getLogins() throws ServiceFailure,
    UnauthorizedOperation;

  /**
   * Invalida um login ativo no barramento.
   * 
   * @param loginInfo Informa��o de login.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    UnauthorizedOperation;

  /*
   * CONFIGURA��O DIN�MICA
   */

  /**
   * Indica se o barramento disponibiliza a interface de reconfigura��o din�mica.
   *
   * @return <code>true</code> caso o barramento forne�a a interface de reconfigura��o
   * din�mica e <code>false</code> caso contr�rio.
   */
  boolean isReconfigurationCapable();

  /**
   * Recarrega o arquivo de configura��es no barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void reloadConfigsFile() throws ServiceFailure, UnauthorizedOperation;

  /**
   * Confere direitos de administra��o a uma lista de entidades.
   *
   * @param users Lista de entidades que receber� direitos de administra��o.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void grantAdminTo(List<String> users) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Revoga direitos de administra��o de uma lista de entidades.
   *
   * @param users Lista de entidades que perder� direitos de administra��o.
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
   * @param validator Identifica��o do validador.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void addValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Remove um validador do barramento.
   *
   * @param validator Identifica��o do validador.
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void delValidator(String validator) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna uma lista dos validadores atualmente dispon�veis no barramento.
   *
   * @return Lista contendo as identifica��es dos validadores.
   * @throws ServiceFailure
   */
  List<String> getValidators() throws ServiceFailure;

  /**
   * Configura a propriedade maxchannels do barramento.
   *
   * @param maxchannels N�mero m�ximo de canais a serem utilizados.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setMaxChannels(int maxchannels) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade maxchannels do barramento.
   *
   * @return N�mero m�ximo de canais configurado atualmente.
   *
   * @throws ServiceFailure
   */
  int getMaxChannels() throws ServiceFailure;

  /**
   * Configura a propriedade loglevel do barramento.
   *
   * @param loglevel N�vel de log do barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setLogLevel(short loglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade loglevel do barramento.
   *
   * @return N�vel de log do barramento configurado atualmente.
   *
   * @throws ServiceFailure
   */
  short getLogLevel() throws ServiceFailure;

  /**
   * Configura a propriedade oilloglevel do barramento.
   *
   * @param oilLoglevel N�vel de log do middleware OiL no barramento.
   *
   * @throws ServiceFailure
   * @throws UnauthorizedOperation
   */
  void setOilLogLevel(short oilLoglevel) throws ServiceFailure, UnauthorizedOperation;

  /**
   * Retorna o valor atual da propriedade oilloglevel do barramento.
   *
   * @return N�vel de log do middleware OiL configurado atualmente.
   *
   * @throws ServiceFailure
   */
  short getOilLogLevel() throws ServiceFailure;
}
