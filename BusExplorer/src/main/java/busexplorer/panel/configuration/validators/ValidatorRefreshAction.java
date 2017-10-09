package busexplorer.panel.configuration.validators;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

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
   *  @param parentWindow janela pai.
   *
   */
  public ValidatorRefreshAction(JFrame parentWindow) {
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
    BusExplorerTask<List<ValidatorWrapper>> task =
      new BusExplorerTask<List<ValidatorWrapper>>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(ValidatorWrapper.convertToInfo(Application.login().admin.getPasswordValidators()));
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
