package admin.action.categories;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import admin.desktop.dialog.BusAdminAbstractInputDialog;
import admin.wrapper.EntityCategoryDescWrapper;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Categorias
 * 
 * @author Tecgraf
 */
public class CategoryInputDialog extends
  BusAdminAbstractInputDialog<EntityCategoryDescWrapper> {
  private JLabel categoryIDLabel;
  private JTextField categoryIDField;
  private JLabel categoryNameLabel;
  private JTextField categoryNameField;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param blockType Modo de Bloqueio da janela mãe.
   */
  public CategoryInputDialog(JFrame parentWindow, String title,
    CRUDPanel<EntityCategoryDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, title, panel, admin);
  }

  @Override
  protected boolean accept() {
    if (hasValidFields()) {
      EntityCategoryDesc category = new EntityCategoryDesc();
      category.id = getCategoryID();
      category.name = getCategoryName();

      setNewRow(new EntityCategoryDescWrapper(category));
      if (apply()) {
        return true;
      }
    }
    return false;
  }

  private String getCategoryName() {
    return this.categoryNameField.getText();
  }

  private String getCategoryID() {
    return this.categoryIDField.getText();
  }

  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());

    categoryIDLabel =
      new JLabel(LNG.get("CategoryInputDialog.categoryID.label"));
    panel.add(categoryIDLabel, new GBC(0, 0).west());

    categoryIDField = new JTextField(30);
    panel.add(categoryIDField, new GBC(0, 1).west());

    categoryNameLabel =
      new JLabel(LNG.get("CategoryInputDialog.categoryName.label"));
    panel.add(categoryNameLabel, new GBC(0, 2).west());

    categoryNameField = new JTextField(30);
    panel.add(categoryNameField, new GBC(0, 3).west());

    return panel;
  }

  @Override
  protected void openBusCall() throws Exception {
    EntityCategoryDescWrapper newCategoryWrapper = getNewRow();
    EntityCategoryDesc newCategoryDesc =
      newCategoryWrapper.getEntityCategoryDesc();

    admin.createCategory(newCategoryDesc.id, newCategoryDesc.name);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String categoryID = categoryIDField.getText();

    if (categoryID.equals("")) {
      setErrorMessage(LNG.get("CategoryInputDialog.error.validation.emptyID"));
      return false;
    }

    setErrorMessage(null);
    return true;
  }
}
