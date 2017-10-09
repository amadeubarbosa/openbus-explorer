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
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.services.governance.v1_0.Integration;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
  private JLabel contractValidationLabel;
  private JCheckBox contractValidationBox;

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
   * @param consumers Lista de objetos locais para uso nos componentes swing e que representam os consumidores.
   * @param providers Lista de objetos locais para uso nos componentes swing e que representam os provedores.
   * @param contracts Lista de objetos locais para uso nos componentes swing e que representam os contratos.
   *
   * @see ConsumerWrapper
   * @see ProviderWrapper
   * @see ContractWrapper
   */
  public IntegrationInputDialog(Window parentWindow,
                                TablePanelComponent<IntegrationWrapper> panel,
                                List<ConsumerWrapper> consumers, List<ProviderWrapper> providers, List<ContractWrapper> contracts) {
    super(parentWindow);
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

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (editingIntegration == null) {
          Integration integration = Application.login()
            .extension.getIntegrationRegistry().add();
          integration.consumer(Application.login().extension.getConsumerRegistry()
            .get(getConsumerSelected().name()));
          integration.provider(Application.login().extension.getProviderRegistry()
            .get(getProviderSelected().name()));
          integration.activated(getActivation());
          ArrayList<String> changes = new ArrayList<>();
          for (String contract : getContractNameSelected()) {
            if (integration.addContract(contract) == false) {
              if (shouldAddContractToProvider()) {
                if (integration.provider().addContract(contract) == false) {
                  for (String revert : changes) {
                    integration.provider().removeContract(revert);
                  }
                  Application.login()
                    .extension.getIntegrationRegistry().remove(integration.id());
                  throw new IllegalArgumentException(
                    Language.get(IntegrationInputDialog.class,
                      "error.provider.doesnt.support.contract", contract));
                } else {
                  changes.add(contract);
                }
                if (integration.addContract(contract) == false) {
                  Application.login()
                    .extension.getIntegrationRegistry().remove(integration.id());
                  throw new IllegalStateException(
                    Language.get(IntegrationInputDialog.class,
                      "error.couldntaddcontracts", contract));
                }
              } else {
                Application.login()
                  .extension.getIntegrationRegistry().remove(integration.id());
                throw new IllegalArgumentException(
                  Language.get(IntegrationInputDialog.class,
                    "error.provider.doesnt.support.contract", contract));
              }
            }
          }
          editingIntegration = new IntegrationWrapper(integration);
        } else {
          try {
            editingIntegration.contracts(getContractNameSelected(), shouldAddContractToProvider());
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
              Language.get(IntegrationInputDialog.class,
                "error.provider.doesnt.support.contract", e.getMessage()), e);
          } catch (IllegalStateException e) {
            throw new IllegalStateException(
              Language.get(IntegrationInputDialog.class,
                "error.couldntaddcontracts", e.getMessage()), e);
          }
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

    task.execute(this, getString( "waiting.title"), getString( "waiting.msg"));
    return task.getStatus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JPanel buildFields() {
    setMinimumSize(new Dimension(550, 400));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    consumerLabel =
      new JLabel(Language.get(this.getClass(), "consumer.label"));
    panel.add(consumerLabel, "grow");

    consumerCombo =
      new JComboBox<>(consumers.keySet().toArray(new String[consumers.size()
        ]));
    panel.add(consumerCombo, "grow");

    providerLabel =
      new JLabel(Language.get(this.getClass(), "provider.label"));
    panel.add(providerLabel, "grow");

    providerCombo = new JComboBox<>(providers.keySet().toArray(new String[providers.size()
      ]));
    panel.add(providerCombo, "grow");

    contractLabel =
      new JLabel(Language.get(this.getClass(), "contracts.label"));
    panel.add(contractLabel, "grow");

    contractList = new JList<>(contracts.keySet().toArray(new String[contracts.size()]));
    contractList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    contractList.setVisibleRowCount(8);
    contractList.addListSelectionListener(listener -> {
      if ((listener.getFirstIndex() != -1) && (listener.getLastIndex() != -1)) {
        clearErrorMessage();
      }
    });
    panel.add(new JScrollPane(contractList), "grow, push");

    JPanel checkBoxesGroup = new JPanel(new MigLayout("fill, ins 0, flowx"));

    activationBox =
      new JCheckBox();
    activationBox.setSelected(false);
        checkBoxesGroup.add(activationBox);

    activationLabel =
      new JLabel(Language.get(this.getClass(), "activated.label"));
        checkBoxesGroup.add(activationLabel, "grow, wrap");
    activationLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        activationBox.setSelected(!activationBox.isSelected());
      }
    });

    contractValidationBox =
      new JCheckBox();
    contractValidationBox.setSelected(false);
        checkBoxesGroup.add(contractValidationBox);

    contractValidationLabel =
      new JLabel(Language.get(this.getClass(), "contractvalidation.label"));
        checkBoxesGroup.add(contractValidationLabel, "grow");
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if(getConsumerSelected() == null) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.consumer"));
      consumerCombo.addActionListener(al -> clearErrorMessage());
      return false;
    }
    if(getProviderSelected() == null) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.provider"));
      providerCombo.addActionListener(al -> clearErrorMessage());
      return false;
    }
    if(getContractNameSelected().size() == 0) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.contracts"));
      contractList.addListSelectionListener(listener -> {
        if ((listener.getFirstIndex() != -1) && (listener.getLastIndex() != -1)) {
          clearErrorMessage();
        }
      });
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
  private boolean shouldAddContractToProvider() {
    return contractValidationBox.isSelected();
  }
  private Boolean getActivation() {
    return this.activationBox.isSelected();
  }
}
