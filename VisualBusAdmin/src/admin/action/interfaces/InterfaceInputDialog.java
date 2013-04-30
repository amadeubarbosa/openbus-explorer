package admin.action.interfaces;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import admin.BusAdmin;
import admin.desktop.SimpleWindow;
import admin.desktop.dialog.BusAdminAbstractInputDialog;
import admin.wrapper.InterfaceWrapper;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceInputDialog extends
  BusAdminAbstractInputDialog<InterfaceWrapper> {
  private JLabel interfaceNameLabel;
  private JTextField interfaceNameField;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param blockType Modo de Bloqueio da janela mãe.
   */
  public InterfaceInputDialog(SimpleWindow parentWindow, String title,
    CRUDPanel<InterfaceWrapper> panel, BusAdmin admin) {
    super(parentWindow, title, panel, admin);
  }

  @Override
  protected boolean accept() {
    if (hasValidFields()) {
      String interfaceName = getInterfaceName();

      setNewRow(new InterfaceWrapper(interfaceName));
      if (apply()) {
        return true;
      }
    }
    return false;
  }

  private String getInterfaceName() {
    return this.interfaceNameField.getText();
  }

  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());

    interfaceNameLabel =
      new JLabel(LNG.get("InterfaceInputDialog.interfaceName.label"));
    panel.add(interfaceNameLabel, new GBC(0, 0).west());

    interfaceNameField = new JTextField(30);
    panel.add(interfaceNameField, new GBC(0, 1).west());

    return panel;
  }

  @Override
  protected void openBusCall() throws Exception {
    InterfaceWrapper newInterfaceWrapper = getNewRow();
    String interfaceName = newInterfaceWrapper.getInterface();

    admin.createInterface(interfaceName);

  }
}
