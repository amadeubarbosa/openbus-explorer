package busexplorer;

import scs.core.IComponent;
import tecgraf.diagnostic.addons.openbus.v20.OpenBusMonitor;
import tecgraf.diagnostic.commom.StatusCode;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.assistant.Assistant;
import tecgraf.openbus.assistant.AssistantParams;
import tecgraf.openbus.assistant.OnFailureCallback;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_0.services.access_control.AccessDenied;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;
import admin.BusAdmin;
import admin.BusAdminImpl;

/**
 * Trata, analisa e armazena dados de login no barramento.
 *
 * @author Tecgraf
 */
public class BusExplorerLogin {
  /** Entidade. */
  public final String entity;
  /** Host. */
  public final String host;
  /** Porta. */
  public final int port;
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
   * @param host Host.
   * @param port Porta.
   */
  public BusExplorerLogin(BusAdmin admin, String entity, String host, int port)
  {
    this.admin = admin;
    this.entity = entity;
    this.host = host;
    this.port = port;
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
    if (admin instanceof BusAdminImpl) {
      ((BusAdminImpl) admin).connect(host, port, assistant.orb());
    }
  }

  /**
   * Realiza o login.
   *
   * @param login Informações de login.
   * @param password Senha.
   */
  public static void doLogin(BusExplorerLogin login, String password) throws
    Exception {
    /** Intervalo de tempo para verificar se o login já foi efetuado */
    final int LOGIN_CHECK_INTERVAL = 250;
    /** Número máximo de tentativas de login */
    final int MAX_LOGIN_FAILS = 3;

    OnFailureCallbackWithException callback =
     new OnFailureCallbackWithException() {
      volatile int failedAttempts = 0;

      volatile Exception exception = null;

      @Override
      public void onStartSharedAuthFailure(Assistant arg0, Throwable arg1) {
        // não iremos utilizar este recurso
      }

      @Override
      public void onRegisterFailure(Assistant arg0, IComponent arg1,
        ServiceProperty[] arg2, Throwable arg3) {
        // não iremos utilizar este recurso
      }

      @Override
      public void onLoginFailure(Assistant arg0, Throwable arg1) {
        if (++failedAttempts == MAX_LOGIN_FAILS ||
          arg1 instanceof AccessDenied) {
          exception = (Exception) arg1;
        }
      }

      @Override
      public void onFindFailure(Assistant arg0, Throwable arg1) {
        // TODO precisamos realizar algum tratamento aqui?
      }

      @Override
      public Exception getException() {
        return exception;
      }
    };

    AssistantParams params = new AssistantParams();
    params.callback = callback;

    login.assistant =
      Assistant.createWithPassword(login.host, login.port, login.entity,
        password.getBytes(), params);

    OpenBusMonitor monitor =
      new OpenBusMonitor("openbus", (OpenBusContext)
        login.assistant.orb().resolve_initial_references("OpenBusContext"));

    while (true) {
      if (monitor.checkResource().code == StatusCode.OK) {
        login.connectToAdmin();
        login.checkAdminRights();
        break;
      }
      if (callback.getException() != null) {
        login.assistant.shutdown();
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
     * disparo de exceção.
     */
    public Exception getException();
  }

}
