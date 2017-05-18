package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de a��o para criar uma entidade. Esta dispara um di�logo.
 * 
 * @author Tecgraf
 */
public class IntegrationEditAction extends OpenBusAction<IntegrationWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public IntegrationEditAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
      new BusExplorerTask<Void>(
        Application.exceptionHandler(), ExceptionContext.BusCore) {

        List<ConsumerWrapper> consumers;
        List<ProviderWrapper> providers;
        List<ContractWrapper> contracts;

      @Override
      protected void performTask() throws Exception {
        consumers = ConsumerWrapper.convertToInfo(Application.login().extension.getConsumers());
        providers = ProviderWrapper.convertToInfo(Application.login().extension.getProviders());
        contracts = ContractWrapper.convertToInfo(Application.login().extension.getContracts());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          IntegrationInputDialog dialog =
            new IntegrationInputDialog(IntegrationEditAction.this.parentWindow,
              getTablePanelComponent(), admin, consumers, providers, contracts);
          dialog.showDialog();
          dialog.setEditionMode(getTablePanelComponent().getSelectedElement());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
