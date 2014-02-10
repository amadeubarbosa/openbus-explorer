package busexplorer.panel.authorizations;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.exception.BusExplorerTask;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import exception.handling.ExceptionContext;

/**
 * Classe de a��o para criar uma autoriza��o. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class AuthorizationAddAction extends OpenBusAction<AuthorizationWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public AuthorizationAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(AuthorizationAddAction.class.getSimpleName() + ".name"));
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
  public void actionPerformed(ActionEvent arg0) {
    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application
        .exceptionHandler(), ExceptionContext.BusCore) {

      List<String> entitiesIDList = null;
      List<String> interfacesList = null;

      @Override
      protected void performTask() throws Exception {
        entitiesIDList = new LinkedList<String>();
        List<RegisteredEntityDesc> entitiesDescList = admin.getEntities();
        interfacesList = admin.getInterfaces();
        for (RegisteredEntityDesc entityDesc : entitiesDescList) {
          entitiesIDList.add(entityDesc.id);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          new AuthorizationInputDialog(AuthorizationAddAction.this.parentWindow,
            getPanelComponent(), admin, entitiesIDList,
            interfacesList).showDialog();
        }
      }
    };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
