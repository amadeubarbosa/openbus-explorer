package busexplorer.panel.entities;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerAbstractInputDialog;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.EntityInfo;
import exception.handling.ExceptionContext;

/**
 * Classe que d� a especializa��o necess�ria ao Di�logo de Cadastro de Entidades
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

  private TreeMap<String, EntityCategoryDesc> categories =
    new TreeMap<String, EntityCategoryDesc>(String.CASE_INSENSITIVE_ORDER);

  private PanelComponent<EntityInfo> panel;

  private EntityInfo editingEntity = null;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo.
   * @param panel Painel a ser atualizado ap�s a adi��o/edi��o.
   * @param admin Acesso �s funcionalidade de administra��o do barramento.
   * @param categoryDescList Lista de categorias.
   */
  public EntityInputDialog(Window parentWindow,
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

    Task<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          if (editingEntity == null) {
            EntityCategory category = getCategory().ref;
            category.registerEntity(getEntityId(), getEntityName());
          } else {
            RegisteredEntity entity = editingEntity.getDescriptor().ref;
            entity.setName(getEntityName());
          }
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
      new JLabel(Utils.getString(this.getClass(), "categoryID.label"));
    panel.add(categoryIDLabel, new GBC(baseGBC).gridy(0).none());

    categoryIDCombo =
      new JComboBox(categories.keySet().toArray(new String[categories.size()]));
    panel.add(categoryIDCombo, new GBC(baseGBC).gridy(1).horizontal());

    entityIDLabel =
      new JLabel(Utils.getString(this.getClass(), "entityID.label"));
    panel.add(entityIDLabel, new GBC(baseGBC).gridy(2).none());

    entityIDField = new JTextField();
    panel.add(entityIDField, new GBC(baseGBC).gridy(3).horizontal());

    entityNameLabel =
      new JLabel(Utils.getString(this.getClass(), "entityName.label"));
    panel.add(entityNameLabel, new GBC(baseGBC).gridy(4).none());

    entityNameField = new JTextField();
    panel.add(entityNameField, new GBC(baseGBC).gridy(5).horizontal());

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

    clearErrorMessage();
    return true;
  }

  /**
   * Configura o di�logo para trabalhar em modo de edi��o.
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
   * Obt�m o identificador da entidade a ser adicionada/editada.
   *
   * @return o identificador da entidade a ser adicionada/editada.
   */
  private String getEntityId() {
    return this.entityIDField.getText();
  }

  /**
   * Obt�m o nome da entidade a ser adicionada/editada.
   *
   * @return o nome da entidade a ser adicionada/editada.
   */
  private String getEntityName() {
    return this.entityNameField.getText();
  }

  /**
   * Obt�m a categoria que conter� a entidade a ser adicionada/editada.
   *
   * @return a categoria que conter� a entidade a ser adicionada/editada.
   */
  private EntityCategoryDesc getCategory() {
    return categories.get(this.categoryIDCombo.getSelectedItem());
  }
}