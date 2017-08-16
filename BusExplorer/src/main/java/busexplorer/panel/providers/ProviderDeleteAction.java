package busexplorer.panel.providers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
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
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    performChecksAndRemoteTasks(getTablePanelComponent().getSelectedElements(),
      ProviderDeleteAction.this.getTablePanelComponent()::removeSelectedElements);
  }

  public boolean performChecksAndRemoteTasks(Collection<ProviderWrapper> providers, Runnable delegateAfterTaskUI) {
    final ConsistencyValidationDialog.DeleteOptions removeFlags = new ConsistencyValidationDialog.DeleteOptions();

    ConsistencyValidationResult consistencyValidationResult = new ConsistencyValidationResult();

    BusExplorerTask<Void> deleteProviderTask =
      DeleteProviderTask(providers, delegateAfterTaskUI, removeFlags, consistencyValidationResult);

    BusExplorerTask<Void> extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(providers, consistencyValidationResult);

    BusExplorerTask<Void> governanceDependencyCheckTask =
      GovernanceDependencyCheckTask(providers, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    governanceDependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    Runnable effectiveDeletion = () -> deleteProviderTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus()) {
      if (consistencyValidationResult.isEmpty())
      {
        effectiveDeletion.run();
      } else {
        BusExplorerAbstractInputDialog confirmation =
          new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"),
            consistencyValidationResult, removeFlags, effectiveDeletion);
        confirmation.showDialog();
      }
    }

    return deleteProviderTask.getStatus();
  }

  public static BusExplorerTask<Void> GovernanceDependencyCheckTask(Collection<ProviderWrapper> providers,
                                                                    ConsistencyValidationResult consistencyValidationResult){
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
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
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
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

  public static BusExplorerTask<Void> DeleteProviderTask(Collection<ProviderWrapper> providers,
                                                         Runnable delegateAfterTaskUI, ConsistencyValidationDialog.DeleteOptions removeFlags,
                                                         ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {

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
              Application.login().admin.removeCertificate(login.entity);
            }
            for (Map.Entry<RegisteredEntityDesc, List<String>> entry : busQuery.filterAuthorizations().entrySet()) {
              for (String auth : entry.getValue()) {
                entry.getKey().ref.revokeInterface(auth);
              }
              entry.getKey().ref.remove();
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
