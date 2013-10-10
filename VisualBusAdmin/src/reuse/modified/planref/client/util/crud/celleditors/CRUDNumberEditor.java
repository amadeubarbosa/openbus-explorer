package reuse.modified.planref.client.util.crud.celleditors;

import java.awt.Component;

import javax.swing.JTable;

/**
 * Editor de valores numéricos.
 */
public class CRUDNumberEditor extends CRUDAbstractNumericEditor {

  /**
   * Tipos de dados disponíveis
   */
  public static enum VALUE_TYPE {
    /** Valores do tipo Double */
    DOUBLE,
    /** Valor do tipo Float */
    FLOAT,
    /** Valor do tipo Number */
    NUMBER
  }

  /**
   * O tipo do valor numérico criado
   */
  private final VALUE_TYPE valueType;

  /**
   * Construtor.
   */
  public CRUDNumberEditor() {
    this(false);
  }

  /**
   * Constrói um editor para o tipo double
   * 
   * @param nullable Se o valor pode ser nulo
   */
  public CRUDNumberEditor(boolean nullable) {
    this(nullable, VALUE_TYPE.DOUBLE);
  }

  /**
   * Construtor.
   * 
   * @param nullable indicador se aceita nulo ou não.
   * @param valueType Tipo do valor
   */
  public CRUDNumberEditor(boolean nullable, VALUE_TYPE valueType) {
    super(nullable);
    this.valueType = valueType;
  }

  /**
   * Atribui um valor mínimo possível
   * 
   * @param minValue Valor mínimo a ser atribuído
   */
  public void setMinValue(Number minValue) {
    ntf.setMinValue(minValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
    boolean isSelected, int row, int column) {
    Number dblValue = 0D;

    if (value != null) {
      try {
        switch (valueType) {
          case DOUBLE:
          case NUMBER:
            dblValue = Double.valueOf(value.toString());
            break;
          case FLOAT:
            dblValue = Float.valueOf(value.toString());
            break;
          default:
            // do nothing
        }
      }
      catch (NumberFormatException e) {
        dblValue = 0D;
      }
    }
    ntf.setNumber(dblValue);
    return ntf;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellEditorValue() {
    Number number = ntf.getNumber();
    if (number != null) {
      switch (valueType) {
        case DOUBLE:
        case NUMBER:
          number = number.doubleValue();
          break;
        case FLOAT:
          number = number.floatValue();
          break;
        default:
          number = number.doubleValue();
      }
    }
    return number;
  }
}
