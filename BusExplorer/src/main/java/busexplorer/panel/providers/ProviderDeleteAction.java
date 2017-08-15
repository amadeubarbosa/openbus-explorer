package busexplorer.panel.providers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.logins.LoginTableProvider;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.openbus.core.v2_1.services.access_control.LoginInfo;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import tecgraf.openbus.services.governance.v1_0.Integration;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

  public void performChecksAndRemoteTasks(Collection<ProviderWrapper> providers, Runnable postExecutionHook) {
    final ProviderDeleteOptions removeFlags = new ProviderDeleteOptions();
    HashMap<Integer, IntegrationWrapper> inconsistentIntegrations = new HashMap<>();
    List<OfferWrapper> inconsistentOffers = new ArrayList<>();
    List<LoginWrapper> inconsistentLogins = new ArrayList<>();
    List<EntityWrapper> inconsistentEntities = new ArrayList<>();
    List<AuthorizationWrapper> inconsistentAuthorizations = new ArrayList<>();

    BusExplorerTask<Void> removeProviderTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

        @Override
        protected void doPerformTask() throws Exception {
          int i = 0;
          if (removeFlags.integrations) {
            for (Integer id : inconsistentIntegrations.keySet()) {
              Application.login().extension.getIntegrationRegistry().remove(id);
            }
          }

          for (ProviderWrapper provider : providers) {
            if (removeFlags.governanceData) {
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
          if (getStatus() && (postExecutionHook != null)) {
            postExecutionHook.run();
          }
        }
      };

    BusExplorerTask<Void> dependencyCheckTask =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {
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
                inconsistentIntegrations.put(integration.id(), new IntegrationWrapper(integration));
              }
            }
            // retrieve other data
            BusQuery busQuery = new BusQuery(provider.busquery());
            for (ServiceOfferDesc offer : busQuery.filterOffers()) {
              inconsistentOffers.add(new OfferWrapper(offer));
            }
            for (RegisteredEntityDesc entity : busQuery.filterEntities()) {
              inconsistentEntities.add(new EntityWrapper(entity));
              for (LoginInfo login : Application.login().admin.getLogins()) {
                if (login.entity.equals(entity.id)) {
                  inconsistentLogins.add(new LoginWrapper(login));
                }
              }
            }
            for (Map.Entry<RegisteredEntityDesc, List<String>> e : busQuery.filterAuthorizations().entrySet()) {
              e.getValue().forEach( iface -> inconsistentAuthorizations.add(new AuthorizationWrapper(e.getKey(), iface)));
            }

            this.setProgressStatus(100*i/providers.size());
            i++;
          }
        }
      };

    Runnable removeProviderHook = () -> removeProviderTask.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0, true, false);

    dependencyCheckTask.execute(parentWindow, getString("waiting.dependency.title"),
      getString("waiting.dependency.msg"), 2, 0, true, false);

    if (dependencyCheckTask.getStatus()) {
      if (!inconsistentIntegrations.isEmpty()) {
        BusExplorerAbstractInputDialog confirmation =
          new BusExplorerAbstractInputDialog(parentWindow,
            getString("confirm.title")) {

            public JPanel buildFields() {
              setMinimumSize(new Dimension(500, 320));
              JPanel panel = new JPanel(new MigLayout("fill, ins 0, flowy"));
              panel.add(new JLabel(
                ProviderDeleteAction.this.getString("affected.message")), "grow");
              panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
              panel.add(new JLabel(Language.get(MainDialog.class, "integration.title")), "grow");

              ObjectTableModel<IntegrationWrapper> model = new ObjectTableModel<>(
                new ArrayList<>(inconsistentIntegrations.values()), new IntegrationTableProvider());
              RefreshablePanel pane = new TablePanelComponent<>(model, new ArrayList<>(), false);
              pane.setPreferredSize(new Dimension(100, 150));
              panel.add(pane, "grow, pad 0 10 0 -10");

              panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

              JPanel checkBoxesGroup = new JPanel(new MigLayout("fill, ins 0, flowx"));

              JCheckBox contractValidationBox = new JCheckBox();
              contractValidationBox.setSelected(false);
              contractValidationBox.addChangeListener(removeFlags.integrationsChangeListener);
              checkBoxesGroup.add(contractValidationBox);

              JLabel contractValidationLabel =
                new JLabel(Language.get(ProviderDeleteAction.class, "affected.integrations.removal"));
              checkBoxesGroup.add(contractValidationLabel, "grow, wrap");
              contractValidationLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  super.mouseClicked(e);
                  contractValidationBox.setSelected(!contractValidationBox.isSelected());
                }
              });
              panel.add(checkBoxesGroup);

              return panel;
            }

            @Override
            protected boolean accept() {
              removeProviderHook.run();
              return true;
            }

            @Override
            protected boolean hasValidFields() {
              return true;
            }
          };
        confirmation.showDialog();
      } else {
        if (inconsistentOffers.isEmpty() && inconsistentLogins.isEmpty() && inconsistentEntities.isEmpty() && inconsistentAuthorizations.isEmpty())
        {
          removeProviderHook.run();
          return;
        } else {
          BusExplorerAbstractInputDialog confirmation =
            new BusExplorerAbstractInputDialog(parentWindow,
              getString("confirm.title")) {

              public JPanel buildFields() {
                setMinimumSize(new Dimension(500, 320));
                JPanel panel = new JPanel(new MigLayout("fill, ins 0, flowy"));
                panel.add(new JLabel(
                  ProviderDeleteAction.this.getString("affected.message")), "grow");
                panel.add(new JSeparator(JSeparator.HORIZONTAL), "grow");

                panel.add(new JLabel(Language.get(MainDialog.class, "offer.title")), "grow");

                RefreshablePanel offerspane = new TablePanelComponent<>(new ObjectTableModel<>(
                  inconsistentOffers, new OfferTableProvider()), new ArrayList<>(), false);
                offerspane.setPreferredSize(new Dimension(100, 150));
                panel.add(offerspane, "grow, pad 0 10 0 -10");

                panel.add(new JLabel(Language.get(MainDialog.class, "login.title")), "grow");
                RefreshablePanel loginspane = new TablePanelComponent<>(new ObjectTableModel<>(
                  inconsistentLogins, new LoginTableProvider()), new ArrayList<>(), false);
                loginspane.setPreferredSize(new Dimension(100, 150));
                panel.add(loginspane, "grow, pad 0 10 0 -10");

                panel.add(new JLabel(Language.get(MainDialog.class, "entity.title")), "grow");

                RefreshablePanel entitiespane = new TablePanelComponent<>(new ObjectTableModel<>(
                  inconsistentEntities, new EntityTableProvider()), new ArrayList<>(), false);
                entitiespane.setPreferredSize(new Dimension(100, 150));
                panel.add(entitiespane, "grow, pad 0 10 0 -10");

                panel.add(new JLabel(Language.get(MainDialog.class, "authorization.title")), "grow");
                RefreshablePanel authspane = new TablePanelComponent<>(new ObjectTableModel<>(
                  inconsistentAuthorizations, new AuthorizationTableProvider()), new ArrayList<>(), false);
                authspane.setPreferredSize(new Dimension(100, 150));
                panel.add(authspane, "grow, pad 0 10 0 -10");

                panel.add(new JSeparator(JSeparator.HORIZONTAL), "grow");

                JPanel checkBoxesGroup = new JPanel(new MigLayout("fill, ins 0, flowx"));
                JCheckBox authorizationValidationBox = new JCheckBox();
                authorizationValidationBox.setSelected(false);
                authorizationValidationBox.addChangeListener(removeFlags.governanceDataChangeListener);
                checkBoxesGroup.add(authorizationValidationBox);

                JLabel authorizationValidationLabel =
                  new JLabel(Language.get(ProviderDeleteAction.class, "affected.governanceData.removal"));
                checkBoxesGroup.add(authorizationValidationLabel, "grow");
                authorizationValidationLabel.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    authorizationValidationBox.setSelected(!authorizationValidationBox.isSelected());
                  }
                });
                panel.add(checkBoxesGroup);

                return panel;
              }

              @Override
              protected boolean accept() {
                removeProviderHook.run();
                return true;
              }

              @Override
              protected boolean hasValidFields() {
                return true;
              }
            };
          confirmation.showDialog();
        }
      }
    }
  }

  class ProviderDeleteOptions {
    /**
     * Flag para remoção das integrações
     */
    boolean integrations = false;
    /**
     * Ouvinte para troca do valor da flag de integrações por componentes de UI
     */
    final ChangeListener integrationsChangeListener = changeEvent -> this.integrations = ! this.integrations;
    /**
     * Flag para remoção dos dados da governança
     */
    boolean governanceData = false;
    /**
     * Ouvinte para troca do valor da flag dos dados da governança por componentes de UI
     */
    final ChangeListener governanceDataChangeListener = changeEvent -> this.governanceData = ! this.governanceData;
  }
}
