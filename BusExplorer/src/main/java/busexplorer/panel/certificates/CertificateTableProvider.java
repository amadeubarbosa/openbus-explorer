package busexplorer.panel.certificates;

import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CertificateTableProvider implements
  ObjectTableProvider<CertificateWrapper> {

  /** �ndice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    return new String[] { Language.get(this.getClass(), "entity") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    return new Class<?>[] { String.class };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(CertificateWrapper row, int col) {
    switch (col) {
      case ENTITY_ID:
        return row.getEntity();
      default:
        break;
    }
    return null;
  }
}
