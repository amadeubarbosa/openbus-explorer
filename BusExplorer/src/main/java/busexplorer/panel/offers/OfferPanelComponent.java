package busexplorer.panel.offers;

import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableColumn;

import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.javautils.gui.table.SortableTable;
import busexplorer.panel.PanelActionInterface;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.OfferInfo;

/**
 * Painel da tabela de ofertas
 * 
 * @author Tecgraf
 */
public class OfferPanelComponent extends PanelComponent<OfferInfo> {

  /**
   * Construtor
   * 
   * @param pInfo Lista com os dados da tabela.
   * @param pTableProvider Provedor de dados da tabela.
   * @param actions Conjunto de ações relacionadas ao componente.
   */
  public OfferPanelComponent(List<OfferInfo> pInfo,
    ObjectTableProvider<OfferInfo> pTableProvider,
    List<? extends PanelActionInterface<OfferInfo>> actions) {
    super(pInfo, pTableProvider, actions);
  }

  /**
   * Construtor.
   * 
   * @param pTableModel Modelo da tabela.
   * @param actions Conjunto de ações relacionadas ao componente.
   */
  public OfferPanelComponent(ObjectTableModel<OfferInfo> pTableModel,
    List<? extends PanelActionInterface<OfferInfo>> actions) {
    super(pTableModel, actions);
  }

  /**
   * {@inheritDoc} Atualizando para acertar altura e larguras de célula
   * multi-linha.
   */
  @Override
  public void setElements(List<OfferInfo> objects) {
    super.setElements(objects);
    SortableTable table = this.getTable();
    for (int idx = 0; idx < table.getRowCount(); idx++) {
      ObjectTableModel<OfferInfo> model = this.getTableModel();
      int i = table.convertRowIndexToModel(idx);
      OfferInfo offerInfo = model.getRow(i);
      Vector<String> interfaces = offerInfo.getInterfaces();

      Insets insets = table.getInsets();
      FontMetrics metrics = table.getFontMetrics(getFont());
      // Calcula as dimensões do texto.
      int width = 0;
      for (String aLine : interfaces) {
        width = Math.max(width, metrics.stringWidth(aLine));
      }
      int height = +((metrics.getHeight() + 2) * interfaces.size());

      // Inclui o gap e os insets
      //width += insets.left + insets.right;
      height += insets.top + insets.bottom;
      table.setRowHeight(i, height);

      TableColumn column =
        table.getColumn(Utils.getString(OfferTableProvider.class, "interface"));
      int w = column.getWidth();
      if (w < width) {
        column.setWidth(width);
        column.setMinWidth(width * 2 / 3);
      }
    }
  }

}
