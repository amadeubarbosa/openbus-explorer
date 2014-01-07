package busexplorer.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que det�m as informa��es locais da interface para apresenta��o em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class InterfaceInfo {
  /** Nome da interface */
  private final String name;

  /**
   * Construtor.
   * 
   * @param name nome da interface.
   */
  public InterfaceInfo(String name) {
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
   * {@link InterfaceInfo}
   * 
   * @param interfaces a lista de {@link String}
   * @return a lista de {@link InterfaceInfo}
   */
  public static List<InterfaceInfo> convertToInfo(List<String> interfaces) {
    List<InterfaceInfo> list = new ArrayList<InterfaceInfo>();
    for (String interfaceName : interfaces) {
      list.add(new InterfaceInfo(interfaceName));
    }
    return list;
  }

}
