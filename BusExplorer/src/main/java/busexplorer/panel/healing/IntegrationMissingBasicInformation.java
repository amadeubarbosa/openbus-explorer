package busexplorer.panel.healing;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.services.governance.v1_0.Integration;

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

public class IntegrationMissingBasicInformation extends IntegrationRefreshAction {

  private Consumer<TablePanelComponent> updateReportHook = null;

  public IntegrationMissingBasicInformation(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * Construtor opcional que permite um tratador que será disparado quando
   * não houver mais elementos na tabela desse tipo de pendência.
   *
   * @param parentWindow Frame de onde a ação será disparada
   * @param updateReportHook Tarefa de atualização do relatório de pendências quando a situação for normalizada
   */
  public IntegrationMissingBasicInformation(JFrame parentWindow, Consumer<TablePanelComponent> updateReportHook) {
    this(parentWindow);
    this.updateReportHook = updateReportHook;
  }

  protected TablePanelComponent<IntegrationWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new IntegrationDeleteAction((JFrame) parentWindow));
      actions.add(new IntegrationEditAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new IntegrationTableProvider()),
        actions, false, true));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    getTablePanelComponent().getElements().clear();
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<IntegrationWrapper>> task =
      new BusExplorerTask<List<IntegrationWrapper>>(ExceptionContext.Service) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          ArrayList<Integration> result = new ArrayList<>();
          List<Integration> integrations = Application.login().extension.getIntegrations();
          for (Integration integration : integrations) {
            if (integration.consumer() == null || integration.provider() == null || integration.contracts().length == 0) {
              result.add(integration);
            }
            setProgressStatus(100*i/integrations.size());
            i++;
          }
          setResult(IntegrationWrapper.convertToInfo(result));
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            TablePanelComponent tablePanelComponent = getTablePanelComponent();
            if (getResult().isEmpty() && updateReportHook != null && tablePanelComponent.getParent() != null) {
              updateReportHook.accept(tablePanelComponent);
            } else {
              tablePanelComponent.setElements(getResult());
            }
          }
        }
      };

    task.execute(parentWindow, Language.get(this.getClass().getSuperclass(), "waiting.title"),
      Language.get(this.getClass().getSuperclass(), "waiting.msg"), 2, 0, true, false);
  }
}
