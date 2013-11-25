package busexplorer.action.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;

/**
 * Ação que atualiza a tabela de entidades
 * 
 * @author Tecgraf
 * 
 */
public class EntityRefreshAction extends OpenBusAction<RegisteredEntityDesc> {

  public EntityRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get("EntityRefreshAction.name"));
  }

  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task<List<RegisteredEntityDesc>> task =
      new Task<List<RegisteredEntityDesc>>() {

        @Override
        protected void performTask() throws Exception {
          setResult(admin.getEntities());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getPanelComponent().setElements(getResult());
          }
        }
      };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"), LNG
      .get("ListAction.waiting.msg"));
  }

}
