package busexplorer.panel.providers;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import busexplorer.Application;
import busexplorer.desktop.dialog.ConsistencyValidationDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;

/**
 * Classe de ação para a remoção de uma entidade.
 *
 * @author Tecgraf
 */
public class ProviderDeleteAction extends OpenBusAction<ProviderWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ProviderDeleteAction(Window parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (InputDialog.showConfirmDialog(parentWindow,
      getString("confirm.msg"),
      getString("confirm.title")) != JOptionPane.YES_OPTION) {
      return;
    }

    Collection<ProviderWrapper> providers = getTablePanelComponent().getSelectedElements();
    Runnable delegateAfterTaskUI = getTablePanelComponent()::removeSelectedElements;

    final ConsistencyValidationDialog.DeleteOptions removeFlags = new ConsistencyValidationDialog.DeleteOptions();
    ConsistencyValidationResult consistencyValidationResult = new ConsistencyValidationResult();

    BusExplorerTask deleteProviderTask =
      DeleteProviderTask(providers, delegateAfterTaskUI, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteProviderTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (ExecuteAllDependencyCheckTasks(parentWindow, providers, consistencyValidationResult)) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

  public static boolean ExecuteAllDependencyCheckTasks(Window parentWindow,
                                                       Collection<ProviderWrapper> providers,
                                                       ConsistencyValidationResult consistencyValidationResult) {
    String title = Language.get(ConsistencyValidationDialog.class, "waiting.dependency.title");
    String waitingMessage = Language.get(ProviderDeleteAction.class, "waiting.dependency.msg");

    BusExplorerTask extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(providers, consistencyValidationResult);

    BusExplorerTask governanceDependencyCheckTask =
      GovernanceDependencyCheckTask(providers, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    governanceDependencyCheckTask.execute(parentWindow, title,waitingMessage, 2, 0, true, false);

    return extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus();
  }

  public static BusExplorerTask<Void> GovernanceDependencyCheckTask(Collection<ProviderWrapper> providers,
                                                                    ConsistencyValidationResult consistencyValidationResult){
    return new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        for (ProviderWrapper provider : providers) {
          // retrieve other data
          BusQuery busQuery = new BusQuery(provider.busquery());
          for (ServiceOfferDesc offer : busQuery.filterOffers()) {
            consistencyValidationResult.getInconsistentOffers().add(new OfferWrapper(offer));
          }
          for (RegisteredEntityDesc entity : busQuery.filterEntities()) {
            consistencyValidationResult.getInconsistentEntities().add(new EntityWrapper(entity));
            for (LoginInfo login : Application.login().admin.getLogins()) {
              if (login.entity.equals(entity.id)) {
                consistencyValidationResult.getInconsistentLogins().add(new LoginWrapper(login));
              }
            }
          }
          for (Map.Entry<RegisteredEntityDesc, List<String>> e : busQuery.filterAuthorizations().entrySet()) {
            e.getValue().forEach( iface -> consistencyValidationResult.getInconsistentAuthorizations()
              .add(new AuthorizationWrapper(e.getKey(), iface)));
          }
          this.setProgressStatus(100*i/providers.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<ProviderWrapper> providers,
                                                                   ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        for (ProviderWrapper provider : providers) {
          // retrieve integration list
          String providerName = provider.remote().name();
          for (Integration integration : Application.login().extension.getIntegrationRegistry().integrations()) {
            Provider p = integration.provider();
            if (p != null && p.name().equals(providerName)) {
              consistencyValidationResult.getInconsistentIntegrations()
                .put(integration.id(), new IntegrationWrapper(integration));
            }
          }
          this.setProgressStatus(100*i/providers.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> DeleteProviderTask(Collection<ProviderWrapper> providers, Runnable delegateAfterTaskUI,
                                                         ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                         ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.Service) {

      @Override
      protected void doPerformTask() throws Exception {
        int i = 0;
        if (removeFlags.isFullyGovernanceRemoval()) {
          for (Integer id : consistencyValidationResult.getInconsistentIntegrations().keySet()) {
            Application.login().extension.getIntegrationRegistry().remove(id);
          }
        }

        for (ProviderWrapper provider : providers) {
          if (removeFlags.isFullyGovernanceRemoval()) {
            BusQuery busQuery = new BusQuery(provider.busquery());
            for (ServiceOfferDesc offer : busQuery.filterOffers()) {
              LoginInfo login = offer.ref.owner();
              Application.login().admin.invalidateLogin(login);
              // ensures this entity will not login again
              Application.login().admin.removeCertificate(login.entity);
            }
            for (Map.Entry<RegisteredEntityDesc, List<String>> authorizations : busQuery.filterAuthorizations().entrySet()) {
              for (String iface : authorizations.getValue()) {
                authorizations.getKey().ref.revokeInterface(iface);
              }
            }
            for (RegisteredEntityDesc entityDesc : busQuery.filterEntities()) {
              // if no offer, ensures certificate removal
              Application.login().admin.removeCertificate(entityDesc.id);
              entityDesc.ref.remove();
            }
          }
          Application.login().extension.getProviderRegistry().remove(provider.name());
          this.setProgressStatus(100*i/providers.size());
          i++;
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus() && (delegateAfterTaskUI != null)) {
          delegateAfterTaskUI.run();
        }
      }
    };
  }

}
