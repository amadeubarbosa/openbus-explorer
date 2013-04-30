package admin.action.entities;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.RegisteredEntityDescWrapper;

/**
 * Classe de ação para criar uma entidade. Esta dispara um diálogo.
 * 
 * @author Tecgraf
 */
public class EntityAddAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de entidades */
  private CRUDPanel<RegisteredEntityDescWrapper> panel;

  /**
   * Lista que contém os IDs das categorias. O ID da categoria é chave
   * estrangeira da entidade a ser cadastrada no barramento.
   */
  private List<String> categoryIDList;

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param panel painel de CRUD
   */
  public EntityAddAction(SimpleWindow parentWindow,
    CRUDPanel<RegisteredEntityDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("EntityAddAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_ADD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    new SimpleWindowRemoteTask(parentWindow,
      LNG.get("AddAction.waiting.title"), LNG.get("AddAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      protected void performTask() throws Exception {
        List<EntityCategoryDesc> categoryDescList = admin.getCategories();
        categoryIDList = new LinkedList<String>();

        for (EntityCategoryDesc categoryDesc : categoryDescList) {
          categoryIDList.add(categoryDesc.id);
        }

      }

      @Override
      protected void updateUI() {
        if (hasNoException()) {
          new EntityInputDialog(parentWindow, LNG
            .get("EntityAddAction.inputDialog.title"), panel, admin,
            categoryIDList).showDialog();
        }
        else {
          JOptionPane.showMessageDialog(parentWindow, getTaskException()
            .getMessage(), LNG.get("ProgressDialog.error.title"),
            JOptionPane.ERROR_MESSAGE);
        }
      }
    }.start();

  }

}
