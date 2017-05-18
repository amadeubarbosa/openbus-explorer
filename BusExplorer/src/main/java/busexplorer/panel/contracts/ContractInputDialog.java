package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.services.governance.v1_0.Contract;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Collections;
import java.util.List;

/**
 * Diálogo de adição/edição de contratos que será responsável pela manutenção
 * de objetos {@link ContractWrapper} (para uso nos componentes swing) a partir de
 * uma referência remota do objeto do contrato {@link Contract}.
 *
 * @see ContractWrapper
 * @see Contract
 *
 * @author Tecgraf
 */
public class ContractInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel nameLabel;
  private JTextField nameTextField;
  private JLabel interfacesLabel;
  private JList<String> interfacesList;

  // dependency
  private final List<String> interfaces;

  private TablePanelComponent<ContractWrapper> panel;

  private ContractWrapper editingContract = null;

  /**
   * Construtor do diálogo de adição/edição de contratos.
   *
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel de {@link ContractWrapper} a ser atualizado após a adição/edição.
   * @param admin Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
   * @param interfaces Lista dos nomes das interfaces cadastradas.
   */
  public ContractInputDialog(Window parentWindow,
                             TablePanelComponent<ContractWrapper> panel, BusAdminFacade admin,
                             List<String> interfaces) {
    super(parentWindow, admin);
    this.panel = panel;
    this.interfaces = interfaces;
    Collections.sort(this.interfaces);
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
      new BusExplorerTask<Void>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          if (editingContract == null) {
            Contract contract = Application.login()
              .extension.getContractRegistry().add(nameTextField.getText());
            interfacesList.getSelectedValuesList()
              .forEach(iface -> contract.addInterface(iface));
            editingContract = new ContractWrapper(contract);
          } else {
            editingContract.interfaces(interfacesList.getSelectedValuesList());
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            panel.refresh(null);
            panel.selectElement(editingContract, true);
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

    nameTextField = new JTextField();
    panel.add(nameTextField, new GBC(baseGBC).gridy(1).horizontal());

    interfacesLabel =
      new JLabel(Language.get(this.getClass(), "interfaces.label"));
    panel.add(interfacesLabel, new GBC(baseGBC).gridy(2).none());

    interfacesList =
      new JList(this.interfaces.toArray());
    interfacesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    panel.add(new JScrollPane(interfacesList), new GBC(baseGBC).gridy(3).horizontal());

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
        "error.validation.name"));
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
  public void setEditionMode(ContractWrapper info) {
    this.editingContract = info;
    this.nameTextField.setText(info.name());
    this.nameTextField.setEditable(false);
    // collect all indices of JList to mark as selected
    List<String> updated = info.interfaces();
    if (updated.size() > 0) {
      // scroll to first item selected
      this.interfacesList.setSelectedValue(updated.get(0), true);
      // mark as selected
      int[] indices = new int[updated.size()];
      for (int i = 0; i < indices.length; i++) {
        indices[i] = this.interfaces.indexOf(updated.get(i));
      }
      this.interfacesList.setSelectedIndices(indices);
    }
  }
}
