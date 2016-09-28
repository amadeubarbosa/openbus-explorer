package busexplorer.panel.configuration.validators;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que detém as informações locais do validador para apresentação em
 * tabelas.
 * 
 * @author Tecgraf
 */
public class ValidatorWrapper {
  /** validador */
  private String validator;

  /**
   * Construtor.
   * 
   * @param validator nome do validador
   */
  public ValidatorWrapper(String validator) {
    this.validator = validator;
  }

  /**
   * Compara um objeto à instância de {@link ValidatorWrapper}.
   *
   * @param o Objeto a ser comparado.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ValidatorWrapper)) {
      return false;
    }
    ValidatorWrapper other = (ValidatorWrapper) o;
    return validator.equals(other.validator);
  }

  /**
   * Código hash do objeto.
   *
   * @return Código hash do objeto.
   */
  @Override
  public int hashCode() {
    return validator.hashCode() ;
  }

  /**
   * Recupera o nome do validador.
   * 
   * @return o nome do validador.
   */
  public String getValidator() {
    return validator;
  }

  /**
   * Método utilitário para converter lista de {@link String} para
   * {@link ValidatorWrapper}
   * 
   * @param validators a lista de {@link String}
   * @return a lista de {@link ValidatorWrapper}
   */
  public static List<ValidatorWrapper> convertToInfo(List<String> validators) {
    List<ValidatorWrapper> list = new ArrayList<ValidatorWrapper>();
    for (String validator : validators) {
      list.add(new ValidatorWrapper(validator));
    }
    return list;
  }
}
