package busexplorer.exception.handling;

/**
 * Esta enumera��o deveria manter todas as exce��es que se deseja tratar pelo
 * {@link ExceptionHandler}. Como n�o � poss�vel estender um enum, deve-se
 * definir uma nova enumera��o. O {@link ExceptionHandler} ir� obter esta
 * enumera��o atrav�s da defini��o de uma {@link HandlingException}.
 * 
 * @author Tecgraf
 */
public enum ExceptionType {
  // Contexto de Login (LoginBy* + BusCore)
  AccessDenied,
  AlreadyLoggedIn,
  UnknownDomain,
  TooManyAttempts,
  // Contexto BusCore
  ServiceFailure,
  UnauthorizedOperation,
  UnauthorizedFacets,
  InvalidService,
  EntityAlreadyRegistered,
  EntityCategoryAlreadyExists,
  InvalidCertificate,
  InterfaceInUse,
  InvalidInterface,
  AuthorizationInUse,
  // Exce��es CORBA
  InvalidName,
  NO_PERMISSION,
  COMM_FAILURE,
  TRANSIENT,
  OBJECT_NOT_EXIST,
  // Outros
  IncompatibleBus,
  IllegalArgumentException,
  /** Exce��es n�o categorizadas */
  Unspecified;

  /**
   * Recupera um {@link ExceptionType} a partir da exce��o real.
   * 
   * @param exception a exce��o real
   * @return a enumera��o que representa a exce��o
   */
  public static ExceptionType getType(Exception exception) {
    try {
      Class<? extends Exception> theClass = exception.getClass();
      return ExceptionType.valueOf(theClass.getSimpleName());
    }
    catch (IllegalArgumentException ex) {
      return ExceptionType.Unspecified;
    }
  }

}
