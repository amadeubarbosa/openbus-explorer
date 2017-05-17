package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.javautils.gui.StandardDialogs;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class IntegrationAddAction extends OpenBusAction<IntegrationWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public IntegrationAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(IntegrationAddAction.class.getSimpleName()
      + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.ADD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

        private List<ConsumerWrapper> consumers;
        private List<ProviderWrapper> providers;
        private List<ContractWrapper> contracts;
        @Override
        protected void performTask() throws Exception {
          consumers = ConsumerWrapper.convertToInfo(Application.login().extension.getConsumers());
          providers = ProviderWrapper.convertToInfo(Application.login().extension.getProviders());
          contracts = ContractWrapper.convertToInfo(Application.login().extension.getContracts());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            String errorMessage = null;
            if (consumers.size() == 0) {
              errorMessage = getString("error.noconsumers");
            }
            if (providers.size() == 0) {
              errorMessage = getString("error.noproviders");
            }
            if (contracts.size() == 0) {
              errorMessage = getString("error.nocontracts");
            }
            if (errorMessage != null) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"), errorMessage);
            }
            else {
              new IntegrationInputDialog(IntegrationAddAction.this.parentWindow,
                getTablePanelComponent(), admin, consumers, providers, contracts).showDialog();
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
