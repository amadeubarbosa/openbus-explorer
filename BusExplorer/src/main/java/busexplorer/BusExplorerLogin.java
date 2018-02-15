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
  /** Endere�o. */
  public final BusAddress address;
  /** Dom�nio de autentica��o */
  public final String domain;
  /** Inst�ncia de administra��o do baramento. */
  public BusAdminFacade admin;
  /** Inst�ncia de configura��o da auditoria. */
  public BusAuditFacade audit;
  /** Inst�ncia da fachada de extens�o a governan�a. */
  public BusExtensionFacade extension;
  /** Tentativas de login */
  private static final short MAX_RETRIES = 3;
  /** Contexto de conex�es OpenBus */
  private final OpenBusContext context;
  /** Indica se o login possui permiss�es administrativas. */
  private boolean adminRights = false;
  /** Conex�o com o barramento */
  private Connection conn;
  /** Callback de Relogin do OpenBus SDK Java que pode ser customizada pela aplica��o */
  private OnReloginCallback onReloginCallback;

  /**
   * Construtor para o objeto que representa o login no barramento.
   * � realizada a valida��o do endere�o usando os m�todos fornecidos no {@link BusAddress}.
   *
   * @param address endere�o do barramento
   * @param entity entidade a ser autenticada no barramento
   * @param domain dom�nio de autentica��o para autenticar a entidade no barramento
   *
   * @see BusAddress#checkBusReference()
   * @see BusAddress#checkBusVersion()
   *
   * @throws InvalidName caso o ORB do OpenBus SDK Java n�o possua refer�ncia para {@link OpenBusContext}
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

  /** Permite a personaliza��o da {@link OnReloginCallback} presente no OpenBus SDK Java
   *  e garante o comportamento necess�rio para atualizar as informa��es armazenadas no {@link BusExplorerLogin}.
   *
   *  @param callback implementa��o personalizada do {@link OnReloginCallback}
   */
  public void onRelogin(OnReloginCallback callback) {
    this.onReloginCallback = callback;
  }

  /**
   * Realiza o login no barramento usando a senha fornecida nessa chamada.
   *
   * @param password Senha.
   *
   * @throws AccessDenied caso as credenciais (entidade ou senha) n�o sejam v�lidas
   * @throws AlreadyLoggedIn caso a conex�o com o barramento j� esteja autenticada
   * @throws IllegalArgumentException caso n�o seja poss�vel determinar o {@code corbaloc} do endere�o do barramento
   * @throws ServantNotActive caso n�o seja poss�vel instanciar o observador de ofertas do BusExtension
   * @throws ServiceFailure caso n�o seja poss�vel acessar o servi�o remoto
   * @throws TooManyAttempts caso tenham havido muitas tentativas repetidas de autentica��o por senha no barramento com a mesma entidade
   * @throws UnknownDomain caso o dom�nio de autentica��o n�o seja conhecido pelo barramento
   * @throws WrongEncoding caso tenha havido um erro na codifica��o do handshake do protocolo OpenBus (pode ser um erro interno na biblioteca do OpenBus SDK Java)
   * @throws WrongPolicy caso tenha havido um erro no adaptador de objetos que impediu a instancia��o do observador de ofertas do BusExtension
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
            //TODO: a OnReloginCallback n�o aceita exce��es, exibir outro di�logo?
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
            // observador da remo��o da nova oferta registrada
            offer.subscribeObserver(new OfferObserver() {
              @Override
              public void propertiesChanged(RemoteOffer offer) {
              }

              @Override
              public void removed(RemoteOffer offer) {
                // disparo da callback tamb�m quando a oferta for novamente removida
                onReloginCallback.onRelogin(context.defaultConnection(), conn.login());
              }
            });
          }
          catch (ServantNotActive | WrongPolicy ex) {
            // TODO: subscri��o n�o aceita lan�ar exce��o, exibir outro di�logo?
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
   * Indica se o usu�rio autenticado no momento possui permiss�o de
   * administra��o.
   *
   * @return <code>true</code> se possui permiss�o de administra��o e
   *         <code>false</code> caso contr�rio. Se o login n�o foi realizado, o
   *         retorno ser� <code>false</code>.
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
   * Realiza uma verifica��o sobre a permiss�o de administra��o deste login e
   * armazena o resultado em uma membro auxiliar.
   *
   * @throws ServiceFailure caso aconte�a um erro imprevisto no servi�o remoto
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
   * Obt�m o contexto da comunica��o com o barramento.
   * @return o contexto da biblioteca do OpenBus.
   */
  public OpenBusContext getOpenBusContext() {
    return context;
  }

  /**
   * Obt�m o ORB associado ao contexto da comunica��o com o barramento.
   * @return a inst�ncia de {@link ORB} obtida atrav�s do {@link OpenBusContext#ORB()}
   */
  public ORB getORB() {
    return getOpenBusContext().ORB();
  }
}
