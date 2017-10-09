package busexplorer.panel.configuration.validators;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

/**
 * Classe de ação para a recarga de um validador no servidor.
 * 
 * @author Tecgraf
 */
public class ValidatorRestartAction extends OpenBusAction<ValidatorWrapper> {

  /**
   * Construtor da ação.
   *
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   */
  public ValidatorRestartAction(JFrame parentWindow) {
    super(parentWindow);
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
    if (InputDialog.showConfirmDialog(parentWindow,
      getString("confirm.explanation") + "\n" + getString("confirm.msg"),
      getString("confirm.title")) != JOptionPane.YES_OPTION) {
      return;
    }

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        BusAdminFacade admin = Application.login().admin;
        String validator = getTablePanelComponent().getSelectedElement()
                .getValidator();
        admin.delPasswordValidator(validator);
        admin.addPasswordValidator(validator);
      }

      @Override
      protected void afterTaskUI() {}
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }
}
