package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderEditAction;
import busexplorer.panel.providers.ProviderRefreshAction;
import busexplorer.panel.providers.ProviderTableProvider;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.services.governance.v1_0.Provider;

public class ProviderMissingBusQuery extends ProviderRefreshAction {
  public ProviderMissingBusQuery(JFrame parentWindow) {
    super(parentWindow);
  }

  protected TablePanelComponent<ProviderWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new ProviderDeleteAction(parentWindow));
      actions.add(new ProviderEditAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new ProviderTableProvider()),
        actions, false, false));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<ProviderWrapper>> task =
      new BusExplorerTask<List<ProviderWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          ArrayList<Provider> result = new ArrayList<>();
          for (Provider provider : Application.login().extension.getProviders()) {
            if (provider.busquery().isEmpty()) {
              result.add(provider);
            }
          }
          setResult(ProviderWrapper.convertToInfo(result));
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().setElements(getResult());
          }
        }
      };

    task.execute(parentWindow, Language.get(this.getClass().getSuperclass(), "waiting.title"),
      Language.get(this.getClass().getSuperclass(), "waiting.msg"), 2, 0);
  }
}
