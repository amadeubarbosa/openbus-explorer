package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.desktop.dialog.ConsistencyValidationDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Integration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Classe de ação para a remoção de uma entidade.
 *
 * @author Tecgraf
 */
public class ConsumerDeleteAction extends OpenBusAction<ConsumerWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public ConsumerDeleteAction(JFrame parentWindow) {
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

    final ConsistencyValidationDialog.DeleteOptions removeFlags = new ConsistencyValidationDialog.DeleteOptions();
    ConsistencyValidationResult consistencyValidationResult = new ConsistencyValidationResult();
    Collection<ConsumerWrapper> consumers = getTablePanelComponent().getSelectedElements();

    BusExplorerTask deleteConsumerTask =
      DeleteConsumerTask(consumers, getTablePanelComponent()::removeSelectedElements, removeFlags, consistencyValidationResult);

    Runnable effectiveDeletion = () -> deleteConsumerTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    if (ExecuteAllDependencyCheckTasks(parentWindow, consumers, consistencyValidationResult)) {
      if (consistencyValidationResult.isEmpty()) {
        effectiveDeletion.run();
      } else {
        new ConsistencyValidationDialog(this.parentWindow, getString("confirm.title"), this.getClass(),
          consistencyValidationResult, removeFlags, effectiveDeletion).showDialog();
      }
    }
  }

  public static boolean ExecuteAllDependencyCheckTasks(Window parentWindow,
                                                       Collection<ConsumerWrapper> consumers,
                                                       ConsistencyValidationResult consistencyValidationResult) {
    String title = Language.get(ConsistencyValidationDialog.class, "waiting.dependency.title");
    String waitingMessage = Language.get(ConsumerDeleteAction.class, "waiting.dependency.msg");

    BusExplorerTask extensionDependencyCheckTask =
      ExtensionDependencyCheckTask(consumers, consistencyValidationResult);

    BusExplorerTask governanceDependencyCheckTask =
      GovernanceDependencyCheckTask(consumers, consistencyValidationResult);

    extensionDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    governanceDependencyCheckTask.execute(parentWindow, title, waitingMessage, 2, 0, true, false);

    return extensionDependencyCheckTask.getStatus() && governanceDependencyCheckTask.getStatus();
  }

  public static BusExplorerTask<Void> GovernanceDependencyCheckTask(Collection<ConsumerWrapper> consumers,
                                                                    ConsistencyValidationResult consistencyValidationResult){
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        for (ConsumerWrapper consumer : consumers) {
          // retrieve other data
          BusQuery busQuery = new BusQuery(consumer.busquery());
          for (RegisteredEntityDesc entity : busQuery.filterEntities()) {
            consistencyValidationResult.getInconsistentEntities().add(new EntityWrapper(entity));
            for (LoginInfo login : Application.login().admin.getLogins()) {
              if (login.entity.equals(entity.id)) {
                consistencyValidationResult.getInconsistentLogins().add(new LoginWrapper(login));
              }
            }
          }
          this.setProgressStatus(100*i/consumers.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> ExtensionDependencyCheckTask(Collection<ConsumerWrapper> consumers,
                                                                   ConsistencyValidationResult consistencyValidationResult) {
    return new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        setProgressDialogEnabled(true);
        int i = 0;
        for (ConsumerWrapper consumer : consumers) {
          String consumerName = consumer.remote().name();
          for (Integration integration : Application.login().extension.getIntegrationRegistry().integrations()) {
            if (integration.consumer().name().equals(consumerName)) {
              consistencyValidationResult
                .getInconsistentIntegrations().put(integration.id(), new IntegrationWrapper(integration));
            }
          }
          this.setProgressStatus(100*i/consumers.size());
          i++;
        }
      }
    };
  }

  public static BusExplorerTask<Void> DeleteConsumerTask(Collection<ConsumerWrapper> consumers, Runnable delegateAfterTaskUI,
                                                         ConsistencyValidationDialog.DeleteOptions removeFlags,
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
        for (ConsumerWrapper consumer : consumers) {
          if (removeFlags.isFullyGovernanceRemoval()) {
            BusQuery busQuery = new BusQuery(consumer.busquery());
            for (RegisteredEntityDesc entityDesc : busQuery.filterEntities()) {
              for (LoginInfo login : Application.login().admin.getLogins()) {
                if (entityDesc.id.equals(login.entity)) {
                  Application.login().admin.invalidateLogin(login);
                  // ensures this entity will not login again
                  Application.login().admin.removeCertificate(login.entity);
                }
              }
              // if no login, ensures certificate removal
              Application.login().admin.removeCertificate(entityDesc.id);
              entityDesc.ref.remove();
            }
          }
          Application.login().extension.getConsumerRegistry().remove(consumer.name());
          this.setProgressStatus(100*i/consumers.size());
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
