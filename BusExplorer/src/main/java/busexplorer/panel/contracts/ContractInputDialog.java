package busexplorer.panel.contracts;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdmin;
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
 * Classe que d� a especializa��o necess�ria ao Di�logo de Cadastro de Entidades
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


  public ContractInputDialog(Window parentWindow,
                             TablePanelComponent<ContractWrapper> panel, BusAdmin admin,
                             List<String> interfaces) {
    super(parentWindow, LNG.get(ContractInputDialog.class.getSimpleName()
      + ".title"), admin);
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

    task.execute(this, Utils.getString(this.getClass(), "waiting.title"),
      Utils.getString(this.getClass(), "waiting.msg"));
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
      new JLabel(Utils.getString(this.getClass(), "name.label"));
    panel.add(nameLabel, new GBC(baseGBC).gridy(0).none());

    nameTextField = new JTextField();
    panel.add(nameTextField, new GBC(baseGBC).gridy(1).horizontal());

    interfacesLabel =
      new JLabel(Utils.getString(this.getClass(), "interfaces.label"));
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
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.name"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Configura o di�logo para trabalhar em modo de edi��o.
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