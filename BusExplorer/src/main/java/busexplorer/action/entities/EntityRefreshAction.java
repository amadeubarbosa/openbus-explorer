package busexplorer.action.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.wrapper.EntityInfo;

/**
 * A��o que atualiza a tabela de entidades
 * 
 * @author Tecgraf
 * 
 */
public class EntityRefreshAction extends OpenBusAction<EntityInfo> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administra��o.
   */
  public EntityRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(EntityRefreshAction.class
      .getSimpleName()
      + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Task<List<EntityInfo>> task = new Task<List<EntityInfo>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(EntityInfo.convertToInfo(admin.getEntities()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
