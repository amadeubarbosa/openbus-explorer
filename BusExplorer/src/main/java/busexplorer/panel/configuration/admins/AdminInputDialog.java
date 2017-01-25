package busexplorer.panel.configuration.admins;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
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
    panel, BusAdmin admin) {
    super(parentWindow, LNG.get(AdminInputDialog.class.getSimpleName() +
      ".title"), admin);
    this.panel = panel;
  }

  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    final List<String> grantTo = new ArrayList<String>();
    grantTo.add(adminNameField.getText());

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {


      @Override
      protected void performTask() throws Exception {
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
    GBC baseGBC = new GBC().gridx(0).insets(3).west();

    adminNameLabel =
      new JLabel(LNG.get("AdminInputDialog.adminName.label"));
    panel.add(adminNameLabel, new GBC(baseGBC).gridy(0).none());

    adminNameField = new JTextField(30);
    panel.add(adminNameField, new GBC(baseGBC).gridy(1).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String administrator = adminNameField.getText();

    if (administrator.equals("")) {
      setErrorMessage(Utils.getString(this.getClass(),
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
