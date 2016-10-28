package busexplorer.utils;

import java.util.Vector;

import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;

/**
 * Classe utilitária com alguns métodos auxiliares.
 * 
 * @author Tecgraf
 */
public class Utils {

  /**
   * Recupera o valor de uma dada propriedade em uma oferta.
   * 
   * @param offer o descritor da oferta
   * @param prop a propriedade sendo buscada
   * @return o valor da propriedade na oferta, ou <code>null</code> caso não a
   *         propriedade não exista.
   */
  static public String getProperty(ServiceOfferDesc offer, String prop) {
    ServiceProperty[] properties = offer.properties;
    for (ServiceProperty property : properties) {
      if (property.name.equals(prop)) {
        return property.value;
      }
    }
    return null;
  }

  /**
   * Recupera os valores de uma dada propriedade em uma oferta.
   * 
   * @param offer o descritor da oferta
   * @param prop a propriedade sendo buscada
   * @return a lista de valores na propriedade na oferta.
   */
  static public Vector<String> getProperties(ServiceOfferDesc offer, String prop) {
    Vector<String> list = new Vector<>();
    ServiceProperty[] properties = offer.properties;
    for (ServiceProperty property : properties) {
      if (property.name.equals(prop)) {
        list.add(property.value);
      }
    }
    return list;
  }

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param clazz a classe cuja chave esta associada.
   * @param key a chave
   * @return o valor associado à chave.
   */
  static public String getString(Class<?> clazz, String key) {
    return LNG.get(clazz.getSimpleName() + "." + key);
  }

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param clazz a classe cuja chave esta associada.
   * @param key a chave
   * @param args argumentos a serem formatados na mensagem.
   * @return o valor associado à chave.
   */
  static public String getString(Class<?> clazz, String key, Object... args) {
    return LNG.get(clazz.getSimpleName() + "." + key, args);
  }
}
