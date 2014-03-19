package busexplorer.panel.logins;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;

/**
 * Classe que det�m as informa��es locais de login para apresenta��o em tabelas.
 * 
 * @author Tecgraf
 */
public class LoginWrapper {
  /** Informa��es do login */
  private LoginInfo info;

  /** Identificador do login */
  private final String id;
  /** Entidade */
  private final String entity;

  /**
   * Construtor.
   * 
   * @param info informa��es de login
   */
  public LoginWrapper(LoginInfo info) {
    this.info = info;
    this.id = info.id;
    this.entity = info.entity;
  }

  /**
   * Compara um objeto � inst�ncia de {@link LoginWrapper}.
   *
   * O m�todo n�o leva em considera��o o objeto que cont�m as informa��es de
   * login.
   * 
   * @param o Objeto a ser comparado.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LoginWrapper)) {
      return false;
    }
    LoginWrapper other = (LoginWrapper) o;
    return id.equals(other.id) && entity.equals(other.entity);
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
   * Recupera a pr�pria informa��o de login.
   * 
   * @return a informa��o de login.
   */
  public LoginInfo getInfo() {
    return info;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link LoginInfo} para
   * {@link LoginWrapper}
   * 
   * @param logins a lista de {@link LoginInfo}
   * @return a lista de {@link LoginWrapper}
   */
  public static List<LoginWrapper> convertToInfo(List<LoginInfo> logins) {
    List<LoginWrapper> list = new ArrayList<LoginWrapper>();
    for (LoginInfo login : logins) {
      list.add(new LoginWrapper(login));
    }
    return list;
  }

}
