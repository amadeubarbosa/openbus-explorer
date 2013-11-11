package admin;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TRANSIENT;

import scs.core.IComponent;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_0.services.access_control.LoginRegistry;
import tecgraf.openbus.core.v2_0.services.access_control.LoginRegistryHelper;
import tecgraf.openbus.core.v2_0.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.CertificateRegistry;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.CertificateRegistryHelper;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_0.services.offer_registry.OfferRegistry;
import tecgraf.openbus.core.v2_0.services.offer_registry.OfferRegistryHelper;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.AuthorizationInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityAlreadyRegistered;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityRegistry;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityRegistryHelper;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceRegistry;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceRegistryHelper;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InvalidInterface;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

public class BusAdminImpl implements BusAdmin {
  private String host;
  private short port;
  private ORB orb;

  private EntityRegistry entityRegistry;
  private CertificateRegistry certificateRegistry;
  private InterfaceRegistry interfaceRegistry;
  private OfferRegistry offerRegistry;
  private LoginRegistry loginRegistry;

  public BusAdminImpl(String host, short port, ORB orb) {
    setHostPort(host, port, orb);
  }

  public void setHostPort(String host, short port, ORB orb) {
    this.host = host;
    this.port = port;
    this.orb = orb;

    obtainRegistries();
  }

