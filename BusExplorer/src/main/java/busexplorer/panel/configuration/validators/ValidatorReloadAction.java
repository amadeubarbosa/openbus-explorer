package busexplorer.panel.configuration.validators;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;
import tecgraf.javautils.LNG;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Classe de a��o para a recarga de um validador no servidor.
 * 
 * @author Tecgraf
 */
public class ValidatorReloadAction extends OpenBusAction<ValidatorWrapper> {

  /**
   * Construtor da a��o.
   *
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin
   */
  public ValidatorReloadAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(ValidatorReloadAction.class.getSimpleName() + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.RELOAD;
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
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
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
        admin.reloadValidator(getPanelComponent().getSelectedElement()
          .getValidator());
      }

      @Override
      protected void afterTaskUI() {}
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
