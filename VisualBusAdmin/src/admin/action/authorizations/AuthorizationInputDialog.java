package admin.action.authorizations;

import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.dialog.BusAdminAbstractInputDialog;
import admin.wrapper.AuthorizationWrapper;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Autorizações
 * 
 * @author Tecgraf
 */
public class AuthorizationInputDialog extends
  BusAdminAbstractInputDialog<AuthorizationWrapper> {
  private JLabel entityIDLabel;
  private JComboBox entityIDCombo;
  private JLabel interfaceNameLabel;
  private JComboBox interfaceNameCombo;

  private List<String> entitiesIDList;
  private List<String> interfacesList;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param blockType Modo de Bloqueio da janela mãe.
   */
  public AuthorizationInputDialog(SimpleWindow parentWindow, String title,
    SimpleWindowBlockType blockType, CRUDPanel<AuthorizationWrapper> panel,
    BusAdmin admin, List<String> entitiesIDList, List<String> interfacesList) {
    super(parentWindow, title, panel, admin);

    this.entitiesIDList = entitiesIDList;
    this.interfacesList = interfacesList;
  }

  @Override
  protected boolean accept() {
    if (hasValidFields()) {
      RegisteredEntityDesc entity = new RegisteredEntityDesc();
      entity.id = getEntityID();

      String interfaceName = getInterfaceName();

      setNewRow(new AuthorizationWrapper(entity, interfaceName));
      if (apply()) {
        return true;
      }
    }
    return false;
  }

  private String getEntityID() {
    return (String) this.entityIDCombo.getSelectedItem();
  }

  private String getInterfaceName() {
    return (String) this.interfaceNameCombo.getSelectedItem();
  }

  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());

    entityIDLabel =
      new JLabel(LNG.get("AuthorizationInputDialog.entityID.label"));
    panel.add(entityIDLabel, new GBC(0, 0).west());

    entityIDCombo = new JComboBox(entitiesIDList.toArray());
    panel.add(entityIDCombo, new GBC(0, 1).west());

    interfaceNameLabel =
      new JLabel(LNG.get("AuthorizationInputDialog.interfaceName.label"));
    panel.add(interfaceNameLabel, new GBC(0, 2).west());

    interfaceNameCombo = new JComboBox(interfacesList.toArray());
    panel.add(interfaceNameCombo, new GBC(0, 3).west());

    return panel;
  }

  @Override
  protected void openBusCall() throws Exception {
    AuthorizationWrapper newAuthorizationWrapper = getNewRow();

    String entityID = newAuthorizationWrapper.getEntity().id;
    String interfaceName = newAuthorizationWrapper.getInterface();

    admin.setAuthorization(entityID, interfaceName);

  }

}
