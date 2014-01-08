package busexplorer.exception;

import org.omg.CORBA.NO_PERMISSION;

import tecgraf.openbus.core.v2_0.services.ServiceFailure;
import tecgraf.openbus.core.v2_0.services.access_control.InvalidRemoteCode;
import tecgraf.openbus.core.v2_0.services.access_control.NoLoginCode;
import tecgraf.openbus.core.v2_0.services.access_control.UnknownBusCode;
import tecgraf.openbus.core.v2_0.services.access_control.UnverifiedLoginCode;
import exception.handling.ExceptionContext;
import exception.handling.ExceptionHandler;
import exception.handling.ExceptionType;

/**
 * Tratador de exce��es padr�o para os demos.
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
            exception.setErrorMessage("a senha fornecida foi negada");
            break;

          case LoginByCertificate:
            exception
              .setErrorMessage("a chave n�o corresponde ao certificado da entidade cadastrado junto ao barramento");
            break;

          default:
            exception
              .setErrorMessage("autentica��o junto ao barramento falhou.");
            break;
        }
        break;

      case ServiceFailure:
        switch (context) {
          case BusCore:
            exception.setErrorMessage(String.format(
              "falha severa no barramento: %s",
              ((ServiceFailure) theException).message));
            break;

          default:
            exception.setErrorMessage(String.format(
              "falha severa no servi�o: %s",
              ((ServiceFailure) theException).message));
            break;
        }
        break;

      case OBJECT_NOT_EXIST:
        switch (context) {
          case BusCore:
            exception
              .setErrorMessage("n�o existe refer�ncia para o barramento no host e porta especificados.");
            break;

          default:
            exception.setErrorMessage("refer�ncia para o servi�o n�o existe");
            break;
        }
        break;

      case TRANSIENT:
        switch (context) {
          case BusCore:
            exception
              .setErrorMessage("o barramento est� inacess�vel no momento");
            break;

          default:
            exception.setErrorMessage("servi�o est� indispon�vel no momento.");
            break;
        }
        break;

      case COMM_FAILURE:
        switch (context) {
          case BusCore:
            exception
              .setErrorMessage("falha de comunica��o ao acessar servi�os n�cleo do barramento");
            break;

          default:
            exception
              .setErrorMessage("falha de comunica��o ao acessar servi�o.");
        }
        break;

      case NO_PERMISSION:
        NO_PERMISSION noPermission = (NO_PERMISSION) theException;
        switch (context) {
          case Service:
            switch (noPermission.minor) {
              case NoLoginCode.value:
                exception.setErrorMessage("n�o h� um login v�lido no momento");
                break;

              case UnknownBusCode.value:
                exception
                  .setErrorMessage("o servi�o encontrado n�o est� mais logado ao barramento");
                break;

              case UnverifiedLoginCode.value:
                exception
                  .setErrorMessage("o servi�o encontrado n�o foi capaz de validar a chamada");
                break;

              case InvalidRemoteCode.value:
                exception
                  .setErrorMessage("integra��o do servi�o encontrado com o barramento est� incorreta");
                break;
            }
            break;

          default:
            if (noPermission.minor == NoLoginCode.value) {
              exception.setErrorMessage("n�o h� um login v�lido no momento");
            }
            else {
              exception.setErrorMessage(String.format(
                "Erro NO_PERMISSION inesperado. Minor code = %d",
                noPermission.minor));
            }
            break;
        }
        break;

      case InvalidName:
        // Este erro nunca deveria ocorrer se o c�digo foi bem escrito
        exception.setErrorMessage(String.format("CORBA.InvalidName: %s",
          theException.getMessage()));
        System.exit(1);
        break;

      case Unspecified:
      default:
        exception.setErrorMessage(String.format(
          "Erro n�o esperado.\nExce��o: %s\nMensagem: %s", theException
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

}
