package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationDeleteAction;
import busexplorer.panel.authorizations.AuthorizationRefreshAction;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Provider;

public class AuthorizationMissingProvider extends AuthorizationRefreshAction {
  public AuthorizationMissingProvider(JFrame parentWindow) {
    super(parentWindow);
  }

  protected TablePanelComponent<AuthorizationWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new AuthorizationDeleteAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new AuthorizationTableProvider()),
        actions, false, false));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    getTablePanelComponent().getElements().clear();
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<AuthorizationWrapper>> task =
      new BusExplorerTask<List<AuthorizationWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          Map<RegisteredEntityDesc, List<String>> result = Application.login().admin.getAuthorizations();
          int size = result.size();
          List<Provider> providers = Application.login().extension.getProviders();
          for (Iterator<Map.Entry<RegisteredEntityDesc, List<String>>> it = result.entrySet().iterator(); it.hasNext();) {
            boolean found = false;
            EntityWrapper entityGranted = new EntityWrapper(it.next().getKey());
            for (Provider provider : providers) {
              if (provider.busquery().isEmpty() == false) {
                Map<RegisteredEntityDesc, List<String>> map = new BusQuery(provider.busquery()).filterAuthorizations(result);
                for (RegisteredEntityDesc providerEntity : map.keySet()) {
                  if (new EntityWrapper(providerEntity).equals(entityGranted)) {
                    it.remove();
                    found = true;
                    break;
                  }
                }
              }
              if (found) {
                break;
              }
            }
            setProgressStatus(100*i/size);
            i++;
          }
          setResult(AuthorizationWrapper.convertToInfo(result));
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().setElements(getResult());
          }
        }
      };

    task.execute(parentWindow, Language.get(this.getClass().getSuperclass(), "waiting.title"),
      Language.get(this.getClass().getSuperclass(), "waiting.msg"), 2, 0, true, false);
  }
}
