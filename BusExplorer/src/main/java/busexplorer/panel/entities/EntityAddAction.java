package busexplorer.panel.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.StandardDialogs;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * Classe de a��o para criar uma entidade. Esta dispara um di�logo.
 * 
 * @author Tecgraf
 */
public class EntityAddAction extends OpenBusAction<EntityWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public EntityAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(EntityAddAction.class.getSimpleName()
      + ".name"));
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
      new BusExplorerTask<List<EntityCategoryDesc>>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          setResult(admin.getCategories());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            List<EntityCategoryDesc> result = getResult();
            
            if (result.size() == 0) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"),
                getString("error.noCategories.msg"));
            }
            else {
              new EntityInputDialog(EntityAddAction.this.parentWindow,
                getTablePanelComponent(), admin, getResult()).showDialog();
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
