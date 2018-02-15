package busexplorer.panel.providers;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.utils.BusExplorerTask;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class ProviderEditAction extends OpenBusAction<ProviderWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ProviderEditAction(JFrame parentWindow) {
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
    BusExplorerTask<List<ContractWrapper>> task =
      new BusExplorerTask<List<ContractWrapper>>(ExceptionContext.Service) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(ContractWrapper.convertToInfo(Application.login().extension.getContracts()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          ProviderInputDialog dialog =
            new ProviderInputDialog(ProviderEditAction.this.parentWindow,
              getTablePanelComponent(), getResult());
          dialog.showDialog();
          ProviderWrapper entity = getTablePanelComponent().getSelectedElement();
          dialog.setEditionMode(entity);
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
