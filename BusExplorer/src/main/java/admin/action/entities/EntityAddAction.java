package admin.action.entities;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
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
  public EntityAddAction(JFrame parentWindow,
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
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<EntityCategoryDesc> categoryDescList = admin.getCategories();
        categoryIDList = new LinkedList<String>();

        for (EntityCategoryDesc categoryDesc : categoryDescList) {
          categoryIDList.add(categoryDesc.id);
        }
      }

      @Override
      protected void afterTaskUI() {
        new EntityInputDialog(EntityAddAction.this.parentWindow, LNG
          .get("EntityAddAction.inputDialog.title"), panel, admin,
          categoryIDList).showDialog();
      }
    };

    task.execute(parentWindow, LNG.get("AddAction.waiting.title"), LNG
      .get("AddAction.waiting.msg"));

  }

}
