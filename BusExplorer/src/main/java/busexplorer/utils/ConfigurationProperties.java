package busexplorer.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Propriedades de configuração do BusExplorer.
 *
 * As propriedades de configuração, por padrão, são obtidas do arquivo
 * "busexplorer.conf" presente no próprio diretório de execução. O usuário pode
 * especificar um arquivo em particular através da propriedade "configPath".
 *
 * @author Tecgraf
 */
public class ConfigurationProperties extends Properties {
  /**
   * Construtor.
   */
  public ConfigurationProperties() {
    try {
      String configPath = System.getProperty("configPath") != null ?
        System.getProperty("configPath") : "busexplorer.conf";

      FileInputStream configStream = new FileInputStream(configPath);
      load(configStream);
    }
    catch (FileNotFoundException e) {
      System.err.println(Utils.getString(ConfigurationProperties.class,
        "warning.configFileNotFound"));
    }
    catch (IOException e) {
      System.err.println(Utils.getString(ConfigurationProperties.class,
        "warning.configFileLoadFailure"));
    }
  }
}
