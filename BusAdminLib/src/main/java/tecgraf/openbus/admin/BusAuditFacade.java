package tecgraf.openbus.admin;

import tecgraf.openbus.core.v2_1.services.admin.v1_1.AuditConfigurationOperations;

/**
 * Fachada para as funcionalidades da configuração da Auditoria no núcleo do OpenBus.
 *
 * @author Tecgraf
 */
public interface BusAuditFacade extends AuditConfigurationOperations {

  /**
   * Verifica se o Serviço de Configuração da Auditoria está disponível.
   *
   * @return {@code true} caso a interface de configuração de auditoria esteja disponível
   * e {@code false} caso não esteja disponível ou aconteça alguma exceção que impeça essa verificação
   */
  boolean isAuditCapable();
}
