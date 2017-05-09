package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * A��o que atualiza a tabela de administradores
 * 
 * @author Tecgraf
 * 
 */
public class AdminRefreshAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor.
   *
   * @param parentWindow janela pai.
   * @param admin biblioteca de administra��o.
   */
  public AdminRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
    LNG.get(AdminRefreshAction.class.getSimpleName() + ".name"));
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
      new BusExplorerTask<List<AdminWrapper>>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(AdminWrapper.convertToInfo(admin.getAdmins()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}