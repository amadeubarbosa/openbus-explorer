package reuse.modified.planref.client.util.crud;

import tecgraf.javautils.gui.table.DefaultObjectTableProvider;
import tecgraf.javautils.gui.table.ModifiableObjectTableProvider;

/**
 * Este TableProvider modifica o comportamento do
 * {@link ModifiableObjectTableProvider} pois al�m de passar os indices de linha
 * e coluna de uma c�lula, ele passa tamb�m o objeto representado na linha,
 * permitindo que o desenvolvedor fa�a verifica��es sobre o objeto para definir
 * se o mesmo pode ou n�o ser editado!
 * 
 * @author Tecgraf
 */
public abstract class VerifiableModifiableObjectTableProvider extends
  DefaultObjectTableProvider {

  /**
   * M�todo chamado pela tabela logo ap�s o usu�rio ter editado uma das c�lulas
   * da tabela. Especifica como atualizar o objeto-linha com o valor entrado
   * pelo usu�rio na coluna especificada.
   * 
   * @param row objeto representando uma linha da tabela.
   * @param newValue valor digitado pelo usu�rio, para substituir o valor atual
   *        da informa��o no objeto-linha.
   * @param colIndex �ndice da coluna na tabela, a ser mapeada para a
   *        propriedade correta do objeto-linha.
   */
  public abstract void setValueAt(Object row, Object newValue, int colIndex);

  /**
   * Indica para a tabela se ela deve permitir ou n�o a edi��o em determinada
   * c�lula.
   * 
   * @param row Objeto que representa que tem seus valores descritos em uma
   *        linha da tabela.
   * @param rowIndex �ndice da linha da c�lula.
   * @param columnIndex �ndice da coluna da c�lula.
   * 
   * @return <code>true</code> caso a c�lula possa ser editada,
   *         <code>false</code> caso contr�rio.
   */
  public abstract boolean isCellEditable(Object row, int rowIndex,
    int columnIndex);

  /**
   * M�todo para verificar se a linha da tabela est� v�lida, com �nfase no valor
   * representado pela c�lula da coluna passada como par�metro.
   * 
   * @param row Objeto que representa que tem seus valores descritos em uma
   *        linha da tabela.
   * @param columnIndex
   * 
   * @return se o valor da c�lula est� v�lido.
   */
  public abstract boolean isValid(Object row, int columnIndex);

  /**
   * M�todo que retorna o motivo pelo qual a c�lula da tabela est� considerada
   * inv�ida.
   * 
   * @param columnIndex coluna da tabela que representa um atributo do objeto
   * @return a descri��o da invalidez da c�lula
   */
  public abstract String getTooltip(int columnIndex);

}
