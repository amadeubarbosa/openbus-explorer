package busexplorer;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CORBA.TRANSIENT;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import com.google.common.collect.ArrayListMultimap;

import tecgraf.openbus.Connection;
import tecgraf.openbus.OfferObserver;
import tecgraf.openbus.OnReloginCallback;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.RemoteOffer;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.admin.BusAdminImpl;
import tecgraf.openbus.admin.BusAuditFacade;
import tecgraf.openbus.admin.BusAuditImpl;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.AccessDenied;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_1.services.access_control.TooManyAttempts;
import tecgraf.openbus.core.v2_1.services.access_control.UnknownDomain;
import tecgraf.openbus.core.v2_1.services.access_control.WrongEncoding;
import tecgraf.openbus.exception.AlreadyLoggedIn;
import tecgraf.openbus.extension.BusExtensionFacade;
import tecgraf.openbus.extension.BusExtensionImpl;

import busexplorer.utils.BusAddress;

/**
 * Trata, analisa e armazena dados de login no barramento.
 *
 * @author Tecgraf
 */
public class BusExplorerLogin {
  /** Entidade. */
  public final LoginInfo info;
  /** Endereço. */
  public final BusAddress address;
  /** Domínio de autenticação */
  public final String domain;
  /** Instância de administração do baramento. */
  public BusAdminFacade admin;
  /** Instância de configuração da auditoria. */
  public BusAuditFacade audit;
  /** Instância da fachada de extensão a governança. */
  public BusExtensionFacade extension;
  /** Tentativas de login */
  private static final short MAX_RETRIES = 3;
  /** Contexto de conexões OpenBus */
  private final OpenBusContext context;
  /** Indica se o login possui permissões administrativas. */
  private boolean adminRights = false;
  /** Conexão com o barramento */
  private Connection conn;
  /** Callback de Relogin do OpenBus SDK Java que pode ser customizada pela aplicação */
  private OnReloginCallback onReloginCallback;

  /**
   * Construtor para o objeto que representa o login no barramento.
   * É realizada a validação do endereço usando os métodos fornecidos no {@link BusAddress}.
   *
   * @param address endereço do barramento
   * @param entity entidade a ser autenticada no barramento
   * @param domain domínio de autenticação para autenticar a entidade no barramento
   *
   * @see BusAddress#checkBusReference()
   * @see BusAddress#checkBusVersion()
   *
   * @throws InvalidName caso o ORB do OpenBus SDK Java não possua referência para {@link OpenBusContext}
   */
  public BusExplorerLogin(BusAddress address, String entity, String domain) throws InvalidName {
    address.checkBusVersion();
    address.checkBusReference();

    this.info = new LoginInfo();
    this.info.entity = entity;
    this.address = address;
    this.domain = domain;
    this.context = (OpenBusContext) ORBInitializer.initORB()
      .resolve_initial_references("OpenBusContext");
  }

  /** Permite a personalização da {@link OnReloginCallback} presente no OpenBus SDK Java
   *  e garante o comportamento necessário para atualizar as informações armazenadas no {@link BusExplorerLogin}.
   *
   *  @param callback implementação personalizada do {@link OnReloginCallback}
   */
  public void onRelogin(OnReloginCallback callback) {
    this.onReloginCallback = callback;
  }

