package busexplorer.panel.entities;

import java.util.ArrayList;
import java.util.List;

import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

/**
 * Classe que detém as informações locais da entidade para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class EntityWrapper {
  // TODO: devemos manter essa referência?
  /** objeto descritor de entidade */
  private RegisteredEntityDesc desc;

  /** Identificador */
  private final String id;
  /** Nome descritivo */
  private String name;
  /** Identifacador da categoria associada */
  private final String category;

  /**
   * Construtor.
   * 
   * @param desc descritor da entidade
   */
  public EntityWrapper(RegisteredEntityDesc desc) {
    this.desc = desc;
    this.id = desc.id;
    this.name = desc.name;
    this.category = desc.category.id();
  }

  /**
   * Compara um objeto à instância de {@link EntityWrapper}.
   *
   * O método não leva em consideração o objeto descritor da entidade.
   * 
   * @param o Objeto a ser comparado.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EntityWrapper)) {
      return false;
    }
    EntityWrapper other = (EntityWrapper) o;
    return id.equals(other.id) && name.equals(other.name) &&
      category.equals(other.category);
  }

  /**
   * Recupera o identificador da entidade.
   * 
   * @return o identificador.
   */
  public String getId() {
    return id;
  }

  /**
   * Recupera o nome descritivo da entidade.
   * 
   * @return o nome.
   */
  public String getName() {
    return name;
  }

  /**
   * Atualiza o nome descritivo da entidade.
   * 
   * @param name o novo nome.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Recupera o identificador da categoria associada.
   * 
   * @return o identificador da categoria.
   */
  public String getCategory() {
    return category;
  }

  /**
   * Recupera o próprio objeto descritor de entidade.
   * 
   * @return o objeto descritor.
   */
  public RegisteredEntityDesc getDescriptor() {
    return desc;
  }

  /**
   * Método utilitário para converter lista de {@link RegisteredEntityDesc} para
   * {@link EntityWrapper}
   * 
   * @param entities a lista de {@link RegisteredEntityDesc}
   * @return a lista de {@link EntityWrapper}
   */
  public static List<EntityWrapper> convertToInfo(
    List<RegisteredEntityDesc> entities) {
    List<EntityWrapper> list = new ArrayList<EntityWrapper>();
    for (RegisteredEntityDesc entity : entities) {
      list.add(new EntityWrapper(entity));
    }
    return list;
  }

}
