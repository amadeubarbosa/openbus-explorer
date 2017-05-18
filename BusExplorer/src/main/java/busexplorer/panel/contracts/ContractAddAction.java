package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.gui.StandardDialogs;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class ContractAddAction extends OpenBusAction<ContractWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public ContractAddAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
    BusExplorerTask<List<String>> task =
      new BusExplorerTask<List<String>>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          setResult(admin.getInterfaces());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            List<String> result = getResult();

            if (result.size() == 0) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"), getString("error.nointerfaces"));
            }
            else {
              new ContractInputDialog(ContractAddAction.this.parentWindow,
                getTablePanelComponent(), admin, getResult()).showDialog();
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
