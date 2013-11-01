package admin;

import java.util.List;
import java.util.Map;

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
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InvalidInterface;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Essa interface representa os comandos b�sicos utilizados para acessar os
 * servi�os do barramento, necess�rios para sua administra��o.
 * 
 * @author Tecgraf
 */
public interface BusAdmin {

  /**
   * Retorna uma lista que cont�m descri��es de todas as categorias das
   * entidades do barramento.
   * 
   * @return lista de descri��es de categoria
   * @throws ServiceFailure
   */
  public List<EntityCategoryDesc> getCategories() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que cont�m todas as descri��es de ofertas de servi�os do
   * barramento.
   * 
   * @return lista de descri��es de ofertas de servi�os
   * @throws ServiceFailure
   */
  public List<ServiceOfferDesc> getOffers() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que cont�m as IDs de todas as interfaces registradas no
   * barramento.
   * 
   * @return lista de IDs das interfaces
   * @throws ServiceFailure
   */
  public List<String> getInterfaces() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que cont�m descri��es de todas entidades registradas no
   * barramento.
   * 
   * @return descri��o da entidade
   * @throws ServiceFailure
   */
  public List<RegisteredEntityDesc> getEntities() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que cont�m IDs de todas as etidades com certificado
   * registrado no barramento.
   * @return lista de IDs das entidades com certificado
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public List<String> getEntitiesWithCertificate() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation;

  /**
   * Retorna um map que cont�m as descri��es de todas as entidades registradas
   * no barramento, associadas �s suas respectivas autoriza��es.
   * 
   * @return map com par chave-valor: descri��o da entidade registrada no
   *         barramento e lista de autoriza��es associadas a essa entidade,
   *         respectivamente.
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  public Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna lista que cont�m as informa��es de todos os logins ativos no
   * barramento
   * 
   * @return Lista contendo informa��es de login das entidades
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public List<LoginInfo> getLogins() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation;

  /**
   * Remove uma oferta de servi�o
   *
   * @param desc Descri��o da oferta
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public void removeOffer(ServiceOfferDesc desc)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation;

  /**
   * Invalida um login ativo no barramento
   *
   * @param LoginInfo Informa��o de login
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public void invalidateLogin(LoginInfo loginInfo) 
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation;

  /**
   * Cadastra uma nova categoria no barramento
   * 
   * @param categoryID ID da nova categoria
   * @param categoryName Nome da nova categoria
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   * @throws EntityCategoryAlreadyExists
   */
  public void createCategory(String categoryID, String categoryName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityCategoryAlreadyExists;

  /**
   * Cadastra uma nova interface no barramento
   * 
   * @param interfaceName nome da nova interface
   */
  public void createInterface(String interfaceName) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation,
    InvalidInterface;

  /**
   * Cadastra uma nova entidade no barramento
   * 
   * @param entityID ID da nova entidade
   * @param entityName Nome da nova entidade
   * @param categoryID ID de alguma categoria j� cadastrada
   */
  public void createEntity(String entityID, String entityName, String categoryID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityAlreadyRegistered;

  /**
   * Registra um certificado no barramento
   *
   * @param entityID ID da entidade
   * @param certificate Certificado
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   * @throws InvalidCertificate
   */
  public void registerCertificate(String entityID, byte[] certificate) throws
    ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidCertificate;

  /**
   * Remove uma categoria do barramento
   *
   * @param categoryID ID da categoria a ser removida
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   * @throws EntityCategoryInUse
   */
  public void removeCategory(String categoryID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityCategoryInUse;

  /**
   * Remove uma interface do barramento
   * @param interfaceName Nome da interface a ser removida
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   * @throws InterfaceInUse
   */
  public void removeInterface(String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InterfaceInUse;

  /**
   * Remove o registro de uma entidade do barramento
   *
   * @param entityID ID da entidade a ser removida
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public void removeEntity(String entityID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation;

  /**
   * Remove o certificado vinculado a uma entidade do barramento
   *
   * @param entityID ID da entidade a ser removida
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public void removeCertificate(String entityID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation;

  /**
   * Concede autoriza��o para uma interface a uma entidade
   * 
   * @param entityID ID da entidade que vai receber a autoriza��o
   * @param interfaceName interface que vai ser autorizada
   */
  public void setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autoriza��o para uma interface de uma entidade
   *
   * @param entityID ID da entidade que vai ter a autoriza��o removida
   * @param interfaceName interface que vai ser autorizada
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   * @throws InvalidInterface
   * @throws AuthorizationInUse
   */
  public void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidInterface, AuthorizationInUse;
}