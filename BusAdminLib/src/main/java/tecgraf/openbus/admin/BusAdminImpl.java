package tecgraf.openbus.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.Object;
import scs.core.IComponent;
import scs.core.IComponentHelper;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.access_control.LoginRegistry;
import tecgraf.openbus.core.v2_1.services.access_control.LoginRegistryHelper;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.CertificateRegistry;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.CertificateRegistryHelper;
import tecgraf.openbus.core.v2_1.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_1.services.admin.v1_0.Configuration;
import tecgraf.openbus.core.v2_1.services.admin.v1_0.ConfigurationHelper;
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
 * A classe implementa os comandos especificados na interface {@link BusAdminFacade}.
 *
 * @author Tecgraf
 */
public class BusAdminImpl implements BusAdminFacade {
  /** Endere�o do barramento. */
  private final Object reference;
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
  /** Configura��o din�mica do barramento. */
  private Configuration configuration = null;

  /**
   * Construtor da biblioteca de administra��o a partir de uma refer�ncia para o barramento.
   *
   * @param reference objeto remoto para o {@link IComponent} do barramento.
   *
   * @throws IncompatibleBus caso n�o seja poss�vel encontrar as interfaces
   *                         de administra��o do barramento na refer�ncia fornecida.
   */
  public BusAdminImpl(Object reference) {
    this.reference = reference;
    this.setRegistries();
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
    return new ArrayList<>(Arrays.asList(array));
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
    return new ArrayList<>(Arrays.asList(array));
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
  public RegisteredEntity getEntity(String entityID) throws ServiceFailure {
    return this.entityRegistry.getEntity(entityID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeEntity(String entityID) throws ServiceFailure,
    UnauthorizedOperation {
    RegisteredEntity entity = this.getEntity(entityID);
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
    return new ArrayList<>(Arrays.asList(array));
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
    return new ArrayList<>(Arrays.asList(array));
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
   * AUTORIZA��ES
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<RegisteredEntityDesc, List<String>> getAuthorizations()
    throws ServiceFailure {
    Map<RegisteredEntityDesc, List<String>> map =
      new LinkedHashMap<>();
    RegisteredEntityDesc[] entitiesDesc =
      this.entityRegistry.getAuthorizedEntities();
    for (RegisteredEntityDesc entityDesc : entitiesDesc) {
      map.put(entityDesc, new ArrayList(Arrays.asList(entityDesc.ref.getGrantedInterfaces())));
    }
    return map;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean setAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface {
    RegisteredEntity entity = this.getEntity(entityID);
    return entity.grantInterface(interfaceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void revokeAuthorization(String entityID, String interfaceName)
    throws ServiceFailure, UnauthorizedOperation, InvalidInterface,
    AuthorizationInUse {
    RegisteredEntity entity = this.getEntity(entityID);
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
    return new ArrayList<>(Arrays.asList(array));
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
    return new ArrayList<>(Arrays.asList(array));
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
   * Reconfigura��o din�mica
   */

  @Override
  public boolean isReconfigurationCapable() {
    return (configuration != null);
  }

  @Override
  public void reloadConfigsFile() throws ServiceFailure, UnauthorizedOperation {
    configuration.reloadConfigsFile();
  }

  @Override
  public void grantAdminTo(List<String> users) throws ServiceFailure, UnauthorizedOperation {
    configuration.grantAdminTo(users.toArray(new String[users.size()]));
  }

  @Override
  public void revokeAdminFrom(List<String> users) throws ServiceFailure, UnauthorizedOperation {
    configuration.revokeAdminFrom(users.toArray(new String[users.size()]));
  }

  @Override
  public List<String> getAdmins() throws ServiceFailure {
    String[] admins = configuration.getAdmins();
    List<String> ret = new ArrayList<String>();
    Collections.addAll(ret, admins);
    return ret;
  }

  @Override
  public void addPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation {
    configuration.addPasswordValidator(validator);
  }

  @Override
  public void delPasswordValidator(String validator) throws ServiceFailure, UnauthorizedOperation {
    configuration.delPasswordValidator(validator);
  }

  @Override
  public List<String> getPasswordValidators() throws ServiceFailure {
    String[] validators = configuration.getPasswordValidators();
    List<String> ret = new ArrayList<String>();
    Collections.addAll(ret, validators);
    return ret;
  }

  @Override
  public void setMaxChannels(int maxchannels) throws ServiceFailure, UnauthorizedOperation {
    configuration.setMaxChannels(maxchannels);
  }

  @Override
  public int getMaxChannels() throws ServiceFailure {
    return configuration.getMaxChannels();
  }

  @Override
  public void setMaxCacheSize(int maxcachesize) throws ServiceFailure, UnauthorizedOperation {
    configuration.setMaxCacheSize(maxcachesize);
  }

  @Override
  public int getMaxCacheSize() throws ServiceFailure {
    return configuration.getMaxCacheSize();
  }

  @Override
  public void setCallsTimeout(int timeout) throws ServiceFailure, UnauthorizedOperation {
    configuration.setCallsTimeout(timeout);
  }

  @Override
  public int getCallsTimeout() throws ServiceFailure {
    return configuration.getCallsTimeout();
  }

  @Override
  public void setLogLevel(short loglevel) throws ServiceFailure, UnauthorizedOperation {
    configuration.setLogLevel(loglevel);
  }

  @Override
  public short getLogLevel() throws ServiceFailure {
    return configuration.getLogLevel();
  }

  @Override
  public void setOilLogLevel(short oilLoglevel) throws ServiceFailure, UnauthorizedOperation {
    configuration.setOilLogLevel(oilLoglevel);
  }

  @Override
  public short getOilLogLevel() throws ServiceFailure {
    return configuration.getOilLogLevel();
  }

  /*
   * M�todos auxiliares
   */

  /**
   * Obt�m a refer�ncia para o barramento, tipicamente um {@link IComponent}
   *
   * @return inst�ncia de {@link org.omg.CORBA.Object} da refer�ncia inicial para o barramento
   */
  public Object getBusReference() {
    return reference;
  }

  /**
   * Preenche os objetos locais das refer�ncias das facetas de administra��o do barramento.
   */
  private void setRegistries() {
    IComponent iComponent = IComponentHelper.narrow(getBusReference());
    if (iComponent == null) {
      throw new IncompatibleBus(
        LNG.get("IncompatibleBus.missing.icomponent"));
    }

    org.omg.CORBA.Object entityRegistryObj =
      iComponent.getFacet(EntityRegistryHelper.id());
    if (entityRegistryObj == null) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.entityregistry"));
    }
    this.entityRegistry = EntityRegistryHelper.narrow(entityRegistryObj);

    org.omg.CORBA.Object certificateRegistryObj =
      iComponent.getFacet(CertificateRegistryHelper.id());
    if (certificateRegistryObj == null) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.certificateregistry"));
    }
    this.certificateRegistry =
      CertificateRegistryHelper.narrow(certificateRegistryObj);

    org.omg.CORBA.Object interfaceRegistryObj =
      iComponent.getFacet(InterfaceRegistryHelper.id());
    if (interfaceRegistryObj == null) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.interfaceregistry"));
    }
    this.interfaceRegistry =
      InterfaceRegistryHelper.narrow(interfaceRegistryObj);

    org.omg.CORBA.Object offerRegistryObj =
      iComponent.getFacet(OfferRegistryHelper.id());
    if (offerRegistryObj == null) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.offerregistry"));
    }
    this.offerRegistry = OfferRegistryHelper.narrow(offerRegistryObj);

    org.omg.CORBA.Object loginRegistryObj =
      iComponent.getFacet(LoginRegistryHelper.id());
    if (loginRegistryObj == null) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.loginregistry"));
    }
    this.loginRegistry = LoginRegistryHelper.narrow(loginRegistryObj);

    //TODO: Esta vers�o do BusExplorer ainda tolera que o barramento n�o possua a interface
    //TODO: para reconfigura��o din�mica. Para mudar isso basta alterar o teste abaixo e
    //TODO: lan�ar a exce��o: throw new IncompatibleBus(LNG.get("IncompatibleBus.error.configuration"));
    org.omg.CORBA.Object configurationObj =
      iComponent.getFacet(ConfigurationHelper.id());
    if (configurationObj != null) {
      this.configuration = ConfigurationHelper.narrow(configurationObj);
    }
  }
}
