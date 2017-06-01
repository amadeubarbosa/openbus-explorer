package busexplorer.panel.entities;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.gui.StandardDialogs;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryDesc;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de a��o para criar uma entidade. Esta dispara um di�logo.
 * 
 * @author Tecgraf
 */
public class EntityAddAction extends OpenBusAction<EntityWrapper> {

  /**
   * Construtor da a��o.
   *  @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   *
   */
  public EntityAddAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.ADD;
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
  public void actionPerformed(ActionEvent arg0) {
    BusExplorerTask<List<EntityCategoryDesc>> task =
      new BusExplorerTask<List<EntityCategoryDesc>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          setResult(Application.login().admin.getCategories());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            List<EntityCategoryDesc> result = getResult();

            if (result.size() == 0) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"), getString("error.noCategories.msg"));
            }
            else {
              new EntityInputDialog(EntityAddAction.this.parentWindow,
                getTablePanelComponent(), getResult()).showDialog();
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
