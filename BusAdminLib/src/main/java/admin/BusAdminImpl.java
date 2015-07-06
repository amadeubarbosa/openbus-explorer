package admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

import scs.core.IComponent;
import scs.core.IComponentHelper;
import tecgraf.openbus.core.v2_1.BusObjectKey;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.access_control.LoginRegistry;
import tecgraf.openbus.core.v2_1.services.access_control.LoginRegistryHelper;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.CertificateRegistry;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.CertificateRegistryHelper;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_1.services.offer_registry.OfferRegistry;
import tecgraf.openbus.core.v2_1.services.offer_registry.OfferRegistryHelper;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.AuthorizationInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityAlreadyRegistered;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityRegistry;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityRegistryHelper;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InterfaceRegistry;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InterfaceRegistryHelper;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InvalidInterface;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * A classe implementa os comandos especificados na interface
 * {@link admin.BusAdmin}.
 * 
 * @author Tecgraf
 */
public class BusAdminImpl implements BusAdmin {
  /** {@link ORB} do barramento. */
  private ORB orb;

  /** Registro de entidades do barramento. */
  private EntityRegistry entityRegistry;
  /** Registro de certificados do barramento. */
  private CertificateRegistry certificateRegistry;
  /** Registro de interfaces do barramento. */
  private InterfaceRegistry interfaceRegistry;
  /** Registro de ofertas do barramento. */
  private OfferRegistry offerRegistry;
  /** Registro de logins do barramento. */
  private LoginRegistry loginRegistry;

  /**
   * Construtor da classe.
   */
  public BusAdminImpl() {
  }

  /**
   * Conecta a instância do objeto a um barramento. Como efeito colateral,
   * atualiza as referências aos registros do barramento.
   * 
   * @param host Host do barramento
   * @param port Porta do barramento
   * @param orb ORB do barramento
   */
  public void connect(String host, int port, ORB orb) {
    org.omg.CORBA.Object ref = buildCorbaLoc(host, port, orb);
    connect(ref, orb);
  }

  /**
   * Conecta a instância do objeto a um barramento. Como efeito colateral,
   * atualiza as referências aos registros do barramento.
   * 
   * @param ref Referência de um barramento
   * @param orb ORB do barramento
   */
  public void connect(Object ref, ORB orb) {
    this.orb = orb;
    obtainRegistries(ref);
  }

  /*
   * CATEGORIAS
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EntityCategoryDesc> getCategories() throws ServiceFailure {
    EntityCategoryDesc[] array = this.entityRegistry.getEntityCategories();
    return new ArrayList<EntityCategoryDesc>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityCategory createCategory(String categoryID, String categoryName)
    throws ServiceFailure, UnauthorizedOperation, EntityCategoryAlreadyExists {
    return this.entityRegistry.createEntityCategory(categoryID, categoryName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCategory(String categoryID) throws ServiceFailure,
    UnauthorizedOperation, EntityCategoryInUse {
    EntityCategory category = this.entityRegistry.getEntityCategory(categoryID);
    category.remove();
  }

  /*
   * ENTIDADES
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RegisteredEntityDesc> getEntities() throws ServiceFailure {
    RegisteredEntityDesc[] array = this.entityRegistry.getEntities();
    return new ArrayList<RegisteredEntityDesc>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredEntity createEntity(String entityID, String entityName,
    String categoryID) throws ServiceFailure, UnauthorizedOperation,
    EntityAlreadyRegistered {
    EntityCategory category = this.entityRegistry.getEntityCategory(categoryID);
    return category.registerEntity(entityID, entityName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeEntity(String entityID) throws ServiceFailure,
    UnauthorizedOperation {
    RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
    if (entity != null) {
      entity.remove();
      return true;
    }
    return false;
  }

  /*
   * CERTIFICADOS
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getEntitiesWithCertificate() throws ServiceFailure,
    UnauthorizedOperation {
    String[] array = this.certificateRegistry.getEntitiesWithCertificate();
    return new ArrayList<String>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCertificate(String entityID, byte[] certificate)
    throws ServiceFailure, UnauthorizedOperation, InvalidCertificate {
    this.certificateRegistry.registerCertificate(entityID, certificate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCertificate(String entityID) throws ServiceFailure,
    UnauthorizedOperation {
    this.certificateRegistry.removeCertificate(entityID);
  }

  /*
   * INTERFACES
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getInterfaces() throws ServiceFailure {
    String[] array = this.interfaceRegistry.getInterfaces();
    return new ArrayList<String>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InvalidInterface {
    return this.interfaceRegistry.registerInterface(interfaceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeInterface(String interfaceName) throws ServiceFailure,
    UnauthorizedOperation, InterfaceInUse {
    this.interfaceRegistry.removeInterface(interfaceName);
  }

  /*
   * AUTORIZAÇÕES
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure {
    Map<RegisteredEntityDesc, List<String>> map =
      new LinkedHashMap<RegisteredEntityDesc, List<String>>();
    RegisteredEntityDesc[] entitiesDesc =
      this.entityRegistry.getAuthorizedEntities();
    for (RegisteredEntityDesc entityDesc : entitiesDesc) {
      map.put(entityDesc, Arrays.asList(entityDesc.ref.getGrantedInterfaces()));
    }
    return map;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface {
    RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
    return entity.grantInterface(interfaceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface,
    AuthorizationInUse {
    RegisteredEntity entity = this.entityRegistry.getEntity(entityID);
    entity.revokeInterface(interfaceName);
  }

  /*
   * OFERTAS
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ServiceOfferDesc> getOffers() throws ServiceFailure {
    ServiceOfferDesc[] array = this.offerRegistry.getAllServices();
    return new ArrayList<ServiceOfferDesc>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeOffer(ServiceOfferDesc desc) throws ServiceFailure,
    UnauthorizedOperation {
    desc.ref.remove();
  }

  /*
   * LOGINS
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public List<LoginInfo> getLogins() throws ServiceFailure,
    UnauthorizedOperation {
    LoginInfo[] array = this.loginRegistry.getAllLogins();
    return new ArrayList<LoginInfo>(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateLogin(LoginInfo loginInfo) throws ServiceFailure,
    UnauthorizedOperation {
    this.loginRegistry.invalidateLogin(loginInfo.id);
  }

  /*
   * Métodos auxiliares
   */

