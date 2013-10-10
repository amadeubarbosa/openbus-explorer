package reuse.modified.planref.client.util.crud.celleditors;

import java.awt.Color;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTextField;

import reuse.modified.logistic.client.util.NumberTextField;


/**
 * Editor abstrato para células de tipos numéricos
 * 
 * @author Tecgraf
 */
abstract class CRUDAbstractNumericEditor extends DefaultCellEditor {

  /** O campo de edição dos valores numéricos. */
  protected final NumberTextField ntf;
  /** Cor padrão do texto */
  private final Color regularForegroundColor;

  /**
   * Construtor.
   */
  public CRUDAbstractNumericEditor() {
    this(false);
  }

  /**
   * Construtor.
   * 
   * @param nullable indicador se aceita nulo ou não.
   */
  public CRUDAbstractNumericEditor(boolean nullable) {
    super(new NumberTextField(new JLabel(), "#,##0.0####", nullable));
    ntf = (NumberTextField) getComponent();
    ntf.setHorizontalAlignment(JTextField.RIGHT);
    regularForegroundColor = ntf.getForeground();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stopCellEditing() {
    ntf.setForeground(regularForegroundColor);
    if (ntf.isEditValid()) {
      try {
        ntf.commitEdit();
      }
      catch (java.text.ParseException exc) {
        ntf.setForeground(Color.red);
      }
      return super.stopCellEditing();
    }
    else {
      ntf.setForeground(Color.red);
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelCellEditing() {
    ntf.setForeground(regularForegroundColor);
    super.cancelCellEditing();
  }
}
