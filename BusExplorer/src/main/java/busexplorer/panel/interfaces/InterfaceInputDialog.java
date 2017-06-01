package busexplorer.panel.interfaces;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Window;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Interfaces
 * 
 * @author Tecgraf
 */
public class InterfaceInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel interfaceNameLabel;
  private JTextField interfaceNameField;

  private TablePanelComponent<InterfaceWrapper> panel;

  /**
   * Construtor.
   *  @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição.
   */
  public InterfaceInputDialog(Window parentWindow, TablePanelComponent<InterfaceWrapper>
    panel) {
    super(parentWindow);
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

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        Application.login().admin.createInterface(getInterfaceName());
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          InterfaceWrapper wrapper = new InterfaceWrapper(getInterfaceName());

          panel.refresh(null);
          panel.selectElement(wrapper, true);
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
    setMinimumSize(new Dimension(450, 185));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    interfaceNameLabel =
      new JLabel(Language.get(this.getClass(),"interfaceName.label"));
    panel.add(interfaceNameLabel, "grow");

    interfaceNameField = new JTextField(30);
    interfaceNameField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (interfaceNameField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(InterfaceInputDialog.class,
            "error.validation.name"));
        } else {
          clearErrorMessage();
        }
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        this.insertUpdate(documentEvent); //no difference
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
      }
    });
    panel.add(interfaceNameField, "grow");

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String interfaceName = interfaceNameField.getText().trim();
    if (interfaceName.isEmpty() || !interfaceName.startsWith("IDL:")) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.name"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Obtém o nome da interface a ser adicionada.
   *
   * @return o nome da interface a ser adicionada.
   */
  private String getInterfaceName() {
    return this.interfaceNameField.getText();
  }
}
