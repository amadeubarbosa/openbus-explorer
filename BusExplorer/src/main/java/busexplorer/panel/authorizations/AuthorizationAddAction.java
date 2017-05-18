package busexplorer.panel.authorizations;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.gui.StandardDialogs;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe de ação para criar uma autorização. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class AuthorizationAddAction extends OpenBusAction<AuthorizationWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public AuthorizationAddAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.ADD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

      List<String> entitiesIDList = null;
      List<String> interfacesList = null;

      @Override
      protected void performTask() throws Exception {
        entitiesIDList = new LinkedList<>();
        List<RegisteredEntityDesc> entitiesDescList = admin.getEntities();
        interfacesList = admin.getInterfaces();
        for (RegisteredEntityDesc entityDesc : entitiesDescList) {
          entitiesIDList.add(entityDesc.id);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          if (entitiesIDList.size() == 0) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"),
                getString("error.noEntities.msg"));
          }
          else if (interfacesList.size() == 0) {
              StandardDialogs.showErrorDialog(parentWindow,
                getString("error.title"),
                getString("error.noInterfaces.msg"));
          }
          else {
            new AuthorizationInputDialog(AuthorizationAddAction.this.parentWindow,
              getTablePanelComponent(), admin, entitiesIDList,
              interfacesList).showDialog();
          }
        }
      }
    };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
