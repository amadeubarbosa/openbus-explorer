package busexplorer.panel.interfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que det�m as informa��es locais da interface para apresenta��o em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class InterfaceWrapper {
  /** Nome da interface */
  private final String name;

  /**
   * Construtor.
   * 
   * @param name nome da interface.
   */
  public InterfaceWrapper(String name) {
    this.name = name;
  }

  /**
   * Recupera o nome da interface.
   * 
   * @return o nome da interface.
   */
  public String getName() {
    return name;
  }

  /**
   * M�todo utilit�rio para converter lista de {@link String} para
   * {@link InterfaceWrapper}
   * 
   * @param interfaces a lista de {@link String}
   * @return a lista de {@link InterfaceWrapper}
   */
  public static List<InterfaceWrapper> convertToInfo(List<String> interfaces) {
    List<InterfaceWrapper> list = new ArrayList<InterfaceWrapper>();
    for (String interfaceName : interfaces) {
      list.add(new InterfaceWrapper(interfaceName));
    }
    return list;
  }

}
