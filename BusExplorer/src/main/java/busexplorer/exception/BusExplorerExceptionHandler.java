package busexplorer.exception;

import busexplorer.exception.handling.ExceptionContext;
import busexplorer.exception.handling.ExceptionHandler;
import busexplorer.exception.handling.ExceptionType;
import busexplorer.utils.Language;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.TRANSIENT;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.access_control.FailedLoginAttemptDomain;
import tecgraf.openbus.core.v2_1.services.access_control.InvalidRemoteCode;
import tecgraf.openbus.core.v2_1.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_1.services.access_control.TooManyAttempts;
import tecgraf.openbus.core.v2_1.services.access_control.UnknownBusCode;
import tecgraf.openbus.core.v2_1.services.access_control.UnverifiedLoginCode;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.AuthorizationInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.InterfaceInUse;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

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
            exception.setErrorMessage(Language.get(this.getClass(),
              "access.denied.password"));
            break;

          case LoginByPrivateKey:
            exception.setErrorMessage(Language.get(this.getClass(),
              "access.denied.key"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "access.denied"));
            break;
        }
        break;

      case TooManyAttempts:
        TooManyAttempts tooMany = (TooManyAttempts) theException;
        switch (tooMany.domain.value()) {
          case FailedLoginAttemptDomain._ADDRESS:
            exception.setErrorMessage(Language.get(this.getClass(),
              "too.many.attempts.address", tooMany.penaltyTime));
            break;
          case FailedLoginAttemptDomain._ENTITY:
            exception.setErrorMessage(Language.get(this.getClass(),
              "too.many.attempts.entity", tooMany.penaltyTime));
            break;
          case FailedLoginAttemptDomain._VALIDATOR:
            exception.setErrorMessage(Language.get(this.getClass(),
              "too.many.attempts.validator", tooMany.penaltyTime));
            break;
        }
        break;

      case UnknownDomain:
        exception.setErrorMessage(Language.get(this.getClass(),
          "unknown.domain"));
        break;

      case ServiceFailure:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "service.failure.core", ((ServiceFailure) theException).message));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "service.failure", ((ServiceFailure) theException).message));
            break;
        }
        break;

      case UnauthorizedOperation:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "unauthorized.operation.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "unauthorized.operation"));
            break;
        }
        break;

      case EntityAlreadyRegistered:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "entity.already.registered.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "entity.already.registered"));
            break;
        }
        break;

      case EntityCategoryAlreadyExists:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "category.already.exists.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "category.already.exists"));
            break;
        }
        break;

      case InvalidCertificate:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "invalid.certificate.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "invalid.certificate"));
            break;
        }
        break;

      case InterfaceInUse:
        StringBuilder builder = new StringBuilder();
        RegisteredEntityDesc[] entities = ((InterfaceInUse) theException).entities;
        for (int i = 0; i < entities.length; i++) {
          builder.append(entities[i].id);
          if (i+1 < entities.length) {
            builder.append(" ,");
          }
        }
        exception.setErrorMessage(Language.get(this.getClass(),
          "interface.inuse", builder.toString()));
        break;

      case InvalidInterface:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(Language.get(this.getClass(),
              "invalid.interface.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "invalid.interface"));
            break;
        }
        break;

      case AuthorizationInUse:
        builder = new StringBuilder();
        ServiceOfferDesc[] offers = ((AuthorizationInUse) theException).offers;
        for (int i = 0; i < offers.length; i++) {
          for (ServiceProperty prop : offers[i].properties) {
            if (prop.name.equals("openbus.offer.id")) {
              builder.append(prop.value);
              break;
            }
          }
          if (i+1 < offers.length) {
            builder.append(" ,");
          }
        }
        exception.setErrorMessage(Language.get(this.getClass(),
          "authorization.inuse", builder.toString()));
        break;

      case OBJECT_NOT_EXIST:
        switch (context) {
          case BusCore:
          case LoginByPassword:
          case LoginByPrivateKey:
            exception.setErrorMessage(Language.get(this.getClass(),
              "not.exist.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "not.exist"));
            break;
        }
        break;

      case TRANSIENT:
        switch (context) {
          case BusCore:
          case LoginByPassword:
          case LoginByPrivateKey:
            exception.setErrorMessage(Language.get(this.getClass(),
              "transient.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "transient"));
            break;
        }
        break;

      case COMM_FAILURE:
        switch (context) {
          case BusCore:
          case LoginByPassword:
          case LoginByPrivateKey:
            exception.setErrorMessage(Language.get(this.getClass(),
              "comm.failure.core"));
            break;

          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "comm.failure"));
        }
        break;

      case NO_PERMISSION:
        NO_PERMISSION noPermission = (NO_PERMISSION) theException;
        switch (context) {
          case Service:
            switch (noPermission.minor) {
              case NoLoginCode.value:
                exception.setErrorMessage(Language.get(this.getClass(),
                  "no.permission.no.login"));
                break;

              case UnknownBusCode.value:
                exception.setErrorMessage(Language.get(this.getClass(),
                  "unknown.bus"));
                break;

              case UnverifiedLoginCode.value:
                exception.setErrorMessage(Language.get(this.getClass(),
                  "unverified.login"));
                break;

              case InvalidRemoteCode.value:
                exception.setErrorMessage(Language.get(this.getClass(),
                  "no.permission.invalid.remote"));
                break;
            }
            break;

          default:
            if (noPermission.minor == NoLoginCode.value) {
              exception.setErrorMessage(Language.get(this.getClass(),
                "no.permission.no.login"));
            }
            else {
              exception.setErrorMessage(Language.get(this.getClass(),
                "no.permission.unspected", noPermission.minor));
            }
            break;
        }
        break;

      case InvalidName:
        // Esse erro sobre a configuração do POA só ocorre por falha no SDK
        exception.setErrorMessage(Language.get(this.getClass(),
            "unspecified", theException.getClass().getName(),
                theException.getMessage()));
        break;

      case IncompatibleBus:
        switch (context) {
          case LoginByPassword:
          case LoginByPrivateKey:
            exception.setErrorMessage(Language.get(this.getClass(),
              "incompatible.bus.login", theException.getMessage()));
            break;
          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "incompatible.bus", theException.getMessage()));
            break;
        }
        break;

      case IllegalArgumentException:
        switch (context) {
          case LoginByPrivateKey:
          case LoginByPassword:
            Throwable cause = theException.getCause();
            if ((cause != null) &&
                ( cause instanceof COMM_FAILURE ||
                  cause instanceof TRANSIENT ||
                  cause instanceof NO_PERMISSION )) {
                StringBuilder sb = new StringBuilder();
                sb.append(Language.get(this.getClass(),
                        "illegal.address", theException.getMessage()));
                sb.append("\n\n");
                sb.append(cause.getMessage());
                exception.setErrorMessage(sb.toString());
            } else {
              exception.setErrorMessage(Language.get(this.getClass(),
                "illegal.wrong.address", theException.getMessage()));
            }
            break;
          default:
            exception.setErrorMessage(Language.get(this.getClass(),
              "illegal.argument", theException.getMessage()));
            break;
        }
        break;

      case Unspecified:
      default:
        if (theException != null) {
          exception.setErrorMessage(Language.get(this.getClass(),
            "unspecified", theException.getClass().getName(), theException
              .getMessage()));
        } else {
          exception.setErrorMessage(Language.get(this.getClass(),
            "javaerror"));
        }
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
