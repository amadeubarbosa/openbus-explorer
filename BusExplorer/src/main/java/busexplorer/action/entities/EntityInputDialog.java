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
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import test.BusExplorerAbstractInputDialog;
import test.PanelComponent;
import admin.BusAdmin;

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
  private PanelComponent<RegisteredEntityDesc> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   */
  public EntityInputDialog(JFrame parentWindow, String title,
    PanelComponent<RegisteredEntityDesc> panel, BusAdmin admin,
    List<EntityCategoryDesc> categoryDescList) {
    super(parentWindow, title, admin);
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

    final String id = getEntityID();
    final String name = getEntityName();
    final String category = getCategory().id;

    Task<RegisteredEntityDesc> task = new Task<RegisteredEntityDesc>() {

      @Override
      protected void performTask() throws Exception {
        RegisteredEntity entity = admin.createEntity(id, name, category);
        setResult(entity.describe());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
        }
      }
    };
    task.execute(this, LNG.get("AddAction.waiting.title"), LNG
      .get("AddAction.waiting.msg"));
    return task.getStatus();
  }

  private String getEntityID() {
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

    entityIDLabel = new JLabel(LNG.get("EntityInputDialog.entityID.label"));
    panel.add(entityIDLabel, new GBC(0, 0).west());

    entityIDField = new JTextField(30);
    panel.add(entityIDField, new GBC(0, 1).west());

    categoryIDLabel = new JLabel(LNG.get("EntityInputDialog.categoryID.label"));
    panel.add(categoryIDLabel, new GBC(0, 2).west());

    categoryIDCombo =
      new JComboBox(categories.keySet().toArray(new String[categories.size()]));
    panel.add(categoryIDCombo, new GBC(0, 3).west());

    entityNameLabel =
      new JLabel(LNG.get("InterfaceInputDialog.interfaceName.label"));
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
      setErrorMessage(LNG.get("EntityInputDialog.error.validation.emptyID"));
      return false;
    }

    setErrorMessage(null);
    return true;
  }
}