  /**
   * Constrói a URL corbaloc
   * 
   * @param host endereço do barramento
   * @param port porta do barramento
   * @param orb instância do orb do barramento
   * @return a corbaloc criada
   */
  private static org.omg.CORBA.Object buildCorbaLoc(String host, int port,
    ORB orb) {
    String str =
      String.format("corbaloc::1.0@%s:%d/%s", host, port, BusObjectKey.value);
    return orb.string_to_object(str);
  }

  /**
   * Obtém as facetas dos registros do barramento.
   * 
   * @param obj referência para o barramento
   */
  private void obtainRegistries(Object obj) {
    IComponent iComponent = IComponentHelper.narrow(obj);
    if (iComponent == null) {
      throw new IncompatibleBus(
        "Não foi possível recuperar referência para barramento");
    }

    String error = "Referência para '%' não encontrada.";
    org.omg.CORBA.Object entityRegistryObj =
      iComponent.getFacet(EntityRegistryHelper.id());
    if (entityRegistryObj == null) {
      throw new IncompatibleBus(String.format(error, "registro de entidades"));
    }
    this.entityRegistry = EntityRegistryHelper.narrow(entityRegistryObj);

    org.omg.CORBA.Object certificateRegistryObj =
      iComponent.getFacet(CertificateRegistryHelper.id());
    if (certificateRegistryObj == null) {
      throw new IncompatibleBus(String
        .format(error, "registro de certificados"));
    }
    this.certificateRegistry =
      CertificateRegistryHelper.narrow(certificateRegistryObj);

    org.omg.CORBA.Object interfaceRegistryObj =
      iComponent.getFacet(InterfaceRegistryHelper.id());
    if (interfaceRegistryObj == null) {
      throw new IncompatibleBus(String.format(error, "registro de interfaces"));
    }
    this.interfaceRegistry =
      InterfaceRegistryHelper.narrow(interfaceRegistryObj);

    org.omg.CORBA.Object offerRegistryObj =
      iComponent.getFacet(OfferRegistryHelper.id());
    if (offerRegistryObj == null) {
      throw new IncompatibleBus(String.format(error, "registro de ofertas"));
    }
    this.offerRegistry = OfferRegistryHelper.narrow(offerRegistryObj);

    org.omg.CORBA.Object loginRegistryObj =
      iComponent.getFacet(LoginRegistryHelper.id());
    if (loginRegistryObj == null) {
      throw new IncompatibleBus(String.format(error, "registro de logins"));
    }
    this.loginRegistry = LoginRegistryHelper.narrow(loginRegistryObj);
  }
}
