package busexplorer.action.categories;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.wrapper.CategoryInfo;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CategoryRefreshAction extends OpenBusAction<CategoryInfo> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public CategoryRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
    LNG.get(CategoryRefreshAction.class.getSimpleName() + ".name"));
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
    Task<List<CategoryInfo>> task = new Task<List<CategoryInfo>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(CategoryInfo.convertToInfo(admin.getCategories()));
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
