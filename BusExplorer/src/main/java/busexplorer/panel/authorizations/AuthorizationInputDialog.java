package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Window;
import java.util.Collections;
import java.util.List;

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

  private TablePanelComponent<AuthorizationWrapper> panel;

  /**
   * Construtor.
   *  @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição.
   * @param entitiesIDList Lista de entidades.
   * @param interfacesList Lista de interfaces.
   */
  public AuthorizationInputDialog(Window parentWindow,
                                  TablePanelComponent<AuthorizationWrapper> panel, List<String>
                                    entitiesIDList, List<String> interfacesList) {
    super(parentWindow);

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

    BusExplorerTask<Void> task = new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        String entityID = getEntityID();
        String[] selectedInterfaces = getSelectedInterfaces();

        for (String selectedInterface : selectedInterfaces) {
          Application.login().admin.setAuthorization(entityID, selectedInterface);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          RegisteredEntityDesc desc = new RegisteredEntityDesc();
          desc.id = getEntityID();

          AuthorizationWrapper wrapper = new AuthorizationWrapper(desc,
            getSelectedInterfaces()[0]);

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
  public JPanel buildFields() {
    setMinimumSize(new Dimension(550,350));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    entityIDLabel =
      new JLabel(Language.get(this.getClass(), "entityID.label"));
    panel.add(entityIDLabel, "grow");

    entityIDCombo = new JComboBox<>(entitiesIDList.toArray());
    panel.add(entityIDCombo, "grow");

    interfacesLabel =
      new JLabel(Language.get(this.getClass(), "interfaces.label"));
    panel.add(interfacesLabel, "grow");

    interfacesScrollList = new JList<>(interfacesList.toArray());
    interfacesScrollList.addListSelectionListener(listener -> {
      if ((listener.getFirstIndex() != -1) && (listener.getLastIndex() != -1)) {
        clearErrorMessage();
      }
    });
    panel.add(new JScrollPane(interfacesScrollList),"grow");
      
    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (interfacesScrollList.isSelectionEmpty()) {
      setErrorMessage(Language.get(this.getClass(),
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
    List selectedInterfaces = this.interfacesScrollList.getSelectedValuesList();

    return (String[]) selectedInterfaces.toArray(new
      String[selectedInterfaces.size()]);
  }
}
