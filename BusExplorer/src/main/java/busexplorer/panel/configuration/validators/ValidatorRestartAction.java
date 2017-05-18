package busexplorer.panel.configuration.validators;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

/**
 * Classe de a��o para a recarga de um validador no servidor.
 * 
 * @author Tecgraf
 */
public class ValidatorRestartAction extends OpenBusAction<ValidatorWrapper> {

  /**
   * Construtor da a��o.
   *
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin a refer�ncia para fachada {@link BusAdmin} do Servi�o de Configura��o
   */
  public ValidatorRestartAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin);
    putValue(SHORT_DESCRIPTION, getString("tooltip"));
    putValue(SMALL_ICON, ApplicationIcons.ICON_RESTART_16);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.OTHER_SINGLE_SELECTION;
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
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow,
        getString("confirm.explanation") + "\n" + getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        String validator = getTablePanelComponent().getSelectedElement()
                .getValidator();
        admin.delPasswordValidator(validator);
        admin.addPasswordValidator(validator);
      }

      @Override
      protected void afterTaskUI() {}
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
