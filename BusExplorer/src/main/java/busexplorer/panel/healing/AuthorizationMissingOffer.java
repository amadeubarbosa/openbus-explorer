package busexplorer.panel.healing;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationDeleteAction;
import busexplorer.panel.authorizations.AuthorizationRefreshAction;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AuthorizationMissingOffer extends AuthorizationRefreshAction {

  private Consumer<TablePanelComponent> updateReportHook = null;

  public AuthorizationMissingOffer(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * Construtor opcional que permite um tratador que será disparado quando
   * não houver mais elementos na tabela desse tipo de pendência.
   *
   * @param parentWindow Frame de onde a ação será disparada
   * @param updateReportHook Tarefa de atualização do relatório de pendências quando a situação for normalizada
   */
  public AuthorizationMissingOffer(JFrame parentWindow, Consumer<TablePanelComponent> updateReportHook) {
    this(parentWindow);
    this.updateReportHook = updateReportHook;
  }

  protected TablePanelComponent<AuthorizationWrapper> buildTableComponent() {
    if (getTablePanelComponent() == null) {
      ArrayList actions = new ArrayList<OpenBusAction>();
      actions.add(new AuthorizationDeleteAction((JFrame) parentWindow));
      actions.add(this);
      this.setTablePanelComponent(new TablePanelComponent<>(
        new ObjectTableModel<>(new ArrayList<>(), new AuthorizationTableProvider()),
        actions, false, true));
    }
    return getTablePanelComponent();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    getTablePanelComponent().getElements().clear();
    BusExplorerTask<List<AuthorizationWrapper>> task =
      new BusExplorerTask<List<AuthorizationWrapper>>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          Map<RegisteredEntityDesc, List<String>> result = Application.login().admin.getAuthorizations();
          int size = result.size();
          ArrayList<OfferWrapper> offers = (ArrayList) OfferWrapper.convertToInfo(Application.login().admin.getOffers());
          for (Iterator<Map.Entry<RegisteredEntityDesc, List<String>>> itAuths = result.entrySet().iterator(); itAuths.hasNext();) {
            Map.Entry<RegisteredEntityDesc, List<String>> entryAuthorization = itAuths.next();
            String entityName = entryAuthorization.getKey().id;
            List<String> grantedInterfaces = entryAuthorization.getValue();
            for (Iterator<OfferWrapper> itOffer = offers.iterator(); itOffer.hasNext();) {
              OfferWrapper offer = itOffer.next();
              if (entityName.equals(offer.getEntityId())) {
                grantedInterfaces.removeAll(offer.getInterfaces());
                itOffer.remove();
                if (grantedInterfaces.isEmpty()) {
                  itAuths.remove();
                  break;
                }
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
