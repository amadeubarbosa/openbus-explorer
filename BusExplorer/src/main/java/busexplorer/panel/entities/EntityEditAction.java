package busexplorer.panel.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.wrapper.EntityInfo;
import exception.handling.ExceptionContext;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class EntityEditAction extends OpenBusAction<EntityInfo> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public EntityEditAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(EntityEditAction.class.getSimpleName()
      + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.EDIT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    Task<List<EntityCategoryDesc>> task =
      new BusExplorerTask<List<EntityCategoryDesc>>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          setResult(admin.getCategories());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            EntityInputDialog dialog =
              new EntityInputDialog(EntityEditAction.this.parentWindow,
                getPanelComponent(), admin, getResult());
            dialog.showDialog();
            EntityInfo entity = getPanelComponent().getSelectedElement();
            dialog.setEditionMode(entity);
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
