package busexplorer.panel.integrations;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import tecgraf.javautils.gui.StandardDialogs;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;

/**
 * Classe de a��o para criar uma entidade. Esta dispara um di�logo.
 * 
 * @author Tecgraf
 */
public class IntegrationAddAction extends OpenBusAction<IntegrationWrapper> {

  /**
   * Construtor da a��o.
   *  @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   *
   */
  public IntegrationAddAction(JFrame parentWindow) {
    super(parentWindow);
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
      new BusExplorerTask<Void>(ExceptionContext.Service) {

        private List<ConsumerWrapper> consumers;
        private List<ProviderWrapper> providers;
        private List<ContractWrapper> contracts;
        @Override
        protected void doPerformTask() throws Exception {
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
                getTablePanelComponent(), consumers, providers, contracts).showDialog();
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
