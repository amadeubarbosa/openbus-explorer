package busexplorer.panel.configuration.admins;

import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Administradores
 * 
 * @author Tecgraf
 */
public class AdminInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel adminNameLabel;
  private JTextField adminNameField;

  private TablePanelComponent<AdminWrapper> panel;

  private AdminWrapper editingAdministrator = null;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição/edição.
   * @param admin Acesso às funcionalidade de administração do barramento.
   */
  public AdminInputDialog(Window parentWindow, TablePanelComponent<AdminWrapper>
    panel, BusAdminFacade admin) {
    super(parentWindow, admin);
    this.panel = panel;
  }

  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    final List<String> grantTo = new ArrayList<String>();
    grantTo.add(adminNameField.getText());

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (editingAdministrator == null) {
          admin.grantAdminTo(grantTo);
        } else {
          List<String> revokeFrom = new ArrayList<String>();
          revokeFrom.add(editingAdministrator.getAdmin());
          admin.grantAdminTo(grantTo);
          admin.revokeAdminFrom(revokeFrom);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(new AdminWrapper(adminNameField.getText()), true);
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
    setMinimumSize(new Dimension(330, 190));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    adminNameLabel =
      new JLabel(Language.get(this.getClass(),"adminName.label"));
    panel.add(adminNameLabel, "grow");

    adminNameField = new JTextField(30);
    adminNameField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (adminNameField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(AdminInputDialog.class,
            "error.validation.emptyID"));
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
    panel.add(adminNameField, "grow");

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (adminNameField.getText().trim().isEmpty()) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.emptyID"));
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
  public void setEditionMode(AdminWrapper info) {
    this.editingAdministrator = info;
    this.adminNameField.setText(info.getAdmin());
  }
}
