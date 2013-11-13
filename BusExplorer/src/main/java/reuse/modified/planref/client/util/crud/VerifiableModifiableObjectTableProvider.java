package reuse.modified.planref.client.util.crud;

import tecgraf.javautils.gui.table.DefaultObjectTableProvider;
import tecgraf.javautils.gui.table.ModifiableObjectTableProvider;

/**
 * Este TableProvider modifica o comportamento do
 * {@link ModifiableObjectTableProvider} pois além de passar os indices de linha
 * e coluna de uma célula, ele passa também o objeto representado na linha,
 * permitindo que o desenvolvedor faça verificações sobre o objeto para definir
 * se o mesmo pode ou não ser editado!
 * 
 * @author Tecgraf
 */
public abstract class VerifiableModifiableObjectTableProvider extends
  DefaultObjectTableProvider {

  /**
   * Método chamado pela tabela logo após o usuário ter editado uma das células
   * da tabela. Especifica como atualizar o objeto-linha com o valor entrado
   * pelo usuário na coluna especificada.
   * 
   * @param row objeto representando uma linha da tabela.
   * @param newValue valor digitado pelo usuário, para substituir o valor atual
   *        da informação no objeto-linha.
   * @param colIndex índice da coluna na tabela, a ser mapeada para a
   *        propriedade correta do objeto-linha.
   */
  public abstract void setValueAt(Object row, Object newValue, int colIndex);

  /**
   * Indica para a tabela se ela deve permitir ou não a edição em determinada
   * célula.
   * 
   * @param row Objeto que representa que tem seus valores descritos em uma
   *        linha da tabela.
   * @param rowIndex índice da linha da célula.
   * @param columnIndex índice da coluna da célula.
   * 
   * @return <code>true</code> caso a célula possa ser editada,
   *         <code>false</code> caso contrário.
   */
  public abstract boolean isCellEditable(Object row, int rowIndex,
    int columnIndex);

  /**
   * Método para verificar se a linha da tabela está válida, com ênfase no valor
   * representado pela célula da coluna passada como parâmetro.
   * 
   * @param row Objeto que representa que tem seus valores descritos em uma
   *        linha da tabela.
   * @param columnIndex
   * 
   * @return se o valor da célula está válido.
   */
  public abstract boolean isValid(Object row, int columnIndex);

  /**
   * Método que retorna o motivo pelo qual a célula da tabela está considerada
   * inváida.
   * 
   * @param columnIndex coluna da tabela que representa um atributo do objeto
   * @return a descrição da invalidez da célula
   */
  public abstract String getTooltip(int columnIndex);

}
