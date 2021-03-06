package busexplorer.panel.healing;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.services.governance.v1_0.Provider;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.offers.OfferDeleteAction;
import busexplorer.panel.offers.OfferPropertiesAction;
import busexplorer.panel.offers.OfferRefreshAction;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.Language;

public class OfferMissingProvider extends OfferRefreshAction {

  private Consumer<TablePanelComponent> updateReportHook = null;

  public OfferMissingProvider(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * Construtor opcional que permite um tratador que ser� disparado quando
   * n�o houver mais elementos na tabela desse tipo de pend�ncia.
   *
   * @param parentWindow Frame de onde a a��o ser� disparada
   * @param updateReportHook Tarefa de atualiza��o do relat�rio de pend�ncias quando a situa��o for normalizada
   */
  public OfferMissingProvider(JFrame parentWindow, Consumer<TablePanelComponent> updateReportHook) {
    this(parentWindow);
    this.updateReportHook = updateReportHook;
  }

  protected TablePanelComponent<OfferWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new OfferDeleteAction((JFrame) parentWindow));
      final OpenBusAction<?> offerPropertiesAction = new OfferPropertiesAction(parentWindow);
      actions.add(offerPropertiesAction);
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new OfferTableProvider()),
        actions, false, true));
      this.getTablePanelComponent().addTableMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
          if (mouseEvent.getClickCount() == 2) {
            offerPropertiesAction.actionPerformed(null);
          }
        }
      });
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    getTablePanelComponent().getElements().clear();
    if (Application.login().extension.isExtensionCapable() == false) {
      return;
    }
    BusExplorerTask<List<OfferWrapper>> task =
      new BusExplorerTask<List<OfferWrapper>>(ExceptionContext.Service) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          List<ServiceOfferDesc> result = Application.login().admin.getOffers();
          int size = result.size();
          List<Provider> providers = Application.login().extension.getProviders();
          for (Iterator<ServiceOfferDesc> it = result.iterator(); it.hasNext();) {
            boolean found = false;
            OfferWrapper offer = new OfferWrapper(it.next());
            for (Provider provider : providers) {
              if (provider.busquery().isEmpty() == false) {
                for (ServiceOfferDesc offerProvider : new BusQuery(provider.busquery()).filterOffers(result)) {
                  if (new OfferWrapper(offerProvider).equals(offer)) {
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

          setResult(OfferWrapper.convertToInfo(result));
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
