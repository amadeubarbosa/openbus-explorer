package admin;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TRANSIENT;

import scs.core.IComponent;
import scs.core.IComponentHelper;
import tecgraf.openbus.core.v2_0.BusObjectKey;
import tecgraf.openbus.core.v2_0.services.access_control.NoLoginCode;

/**
 * Cont�m m�todos/constantes �teis para acesso ao barramento
 * 
 * @author Tecgraf
 */
public class Util {

  // TODO: [tmartins] Localiza��o (idioma) da classe. 

  /**
   * Mensagem em caso de exce��o TRANSIENT.
   * 
   * @see org.omg.CORBA.TRANSIENT
   */
  static final String TRANSIENT_EXCEPTION_MESSAGE =
    "O barramento est� inacess�vel no momento";

  /**
   * Mensagem em caso de exce��o COMM_FAILURE.
   * 
   * @see org.omg.CORBA.COMM_FAILURE
   */
  static final String COMM_FAILURE_EXCEPTION_MESSAGE =
    "Falha de comunica��o ao acessar os servi�os n�cleo do barramento";

  /**
   * Mensagem em caso de exce��o NO_PERMISSION com minor code NoLoginCode.
   * 
   * @see org.omg.CORBA.NO_PERMISSION
   * @see tecgraf.openbus.core.v2_0.services.access_control.NoLoginCode
   */
  static final String NO_LOGIN_EXCEPTION_MESSAGE =
    "N�o h� um login de v�lido no momento";

  /**
   * Mensagem em caso de exce��o UnauthorizedOperation.
   * 
   * @see tecgraf.openbus.core.v2_0.services.UnauthorizedOperation
   */
  public static final String UNAUTHORIZED_OPERATION_EXCEPTION_MESSAGE =
    "Opera��o n�o autorizada";

  /**
   * Mensagem em caso de exce��o InvalidInterface.
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InvalidInterface
   */
  public static final String INVALID_INTERFACE_EXCEPTION_MESSAGE =
    "Nome da interface inv�lido";

  /**
   * Mensagem em caso de exce��o EntityAlreadyRegistered
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityAlreadyRegistered
   */
  public static final String ENTITY_ALREADY_REGISTERED_EXCEPTION_MESSAGE =
    "Entidade j� registrada no barramento";

  /**
   * Mensagem em caso de exce��o EntityCategoryAlreadyExists.
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryAlreadyExists
   */
  public static final String ENTITY_CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE =
    "Categoria j� existente no barramento";

  /**
   * Mensagem em caso exce��o EntityCategoryInUse.
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryInUse
   */
  public static final String ENTITY_CATEGORY_IN_USE_EXCEPTION_MESSAGE =
    "Categoria em uso por uma ou mais entidades";

  /**
   * Mensagem em caso de exce��o InterfaceInUse.
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.InterfaceInUse
   */
  public static final String INTERFACE_IN_USE_EXCEPTION_MESSAGE =
    "Interface em uso por uma ou mais entidades";

  /**
   * Mensagem em caso de exce��o AuthorizationInUse.
   * 
   * @see tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.AuthorizationInUse
   */
  public static final String AUTHORIZATION_IN_USE_EXCEPTION_MESSAGE =
    "Autoriza��o em uso por uma ou mais ofertas";

  /**
   * Mensagem em caso de exce��o InvalidCertificate.
   * 
   * @see tecgraf.openbus.core.v2_0.services.access_control.admin.v1_0.InvalidCertificate
   */
  public static final String INVALID_CERTIFICATE_EXCEPTION_MESSAGE =
    "Certificado inv�lido";

  /**
   * Constr�i a URL corbaloc
   * 
   * @param host endere�o do barramento
   * @param port porta do barramento
   * @param orb inst�ncia do orb do barramento
   * @return a corbaloc criada
   */
  private static org.omg.CORBA.Object buildCorbaLoc(String host, short port,
    ORB orb) {
    String str =
      String.format("corbaloc::1.0@%s:%d/%s", host, port, BusObjectKey.value);

    return orb.string_to_object(str);
  }

  /**
   * Obt�m a faceta IComponent do barramento
   * 
   * @param host endere�o do barramento
   * @param port porta do barramento
   * @param orb inst�ncia do orb do barramento
   * @return a faceta IComponent
   */
  public static IComponent getIComponent(String host, short port, ORB orb) {
    org.omg.CORBA.Object obj = buildCorbaLoc(host, port, orb);

    try {
      return IComponentHelper.narrow(obj);
    }
    catch (TRANSIENT e) {
      throw new TRANSIENT(String.format(Util.TRANSIENT_EXCEPTION_MESSAGE));
    }
    catch (COMM_FAILURE e) {
      throw new COMM_FAILURE(Util.COMM_FAILURE_EXCEPTION_MESSAGE);
    }
    catch (NO_PERMISSION e) {
      if (e.minor == NoLoginCode.value) {
        throw new COMM_FAILURE(Util.NO_LOGIN_EXCEPTION_MESSAGE);
      }
    }

    return null;
  }
}
