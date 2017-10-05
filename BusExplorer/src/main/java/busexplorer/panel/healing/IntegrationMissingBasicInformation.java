package busexplorer.panel.healing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.integrations.IntegrationDeleteAction;
import busexplorer.panel.integrations.IntegrationEditAction;
import busexplorer.panel.integrations.IntegrationRefreshAction;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.services.governance.v1_0.Integration;

public class IntegrationMissingBasicInformation extends IntegrationRefreshAction {

  public IntegrationMissingBasicInformation(JFrame parentWindow) {
    super(parentWindow);
  }

  protected TablePanelComponent<IntegrationWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new IntegrationDeleteAction((JFrame) parentWindow));
      actions.add(new IntegrationEditAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new IntegrationTableProvider()),
        actions, false, false));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<IntegrationWrapper>> task =
      new BusExplorerTask<List<IntegrationWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          ArrayList<Integration> result = new ArrayList<>();
          for (Integration integration : Application.login().extension.getIntegrations()) {
            if (integration.consumer() == null || integration.provider() == null || integration.contracts().length == 0) {
              result.add(integration);
            }
          }
          setResult(IntegrationWrapper.convertToInfo(result));
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
