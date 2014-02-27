package busexplorer.panel.authorizations;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;

import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import exception.handling.ExceptionContext;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Autorizações
 * 
 * @author Tecgraf
 */
public class AuthorizationInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel entityIDLabel;
  private JComboBox entityIDCombo;
  private JLabel interfacesLabel;
  private JList interfacesScrollList;

  private List<String> entitiesIDList;
  private List<String> interfacesList;

  private PanelComponent<AuthorizationWrapper> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição.
   * @param admin Acesso às funcionalidade de administração do barramento.
   * @param entitiesIDList Lista de entidades.
   * @param interfacesList Lista de interfaces.
   */
  public AuthorizationInputDialog(Window parentWindow,
    PanelComponent<AuthorizationWrapper> panel, BusAdmin admin, List<String>
    entitiesIDList, List<String> interfacesList) {
    super(parentWindow, LNG.get(AuthorizationInputDialog.class.getSimpleName() +
      ".title") , admin);

    this.panel = panel;

    Collections.sort(entitiesIDList, String.CASE_INSENSITIVE_ORDER);
    Collections.sort(interfacesList, String.CASE_INSENSITIVE_ORDER);

    this.entitiesIDList = entitiesIDList;
    this.interfacesList = interfacesList;
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

      @Override
      protected void performTask() throws Exception {
        String entityID = getEntityID();
        String[] selectedInterfaces = getSelectedInterfaces();

        for (String selectedInterface : selectedInterfaces) {
          admin.setAuthorization(entityID, selectedInterface);
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

    entityIDLabel =
      new JLabel(Utils.getString(this.getClass(), "entityID.label"));
    panel.add(entityIDLabel, new GBC(0, 0).insets(5).none().west());

    entityIDCombo = new JComboBox(entitiesIDList.toArray());
    panel.add(entityIDCombo, new GBC(0, 1).insets(5).horizontal().west());

    interfacesLabel =
      new JLabel(Utils.getString(this.getClass(), "interfaces.label"));
    panel.add(interfacesLabel, new GBC(0, 2).insets(5).none().west());

    interfacesScrollList = new JList(interfacesList.toArray());
    panel.add(new JScrollPane(interfacesScrollList),
      new GBC(0, 3).insets(5).horizontal().west());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (interfacesScrollList.isSelectionEmpty()) {
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyInterfaces"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Obtém o identificador da entidade da autorização a ser adicionada.
   *
   * @return o identificador da entidade da autorização a ser adicionada.
   */
  private String getEntityID() {
    return (String) this.entityIDCombo.getSelectedItem();
  }

  /**
   * Obtém o nome das interfaces selecionadas para autorização.
   *
   * @return array com o nome das interfaces selecionadas para autorização.
   */
  private String[] getSelectedInterfaces() {
    Object[] selectedInterfaces = this.interfacesScrollList.getSelectedValues();

    return Arrays.copyOf(selectedInterfaces, selectedInterfaces.length,
      String[].class);
  }
}
