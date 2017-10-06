package busexplorer.panel.healing;

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

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProviderMissingBusQuery extends ProviderRefreshAction {

  private Consumer<TablePanelComponent> updateReportHook = null;

  public ProviderMissingBusQuery(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * Construtor opcional que permite um tratador que será disparado quando
   * não houver mais elementos na tabela desse tipo de pendência.
   *
   * @param parentWindow Frame de onde a ação será disparada
   * @param updateReportHook Tarefa de atualização do relatório de pendências quando a situação for normalizada
   */
  public ProviderMissingBusQuery(JFrame parentWindow, Consumer<TablePanelComponent> updateReportHook) {
    this(parentWindow);
    this.updateReportHook = updateReportHook;
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
    getTablePanelComponent().getElements().clear();
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<ProviderWrapper>> task =
      new BusExplorerTask<List<ProviderWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          ArrayList<Provider> result = new ArrayList<>();
          List<Provider> providers = Application.login().extension.getProviders();
          for (Provider provider : providers) {
            if (provider.busquery().isEmpty()) {
              result.add(provider);
            }
            setProgressStatus(100*i/providers.size());
            i++;
          }
          setResult(ProviderWrapper.convertToInfo(result));
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
