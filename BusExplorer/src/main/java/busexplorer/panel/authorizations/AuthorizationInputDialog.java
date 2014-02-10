package busexplorer.panel.authorizations;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.Task;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.PanelComponent;
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
  private JLabel interfaceNameLabel;
  private JComboBox interfaceNameCombo;

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
    Task<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          admin.setAuthorization(getEntityID(), getInterfaceName());
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

    interfaceNameLabel =
      new JLabel(Utils.getString(this.getClass(), "interfaceName.label"));
    panel.add(interfaceNameLabel, new GBC(0, 2).insets(5).none().west());

    interfaceNameCombo = new JComboBox(interfacesList.toArray());
    panel.add(interfaceNameCombo, new GBC(0, 3).insets(5).horizontal().west());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
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
   * Obtém o nome da interface da autorização a ser adicionada.
   *
   * @return o nome da interface da autorização a ser adicionada.
   */
  private String getInterfaceName() {
    return (String) this.interfaceNameCombo.getSelectedItem();
  }
}
