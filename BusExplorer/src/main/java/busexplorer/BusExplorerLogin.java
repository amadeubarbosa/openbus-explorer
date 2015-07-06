package busexplorer;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TRANSIENT;

import scs.core.IComponent;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.assistant.Assistant;
import tecgraf.openbus.assistant.AssistantParams;
import tecgraf.openbus.assistant.OnFailureCallback;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.access_control.AccessDenied;
import tecgraf.openbus.core.v2_1.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_1.services.access_control.TooManyAttempts;
import tecgraf.openbus.core.v2_1.services.access_control.UnknownDomain;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import admin.BusAdmin;
import admin.BusAdminImpl;
import busexplorer.utils.BusAddress;

/**
 * Trata, analisa e armazena dados de login no barramento.
 *
 * @author Tecgraf
 */
public class BusExplorerLogin {
  /** Entidade. */
  public final String entity;
  /** Endereço. */
  public final BusAddress address;
  /** Instância de administração do baramento. */
  private final BusAdmin admin;
  /** Indica se o login possui permissões administrativas. */
  private boolean adminRights = false;
  /** Assistente de conexão ao barramento. */
  private Assistant assistant;

  /**
   * Construtor
   *
   * @param admin Instância de administração do barramento.
   * @param entity Entidade.
   * @param address endereço do barramento.
   */
  public BusExplorerLogin(BusAdmin admin, String entity, BusAddress address) {
    this.admin = admin;
    this.entity = entity;
    this.address = address;
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
   * Obtém o assistente utilizado para o login.
   *
   * @return o assistente utilizado para o login.
   */
  public Assistant getAssistant() {
    return assistant;
  }

  /**
   * Realiza o shutdown do ORB e do assistente, terminando o login.
   */
  public void logout() {
    if (assistant == null) {
      return;
    }
    org.omg.CORBA.ORB orb = assistant.orb();
    assistant.shutdown();
    orb.shutdown(true);
  }

  /**
   * Realiza uma verificação sobre a permissão de administração deste login e
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
   * Conecta-se a uma instância de administração do barramento.
   */
  private void connectToAdmin() {
    BusAdminImpl admin = (BusAdminImpl) this.admin;
    ORB orb = assistant.orb();
    switch (address.getType()) {
      case Address:
        admin.connect(address.getHost(), address.getPort(), orb);
        break;
      case Reference:
        Object ref = orb.string_to_object(address.getIOR());
        admin.connect(ref, orb);
        break;
      default:
        throw new IllegalStateException(
          "Informações sobre barramento inválidas");
    }
  }

  /**
   * Realiza o login.
   *
   * @param login Informações de login.
   * @param password Senha.
   * @param domain Domínio
   * @throws Exception
   */
  public static void doLogin(BusExplorerLogin login, String password,
    String domain) throws Exception {
    /** Intervalo de tempo para verificar se o login já foi efetuado */
    final int LOGIN_CHECK_INTERVAL = 250;
    /** Número máximo de tentativas de login */
    final int MAX_LOGIN_FAILS = 3;

    OnFailureCallbackWithException callback =
      new OnFailureCallbackWithException() {
        volatile int failedAttempts = 0;

        volatile Exception exception = null;

        @Override
        public void onStartSharedAuthFailure(Assistant arg0, Exception arg1) {
          // não iremos utilizar este recurso
        }

        @Override
        public void onRegisterFailure(Assistant arg0, IComponent arg1,
          ServiceProperty[] arg2, Exception arg3) {
          // não iremos utilizar este recurso
        }

        @Override
        public void onLoginFailure(Assistant arg0, Exception arg1) {
          if (++failedAttempts == MAX_LOGIN_FAILS
            || arg1 instanceof AccessDenied || arg1 instanceof TooManyAttempts
            || arg1 instanceof UnknownDomain) {
            exception = arg1;
          }
        }

        @Override
        public void onFindFailure(Assistant arg0, Exception arg1) {
          // TODO precisamos realizar algum tratamento aqui?
          exception = arg1;
        }

        @Override
        public Exception getException() {
          return exception;
        }
      };

    AssistantParams params;
    ORB orb = ORBInitializer.initORB();
    switch (login.address.getType()) {
      case Address:
        params =
          new AssistantParams(login.address.getHost(), login.address.getPort());
        break;
      case Reference:
        Object ref = orb.string_to_object(login.address.getIOR());
        params = new AssistantParams(ref);
        break;
      default:
        throw new IllegalStateException(
          "Informações sobre barramento inválidas");
    }
    params.callback = callback;
    params.orb = orb;

    login.assistant =
      Assistant.createWithPassword(params, login.entity, password.getBytes(),
        domain);

    OpenBusContext context =
      (OpenBusContext) login.assistant.orb().resolve_initial_references(
        "OpenBusContext");
    while (true) {
      try {
        if (!context.getOfferRegistry()._non_existent()) {
          login.connectToAdmin();
          login.checkAdminRights();
          break;
        }
      }
      catch (TRANSIENT e) {
        // retentar
      }
      catch (COMM_FAILURE e) {
        // retentar
      }
      catch (NO_PERMISSION e) {
        if (e.minor == NoLoginCode.value) {
          // retentar
        }
        else {
          throw e;
        }
      }

      if (callback.getException() != null) {
        login.logout();
        throw callback.getException();
      }

      try {
        Thread.sleep(LOGIN_CHECK_INTERVAL);
      }
      catch (InterruptedException e) {
      }
    }
  }

  /**
   * Extensão da callback de notificação de falhas do Assistente OpenBus.
   * Permite a recuperação de possíveis exceções.
   */
  private interface OnFailureCallbackWithException extends OnFailureCallback {
    /**
     * Recupera uma possível exceção durante a execução de uma das tarefas do
     * assistente.
     *
     * @return Uma exceção durante a execução de uma tarefa. Nulo, se não houve
     *         disparo de exceção.
     */
    public Exception getException();
  }

}
