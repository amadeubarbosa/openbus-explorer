package busexplorer.panel.configuration.validators;

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
 * Ação que atualiza a tabela de validadores
 * 
 * @author Tecgraf
 * 
 */
public class ValidatorRefreshAction extends OpenBusAction<ValidatorWrapper> {

  /**
   * Construtor.
   *
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public ValidatorRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
    LNG.get(ValidatorRefreshAction.class.getSimpleName() + ".name"));
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
    BusExplorerTask<List<ValidatorWrapper>> task =
      new BusExplorerTask<List<ValidatorWrapper>>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(ValidatorWrapper.convertToInfo(admin.getPasswordValidators()));
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
