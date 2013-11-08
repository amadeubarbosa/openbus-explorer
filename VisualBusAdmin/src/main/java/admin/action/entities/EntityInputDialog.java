package admin.action.entities;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.desktop.dialog.BusAdminAbstractInputDialog;
import admin.wrapper.RegisteredEntityDescWrapper;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de Entidades
 * 
 * @author Tecgraf
 */
public class EntityInputDialog extends
  BusAdminAbstractInputDialog<RegisteredEntityDescWrapper> {
  private JLabel entityIDLabel;
  private JTextField entityIDField;
  private JLabel categoryIDLabel;
  private JComboBox categoryIDCombo;
  private JLabel entityNameLabel;
  private JTextField entityNameField;
  private List<String> categoryIDList;

  private String entityID;
  private String entityName;
  private String categoryID;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   */
  public EntityInputDialog(JFrame parentWindow, String title,
    CRUDPanel<RegisteredEntityDescWrapper> panel, BusAdmin admin,
    List<String> categoryIDList) {
    super(parentWindow, title, panel, admin);
    this.categoryIDList = categoryIDList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean accept() {
    if (hasValidFields()) {

      RegisteredEntityDesc entityDesc = new RegisteredEntityDesc();
      entityDesc.id = getEntityID();
      entityDesc.name = getEntityName();

      setNewRow(new RegisteredEntityDescWrapper(entityDesc, getCategoryID()));

      if (apply()) {
        return true;
      }
    }
    return false;
  }

  private String getEntityID() {
    return this.entityIDField.getText();
  }

  private String getEntityName() {
    return this.entityNameField.getText();
  }

  private String getCategoryID() {
    return (String) this.categoryIDCombo.getSelectedItem();
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

    categoryIDCombo = new JComboBox(categoryIDList.toArray());
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
  protected void openBusCall() throws Exception {
    RegisteredEntityDescWrapper newEntityDescWrapper = getNewRow();
    RegisteredEntityDesc entityDesc =
      newEntityDescWrapper.getRegisteredEntityDesc();

    admin.createEntity(entityDesc.id, entityDesc.name, newEntityDescWrapper
      .getCategoryID());

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
