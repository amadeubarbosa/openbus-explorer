package busexplorer.desktop.dialog;

import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.logins.LoginTableProvider;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderTableProvider;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.table.ObjectTableModel;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class ConsistencyValidationDialog extends BusExplorerAbstractInputDialog {

  private final ConsistencyValidationResult consistencyValidationResult;
  private final DeleteOptions removeFlags;
  private final Runnable delegate;

  public ConsistencyValidationDialog(Window parentWindow, String title,
                                     ConsistencyValidationResult consistencyValidationResult,
                                     DeleteOptions removeFlags, Runnable removalDelegate) {
    super(parentWindow, title);
    this.consistencyValidationResult = consistencyValidationResult;
    this.removeFlags = removeFlags;
    this.delegate = removalDelegate;
  }

  public static void addCheckListPanel(JPanel panel, String title, String noValuesMessages,
                                ObjectTableModel<?> tableModel) {
    panel.add(new JLabel(title), "grow");
    if (!tableModel.getRows().isEmpty()) {
      RefreshablePanel pane = new TablePanelComponent<>(tableModel, new ArrayList<>(), false);
      pane.setPreferredSize(new Dimension(100, 150));
      panel.add(pane, "grow, push, pad 0 10 0 -10");
    } else {
      JLabel none = new JLabel(noValuesMessages);
      none.setHorizontalAlignment(JTextField.CENTER);
      panel.add(none, "grow");
    }
  }

  public static void addAllGovernanceCheckList(JPanel panel, ConsistencyValidationResult result) {
    addCheckListPanel(panel, Language.get(MainDialog.class, "offer.title"),
      Language.get(ConsistencyValidationDialog.class, "consistency.offers.none"),
      new ObjectTableModel<>(result.getInconsistentOffers(), new OfferTableProvider()));

    addCheckListPanel(panel, Language.get(MainDialog.class, "login.title"),
      Language.get(ConsistencyValidationDialog.class, "consistency.logins.none"),
      new ObjectTableModel<>(result.getInconsistentLogins(), new LoginTableProvider()));

    addCheckListPanel(panel, Language.get(MainDialog.class, "entity.title"),
      Language.get(ConsistencyValidationDialog.class, "consistency.entities.none"),
      new ObjectTableModel<>(result.getInconsistentEntities(), new EntityTableProvider()));

    addCheckListPanel(panel, Language.get(MainDialog.class, "authorization.title"),
      Language.get(ConsistencyValidationDialog.class, "consistency.authorizations.none"),
      new ObjectTableModel<>(result.getInconsistentAuthorizations(), new AuthorizationTableProvider()));
  }

  public static void addAllExtensionCheckList(JPanel panel, ConsistencyValidationResult result) {
    addCheckListPanel(panel, Language.get(MainDialog.class, "integration.title"),
      Language.get(ConsistencyValidationDialog.class,"consistency.integrations.none"),
      new ObjectTableModel<>(new ArrayList<>(result.getInconsistentIntegrations().values()), new IntegrationTableProvider()));

    addCheckListPanel(panel, Language.get(MainDialog.class, "extension.provider.title"),
      Language.get(ConsistencyValidationDialog.class, "consistency.providers.none"),
        new ObjectTableModel<>(new ArrayList<>(result.getInconsistentProviders().values()),
          new ProviderTableProvider()));
  }

  protected JPanel buildFields() {
    setMinimumSize(new Dimension(500, 500));
    setPreferredSize(new Dimension(750, 580));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));
    panel.add(new JLabel(getString("consistency.message")), "grow");
    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

    addAllExtensionCheckList(panel, consistencyValidationResult);
    addAllGovernanceCheckList(panel, consistencyValidationResult);

    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
    panel.add(new JLabel(getString("options.label")), "grow");

    JRadioButton removeDependenciesOption = new JRadioButton(
      Language.get(ProviderDeleteAction.class, "consistency.remove.dependencies"));
    removeDependenciesOption.setSelected(false);
    removeDependenciesOption.addItemListener(removeFlags.fullyGovernanceRemovalChangeListener);

    JRadioButton removeIndexesOnlyOption = new JRadioButton(
      Language.get(ProviderDeleteAction.class, "consistency.remove.indexesonly"));
    removeIndexesOnlyOption.setSelected(true);

    ButtonGroup group = new ButtonGroup();
    group.add(removeDependenciesOption);
    group.add(removeIndexesOnlyOption);

    panel.add(removeDependenciesOption, "grow");
    panel.add(removeIndexesOnlyOption, "grow");

    return panel;
  }

  @Override
  protected boolean accept() {
    delegate.run();
    return true;
  }

  @Override
  protected boolean hasValidFields() {
    return true;
  }

  public static class DeleteOptions {

    private boolean fullyGovernanceRemoval = false;
    /**
     * Ouvinte para troca do valor da flag de integrações por componentes de UI
     */
    public final ItemListener fullyGovernanceRemovalChangeListener = itemEvent -> {
      if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
        this.setFullyGovernanceRemoval(true);
      } else {
        this.setFullyGovernanceRemoval(false);
      }
    };

    /**
     * Flag para remoção das integrações
     */
    public boolean isFullyGovernanceRemoval() {
      return fullyGovernanceRemoval;
    }

    private void setFullyGovernanceRemoval(boolean fullyGovernanceRemoval) {
      this.fullyGovernanceRemoval = fullyGovernanceRemoval;
    }

  }
}
