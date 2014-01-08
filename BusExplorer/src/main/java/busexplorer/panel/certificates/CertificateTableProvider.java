package busexplorer.panel.certificates;

import tecgraf.javautils.gui.table.ObjectTableProvider;
import busexplorer.wrapper.CertificateInfo;

/**
 * Provedor de dados para a tabela de Categorias
 * 
 * @author Tecgraf
 */
public class CertificateTableProvider implements
  ObjectTableProvider<CertificateInfo> {

  /** Índice da coluna ID da Entidade */
  private static final int ENTITY_ID = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getColumnNames() {
    String[] colNames = { "Entidade" };
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
  public Object getCellValue(CertificateInfo row, int col) {
    final CertificateInfo certificate = row;

    switch (col) {
      case ENTITY_ID:
        return certificate.getEntity();

      default:
        break;
    }
    return null;
  }
}
