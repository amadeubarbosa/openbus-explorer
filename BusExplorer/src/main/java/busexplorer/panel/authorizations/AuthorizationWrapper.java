package busexplorer.panel.authorizations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Classe que detém as informações locais da autorização para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class AuthorizationWrapper {
  /** objeto descritor de entidade autorizada */
  private RegisteredEntityDesc desc;

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
  public AuthorizationWrapper(RegisteredEntityDesc desc, String interfaceName) {
    this.desc = desc;
    this.id = desc.id;
    this.interfaceName = interfaceName;
  }

  /**
   * Compara um objeto à instância de {@link AuthorizationWrapper}.
   *
   * O método não leva em consideração o objeto descritor da autorização.
   * 
   * @param o Objeto a ser comparado.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AuthorizationWrapper)) {
      return false;
    }
    AuthorizationWrapper other = (AuthorizationWrapper) o;
    return id.equals(other.id) && interfaceName.equals(other.interfaceName);
  }

  /**
   * Código hash do objeto.
   *
   * @return Código hash do objeto.
   */
  @Override
  public int hashCode() {
    return id.hashCode() ^ interfaceName.hashCode();
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
   * Recupera o objeto descritor da entidade autorizada.
   *
   * @return o objeto descritor da entidade autorizada.
   */
  public RegisteredEntityDesc getEntityDescriptor() {
    return desc;
  }

  /**
   * Método utilitário para converter um mapa de autorizações em uma lista de
   * {@link AuthorizationWrapper}
   * 
   * @param authorizationsMap o mapa de autorizações
   * @return a lista de {@link AuthorizationWrapper}
   */
  public static List<AuthorizationWrapper> convertToInfo(Map<RegisteredEntityDesc,
    List<String>> authorizationsMap) {
    List<AuthorizationWrapper> list = new ArrayList<>();
    for (Map.Entry<RegisteredEntityDesc, List<String>> authorizations :
      authorizationsMap.entrySet()) {
      RegisteredEntityDesc entity = authorizations.getKey();
      for (String interfaceName : authorizations.getValue()) {
        list.add(new AuthorizationWrapper(entity, interfaceName));
      }
    }

    return list;
  }

}
