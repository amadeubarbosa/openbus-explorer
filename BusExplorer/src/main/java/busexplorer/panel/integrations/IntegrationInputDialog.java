package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.consumers.ConsumerWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdmin;
import tecgraf.openbus.services.governance.v1_0.Integration;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.List;
import java.util.TreeMap;

/**
 * Diálogo de adição/edição de integrações que será responsável pela manutenção
 * de objetos {@link IntegrationWrapper} (para uso nos componentes swing) a partir de
 * uma referência remota da integração {@link Integration}.
 *
 * @see IntegrationWrapper
 * @see Integration
 *
 * @author Tecgraf
 */
public class IntegrationInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel consumerLabel;
  private JComboBox consumerCombo;
  private JLabel providerLabel;
  private JComboBox providerCombo;
  private JLabel contractLabel;
  private JList<String> contractList;
  private JLabel activationLabel;
  private JCheckBox activationBox;

  // dependency
  private TreeMap<String, ConsumerWrapper> consumers =
    new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private TreeMap<String, ProviderWrapper> providers =
    new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private TreeMap<String, ContractWrapper> contracts =
    new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private TablePanelComponent<IntegrationWrapper> panel;

  private IntegrationWrapper editingIntegration = null;

  /**
   * Construtor do diálogo de adição/edição de integrações.
   *
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel de {@link IntegrationWrapper} a ser atualizado após a adição/edição.
   * @param admin Referência para fachada {@link BusAdmin} do Serviço de Configuração.
   * @param consumers Lista de objetos locais para uso nos componentes swing e que representam os consumidores.
   * @param providers Lista de objetos locais para uso nos componentes swing e que representam os provedores.
   * @param contracts Lista de objetos locais para uso nos componentes swing e que representam os contratos.
   *
   * @see ConsumerWrapper
   * @see ProviderWrapper
   * @see ContractWrapper
   */
  public IntegrationInputDialog(Window parentWindow,
                                TablePanelComponent<IntegrationWrapper> panel, BusAdmin admin,
                                List<ConsumerWrapper> consumers, List<ProviderWrapper> providers, List<ContractWrapper> contracts) {
    super(parentWindow, admin);
    this.panel = panel;
    for (ConsumerWrapper consumer : consumers) {
      this.consumers.put(consumer.name(), consumer);
    }
    for (ProviderWrapper provider : providers) {
      this.providers.put(provider.name(), provider);
    }
    for (ContractWrapper contract : contracts) {
      this.contracts.put(contract.name(), contract);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        if (editingIntegration == null) {
          Integration integration = Application.login()
            .extension.getIntegrationRegistry().add();
          integration.consumer(Application.login().extension.getConsumerRegistry()
            .get(getConsumerSelected().name()));
          integration.provider(Application.login().extension.getProviderRegistry()
            .get(getProviderSelected().name()));
          integration.activated(getActivation());
          editingIntegration = new IntegrationWrapper(integration);
        } else {
          editingIntegration.contracts(getContractNameSelected());
          editingIntegration.consumer(getConsumerSelected());
          editingIntegration.provider(getProviderSelected());
          editingIntegration.activate(getActivation());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(editingIntegration, true);
        }
      }
    };

    task.execute(this, Language.get(this.getClass(), "waiting.title"),
      Language.get(this.getClass(), "waiting.msg"));
    return task.getStatus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());
    GBC baseGBC = new GBC().gridx(0).insets(5).west();

    consumerLabel =
      new JLabel(Language.get(this.getClass(), "consumer.label"));
    panel.add(consumerLabel, new GBC(baseGBC).gridy(0).none());

    consumerCombo =
      new JComboBox<>(consumers.keySet().toArray(new String[consumers.size()
        ]));
    panel.add(consumerCombo, new GBC(baseGBC).gridy(1).horizontal());

    providerLabel =
      new JLabel(Language.get(this.getClass(), "provider.label"));
    panel.add(providerLabel, new GBC(baseGBC).gridy(2).none());

    providerCombo = new JComboBox<>(providers.keySet().toArray(new String[providers.size()
      ]));
    panel.add(providerCombo, new GBC(baseGBC).gridy(3).horizontal());

    contractLabel =
      new JLabel(Language.get(this.getClass(), "contract.label"));
    panel.add(contractLabel, new GBC(baseGBC).gridy(4).none());

    contractList = new JList<>(contracts.keySet().toArray(new String[contracts.size()]));
    contractList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    contractList.setVisibleRowCount(8);

    panel.add(new JScrollPane(contractList), new GBC(baseGBC).gridy(5).horizontal());

    activationLabel =
      new JLabel(Language.get(this.getClass(), "activated.label"));
    panel.add(activationLabel, new GBC(baseGBC).gridy(6).none());

    activationBox =
      new JCheckBox();
    activationBox.setSelected(false);
    panel.add(activationBox, new GBC(baseGBC).gridy(7).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (contractList.getSelectedValuesList().size() == 0) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.contracts"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Configura o diálogo para trabalhar em modo de edição.
   * 
   * @param info o dado sendo editado.
   */
  public void setEditionMode(IntegrationWrapper info) {
    this.editingIntegration = info;
    this.consumerCombo.setSelectedItem(info.consumer());
    this.providerCombo.setSelectedItem(info.provider());
    this.activationBox.setSelected(info.isActivated());
    // collect all indices of JList to mark as selected
    List<String> updated = info.contracts();
    if (updated.size() > 0) {
      // scroll to first item selected
      this.contractList.setSelectedValue(updated.get(0), true);
      int[] indices = new int[updated.size()];
      int i = 0, k = 0;
      for (String c : this.contracts.keySet()) {
        if (updated.contains(c)) {
          indices[k] = i;
          k++;
        }
        i++;
      }
      this.contractList.setSelectedIndices(indices);
    }
  }

  private ConsumerWrapper getConsumerSelected() {
    return consumers.get(this.consumerCombo.getSelectedItem());
  }
  private ProviderWrapper getProviderSelected() {
    return providers.get(this.providerCombo.getSelectedItem());
  }
  private List<String> getContractNameSelected() {
    return this.contractList.getSelectedValuesList();
  }
  private Boolean getActivation() {
    return this.activationBox.isSelected();
  }
}
