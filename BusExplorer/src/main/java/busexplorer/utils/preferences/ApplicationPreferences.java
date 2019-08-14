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
 * Prefer�ncias de configura��o da aplica��o.
 *
 * As prefer�ncias do usu�rio ser�o armazenadas de acordo com sua pr�pria
 * necessidade. Atentar que as exclus�es de entrada s�o de total
 * responsabilidade da interface que utiliza essa classe, que serve como
 * centralizadoras das prefer�ncias.
 *
 * Todas as prefer�ncias devem ser armazenadas na forma de String, e qualquer
 * tipagem dever� ser resolvida externamente. � interessante que cada aplica��o
 * guarde as strings de preferences em uma classe ou interface separada.
 *
 * Para o m�todo {@link #write(String, String, Collection)} n�o � necess�rio
 * qualquer preocupa��o com os valores sendo escritos, pois, internamente, um
 * scape j� � adicionado para que os valores sejam corretamente separados
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
   * @param preferences inst�ncia de preferences
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  private void write(Preferences preferences, String name, String value) {
    preferences.put(name, value);
  }

  /**
   * Escreve um valor na raiz da aplica��o.
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  public void write(String name, String value) {
    write(preferences, name, value);
  }

  /**
   * Escreve um valor no path indicado, na raiz da aplica��o.
   * @param name nome da propriedade a ser escrita
   * @param value valor da propriedade a ser escrita
   */
  public void write(String path, String name, String value) {
    write(preferences.node(path), name, value);
  }


  /**
   * L� um valor do item indicado.
   * @param preferences inst�ncia de preferences
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas prefer�ncias
   */
  private String read(Preferences preferences, String name) {
    return preferences.get(name, EMPTY);
  }

  /**
   * L� um valor da raiz da aplica��o.
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas prefer�ncias
   */
  public String read(String name) {
    return read(preferences, name);
  }

  /**
   * L� um valor do path indicado.
   * @param name nome da propriedade a ser lida
   * @return valor encontrado nas prefer�ncias
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
   * Escreve uma cole��o de valores no item indicado.
   * @param preferences inst�ncia de preferences
   * @param name nome da propriedade a ser escrita
   * @param value cole��o a ser escrita
   */
   private void write(Preferences preferences, String name, Collection<String> value) {
    String storeValue = value.stream().map(i -> scape(i)).collect(Collectors.joining(SEP));
    preferences.put(name, storeValue);
  }

  /**
   * Escreve uma cole��o de valores na raiz da aplica��o.
   * Antente-se que a ordem n�o importa para o objetivo de armazenamento de
   * prefer�ncias, a escrita ser� feita com qualquer tipo de cole��o e a leitura
   * {@link #readCollection(String)} ser� realizada ser qualquer compromisso de
   * ordena��o.
   * � importante atentar para que o separador {@link #SEP} n�o seja utilizado
   * como um valor escrito. Caso isso aconte�a, os valores retornados ser�o diretamente
   * afetados, corrompendo o valor esperado. Exemplo: caso seja informada a tupla
   * ("nome", "valor1|valor2|val|or3"), 4 (quatro) valores ser�o retornados pelo
   * m�todo {@link #readCollection(String)}: ["valor1", "valor2", "val", "or3"].
   *
   * @param name nome da propriedade a ser escrita
   * @param value cole��o a ser escrita
   */
  public void write(String name, Collection<String> value) {
    write(preferences, name, value);
  }

  /**
   * Escreve uma cole��o de valores no path indicado.
   * Antente-se que a ordem n�o importa para o objetivo de armazenamento de
   * prefer�ncias, a escrita ser� feita com qualquer tipo de cole��o e a leitura
   * {@link #readCollection(String)} ser� realizada ser qualquer compromisso de
   * ordena��o.
   * � importante atentar para que o separador {@link #SEP} n�o seja utilizado
   * como um valor escrito. Caso isso aconte�a, os valores retornados ser�o diretamente
   * afetados, corrompendo o valor esperado. Exemplo: caso seja informada a tupla
   * ("nome", "valor1|valor2|val|or3"), 4 (quatro) valores ser�o retornados pelo
   * m�todo {@link #readCollection(String)}: ["valor1", "valor2", "val", "or3"].
   *
   * @param path caminho no qual o par nome/valor ser� adicionado
   * @param name nome da propriedade a ser escrita
   * @param value cole��o a ser escrita
   */
  public void write(String path, String name, Collection<String> value) {
    write(preferences.node(path), name, value);
  }

  /**
   * Adiciona um item a uma lista.
   *
   * @param name nome a ser adicionado
   * @param value valor a ser adicionado
   * @param addIfExists caso o par�metro name seja encontrado, adiciona valor mesmo que j� exista caso verdadeiro
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
   * @param path caminho no qual o par nome/valor ser� adicionado
   * @param name nome a ser adicionado
   * @param value valor a ser adicionado
   * @param addIfExists caso o par�metro name seja encontrado, adiciona valor mesmo que j� exista caso verdadeiro
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
   * L� uma cole��o de valores do item indicado.
   * H� restri��es para a utiliza��o de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura � afetada em {@link #write(String, Collection)}.
   *
   * N�o h� compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementa��o atualmente.
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
   * L� uma cole��o de valores da raiz da aplica��o.
   * H� restri��es para a utiliza��o de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura � afetada em {@link #write(String, Collection)}.
   *
   * N�o h� compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementa��o atualmente.
   *
   * @see #write(String, Collection)
   * @param name
   * @return
   */
  public Collection<String> readCollection(String name) {
    return readCollection(preferences, name);
  }

  /**
   * L� uma cole��o de valores do path indicado.
   * H� restri��es para a utiliza��o de valores na escrita que afetam diretamente
   * os valores retornados. Verifique como a leitura � afetada em {@link #write(String, Collection)}.
   *
   * N�o h� compromisso com o valor retornado, apesar de uma lista estar sendo retornada
   * como implementa��o atualmente.
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
   * Limpa todas as prefer�ncias de usu�rio.
   */
  public void clear() {
    try {
      preferences.clear();
    } catch (BackingStoreException e) {
      throw new RuntimeException("N�o foi poss�vel limpar as prefer�ncias de usu�rio", e);
    }
  }

}
