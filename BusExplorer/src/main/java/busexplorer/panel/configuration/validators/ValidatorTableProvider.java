package busexplorer.panel.configuration.validators;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Validadores
 * 
 * @author Tecgraf
 */
public class ValidatorTableProvider implements ObjectTableProvider<ValidatorWrapper> {

  /** Índice da coluna Descrição */
  private static final int VALIDATOR_NAME = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[]{ Language.get(this.getClass(), "validator") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[]{ String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(ValidatorWrapper row, int col) {
    switch (col) {
      case VALIDATOR_NAME:
        return row.getValidator();

      default:
        break;
    }
    return null;
  }
}
