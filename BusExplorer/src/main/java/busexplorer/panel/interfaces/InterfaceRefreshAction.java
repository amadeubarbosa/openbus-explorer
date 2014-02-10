package busexplorer.panel.interfaces;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

/**
 * Ação que atualiza a tabela de interfaces
 * 
 * @author Tecgraf
 * 
 */
public class InterfaceRefreshAction extends OpenBusAction<InterfaceWrapper> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public InterfaceRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(InterfaceRefreshAction.class.getSimpleName() + ".name"));
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
    Task<List<InterfaceWrapper>> task = new Task<List<InterfaceWrapper>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(InterfaceWrapper.convertToInfo(admin.getInterfaces()));
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
