package busexplorer.panel.categories;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CategoryRefreshAction extends OpenBusAction<CategoryWrapper> {

  /**
   * Construtor.
   *  @param parentWindow janela pai.
   *
   */
  public CategoryRefreshAction(JFrame parentWindow) {
    super(parentWindow);
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
    BusExplorerTask<List<CategoryWrapper>> task =
      new BusExplorerTask<List<CategoryWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          setResult(CategoryWrapper.convertToInfo(Application.login().admin.getCategories()));
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().setElements(getResult());
          }
        }
      };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
