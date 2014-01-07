package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que det�m as informa��es locais do certificado para apresenta��o em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class CertificateInfo {
  /** Entidade vinculada ao certificado */
  private final String entity;

  /**
   * Construtor.
   * 
   * @param entity entidade vinculada ao certificado
   */
  public CertificateInfo(String entity) {
    this.entity = entity;
  }

  /**
   * Recupera a entidade vinculada ao certificado.
   * 
   * @return a entidade vinculada ao certificado.
   */
  public String getEntity() {
    return entity;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link String} para
   * {@link CertificateInfo}
   * 
   * @param entities lista de {@link String} que representa as entidades com
   *  certificado
   * @return a lista de {@link CertificateInfo}
   */
  public static List<CertificateInfo> convertToInfo(List<String> entities) {
    List<CertificateInfo> list = new ArrayList<CertificateInfo>();
    for (String entity : entities) {
      list.add(new CertificateInfo(entity));
    }
    return list;
  }

}
