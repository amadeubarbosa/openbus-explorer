package busexplorer.panel.entities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerAbstractInputDialog;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.EntityInfo;
import exception.handling.ExceptionContext;

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

  private EntityInfo editingEntity = null;

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
   * Configura o diálogo para trabalhar em modo de edição.
   * 
   * @param info o dado sendo editado.
   */
  public void setEditionMode(EntityInfo info) {
    this.editingEntity = info;
    this.categoryIDCombo.setSelectedItem(info.getCategory());
    this.categoryIDCombo.setEnabled(false);
    this.entityIDField.setText(info.getId());
    this.entityIDField.setEnabled(false);
    this.entityNameField.setText(info.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    if (editingEntity == null) {
      final String id = getEntityId();
      final String name = getEntityName();
      final EntityCategory category = getCategory().ref;

      Task<EntityInfo> task =
        new BusExplorerTask<EntityInfo>(Application.exceptionHandler(),
          ExceptionContext.BusCore) {

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
      task.execute(this, Utils.getString(this.getClass(), "waiting.title"),
        Utils.getString(this.getClass(), "waiting.msg"));
      return task.getStatus();
    }
    else {
      final String name = getEntityName();
      final RegisteredEntity entity = editingEntity.getDescriptor().ref;
      Task<Object> task =
        new BusExplorerTask<Object>(Application.exceptionHandler(),
          ExceptionContext.BusCore) {

          @Override
          protected void performTask() throws Exception {
            entity.setName(name);
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
    GridBagConstraints c = new GridBagConstraints();

    categoryIDLabel =
      new JLabel(Utils.getString(this.getClass(), "categoryID.label"));
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(categoryIDLabel, c);

    categoryIDCombo =
      new JComboBox(categories.keySet().toArray(new String[categories.size()]));
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(categoryIDCombo, c);

    entityIDLabel =
      new JLabel(Utils.getString(this.getClass(), "entityID.label"));
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(entityIDLabel, c);

    entityIDField = new JTextField();
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 3;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 2;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(entityIDField, c);

    entityNameLabel =
      new JLabel(Utils.getString(this.getClass(), "entityName.label"));
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 4;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(entityNameLabel, c);

    entityNameField = new JTextField();
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 5;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 2;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTHWEST;
    panel.add(entityNameField, c);

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String entityID = entityIDField.getText();

    if (entityID.equals("")) {
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    setErrorMessage(null);
    return true;
  }
}
