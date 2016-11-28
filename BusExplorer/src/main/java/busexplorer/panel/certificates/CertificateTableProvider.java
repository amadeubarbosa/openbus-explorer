package busexplorer.panel.certificates;

import busexplorer.utils.Utils;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CertificateTableProvider implements
  ObjectTableProvider<CertificateWrapper> {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { Utils.getString(this.getClass(), "entity") };
    return colNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?>[] getColumnClasses() {
    Class<?>[] colClasses = { String.class };
    return colClasses;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCellValue(CertificateWrapper row, int col) {
    final CertificateWrapper certificate = row;

    switch (col) {
      case ENTITY_ID:
        return certificate.getEntity();

      default:
        break;
    }
    return null;
  }
}
