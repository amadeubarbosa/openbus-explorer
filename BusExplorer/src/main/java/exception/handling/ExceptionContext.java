package exception.handling;

/**
 * Enumera��o dos tipos de contextos
 * 
 * @author Tecgraf
 */
public enum ExceptionContext {
  /** Login por senha */
  LoginByPassword,
  /** Login por chave privada */
  LoginByPrivateKey,
  /** Chamadas ao n�cleo do barramento */
  BusCore,
  /** Chamadas a servi�os */
  Service,
}