package admin;

import java.util.List;
import java.util.Map;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.TRANSIENT;

import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_0.services.offer_registry.AuthorizationInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityAlreadyRegistered;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.InterfaceInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.InvalidInterface;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;

/**
 * Essa interface representa os comandos básicos utilizados para acessar os
 * serviços do barramento, necessários para sua administração.
 * 
 * @author Tecgraf
 */
public interface BusAdmin {

  /**
   * Retorna uma lista que contém descrições de todas as categorias das
   * entidades do barramento.
   * 
   * @return lista de descrições de categoria
   * @throws ServiceFailure
   */
  public List<EntityCategoryDesc> getCategories() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que contém todas as descrições de ofertas de serviços do
   * barramento.
   * 
   * @return lista de descrições de ofertas de serviços
   * @throws ServiceFailure
   */
  public List<ServiceOfferDesc> getOffers() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que contém as IDs de todas as interfaces registradas no
   * barramento.
   * 
   * @return lista de IDs das interfaces
   * @throws ServiceFailure
   */
  public List<String> getInterfaces() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna uma lista que contém descrições de todas entidades registradas no
   * barramento.
   * 
   * @return descrição da entidade
   * @throws ServiceFailure
   */
  public List<RegisteredEntityDesc> getEntities() throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna um map que contém as descrições de todas as entidades registradas
   * no barramento, associadas às suas respectivas autorizações.
   * 
   * @return map com par chave-valor: descrição da entidade registrada no
   *         barramento e lista de autorizações associadas a essa entidade,
   *         respectivamente.
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   */
  public Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION;

  /**
   * Retorna lista que contém as informações de todos os logins ativos no
   * barramento
   * 
   * @return Lista contendo informações de login das entidades
   * @throws ServiceFailure
   * @throws TRANSIENT
   * @throws COMM_FAILURE
   * @throws NO_PERMISSION
   * @throws UnauthorizedOperation
   */
  public List<LoginInfo> getLogins() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation;

  /**
   * Remove uma oferta de serviço
   *
   * @param desc Descrição da oferta
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
   * @param LoginInfo Informação de login
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
   * @param categoryID ID de alguma categoria já cadastrada
   */
  public void createEntity(String entityID, String entityName, String categoryID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityAlreadyRegistered;

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
   * Concede autorização para uma interface a uma entidade
   * 
   * @param entityID ID da entidade que vai receber a autorização
   * @param interfaceName interface que vai ser autorizada
   */
  public void setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidInterface;

  /**
   * Remove autorização para uma interface de uma entidade
   *
   * @param entityID ID da entidade que vai ter a autorização removida
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
