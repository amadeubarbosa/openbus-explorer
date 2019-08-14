package busexplorer.utils.preferences;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Preferências de configuração da aplicação.
 *
 * As preferências do usuário serão armazenadas de acordo com sua própria
 * necessidade. Atentar que as exclusões de entrada são de total
 * responsabilidade da interface que utiliza essa classe, que serve como
 * centralizadoras das preferências.
 *
 * Todas as preferências devem ser armazenadas na forma de String, e qualquer
 * tipagem deverá ser resolvida externamente. É interessante que cada aplicação
 * guarde as strings de preferences em uma classe ou interface separada.
 *
 * Para o método {@link #write(String, String, Collection)} não é necessário
 * qualquer preocupação com os valores sendo escritos, pois, internamente, um
 * scape já é adicionado para que os valores sejam corretamente separados
 * na forma de lista.
 *
 * @author Tecgraf
 */
public class ApplicationPreferences {

  public static final String EMPTY = null;
  public static final String SCAPE_CHAR = "\\\\";
  public static final String SEP = ",";

  private Preferences preferences;

  public ApplicationPreferences(String appname) {
    preferences = Preferences.userRoot().node(appname);
  }

  /**
   * Escreve um valor no item indicado.
   * @param preferences instância de preferences
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  private void write(Preferences preferences, String name, String value) {
    preferences.put(name, value);
  }

  /**
   * Escreve um valor na raiz da aplicação.
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  public void write(String name, String value) {
    write(preferences, name, value);
  }

  /**
   * Escreve um valor no path indicado, na raiz da aplicação.
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  public void write(String path, String name, String value) {
    write(preferences.node(path), name, value);
  }


  /**
   * Lê um valor do item indicado.
   * @param preferences instância de preferences
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas preferências
   */
  private String read(Preferences preferences, String name) {
    return preferences.get(name, EMPTY);
  }

  /**
   * Lê um valor da raiz da aplicação.
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas preferências
   */
  public String read(String name) {
    return read(preferences, name);
  }

  /**
   * Lê um valor do path indicado.
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas preferências
   */
  public String read(String path, String name) {
    return read(preferences.node(path), name);
  }

  /**
   * Adiciona um valor de scape para o item informado.
   * @param item valor sem scape
   * @return valor com scape
   */
  private String scape(String item) {
    return item.replace(SEP, SCAPE_CHAR + SEP);
  }

  /**
   * Retira valor de scape para o item informado.
   * @param item valor com scape
   * @return valor sem scape
   */
  private String unscape(String item) {
    return item.replace(SCAPE_CHAR + SEP, SEP);
  }

  /**
   * Escreve uma coleção de valores no item indicado.
   * @param preferences instância de preferences
   * @param name nome da propriedade a ser escrita
   * @param value coleção a ser escrita
   */
   private void write(Preferences preferences, String name, Collection<String> value) {
    String storeValue = value.stream().map(i -> scape(i)).collect(Collectors.joining(SEP));
    preferences.put(name, storeValue);
  }

  /**
   * Escreve uma coleção de valores na raiz da aplicação.
   * Antente-se que a ordem não importa para o objetivo de armazenamento de
   * preferências, a escrita será feita com qualquer tipo de coleção e a leitura
   * {@link #readCollection(String)} será realizada ser qualquer compromisso de
   * ordenação.
   * É importante atentar para que o separador {@link #SEP} não seja utilizado
   * como um valor escrito. Caso isso aconteça, os valores retornados serão diretamente
   * afetados, corrompendo o valor esperado. Exemplo: caso seja informada a tupla
   * ("nome", "valor1|valor2|val|or3"), 4 (quatro) valores serão retornados pelo
   * método {@link #readCollection(String)}: ["valor1", "valor2", "val", "or3"].
   *
   * @param name nome da propriedade a ser escrita
   * @param value coleção a ser escrita
   */
  public void write(String name, Collection<String> value) {
    write(preferences, name, value);
  }

  /**
   * Escreve uma coleção de valores no path indicado.
   * Antente-se que a ordem não importa para o objetivo de armazenamento de
   * preferências, a escrita será feita com qualquer tipo de coleção e a leitura
   * {@link #readCollection(String)} será realizada ser qualquer compromisso de
   * ordenação.
   * É importante atentar para que o separador {@link #SEP} não seja utilizado
   * como um valor escrito. Caso isso aconteça, os valores retornados serão diretamente
   * afetados, corrompendo o valor esperado. Exemplo: caso seja informada a tupla
   * ("nome", "valor1|valor2|val|or3"), 4 (quatro) valores serão retornados pelo
   * método {@link #readCollection(String)}: ["valor1", "valor2", "val", "or3"].
   *
   * @param path caminho no qual o par nome/valor será adicionado
   * @param name nome da propriedade a ser escrita
   * @param value coleção a ser escrita
   */
  public void write(String path, String name, Collection<String> value) {
    write(preferences.node(path), name, value);
  }

  /**
   * Adiciona um item a uma lista.
   *
   * @param name nome a ser adicionado
   * @param value valor a ser adicionado
   * @param addIfExists caso o parâmetro name seja encontrado, adiciona valor mesmo que já exista caso verdadeiro
   */
  public void addToCollection(String name, String value, boolean addIfExists) {
    Collection<String> items = readCollection(name);
    boolean exists = items.stream().filter(it -> it.equals(value)).findFirst().isPresent();
    if (!exists || (exists && addIfExists)) {
      items.add(value);
      write(name, items);
    }
  }

  /**
   * Adiciona um item a uma lista.
   *
   * @param path caminho no qual o par nome/valor será adicionado
   * @param name nome a ser adicionado
   * @param value valor a ser adicionado
   * @param addIfExists caso o parâmetro name seja encontrado, adiciona valor mesmo que já exista caso verdadeiro
   */
  public void addToCollection(String path, String name, String value, boolean addIfExists) {
    Collection<String> items = readCollection(path, name);
    boolean exists = items.stream().filter(it -> it.equals(value)).findFirst().isPresent();
    if (!exists || (exists && addIfExists)) {
      items.add(value);
      write(path, name, items);
    }
  }


  /**
   * Lê uma coleção de valores do item indicado.
   * Há restrições para a utilização de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura é afetada em {@link #write(String, Collection)}.
   *
   * Não há compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementação atualmente.
   *
   * @see #write(String, Collection)
   * @param preferences
   * @param name
   * @return
   */
  private Collection<String> readCollection(Preferences preferences, String name) {
    String storedValue = preferences.get(name, EMPTY);
    String[] items = storedValue == null ? new String[]{} : storedValue.split("(?<!" + SCAPE_CHAR + ")" + SEP);
    return Stream.of(items)
            .map(item -> unscape(item))
            .collect(Collectors.toList());
  }

  /**
   * Lê uma coleção de valores da raiz da aplicação.
   * Há restrições para a utilização de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura é afetada em {@link #write(String, Collection)}.
   *
   * Não há compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementação atualmente.
   *
   * @see #write(String, Collection)
   * @param name
   * @return
   */
  public Collection<String> readCollection(String name) {
    return readCollection(preferences, name);
  }

  /**
   * Lê uma coleção de valores do path indicado.
   * Há restrições para a utilização de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura é afetada em {@link #write(String, Collection)}.
   *
   * Não há compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementação atualmente.
   *
   * @see #write(String, Collection)
   * @param path
   * @param name
   * @return
   */
  public Collection<String> readCollection(String path, String name) {
    return readCollection(preferences.node(path), name);
  }

  /**
   * Limpa todas as preferências de usuário.
   */
  public void clear() {
    try {
      preferences.clear();
    } catch (BackingStoreException e) {
      throw new RuntimeException("Não foi possível limpar as preferências de usuário", e);
    }
  }

}
