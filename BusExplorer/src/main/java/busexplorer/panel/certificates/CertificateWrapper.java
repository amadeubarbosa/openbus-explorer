package busexplorer.panel.certificates;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que detém as informações locais do certificado para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class CertificateWrapper {
  /** Entidade vinculada ao certificado */
  private final String entity;

  /**
   * Construtor.
   * 
   * @param entity entidade vinculada ao certificado
   */
  public CertificateWrapper(String entity) {
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
   * Método utilitário para converter lista de {@link String} para
   * {@link CertificateWrapper}
   * 
   * @param entities lista de {@link String} que representa as entidades com
   *  certificado
   * @return a lista de {@link CertificateWrapper}
   */
  public static List<CertificateWrapper> convertToInfo(List<String> entities) {
    List<CertificateWrapper> list = new ArrayList<CertificateWrapper>();
    for (String entity : entities) {
      list.add(new CertificateWrapper(entity));
    }
    return list;
  }

}
