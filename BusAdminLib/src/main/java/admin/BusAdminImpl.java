package admin;

import org.omg.CORBA.ORB;
import scs.core.IComponent;
import scs.core.IComponentHelper;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.BusObjectKey;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_0.services.access_control.LoginRegistry;
import tecgraf.openbus.core.v2_0.services.access_control.LoginRegistryHelper;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.CertificateRegistry;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.CertificateRegistryHelper;
import tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.InvalidCertificate;
import tecgraf.openbus.core.v2_0.services.admin.v1_0.Configuration;
import tecgraf.openbus.core.v2_0.services.admin.v1_0.ConfigurationHelper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A classe implementa os comandos especificados na interface
 * {@link admin.BusAdmin}.
 *
 * @author Tecgraf
 */
public class BusAdminImpl implements BusAdmin {
  /** Host do barramento. */
  private String host;
  /** Porta do barramento. */
  private int port;
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
  /** Configuração dinâmica do barramento. */
  private Configuration configuration = null;

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
    this.host = host;
    this.port = port;
    this.orb = orb;
    obtainFacetReferences();
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
   * CONFIGURAÇÕES DINÂMICAS
   */

  /**
   * {@inheritDoc}
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
  public void addValidator(String validator) throws ServiceFailure, UnauthorizedOperation {
    configuration.addValidator(validator);
  }

  @Override
  public void delValidator(String validator) throws ServiceFailure, UnauthorizedOperation {
    configuration.delValidator(validator);
  }

  @Override
  public List<String> getValidators() throws ServiceFailure {
    String[] validators = configuration.getValidators();
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
   * Métodos auxiliares
   */

  /**
   * Obtém as facetas dos registros do barramento.
   */
  private void obtainFacetReferences() {
    String corbaLocStr =
            String.format("corbaloc::1.0@%s:%d/%s", host, port, BusObjectKey.value);

    IComponent iComponent = IComponentHelper.narrow(orb.string_to_object(corbaLocStr));

    if (iComponent == null) {
      throw new IncompatibleBus(
        LNG.get("IncompatibleBus.missing.icomponent",
        new Object[]{corbaLocStr}));
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

    //TODO: Esta versão do BusExplorer ainda tolera que o barramento não possua a interface
    //TODO: para reconfiguração dinâmica. Para mudar isso basta alterar o teste abaixo e
    //TODO: lançar a exceção: throw new IncompatibleBus(LNG.get("IncompatibleBus.error.configuration"));
    org.omg.CORBA.Object configurationObj =
      iComponent.getFacet(ConfigurationHelper.id());
    if (configurationObj != null) {
      this.configuration = ConfigurationHelper.narrow(configurationObj);

    }
  }
}
