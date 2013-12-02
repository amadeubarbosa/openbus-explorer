package busexplorer.action.entities;

import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import test.BusExplorerAbstractInputDialog;
import test.PanelComponent;
import admin.BusAdmin;
import busexplorer.wrapper.EntityInfo;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de Entidades
 * 
 * @author Tecgraf
 */
public class EntityInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel entityIDLabel;
  private JTextField entityIDField;
  private JLabel categoryIDLabel;
  private JComboBox categoryIDCombo;
  private JLabel entityNameLabel;
  private JTextField entityNameField;
  private HashMap<String, EntityCategoryDesc> categories =
    new HashMap<String, EntityCategoryDesc>();
  private PanelComponent<EntityInfo> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   */
  public EntityInputDialog(JFrame parentWindow,
    PanelComponent<EntityInfo> panel, BusAdmin admin,
    List<EntityCategoryDesc> categoryDescList) {
    super(parentWindow, LNG.get(EntityInputDialog.class.getSimpleName()
      + ".title"), admin);
    this.panel = panel;
    for (EntityCategoryDesc desc : categoryDescList) {
      categories.put(desc.id, desc);
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

    final String id = getEntityId();
    final String name = getEntityName();
    final EntityCategory category = getCategory().ref;

    Task<EntityInfo> task = new Task<EntityInfo>() {

      @Override
      protected void performTask() throws Exception {
        RegisteredEntity entity = category.registerEntity(id, name);
        RegisteredEntityDesc desc = entity.describe();
        setResult(new EntityInfo(desc));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
        }
      }
    };
    task.execute(this, getString("waiting.title"), getString("waiting.msg"));
    return task.getStatus();
  }

  private String getEntityId() {
    return this.entityIDField.getText();
  }

  private String getEntityName() {
    return this.entityNameField.getText();
  }

  private EntityCategoryDesc getCategory() {
    return categories.get(this.categoryIDCombo.getSelectedItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());

    entityIDLabel = new JLabel(getString("entityID.label"));
    panel.add(entityIDLabel, new GBC(0, 0).west());

    entityIDField = new JTextField(30);
    panel.add(entityIDField, new GBC(0, 1).west());

    categoryIDLabel = new JLabel(getString("categoryID.label"));
    panel.add(categoryIDLabel, new GBC(0, 2).west());

    categoryIDCombo =
      new JComboBox(categories.keySet().toArray(new String[categories.size()]));
    panel.add(categoryIDCombo, new GBC(0, 3).west());

    entityNameLabel = new JLabel(getString("interfaceName.label"));
    panel.add(entityNameLabel, new GBC(0, 4).west());

    entityNameField = new JTextField(30);
    panel.add(entityNameField, new GBC(0, 5).west());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String entityID = entityIDField.getText();

    if (entityID.equals("")) {
      setErrorMessage(getString("error.validation.emptyID"));
      return false;
    }

    setErrorMessage(null);
    return true;
  }
}
