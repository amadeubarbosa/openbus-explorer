package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;

/**
 * Classe que detém as informações locais da categoria para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class CategoryInfo {
  /** objeto descritor da categoria */
  private EntityCategoryDesc desc;

  /** Identificador */
  private final String id;
  /** Nome descritivo */
  private String name;

  /**
   * Construtor.
   * 
   * @param desc descritor da categoria
   */
  public CategoryInfo(EntityCategoryDesc desc) {
    this.desc = desc;
    this.id = desc.id;
    this.name = desc.name;
  }

  /**
   * Recupera o identificador da categoria.
   * 
   * @return o identificador.
   */
  public String getId() {
    return id;
  }

  /**
   * Recupera o nome descritivo da categoria.
   * 
   * @return o nome.
   */
  public String getName() {
    return name;
  }

  /**
   * Atualiza o nome descritivo da categoria.
   * 
   * @param name o novo nome.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Recupera o próprio objeto descritor da categoria.
   *
   * @return o objeto descritor da categoria.
   */
  public EntityCategoryDesc getDescriptor() {
    return desc;
  }

  /**
   * Método utilitário para converter lista de {@link EntityCategoryDesc} para
   * {@link CategoryInfo}
   * 
   * @param categories a lista de {@link EntityCategoryDesc}
   * @return a lista de {@link CategoryInfo}
   */
  public static List<CategoryInfo> convertToInfo(
    List<EntityCategoryDesc> categories) {
    List<CategoryInfo> list = new ArrayList<CategoryInfo>();
    for (EntityCategoryDesc category : categories) {
      list.add(new CategoryInfo(category));
    }
    return list;
  }

}
