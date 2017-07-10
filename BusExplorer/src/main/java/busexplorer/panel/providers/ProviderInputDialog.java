package busexplorer.panel.providers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import org.omg.CORBA.BAD_PARAM;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
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
  private JTextField nameTextField;
  private JTextField codeTextField;
  private JTextField supportOfficeTextField;
  private JTextField managerOfficeTextField;
  private JTextField supportTextField;
  private JTextField managerTextField;
  private JTextArea queryTextField;
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
   * @param contracts Lista de objetos locais para uso nos componentes swing e que representam os contratos.
   *
   * @see ContractWrapper
   */
  public ProviderInputDialog(Window parentWindow,
                             TablePanelComponent<ProviderWrapper> panel,
                             List<ContractWrapper> contracts) {
    super(parentWindow);
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

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (editingProvider == null) {
          Provider provider;
          try {
            provider =
              Application.login().extension.getProviderRegistry().add(nameTextField.getText().trim());
          } catch (BAD_PARAM e) {
            throw new IllegalArgumentException(
              Language.get(ProviderInputDialog.class, "error.alreadyinuse.name"), e);
          }
          provider.code(codeTextField.getText().trim());
          provider.supportOffice(supportOfficeTextField.getText().trim());
          provider.managerOffice(managerOfficeTextField.getText().trim());
          provider.support(supportTextField.getText().trim().split(","));
          provider.manager(managerTextField.getText().trim().split(","));
          provider.busquery(queryTextField.getText().trim());
          for (String contract : contractList.getSelectedValuesList()) {
            provider.addContract(contract);
          }
          editingProvider = new ProviderWrapper(provider);
        } else {
          try {
            editingProvider.name(nameTextField.getText().trim());
          } catch (BAD_PARAM e) {
            throw new IllegalArgumentException(
              Language.get(ProviderInputDialog.class, "error.alreadyinuse.name"), e);
          }
          editingProvider.code(codeTextField.getText().trim());
          editingProvider.supportOffice(supportOfficeTextField.getText().trim());
          editingProvider.managerOffice(managerOfficeTextField.getText().trim());
          editingProvider.support(Arrays.asList(supportTextField.getText().trim().split(",")));
          editingProvider.manager(Arrays.asList(managerTextField.getText().trim().split(",")));
          editingProvider.busquery(queryTextField.getText().trim());
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
    setMinimumSize(new Dimension(550, 480));
    JPanel panel = new JPanel(new MigLayout("fill, flowy","[]10[]"));

    JLabel nameLabel = new JLabel(Language.get(this.getClass(), "name.label"));
    panel.add(nameLabel, "grow");

    nameTextField =
      new JTextField();
    nameTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (nameTextField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(ProviderInputDialog.class,
            "error.validation.name"));
        } else {
          clearErrorMessage();
        }
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        this.insertUpdate(documentEvent); //no difference
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {

      }
    });
    panel.add(nameTextField, "grow");

    JLabel codeLabel = new JLabel(Language.get(this.getClass(), "code.label"));
    panel.add(codeLabel, "grow");

    codeTextField = new JTextField();
    panel.add(codeTextField, "grow");

    JLabel supportOfficeLabel = new JLabel(Language.get(this.getClass(), "supportoffice.label"));
    panel.add(supportOfficeLabel, "grow");

    supportOfficeTextField = new JTextField();
    panel.add(supportOfficeTextField, "grow");

    JLabel supportLabel = new JLabel(Language.get(this.getClass(), "support.label"));
    panel.add(supportLabel, "grow");

    supportTextField = new JTextField();
    panel.add(supportTextField, "grow");

    JLabel managerOfficeLabel = new JLabel(Language.get(this.getClass(), "manageroffice.label"));
    panel.add(managerOfficeLabel, "grow");

    managerOfficeTextField = new JTextField();
    panel.add(managerOfficeTextField, "grow");

    JLabel managerLabel = new JLabel(Language.get(this.getClass(), "manager.label"));
    panel.add(managerLabel, "grow");

    managerTextField = new JTextField();
    panel.add(managerTextField, "grow");

    JLabel queryLabel = new JLabel(Language.get(this.getClass(), "busquery.label"));
    panel.add(queryLabel, "grow");

    queryTextField = new JTextArea(5, 20);
    queryTextField.setLineWrap(true);
    panel.add(new JScrollPane(queryTextField), "grow, wrap");

    JLabel contractLabel = new JLabel(Language.get(this.getClass(), "contracts.label"));
    panel.add(contractLabel, "grow");

    contractList = new JList<>(contracts.keySet().toArray(new String[contracts.size()]));
    contractList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    contractList.setVisibleRowCount(4);
    contractList.addListSelectionListener(listener -> {
      if ((listener.getFirstIndex() != -1) && (listener.getLastIndex() != -1)) {
        clearErrorMessage();
      }
    });
    panel.add(new JScrollPane(contractList), "grow, spany "+ (panel.getComponentCount()-2));

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (nameTextField.getText().trim().isEmpty()) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.name"));
      return false;
    } else {
      if(contractList.getSelectedValuesList().size() == 0) {
        setErrorMessage(Language.get(this.getClass(),
          "error.validation.contracts"));
        return false;
      }
    }

    clearErrorMessage();
    return true;
  }

  public void setEditionMode(ProviderWrapper info) {
    this.editingProvider = info;
    this.nameTextField.setText(info.name());
    this.codeTextField.setText(info.code());
    this.supportOfficeTextField.setText(info.supportOffice());
    this.managerOfficeTextField.setText(info.managerOffice());
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
