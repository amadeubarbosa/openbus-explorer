package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;

/**
 * Classe que det�m as informa��es locais de login para apresenta��o em tabelas.
 * 
 * @author Tecgraf
 */
public class LoginInfoInfo {
  /** Identificador do login */
  private final String id;
  /** Entidade */
  private final String entity;

  /**
   * Construtor.
   * 
   * @param info informa��es de login
   */
  public LoginInfoInfo(LoginInfo info) {
    this.id = info.id;
    this.entity = info.entity;
  }

  /**
   * Recupera o identificador do login.
   * 
   * @return o nome.
   */
  public String getId() {
    return id;
  }

  /**
   * Recupera o identificador da entidade.
   * 
   * @return o identificador.
   */
  public String getEntityId() {
    return entity;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link LoginInfo} para
   * {@link LoginInfoInfo}
   * 
   * @param entities a lista de {@link RegisteredEntityDesc}
   * @return a lista de {@link LoginInfoInfo}
   */
  public static List<LoginInfoInfo> convertToInfo(
    List<LoginInfo> logins) {
    List<LoginInfoInfo> list = new ArrayList<LoginInfoInfo>();
    for (LoginInfo login : logins) {
      list.add(new LoginInfoInfo(login));
    }
    return list;
  }

}
