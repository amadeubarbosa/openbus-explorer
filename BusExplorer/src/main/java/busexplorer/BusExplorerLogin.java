package busexplorer;

import busexplorer.utils.BusAddress;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TRANSIENT;
import tecgraf.openbus.Connection;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.admin.BusAdmin;
import tecgraf.openbus.admin.BusAdminImpl;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_1.BusObjectKey;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.NoLoginCode;

/**
 * Trata, analisa e armazena dados de login no barramento.
 *
 * @author Tecgraf
 */
public class BusExplorerLogin {
  /** Entidade. */
  public final String entity;
  /** Endere�o. */
  public final BusAddress address;
  /** Inst�ncia de administra��o do baramento. */
  private final BusAdminImpl admin;
  /** Indica se o login possui permiss�es administrativas. */
  private boolean adminRights = false;
  /** Contexto de conex�es OpenBus */
  private OpenBusContext context;
  /** Conex�o com o barramento */
  private Connection conn;
  /** Tentativas de login */
  private static final short MAX_RETRIES = 3;
  /**
   * Construtor
   *
   * @param admin Inst�ncia de administra��o do barramento.
   * @param entity Entidade.
   * @param address endere�o do barramento.
   */
  public BusExplorerLogin(BusAdmin admin, String entity, BusAddress
    address) {
    this.admin = (BusAdminImpl)admin;
    this.entity = entity;
    this.address = address;
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
   * @throws ServiceFailure
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
   * Obt�m as facetas de administra��o do barramento.
   */
  private void getAdminFacets() {
    ORB orb = conn.ORB();
    switch (address.getType()) {
      case Address:
        admin.getAdminFacets(address.getHost(), address.getPort(), orb);
        break;
      case Reference:
        Object ref = orb.string_to_object(address.getIOR());
        admin.getAdminFacets(ref);
        break;
      default:
        throw new IllegalStateException(
          "Informa��es sobre barramento inv�lidas");
    }
  }

  /**
   * Obt�m o contexto da comunica��o com o barramento.
   * @return OpenBusContext o contexto da biblioteca do OpenBus.
   */
  public OpenBusContext getOpenBusContext() {
    return this.context;
  }

  /**
   * Realiza o login.
   *
   * @param login Informa��es de login.
   * @param password Senha.
   * @param domain Dom�nio
   * @throws Exception
   */
  public static void doLogin(BusExplorerLogin login, String password,
    String domain) throws Exception {
    ORB orb = ORBInitializer.initORB();
    login.context = (OpenBusContext) orb.resolve_initial_references
      ("OpenBusContext");
    String corbaloc = "unspecified";
    switch (login.address.getType()) {
      case Address:
        corbaloc = String.format("corbaloc::1.0@%s:%d/%s",
                login.address.getHost(),
                login.address.getPort(),
                BusObjectKey.value);
        break;
      case Reference:
        corbaloc = login.address.getIOR();
        break;
    }

    try {
      Object ref = orb.string_to_object(corbaloc);
      login.conn = login.context.connectByReference(ref);
    } catch (BAD_PARAM e) {
      throw new IllegalArgumentException(corbaloc, e);
    }

    login.context.defaultConnection(login.conn);

    boolean done = false;
    Exception lastFailure = null;
    for (short i = 0; i < MAX_RETRIES; i++) {
      try {
        login.conn.loginByPassword(login.entity, password.getBytes(), domain);
        login.getAdminFacets();
        login.checkAdminRights();
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
      }
      catch (Exception e) {
        login.logout();
        throw e;
      }

      try {
        Thread.sleep(250);
      }
      catch (InterruptedException ignored) {}
    }

    if (!done) {
      throw new IllegalArgumentException(corbaloc, lastFailure);
    }
  }
}
