package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class ContractEditAction extends OpenBusAction<ContractWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ContractEditAction(JFrame parentWindow) {
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
    BusExplorerTask<List<String>> task =
      new BusExplorerTask<List<String>>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(Application.login().admin.getInterfaces());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          ContractInputDialog dialog =
            new ContractInputDialog(ContractEditAction.this.parentWindow,
              getTablePanelComponent(), getResult());
          dialog.showDialog();
          ContractWrapper contract = getTablePanelComponent().getSelectedElement();
          dialog.setEditionMode(contract);
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
