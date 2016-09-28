package busexplorer.panel.configuration.admins;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que det�m as informa��es locais do administrador para apresenta��o em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class AdminWrapper {
  /** entidade administradora */
  private String admin;

  /**
   * Construtor.
   * 
   * @param admin entidade administradora
   */
  public AdminWrapper(String admin) {
    this.admin = admin;
  }

  /**
   * Compara um objeto � inst�ncia de {@link AdminWrapper}.
   *
   * @param o Objeto a ser comparado.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AdminWrapper)) {
      return false;
    }
    AdminWrapper other = (AdminWrapper) o;
    return admin.equals(other.admin);
  }

  /**
   * C�digo hash do objeto.
   *
   * @return C�digo hash do objeto.
   */
  @Override
  public int hashCode() {
    return admin.hashCode() ;
  }

  /**
   * Recupera a entidade administradora.
   * 
   * @return a entidade.
   */
  public String getAdmin() {
    return admin;
  }

  /**
   * Atualiza a entidade administradora.
   * 
   * @param admin a nova entidade administradora.
   */
  public void setAdmin(String admin) {
    this.admin = admin;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link String} para
   * {@link AdminWrapper}
   * 
   * @param admins a lista de {@link String}
   * @return a lista de {@link AdminWrapper}
   */
  public static List<AdminWrapper> convertToInfo(List<String> admins) {
    List<AdminWrapper> list = new ArrayList<AdminWrapper>();
    for (String admin : admins) {
      list.add(new AdminWrapper(admin));
    }
    return list;
  }
}
