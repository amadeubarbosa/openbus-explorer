package busexplorer.panel.logins;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.access_control.LoginInfo;

/**
 * Classe que detém as informações locais de login para apresentação em tabelas.
 * 
 * @author Tecgraf
 */
public class LoginWrapper {
  /** Informações do login */
  private LoginInfo info;

  /** Identificador do login */
  private final String id;
  /** Entidade */
  private final String entity;

  /**
   * Construtor.
   * 
   * @param info informações de login
   */
  public LoginWrapper(LoginInfo info) {
    this.info = info;
    this.id = info.id;
    this.entity = info.entity;
  }

  /**
   * Compara um objeto à instância de {@link LoginWrapper}.
   *
   * O método não leva em consideração o objeto que contém as informações de
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
   * Recupera a própria informação de login.
   * 
   * @return a informação de login.
   */
  public LoginInfo getInfo() {
    return info;
  }

  /**
   * Método utilitário para converter lista de {@link LoginInfo} para
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
