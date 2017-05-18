package busexplorer.panel.entities;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdmin;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategory;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntity;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.List;
import java.util.TreeMap;

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

  private TreeMap<String, EntityCategoryDesc> categories =
    new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private TablePanelComponent<EntityWrapper> panel;

  private EntityWrapper editingEntity = null;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição/edição.
   * @param admin Acesso às funcionalidade de administração do barramento.
   * @param categoryDescList Lista de categorias.
   */
  public EntityInputDialog(Window parentWindow,
                           TablePanelComponent<EntityWrapper> panel, BusAdmin admin,
                           List<EntityCategoryDesc> categoryDescList) {
    super(parentWindow, admin);
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

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      RegisteredEntity entity;

      @Override
      protected void performTask() throws Exception {
        if (editingEntity == null) {
          EntityCategory category = getCategory().ref;
          entity = category.registerEntity(getEntityId(), getEntityName());
        } else {
          entity = editingEntity.getDescriptor().ref;
          entity.setName(getEntityName());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(new EntityWrapper(entity.describe()), true);
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
      new JLabel(Language.get(this.getClass(), "categoryID.label"));
    panel.add(categoryIDLabel, new GBC(baseGBC).gridy(0).none());

    categoryIDCombo =
      new JComboBox<>(categories.keySet().toArray(new String[categories.size()
        ]));
    panel.add(categoryIDCombo, new GBC(baseGBC).gridy(1).horizontal());

    entityIDLabel =
      new JLabel(Language.get(this.getClass(), "entityID.label"));
    panel.add(entityIDLabel, new GBC(baseGBC).gridy(2).none());

    entityIDField = new JTextField();
    panel.add(entityIDField, new GBC(baseGBC).gridy(3).horizontal());

    entityNameLabel =
      new JLabel(Language.get(this.getClass(), "entityName.label"));
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
  public void setEditionMode(EntityWrapper info) {
    this.editingEntity = info;
    this.categoryIDCombo.setSelectedItem(info.getCategory());
    this.categoryIDCombo.setEnabled(false);
    this.entityIDField.setText(info.getId());
    this.entityIDField.setEnabled(false);
    this.entityNameField.setText(info.getName());
  }

  /**
   * Obtém o identificador da entidade a ser adicionada/editada.
   *
   * @return o identificador da entidade a ser adicionada/editada.
   */
  private String getEntityId() {
    return this.entityIDField.getText();
  }

  /**
   * Obtém o nome da entidade a ser adicionada/editada.
   *
   * @return o nome da entidade a ser adicionada/editada.
   */
  private String getEntityName() {
    return this.entityNameField.getText();
  }

  /**
   * Obtém a categoria que conterá a entidade a ser adicionada/editada.
   *
   * @return a categoria que conterá a entidade a ser adicionada/editada.
   */
  private EntityCategoryDesc getCategory() {
    return categories.get(this.categoryIDCombo.getSelectedItem());
  }
}
