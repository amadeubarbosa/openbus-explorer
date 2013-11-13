package busexplorer.action.authorizations;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import busexplorer.desktop.dialog.BusAdminAbstractInputDialog;
import busexplorer.wrapper.AuthorizationWrapper;
import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;

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
   */
  public AuthorizationInputDialog(JFrame parentWindow, String title,
    CRUDPanel<AuthorizationWrapper> panel, BusAdmin admin,
    List<String> entitiesIDList, List<String> interfacesList) {
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

  /*
   * Lança exceção em caso de existência prévia da autorização a ser adicionada;
   * o barramento, por padrão, não o faz.
   */
  private void failOnExistingAuthorization(
    AuthorizationWrapper newAuthorizationWrapper) throws Exception {
    for (AuthorizationWrapper authorization : getRows()) {
      if (newAuthorizationWrapper.equals(authorization)) {
        throw new Exception("Autorização já existente");
      }
    }
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

    failOnExistingAuthorization(newAuthorizationWrapper);
  }

}
