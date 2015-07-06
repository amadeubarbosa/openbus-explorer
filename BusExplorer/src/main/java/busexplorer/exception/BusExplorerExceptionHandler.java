package busexplorer.exception;

import org.omg.CORBA.NO_PERMISSION;

import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.access_control.FailedLoginAttemptDomain;
import tecgraf.openbus.core.v2_1.services.access_control.InvalidRemoteCode;
import tecgraf.openbus.core.v2_1.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_1.services.access_control.TooManyAttempts;
import tecgraf.openbus.core.v2_1.services.access_control.UnknownBusCode;
import tecgraf.openbus.core.v2_1.services.access_control.UnverifiedLoginCode;
import busexplorer.utils.Utils;
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
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "access.denied.password"));
            break;

          case LoginByCertificate:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "access.denied.key"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "access.denied"));
            break;
        }
        break;

      case TooManyAttempts:
        TooManyAttempts tooMany = (TooManyAttempts) theException;
        switch (tooMany.domain.value()) {
          case FailedLoginAttemptDomain._ADDRESS:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "too.many.attempts.address", tooMany.penaltyTime));
            break;
          case FailedLoginAttemptDomain._ENTITY:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "too.many.attempts.entity", tooMany.penaltyTime));
            break;
          case FailedLoginAttemptDomain._VALIDATOR:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "too.many.attempts.validator", tooMany.penaltyTime));
            break;
        }
        break;

      case UnknownDomain:
        exception.setErrorMessage(Utils.getString(this.getClass(),
          "unknown.domain"));
        break;

      case ServiceFailure:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "service.failure.core", ((ServiceFailure) theException).message));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "service.failure", ((ServiceFailure) theException).message));
            break;
        }
        break;

      case UnauthorizedOperation:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "unauthorized.operation.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "unauthorized.operation"));
            break;
        }
        break;

      case EntityAlreadyRegistered:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "entity.already.registered.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "entity.already.registered"));
            break;
        }
        break;

      case EntityCategoryAlreadyExists:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "category.already.exists.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "category.already.exists"));
            break;
        }
        break;

      case InvalidCertificate:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "invalid.certificate.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "invalid.certificate"));
            break;
        }
        break;

      case InterfaceInUse:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "interface.inuse.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "interface.inuse"));
            break;
        }
        break;

      case InvalidInterface:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "invalid.interface.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "invalid.interface"));
            break;
        }
        break;

      case AuthorizationInUse:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "authorization.inuse.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "authorization.inuse"));
            break;
        }
        break;

      case OBJECT_NOT_EXIST:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "not.exist.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "not.exist"));
            break;
        }
        break;

      case TRANSIENT:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "transient.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "transient"));
            break;
        }
        break;

      case COMM_FAILURE:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "comm.failure.core"));
            break;

          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "comm.failure"));
        }
        break;

      case NO_PERMISSION:
        NO_PERMISSION noPermission = (NO_PERMISSION) theException;
        switch (context) {
          case Service:
            switch (noPermission.minor) {
              case NoLoginCode.value:
                exception.setErrorMessage(Utils.getString(this.getClass(),
                  "no.permission.no.login"));
                break;

              case UnknownBusCode.value:
                exception.setErrorMessage(Utils.getString(this.getClass(),
                  "unknown.bus"));
                break;

              case UnverifiedLoginCode.value:
                exception.setErrorMessage(Utils.getString(this.getClass(),
                  "unverified.login"));
                break;

              case InvalidRemoteCode.value:
                exception.setErrorMessage(Utils.getString(this.getClass(),
                  "no.permission.invalid.remote"));
                break;
            }
            break;

          default:
            if (noPermission.minor == NoLoginCode.value) {
              exception.setErrorMessage(Utils.getString(this.getClass(),
                "no.permission.no.login"));
            }
            else {
              exception.setErrorMessage(Utils.getString(this.getClass(),
                "no.permission.unspected", noPermission.minor));
            }
            break;
        }
        break;

      case InvalidName:
        // Este erro nunca deveria ocorrer se o código foi bem escrito
        exception.setErrorMessage(Utils.getString(this.getClass(),
          "corba.invalid.name", theException.getMessage()));
        System.exit(1);
        break;

      case IncompatibleBus:
        switch (context) {
          case LoginByPassword:
          case LoginByCertificate:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "incompatible.bus.login", theException.getMessage()));
            break;
          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "incompatible.bus", theException.getMessage()));
            break;
        }
        break;

      case IllegalArgumentException:
        switch (context) {
          case LoginByPassword:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "illegal.argument.login", theException.getMessage()));
            break;
          default:
            exception.setErrorMessage(Utils.getString(this.getClass(),
              "illegal.argument", theException.getMessage()));
            break;
        }
        break;

      case Unspecified:
      default:
        exception.setErrorMessage(Utils.getString(this.getClass(),
          "unspecified", theException.getClass().getName(), theException
            .getMessage()));
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

}
