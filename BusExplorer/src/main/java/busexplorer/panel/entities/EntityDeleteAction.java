package busexplorer.panel.entities;

import busexplorer.Application;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de ação para a remoção de uma entidade.
 * 
 * @author Tecgraf
 */
public class EntityDeleteAction extends OpenBusAction<EntityWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public EntityDeleteAction(JFrame parentWindow) {
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
          List<EntityWrapper> entities = getTablePanelComponent().getSelectedElements();
          for (EntityWrapper entity : entities) {
            RegisteredEntity ref = entity.getDescriptor().ref;
            ref.remove();
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().removeSelectedElements();
          }
        }
      };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }
}
