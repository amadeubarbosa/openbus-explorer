package busexplorer.exception;

import org.omg.CORBA.NO_PERMISSION;

import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.access_control.InvalidRemoteCode;
import tecgraf.openbus.core.v2_0.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_0.services.access_control.UnknownBusCode;
import tecgraf.openbus.core.v2_0.services.access_control.UnverifiedLoginCode;
import exception.handling.ExceptionContext;
import exception.handling.ExceptionHandler;
import exception.handling.ExceptionType;

/**
 * Tratador de exceções padrão para os demos.
 * 
 * @author Tecgraf
 */
public class BusExplorerExceptionHandler extends
  ExceptionHandler<BusExplorerHandlingException> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleException(BusExplorerHandlingException exception) {
    Exception theException = exception.getException();
    ExceptionType type = exception.getType();
    ExceptionContext context = exception.getContext();
    switch (type) {
      case AccessDenied:
        switch (context) {
          case LoginByPassword:
            exception.setErrorMessage(getString("access.denied.password"));
            break;

          case LoginByCertificate:
            exception.setErrorMessage(getString("access.denied.key"));
            break;

          default:
            exception.setErrorMessage(getString("access.denied"));
            break;
        }
        break;

      case ServiceFailure:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(getString("service.failure.core",
              ((ServiceFailure) theException).message));
            break;

          default:
            exception.setErrorMessage(getString("service.failure",
              ((ServiceFailure) theException).message));
            break;
        }
        break;

      case OBJECT_NOT_EXIST:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(getString("not.exist.core"));
            break;

          default:
            exception.setErrorMessage(getString("not.exist"));
            break;
        }
        break;

      case TRANSIENT:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(getString("transient.core"));
            break;

          default:
            exception.setErrorMessage(getString("transient"));
            break;
        }
        break;

      case COMM_FAILURE:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(getString("comm.failure.core"));
            break;

          default:
            exception.setErrorMessage(getString("comm.failure"));
        }
        break;

      case NO_PERMISSION:
        NO_PERMISSION noPermission = (NO_PERMISSION) theException;
        switch (context) {
          case Service:
            switch (noPermission.minor) {
              case NoLoginCode.value:
                exception.setErrorMessage(getString("no.permission.no.login"));
                break;

              case UnknownBusCode.value:
                exception.setErrorMessage(getString("unknown.bus"));
                break;

              case UnverifiedLoginCode.value:
                exception.setErrorMessage(getString("unverified.login"));
                break;

              case InvalidRemoteCode.value:
                exception
                  .setErrorMessage(getString("no.permission.invalid.remote"));
                break;
            }
            break;

          default:
            if (noPermission.minor == NoLoginCode.value) {
              exception.setErrorMessage(getString("no.permission.no.login"));
            }
            else {
              exception.setErrorMessage(getString("no.permission.unspected",
                noPermission.minor));
            }
            break;
        }
        break;

      case InvalidName:
        // Este erro nunca deveria ocorrer se o código foi bem escrito
        exception.setErrorMessage(getString("corba.invalid.name", theException
          .getMessage()));
        System.exit(1);
        break;

      case Unspecified:
      default:
        exception.setErrorMessage(getString("unspecified", theException
          .getClass().getName(), theException.getMessage()));
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BusExplorerHandlingException getHandlingException(
    Exception exception, ExceptionContext context) {
    return new BusExplorerHandlingException(exception, context);
  }

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param key a chave
   * @return o valor associado à chave.
   */
  protected String getString(String key) {
    return LNG.get(this.getClass().getSimpleName() + "." + key);
  }

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param key a chave
   * @param args argumentos a serem formatados na mensagem.
   * @return o valor associado à chave.
   */
  protected String getString(String key, Object... args) {
    return LNG.get(this.getClass().getSimpleName() + "." + key, args);
  }
}
