package busexplorer.panel.providers;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

import java.util.Vector;

public class ProviderTableProvider implements ObjectTableProvider<ProviderWrapper> {

  private static final int NAME = 0;
  private static final int CODE = 1;
  private static final int SUPPORT_OFFICE = 2;
  private static final int SUPPORT = 3;
  private static final int MANAGER_OFFICE = 4;
  private static final int MANAGER = 5;
  private static final int BUSQUERY = 6;
  private static final int CONTRACTS = 7;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Language.get(this.getClass(), "name"),
      Language.get(this.getClass(), "code"),
      Language.get(this.getClass(), "supportoffice"),
      Language.get(this.getClass(), "support"),
      Language.get(this.getClass(), "manageroffice"),
      Language.get(this.getClass(), "manager"),
      Language.get(this.getClass(), "busquery"),
      Language.get(this.getClass(), "contracts") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class, String.class, String.class,
      Vector.class, String.class, Vector.class, String.class, Vector.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(ProviderWrapper row, int col) {
    switch (col) {
      case NAME:
        return row.name();

      case CODE:
        return row.code();

      case SUPPORT_OFFICE:
        return row.supportoffice();

      case SUPPORT:
        return row.support();

      case MANAGER_OFFICE:
        return row.manageroffice();

      case MANAGER:
        return row.manager();

      case BUSQUERY:
        return row.busquery();

      case CONTRACTS:
        return row.contracts();

      default:
        break;
    }
    return null;
  }
}
