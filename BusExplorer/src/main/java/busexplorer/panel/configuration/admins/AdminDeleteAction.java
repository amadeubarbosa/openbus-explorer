package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de ação para a remoção de um administrador.
 * 
 * @author Tecgraf
 */
public class AdminDeleteAction extends OpenBusAction<AdminWrapper> {

  /**
   * Construtor da ação.
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   */
  public AdminDeleteAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
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
      getString("confirm.msg"),
      getString("confirm.title")) != JOptionPane.YES_OPTION) {
      return;
    }

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        List<String> revoke = new ArrayList<String>();
        List<AdminWrapper> admins = getTablePanelComponent().getSelectedElements();
        for (AdminWrapper admin : admins) {
          revoke.add(admin.getAdmin());
        }
        Application.login().admin.revokeAdminFrom(revoke);
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().removeSelectedElements();
          getTablePanelComponent().refresh(null);
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }
}
