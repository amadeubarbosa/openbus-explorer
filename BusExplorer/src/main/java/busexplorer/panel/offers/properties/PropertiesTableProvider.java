package busexplorer.panel.offers.properties;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;

/**
 * Provedor de dados para a tabela de Ofertas
 * 
 * @author Tecgraf
 */
public class PropertiesTableProvider implements
  ObjectTableProvider<ServiceProperty> {

  /** �ndice da coluna nome da propriedade */
  private static final int NAME = 0;
  /** �ndice da coluna valor da propriedade */
  private static final int VALUE = 1;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[]
      { Language.get(this.getClass(), "name"),
          Language.get(this.getClass(), "value") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class };
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
