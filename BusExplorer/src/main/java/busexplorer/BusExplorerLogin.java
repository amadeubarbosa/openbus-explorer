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
  /** Inst�ncia de administra��o do baramento. */
  private final BusAdmin admin;
  /** Indica se o login possui permiss�es administrativas. */
  private boolean adminRights = false;
  /** Assistente de conex�o ao barramento. */
  private Assistant assistant;

  /**
   * Construtor
   *
   * @param admin Inst�ncia de administra��o do barramento.
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
   * Obt�m o assistente utilizado para o login.
   *
   * @return o assistente utilizado para o login.
   */
  public Assistant getAssistant() {
    return assistant;
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
   * Conecta-se a uma inst�ncia de administra��o do barramento.
   */
  private void connectToAdmin() {
    if (admin instanceof BusAdminImpl) {
      ((BusAdminImpl) admin).connect(host, port, assistant.orb());
    }
  }

  /**
   * Realiza o login.
   *
   * @param login Informa��es de login.
   * @param password Senha.
   */
  public static void doLogin(BusExplorerLogin login, String password) throws
    Exception {
    /** Intervalo de tempo para verificar se o login j� foi efetuado */
    final int LOGIN_CHECK_INTERVAL = 250;
    /** N�mero m�ximo de tentativas de login */
    final int MAX_LOGIN_FAILS = 3;

    OnFailureCallbackWithException callback =
     new OnFailureCallbackWithException() {
      volatile int failedAttempts = 0;

      volatile Exception exception = null;

      @Override
      public void onStartSharedAuthFailure(Assistant arg0, Throwable arg1) {
        // n�o iremos utilizar este recurso
      }

      @Override
      public void onRegisterFailure(Assistant arg0, IComponent arg1,
        ServiceProperty[] arg2, Throwable arg3) {
        // n�o iremos utilizar este recurso
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
   * Extens�o da callback de notifica��o de falhas do Assistente OpenBus.
   * Permite a recupera��o de poss�veis exce��es.
   */
  private interface OnFailureCallbackWithException extends OnFailureCallback {
    /**
     * Recupera uma poss�vel exce��o durante a execu��o de uma das tarefas do
     * assistente.
     *
     * @return Uma exce��o durante a execu��o de uma tarefa. Nulo, se n�o houve
     * disparo de exce��o.
     */
    public Exception getException();
  }

}
