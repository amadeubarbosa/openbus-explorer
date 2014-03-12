package busexplorer.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Propriedades de configura��o do BusExplorer.
 *
 * As propriedades de configura��o, por padr�o, s�o obtidas do arquivo
 * "busexplorer.conf" presente no pr�prio diret�rio de execu��o. O usu�rio pode
 * especificar um arquivo em particular atrav�s da propriedade "configPath".
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
