package reuse.modified.logistic.client.util;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * Classe que define um modelo de combo box que lida com listas.
 */
public class ListComboBoxModel extends AbstractListModel implements
  ComboBoxModel {
  /** Item selecionado */
  private Object selectedItem;
  /** Lista da combo */
  private List<? extends Object> list;

  /**
   * Construtor.
   * 
   * @param list lista a ter a combo criada.
   */
  private ListComboBoxModel(List<? extends Object> list) {
    this.list = list;
    if (list.isEmpty()) {
      return;
    }
    this.selectedItem = list.get(0);
  }

  /**
   * {@inheritDoc}
   */
  public Object getSelectedItem() {
    return selectedItem;
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedItem(Object selectedItem) {
    if (selectedItem == null) {
      return;
    }
    if (!list.contains(selectedItem)) {
      return;
    }
    this.selectedItem = selectedItem;
    fireContentsChanged(this, -1, -1);
  }

  /**
   * {@inheritDoc}
   */
  public int getSize() {
    return list.size();
  }

  /**
   * {@inheritDoc}
   */
  public Object getElementAt(int index) {
    return list.get(index);
  }

  /**
   * Métodos de classe.
   */

  /**
   * Constrói um novo modelo de combo.
   * 
   * @param list lista dos objetos a terem a combo criada.
   * @return modelo de combo.
   */
  public static ListComboBoxModel get(List<? extends Object> list) {
    return new ListComboBoxModel(list);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return list.toString();
  }
}
