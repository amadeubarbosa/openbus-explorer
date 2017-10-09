package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ação que atualiza a tabela de administradores
 * 
 * @author Tecgraf
 * 
 */
public class AdminRefreshAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor.
   *
   * @param parentWindow janela pai.
   */
  public AdminRefreshAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<AdminWrapper>> task =
      new BusExplorerTask<List<AdminWrapper>>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(AdminWrapper.convertToInfo(Application.login().admin.getAdmins()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
