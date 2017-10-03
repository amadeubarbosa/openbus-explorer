package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.providers.ProviderRefreshAction;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.openbus.services.governance.v1_0.Provider;

public class ProvidersMissingFieldsRefreshAction extends ProviderRefreshAction {

  public ProvidersMissingFieldsRefreshAction(JFrame parentWindow) {
    super(parentWindow);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<ProviderWrapper>> task =
      new BusExplorerTask<List<ProviderWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          ArrayList<Provider> result = new ArrayList<>();
          for (Provider provider : Application.login().extension.getProviders()) {
            if ((provider.contracts().length == 0) || provider.code().isEmpty()
              || provider.busquery().isEmpty() || provider.manageroffice().isEmpty()
              || provider.manager().length == 0 || provider.support().length == 0
              || provider.supportoffice().isEmpty()) {
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

    task.execute(parentWindow, Language.get(ProviderRefreshAction.class, "waiting.title"),
      Language.get(ProviderRefreshAction.class, "waiting.msg"), 2, 0);
  }
}