  /**
   * Realiza o login no barramento usando a senha fornecida nessa chamada.
   *
   * @param password Senha.
   *
   * @throws AccessDenied caso as credenciais (entidade ou senha) não sejam válidas
   * @throws AlreadyLoggedIn caso a conexão com o barramento já esteja autenticada
   * @throws IllegalArgumentException caso não seja possível determinar o {@code corbaloc} do endereço do barramento
   * @throws ServantNotActive caso não seja possível instanciar o observador de ofertas do BusExtension
   * @throws ServiceFailure caso não seja possível acessar o serviço remoto
   * @throws TooManyAttempts caso tenham havido muitas tentativas repetidas de autenticação por senha no barramento com a mesma entidade
   * @throws UnknownDomain caso o domínio de autenticação não seja conhecido pelo barramento
   * @throws WrongEncoding caso tenha havido um erro na codificação do handshake do protocolo OpenBus (pode ser um erro interno na biblioteca do OpenBus SDK Java)
   * @throws WrongPolicy caso tenha havido um erro no adaptador de objetos que impediu a instanciação do observador de ofertas do BusExtension
   */
  public void doLogin(String password)
  throws WrongEncoding, AlreadyLoggedIn, ServiceFailure, UnknownDomain, TooManyAttempts, AccessDenied, ServantNotActive,
    WrongPolicy {
    Object reference = getORB().string_to_object(address.toIOR());
    conn = context.connectByReference(reference);
    context.defaultConnection(conn);

    boolean done = false;
    Exception lastFailure = null;
    for (short i = 0; i < MAX_RETRIES; i++) {
      try {
        conn.loginByPassword(info.entity, password.getBytes(), domain);
        conn.onReloginCallback((connection, oldLogin) -> {
          BusExplorerLogin.this.info.id = connection.login().id;
          try {
            BusExplorerLogin.this.checkAdminRights();
          } catch (ServiceFailure ex) {
            //TODO: a OnReloginCallback não aceita exceções, exibir outro diálogo?
            ex.printStackTrace();
          }
          if (onReloginCallback != null) {
            onReloginCallback.onRelogin(connection, oldLogin);
          }
        });
        info.id = conn.login().id;
        admin = new BusAdminImpl(reference);
        audit = new BusAuditImpl(reference, conn);
        extension = new BusExtensionImpl(conn.offerRegistry());
        // observador do registro de ofertas do busextension
        ArrayListMultimap<String, String> busExtensionServiceProps = ArrayListMultimap.create();
        busExtensionServiceProps.put(BusExtensionImpl.SEARCH_CRITERIA_KEY, BusExtensionImpl.SEARCH_CRITERIA_VALUE);
        conn.offerRegistry().subscribeObserver(offer -> {
          try {
            // observador da remoção da nova oferta registrada
            offer.subscribeObserver(new OfferObserver() {
              @Override
              public void propertiesChanged(RemoteOffer offer) {
              }

              @Override
              public void removed(RemoteOffer offer) {
                // disparo da callback também quando a oferta for novamente removida
                onReloginCallback.onRelogin(context.defaultConnection(), conn.login());
              }
            });
          }
          catch (ServantNotActive | WrongPolicy ex) {
            // TODO: subscrição não aceita lançar exceção, exibir outro diálogo?
            ex.printStackTrace();
          }
          // callback que dispara o update nos paineis
          onReloginCallback.onRelogin(context.defaultConnection(), conn.login());
        }, busExtensionServiceProps);
        checkAdminRights();
        done = true;
        break;
      }
      catch (TRANSIENT | COMM_FAILURE e) {
        // retentar
        lastFailure = e;
      }
      catch (NO_PERMISSION e) {
        if (e.minor != NoLoginCode.value) {
          throw e;
        }
        // retentar
        lastFailure = e;
      }
      catch (Exception e) {
        logout();
        throw e;
      }

      try {
        Thread.sleep(250);
      }
      catch (InterruptedException ignored) {}
    }

    if (!done) {
      throw new IllegalArgumentException(address.toIOR(), lastFailure);
    }
  }

  /**
   * Indica se o usuário autenticado no momento possui permissão de
   * administração.
   *
   * @return <code>true</code> se possui permissão de administração e
   *         <code>false</code> caso contrário. Se o login não foi realizado, o
   *         retorno será <code>false</code>.
   */
  public boolean hasAdminRights() {
    return adminRights;
  }

  /**
   * Realiza o shutdown do ORB e do assistente, terminando o login.
   */
  public void logout() {
    try {
      if (conn != null) {
        org.omg.CORBA.ORB orb = conn.ORB();
        conn.logout();
        orb.shutdown(true);
      }
    } catch (Exception ignored) {}
  }

  /**
   * Realiza uma verificação sobre a permissão de administração deste login e
   * armazena o resultado em uma membro auxiliar.
   *
   * @throws ServiceFailure caso aconteça um erro imprevisto no serviço remoto
   */
  private void checkAdminRights() throws ServiceFailure {
    try {
      admin.getLogins();
      adminRights = true;
    }
    catch (UnauthorizedOperation e) {
      adminRights = false;
    }
  }

  /**
   * Obtém o contexto da comunicação com o barramento.
   * @return o contexto da biblioteca do OpenBus.
   */
  public OpenBusContext getOpenBusContext() {
    return context;
  }

  /**
   * Obtém o ORB associado ao contexto da comunicação com o barramento.
   * @return a instância de {@link ORB} obtida através do {@link OpenBusContext#ORB()}
   */
  public ORB getORB() {
    return getOpenBusContext().ORB();
  }
}
