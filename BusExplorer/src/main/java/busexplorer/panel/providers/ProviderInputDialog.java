package busexplorer.panel.providers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Diálogo de adição/edição de provedores que será responsável pela manutenção
 * de objetos {@link ProviderWrapper} (para uso nos componentes swing) a partir de
 * uma referência remota do objeto do provedor {@link Provider}.
 *
 * @see ProviderWrapper
 * @see Provider
 *
 * @author Tecgraf
 */
public class ProviderInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel nameLabel;
  private JTextField nameTextField;
  private JLabel codeLabel;
  private JTextField codeTextField;
  private JLabel officeLabel;
  private JTextField officeTextField;
  private JLabel supportLabel;
  private JTextField supportTextField;
  private JLabel managerLabel;
  private JTextField managerTextField;
  private JLabel queryLabel;
  private JTextField queryTextField;
  private JLabel contractLabel;
  private JList<String> contractList;

  // dependency
  private TreeMap<String, ContractWrapper> contracts =
    new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private TablePanelComponent<ProviderWrapper> panel;

  private ProviderWrapper editingProvider = null;

  /**
   * Construtor do diálogo de adição/edição de provedores.
   *
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel de {@link ProviderWrapper} a ser atualizado após a adição/edição.
   * @param admin Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
   * @param contracts Lista de objetos locais para uso nos componentes swing e que representam os contratos.
   *
   * @see ContractWrapper
   */
  public ProviderInputDialog(Window parentWindow,
                             TablePanelComponent<ProviderWrapper> panel, BusAdminFacade admin,
                             List<ContractWrapper> contracts) {
    super(parentWindow, admin);
    this.panel = panel;
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
        if (editingProvider == null) {
          Provider provider =
            Application.login().extension.getProviderRegistry().add(nameTextField.getText());
          provider.code(codeTextField.getText());
          provider.office(officeTextField.getText());
          provider.support(supportTextField.getText().split(","));
          provider.manager(managerTextField.getText().split(","));
          provider.busquery(queryTextField.getText());
          editingProvider = new ProviderWrapper(provider);
        } else {
          editingProvider.name(nameTextField.getText());
          editingProvider.code(codeTextField.getText());
          editingProvider.office(officeTextField.getText());
          editingProvider.support(Arrays.asList(supportTextField.getText().split(",")));
          editingProvider.manager(Arrays.asList(managerTextField.getText().split(",")));
          editingProvider.busquery(queryTextField.getText());
          editingProvider.contracts(contractList.getSelectedValuesList());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(editingProvider, true);
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


    nameLabel =
      new JLabel(Language.get(this.getClass(), "name.label"));
    panel.add(nameLabel, new GBC(baseGBC).gridy(0).none());

    nameTextField =
      new JTextField();
    panel.add(nameTextField, new GBC(baseGBC).gridy(1).horizontal());

    codeLabel =
      new JLabel(Language.get(this.getClass(), "code.label"));
    panel.add(codeLabel, new GBC(baseGBC).gridy(2).none());

    codeTextField = new JTextField();
    panel.add(codeTextField, new GBC(baseGBC).gridy(3).horizontal());

    officeLabel =
      new JLabel(Language.get(this.getClass(), "office.label"));
    panel.add(officeLabel, new GBC(baseGBC).gridy(4).none());

    officeTextField = new JTextField();
    panel.add(officeTextField, new GBC(baseGBC).gridy(5).horizontal());

    supportLabel =
      new JLabel(Language.get(this.getClass(), "support.label"));
    panel.add(supportLabel, new GBC(baseGBC).gridy(6).none());

    supportTextField = new JTextField();
    panel.add(supportTextField, new GBC(baseGBC).gridy(7).horizontal());

    managerLabel =
      new JLabel(Language.get(this.getClass(), "manager.label"));
    panel.add(managerLabel, new GBC(baseGBC).gridy(8).none());

    managerTextField = new JTextField();
    panel.add(managerTextField, new GBC(baseGBC).gridy(9).horizontal());

    queryLabel =
      new JLabel(Language.get(this.getClass(), "busquery.label"));
    panel.add(queryLabel, new GBC(baseGBC).gridy(10).none());

    queryTextField = new JTextField();
    panel.add(queryTextField, new GBC(baseGBC).gridy(11).horizontal());

    contractLabel =
      new JLabel(Language.get(this.getClass(), "contract.label"));
    panel.add(contractLabel, new GBC(baseGBC).gridy(12).none());

    contractList = new JList<>(contracts.keySet().toArray(new String[contracts.size()]));
    contractList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    contractList.setVisibleRowCount(4);

    panel.add(new JScrollPane(contractList), new GBC(baseGBC).gridy(13).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String name = nameTextField.getText();

    if (name.equals("")) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  public void setEditionMode(ProviderWrapper info) {
    this.editingProvider = info;
    this.nameTextField.setText(info.name());
    this.codeTextField.setText(info.code());
    this.officeTextField.setText(info.office());
    this.supportTextField.setText(String.join(", ", info.support()));
    this.managerTextField.setText(String.join(", ", info.manager()));
    this.queryTextField.setText(info.busquery());
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
}
