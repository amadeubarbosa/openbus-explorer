package busexplorer.panel.offers.properties;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;
import busexplorer.utils.Utils;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class PropertiesTableProvider implements
  ObjectTableProvider<ServiceProperty> {

  /** Índice da coluna nome da propriedade */
  private static final int NAME = 0;
  /** Índice da coluna valor da propriedade */
  private static final int VALUE = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames =
      { Utils.getString(this.getClass(), "name"),
          Utils.getString(this.getClass(), "value") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class, String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(ServiceProperty row, int col) {
    switch (col) {
      case NAME:
        return row.name;

      case VALUE:
        return row.value;

      default:
        break;
    }
    return null;

  }
}
