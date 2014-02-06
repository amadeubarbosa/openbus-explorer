package busexplorer.panel.categories;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerAbstractInputDialog;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.CategoryInfo;
import exception.handling.ExceptionContext;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Categorias
 * 
 * @author Tecgraf
 */
public class CategoryInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel categoryIDLabel;
  private JTextField categoryIDField;
  private JLabel categoryNameLabel;
  private JTextField categoryNameField;

  private PanelComponent<CategoryInfo> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição/edição.
   * @param admin Acesso às funcionalidade de administração do barramento.
   */
  public CategoryInputDialog(Window parentWindow, PanelComponent<CategoryInfo>
    panel, BusAdmin admin) {
    super(parentWindow, LNG.get(CategoryInputDialog.class.getSimpleName() +
      ".title"), admin);
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    Task<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          admin.createCategory(getCategoryID(), getCategoryName());
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            panel.refresh(null);
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

    categoryIDLabel =
      new JLabel(LNG.get("CategoryInputDialog.categoryID.label"));
    panel.add(categoryIDLabel, new GBC(baseGBC).gridy(0).none());

    categoryIDField = new JTextField(30);
    panel.add(categoryIDField, new GBC(baseGBC).gridy(1).horizontal());

    categoryNameLabel =
      new JLabel(LNG.get("CategoryInputDialog.categoryName.label"));
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
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Obtém o identificador da categoria a ser adicionada/editada.
   *
   * @return o identificador da categoria a ser adicionada/editada.
   */
  private String getCategoryID() {
    return this.categoryIDField.getText();
  }

  /**
   * Obtém o nome da categoria a ser adicionada/editada.
   *
   * @return o nome da categoria a ser adicionada/editada.
   */
  private String getCategoryName() {
    return this.categoryNameField.getText();
  }
}
