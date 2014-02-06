package busexplorer.panel.interfaces;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerAbstractInputDialog;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.InterfaceInfo;
import exception.handling.ExceptionContext;

/**
 * Classe que d� a especializa��o necess�ria ao Di�logo de Cadastro de
 * Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel interfaceNameLabel;
  private JTextField interfaceNameField;

  private PanelComponent<InterfaceInfo> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo.
   * @param panel Painel a ser atualizado ap�s a adi��o.
   * @param admin Acesso �s funcionalidade de administra��o do barramento.
   */
  public InterfaceInputDialog(Window parentWindow, PanelComponent<InterfaceInfo>
    panel, BusAdmin admin) {
    super(parentWindow, LNG.get(InterfaceInputDialog.class.getSimpleName() +
      ".title"), admin);
    this.panel = panel;
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
          admin.createInterface(getInterfaceName());
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

    interfaceNameLabel =
      new JLabel(LNG.get("InterfaceInputDialog.interfaceName.label"));
    panel.add(interfaceNameLabel, new GBC(baseGBC).gridy(0).none());

    interfaceNameField = new JTextField(30);
    panel.add(interfaceNameField, new GBC(baseGBC).gridy(1).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String interfaceName = interfaceNameField.getText();

    if (interfaceName.equals("")) {
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Obt�m o nome da interface a ser adicionada.
   *
   * @return o nome da interface a ser adicionada.
   */
  private String getInterfaceName() {
    return this.interfaceNameField.getText();
  }
}
