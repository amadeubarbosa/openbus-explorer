package busexplorer.panel.categories;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Window;

/**
 * Classe que d� a especializa��o necess�ria ao Di�logo de Cadastro de
 * Categorias
 * 
 * @author Tecgraf
 */
public class CategoryInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel categoryIDLabel;
  private JTextField categoryIDField;
  private JLabel categoryNameLabel;
  private JTextField categoryNameField;

  private TablePanelComponent<CategoryWrapper> panel;

  private CategoryWrapper editingCategory = null;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo.
   * @param panel Painel a ser atualizado ap�s a adi��o/edi��o.
   * @param admin Acesso �s funcionalidade de administra��o do barramento.
   */
  public CategoryInputDialog(Window parentWindow, TablePanelComponent<CategoryWrapper>
    panel, BusAdminFacade admin) {
    super(parentWindow, admin);
    this.panel = panel;
  }

  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      EntityCategory category;

      @Override
      protected void performTask() throws Exception {
        if (editingCategory == null) {
          category = admin.createCategory(getCategoryID(), getCategoryName());
        } else {
          category = editingCategory.getDescriptor().ref;
          category.setName(getCategoryName());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(new CategoryWrapper(category.describe()), true);
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

    categoryIDLabel =
      new JLabel(Language.get(this.getClass(),"categoryID.label"));
    panel.add(categoryIDLabel, new GBC(baseGBC).gridy(0).none());

    categoryIDField = new JTextField(30);
    panel.add(categoryIDField, new GBC(baseGBC).gridy(1).horizontal());

    categoryNameLabel =
      new JLabel(Language.get(this.getClass(),"categoryName.label"));
    panel.add(categoryNameLabel, new GBC(baseGBC).gridy(2).none());

    categoryNameField = new JTextField(30);
    panel.add(categoryNameField, new GBC(baseGBC).gridy(3).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String categoryID = categoryIDField.getText();

    if (categoryID.equals("")) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.emptyID"));
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
  public void setEditionMode(CategoryWrapper info) {
    this.editingCategory = info;
    this.categoryIDField.setText(info.getId());
    this.categoryIDField.setEnabled(false);
    this.categoryNameField.setText(info.getName());
  }

  /**
   * Obt�m o identificador da categoria a ser adicionada/editada.
   *
   * @return o identificador da categoria a ser adicionada/editada.
   */
  private String getCategoryID() {
    return this.categoryIDField.getText();
  }

  /**
   * Obt�m o nome da categoria a ser adicionada/editada.
   *
   * @return o nome da categoria a ser adicionada/editada.
   */
  private String getCategoryName() {
    return this.categoryNameField.getText();
  }
}