  private void obtainRegistries() {

    try {
      IComponent iComponent =
        Util.getIComponent(this.host, this.port, this.orb);

      org.omg.CORBA.Object entityRegistryObj =
        iComponent.getFacet(EntityRegistryHelper.id());
      this.entityRegistry = EntityRegistryHelper.narrow(entityRegistryObj);

      org.omg.CORBA.Object certificateRegistryObj =
        iComponent.getFacet(CertificateRegistryHelper.id());
      this.certificateRegistry =
        CertificateRegistryHelper.narrow(certificateRegistryObj);

      org.omg.CORBA.Object interfaceRegistryObj =
        iComponent.getFacet(InterfaceRegistryHelper.id());
      this.interfaceRegistry =
        InterfaceRegistryHelper.narrow(interfaceRegistryObj);

      org.omg.CORBA.Object offerRegistryObj =
        iComponent.getFacet(OfferRegistryHelper.id());
      this.offerRegistry = OfferRegistryHelper.narrow(offerRegistryObj);

      org.omg.CORBA.Object loginRegistryObj =
        iComponent.getFacet(LoginRegistryHelper.id());
      this.loginRegistry = LoginRegistryHelper.narrow(loginRegistryObj);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EntityCategoryDesc> getCategories() throws ServiceFailure {
    try {
      return Arrays.asList(this.entityRegistry.getEntityCategories());
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ServiceOfferDesc> getOffers() throws ServiceFailure {
    try {
      return Arrays.asList(this.offerRegistry.getAllServices());
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getInterfaces() throws ServiceFailure {
    try {
      return Arrays.asList(this.interfaceRegistry.getInterfaces());
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RegisteredEntityDesc> getEntities() throws ServiceFailure {
    try {

      return Arrays.asList(this.entityRegistry.getEntities());
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }

  }

  /**
   * {@inheritDoc}
   */
  public List<String> getEntitiesWithCertificate() throws ServiceFailure,
    UnauthorizedOperation {
    try {
      return Arrays.asList(this.certificateRegistry
        .getEntitiesWithCertificate());
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure {
    Map<RegisteredEntityDesc, List<String>> map =
      new LinkedHashMap<RegisteredEntityDesc, List<String>>();

    try {

      RegisteredEntityDesc[] entitiesDesc =
        this.entityRegistry.getAuthorizedEntities();

      for (RegisteredEntityDesc entityDesc : entitiesDesc) {
        map.put(entityDesc, Arrays
          .asList(entityDesc.ref.getGrantedInterfaces()));
      }

      return map;
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<LoginInfo> getLogins() throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation {

    try {
      return Arrays.asList(this.loginRegistry.getAllLogins());

    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeOffer(ServiceOfferDesc desc) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation {
    try {
      desc.ref.remove();
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation {
    try {
      this.loginRegistry.invalidateLogin(loginInfo.id);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createCategory(String categoryID, String categoryName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityCategoryAlreadyExists {

    try {
      this.entityRegistry.createEntityCategory(categoryID, categoryName);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (EntityCategoryAlreadyExists e) {
      throw new EntityCategoryAlreadyExists(
        Util.ENTITY_CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE, e.existing);
    }
  }

  @Override
  public void createInterface(String interfaceName) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation,
    InvalidInterface {
    try {
      this.interfaceRegistry.registerInterface(interfaceName);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (InvalidInterface e) {
      throw new InvalidInterface(Util.INVALID_INTERFACE_EXCEPTION_MESSAGE,
        e.ifaceId);
    }

  }

  @Override
  public void createEntity(String entityID, String entityName, String categoryID)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, EntityAlreadyRegistered {
    try {
      EntityCategory category =
        this.entityRegistry.getEntityCategory(categoryID);
      category.registerEntity(entityID, entityName);

    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (EntityAlreadyRegistered e) {
      throw new EntityAlreadyRegistered(
        Util.ENTITY_ALREADY_REGISTERED_EXCEPTION_MESSAGE, e.existing);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCertificate(String entityID, byte[] certificate)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidCertificate {
    try {
      this.certificateRegistry.registerCertificate(entityID, certificate);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (InvalidCertificate e) {
      throw new InvalidCertificate(Util.INVALID_CERTIFICATE_EXCEPTION_MESSAGE,
        e.message);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCategory(String categoryID) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation,
    EntityCategoryInUse {
    try {
      EntityCategory category =
        this.entityRegistry.getEntityCategory(categoryID);
      category.remove();
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (EntityCategoryInUse e) {
      throw new EntityCategoryInUse(
        Util.ENTITY_CATEGORY_IN_USE_EXCEPTION_MESSAGE, e.entities);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeEntity(String entityID) throws ServiceFailure, TRANSIENT,
    COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation {
    try {
      RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
      entity.remove();
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCertificate(String entityID) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation {
    try {
      this.certificateRegistry.removeCertificate(entityID);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeInterface(String interfaceName) throws ServiceFailure,
    TRANSIENT, COMM_FAILURE, NO_PERMISSION, UnauthorizedOperation,
    InterfaceInUse {
    try {
      this.interfaceRegistry.removeInterface(interfaceName);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (InterfaceInUse e) {
      throw new InterfaceInUse(Util.INTERFACE_IN_USE_EXCEPTION_MESSAGE,
        e.entities);
    }
  }

  @Override
  public void setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidInterface {
    try {
      RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
      entity.grantInterface(interfaceName);

    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (InvalidInterface e) {
      throw new InvalidInterface(Util.INVALID_INTERFACE_EXCEPTION_MESSAGE,
        e.ifaceId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, TRANSIENT, COMM_FAILURE, NO_PERMISSION,
    UnauthorizedOperation, InvalidInterface, AuthorizationInUse {
    try {
      RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
      entity.revokeInterface(interfaceName);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE, host,
        port), e.minor, e.completed);
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE, e.minor,
        e.completed);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new NO_PERMISSION(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
      throw e;
    }
    catch (UnauthorizedOperation e) {
      throw new UnauthorizedOperation(
        Util.UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE);
    }
    catch (InvalidInterface e) {
      throw new InvalidInterface(Util.INVALID_INTERFACE_EXCEPTION_MESSAGE,
        e.ifaceId);
    }
    catch (AuthorizationInUse e) {
      throw new AuthorizationInUse(Util.AUTHORIZATION_IN_USE_EXCEPTION_MESSAGE,
        e.offers);
    }
  }
}
