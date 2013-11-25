package busexplorer.action.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class EntityAddAction extends OpenBusAction<RegisteredEntityDesc> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param panel painel de CRUD
   */
  public EntityAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get("EntityAddAction.name"));
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
  public void actionPerformed(ActionEvent arg0) {
    Task<List<EntityCategoryDesc>> task = new Task<List<EntityCategoryDesc>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(admin.getCategories());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          new EntityInputDialog(EntityAddAction.this.parentWindow, LNG
            .get("EntityAddAction.inputDialog.title"), getPanelComponent(),
            admin, getResult()).showDialog();
        }
      }
    };
    task.execute(parentWindow, LNG.get("AddAction.waiting.title"), LNG
      .get("AddAction.waiting.msg"));
  }

}
