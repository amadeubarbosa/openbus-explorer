package busexplorer.desktop.dialog;

import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.authorizations.AuthorizationTableProvider;
import busexplorer.panel.contracts.ContractTableProvider;
import busexplorer.panel.entities.EntityTableProvider;
import busexplorer.panel.integrations.IntegrationTableProvider;
import busexplorer.panel.logins.LoginTableProvider;
import busexplorer.panel.offers.OfferTableProvider;
import busexplorer.panel.providers.ProviderTableProvider;
import busexplorer.utils.ConsistencyValidationResult;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.table.ObjectTableModel;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConsistencyValidationDialog extends BusExplorerAbstractInputDialog {

  private final ConsistencyValidationResult consistencyValidationResult;
  private final DeleteOptions removeFlags;
  private final Runnable delegate;
  private final Class languageEntryClass;
  private JPanel panel;

  public ConsistencyValidationDialog(Window parentWindow, String title, Class languageEntryClass,
                                     ConsistencyValidationResult consistencyValidationResult,
                                     DeleteOptions removeFlags, Runnable removalDelegate) {
    super(parentWindow, title);
    if (removalDelegate == null)
      throw new IllegalArgumentException("Removal delegate must be not null");

    this.languageEntryClass = languageEntryClass;
    this.consistencyValidationResult = consistencyValidationResult;
    this.removeFlags = removeFlags;
    this.delegate = removalDelegate;
  }

  private void addCheckListPanel(String title, ObjectTableModel<?> tableModel) {
    if (!tableModel.getRows().isEmpty()) {
      panel.add(new JLabel(title), "grow");
      RefreshablePanel pane = new TablePanelComponent<>(tableModel, new ArrayList<>(), false);
      pane.setPreferredSize(new Dimension(100, 150));
      panel.add(pane, "grow, push, pad 0 10 0 -10");
    }
  }

  private void addAllGovernanceCheckList() {
    ConsistencyValidationResult result = this.consistencyValidationResult;

    addCheckListPanel(Language.get(MainDialog.class, "offer.title"),
      new ObjectTableModel<>(result.getInconsistentOffers().stream().collect(Collectors.toList()), new OfferTableProvider()));

    addCheckListPanel(Language.get(MainDialog.class, "login.title"),
      new ObjectTableModel<>(result.getInconsistentLogins().stream().collect(Collectors.toList()), new LoginTableProvider()));

    addCheckListPanel(Language.get(MainDialog.class, "entity.title"),
      new ObjectTableModel<>(result.getInconsistentEntities().stream().collect(Collectors.toList()), new EntityTableProvider()));

    addCheckListPanel(Language.get(MainDialog.class, "authorization.title"),
      new ObjectTableModel<>(result.getInconsistentAuthorizations().stream().collect(Collectors.toList()), new AuthorizationTableProvider()));
  }

  private void addAllExtensionCheckList() {
    ConsistencyValidationResult result = this.consistencyValidationResult;
    addCheckListPanel(Language.get(MainDialog.class, "integration.title"),
      new ObjectTableModel<>(new ArrayList<>(result.getInconsistentIntegrations().values()), new IntegrationTableProvider()));

    addCheckListPanel(Language.get(MainDialog.class, "extension.provider.title"),
      new ObjectTableModel<>(new ArrayList<>(result.getInconsistentProviders().values()),
          new ProviderTableProvider()));

    addCheckListPanel(Language.get(MainDialog.class, "extension.contract.title"),
      new ObjectTableModel<>(new ArrayList<>(result.getInconsistentContracts().values()),
        new ContractTableProvider()));
  }

  protected JScrollPane buildErrorMessagePane() {
    return null;
  }

  protected JPanel buildFields() {
    setMinimumSize(new Dimension(500, 700));
    setPreferredSize(new Dimension(750, 700));
    panel = new JPanel(new MigLayout("fill, flowy"));

    panel.add(new JLabel(getString("consistency.message")), "grow");
    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");

    addAllExtensionCheckList();
    addAllGovernanceCheckList();

    panel.add(new JSeparator(JSeparator.HORIZONTAL),"grow");
    panel.add(new JLabel(getString("options.label")), "grow");

    JRadioButton removeDependenciesOption = new JRadioButton(
      Language.get(languageEntryClass, "consistency.remove.dependencies"));
    removeDependenciesOption.setSelected(false);
    removeDependenciesOption.addItemListener(removeFlags.fullyGovernanceRemovalChangeListener);

    JRadioButton removeIndexesOnlyOption = new JRadioButton(
      Language.get(languageEntryClass, "consistency.remove.indexesonly"));
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
    // user code
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
     *
     * @return {@code true} quando o usuário escolhe remover todos os dados de governança
     *   ou {@code false} quando opta por remover apenas os dados do evento original de remoção.
     */
    public boolean isFullyGovernanceRemoval() {
      return fullyGovernanceRemoval;
    }

    private void setFullyGovernanceRemoval(boolean fullyGovernanceRemoval) {
      this.fullyGovernanceRemoval = fullyGovernanceRemoval;
    }

  }
}
