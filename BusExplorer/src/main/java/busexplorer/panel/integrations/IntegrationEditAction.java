package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class IntegrationEditAction extends OpenBusAction<IntegrationWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public IntegrationEditAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.EDIT;
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
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      List<ConsumerWrapper> consumers;
      List<ProviderWrapper> providers;
      List<ContractWrapper> contracts;

      @Override
      protected void doPerformTask() throws Exception {
        consumers = ConsumerWrapper.convertToInfo(Application.login().extension.getConsumers());
        providers = ProviderWrapper.convertToInfo(Application.login().extension.getProviders());
        contracts = ContractWrapper.convertToInfo(Application.login().extension.getContracts());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          IntegrationInputDialog dialog =
            new IntegrationInputDialog(IntegrationEditAction.this.parentWindow,
              getTablePanelComponent(), consumers, providers, contracts);
          dialog.showDialog();
          dialog.setEditionMode(getTablePanelComponent().getSelectedElement());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
