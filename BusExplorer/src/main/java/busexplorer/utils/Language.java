package busexplorer.utils;

import tecgraf.javautils.core.lng.LNG;

/**
 * Classe utilit�ria para uso da internacionaliza��o atrav�s do pacote JavaUtils do Tecgraf.
 * 
 * @author Tecgraf
 */
public class Language {

  /**
   * Busca pelo valor associado a chave no {@link LNG}.
   * 
   * @param clazz a classe cuja chave esta associada.
   * @param key a chave
   * @return o valor associado � chave.
   *
   * @see LNG
   */
  static public String get(Class<?> clazz, String key) {
    return LNG.get(clazz.getSimpleName() + "." + key);
  }

  /**
   * Busca pelo valor associado a chave no {@link LNG} com suporte a par�metros para as strings.
   * 
   * @param clazz a classe cuja chave esta associada.
   * @param key a chave
   * @param args argumentos a serem formatados na mensagem.
   * @return o valor associado � chave.
   *
   * @see LNG
   */
  static public String get(Class<?> clazz, String key, Object... args) {
    return LNG.get(clazz.getSimpleName() + "." + key, args);
  }

  /**
   * Verifica se h� valor associado a chave no {@link LNG}.
   *
   * @param clazz a classe cuja chave esta associada.
   * @param key a chave
   *
   * @return {@code true} caso exista algum valor associado � chave ou {@code false} caso contr�rio.
   *
   * @see LNG
   */
  static public boolean hasKey(Class<?> clazz, String key) {
    return LNG.hasKey(clazz.getSimpleName() + "." + key);
  }
}
