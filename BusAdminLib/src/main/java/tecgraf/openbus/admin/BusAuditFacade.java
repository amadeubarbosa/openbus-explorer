package tecgraf.openbus.admin;

import tecgraf.openbus.core.v2_1.services.admin.v1_1.AuditConfigurationOperations;

/**
 * Fachada para as funcionalidades da configura��o da Auditoria no n�cleo do OpenBus.
 *
 * @author Tecgraf
 */
public interface BusAuditFacade extends AuditConfigurationOperations {

  /**
   * Verifica se o Servi�o de Configura��o da Auditoria est� dispon�vel.
   *
   * @return {@code true} caso a interface de configura��o de auditoria esteja dispon�vel
   * e {@code false} caso n�o esteja dispon�vel ou aconte�a alguma exce��o que impe�a essa verifica��o
   */
  boolean isAuditCapable();
}
