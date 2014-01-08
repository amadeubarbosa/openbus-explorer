package busexplorer.action.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.wrapper.EntityInfo;

/**
 * Classe de a��o para criar uma entidade. Esta dispara um di�logo.
 * 
 * @author Tecgraf
 */
public class EntityEditAction extends OpenBusAction<EntityInfo> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
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
    Task<List<EntityCategoryDesc>> task = new Task<List<EntityCategoryDesc>>() {

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
