package busexplorer.panel.categories;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Window;

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
  private JTextArea categoryNameField;

  private TablePanelComponent<CategoryWrapper> panel;

  private CategoryWrapper editingCategory = null;

  /**
   * Construtor.
   *  @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição/edição.
   */
  public CategoryInputDialog(Window parentWindow, TablePanelComponent<CategoryWrapper>
    panel) {
    super(parentWindow);
    this.panel = panel;
  }

  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      EntityCategory category;

      @Override
      protected void doPerformTask() throws Exception {
        if (editingCategory == null) {
          category = Application.login().admin.createCategory(getCategoryID(), getCategoryName());
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
  public JPanel buildFields() {
    setMinimumSize(new Dimension(300, 300));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    categoryIDLabel =
      new JLabel(Language.get(this.getClass(),"categoryID.label"));
    panel.add(categoryIDLabel,"grow");

    categoryIDField = new JTextField(30);
    categoryIDField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (categoryIDField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(CategoryInputDialog.class,
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
    panel.add(categoryIDField, "grow");

    categoryNameLabel =
      new JLabel(Language.get(this.getClass(),"categoryName.label"));
    panel.add(categoryNameLabel, "grow");

    categoryNameField = new JTextArea(5, 20);
    categoryNameField.setLineWrap(true);
    panel.add(new JScrollPane(categoryNameField), "grow, push");

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (categoryIDField.getText().trim().isEmpty()) {
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
  public void setEditionMode(CategoryWrapper info) {
    this.editingCategory = info;
    this.categoryIDField.setText(info.getId());
    this.categoryIDField.setEnabled(false);
    this.categoryNameField.setText(info.getName());
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
