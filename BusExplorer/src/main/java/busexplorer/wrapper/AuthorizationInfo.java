package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Classe que detém as informações locais da autorização para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class AuthorizationInfo {
  /** Identificador da entidade autorizada */
  private final String id;
  /** Interface */
  private final String interfaceName;


  /**
   * Construtor.
   * 
   * @param desc descritor da entidade autorizada
   * @param interfaceName interface 
   */
  public AuthorizationInfo(RegisteredEntityDesc desc, String interfaceName) {
    this.id = desc.id;
    this.interfaceName = interfaceName;
  }


  /**
   * Recupera a interface.
   * 
   * @return a interface.
   */
  public String getInterface() {
    return interfaceName;
  }

  /**
   * Recupera o identificador da entidade autorizada.
   * 
   * @return o identificador da entidade autorizada.
   */
  public String getEntityId() {
    return id;
  }

  /**
   * Método utilitário para converter um mapa de autorizações em uma lista de
   * {@link AuthorizationInfo}
   * 
   * @param authorizationsMap o mapa de autorizações
   * @return a lista de {@link AuthorizationInfo}
   */
  public static List<AuthorizationInfo> convertToInfo(Map<RegisteredEntityDesc,
    List<String>> authorizationsMap) {
    List<AuthorizationInfo> list = new ArrayList<AuthorizationInfo>();
    for (Map.Entry<RegisteredEntityDesc, List<String>> authorizations :
      authorizationsMap.entrySet()) {
      RegisteredEntityDesc entity = authorizations.getKey();
      for (String interfaceName : authorizations.getValue()) {
        list.add(new AuthorizationInfo(entity, interfaceName));
      }
    }

    return list;
  }

}
