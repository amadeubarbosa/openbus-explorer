package admin.action.categories;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.EntityCategoryDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.wrapper.EntityCategoryDescWrapper;

/**
 * Classe de ação para a remoção de uma categoria.
 *
 * @author Tecgraf
 */
public class CategoryDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de categorias */
  private CRUDPanel<EntityCategoryDescWrapper> panel;

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param panel painel de CRUD
   * @param admin 
   */
  public CategoryDeleteAction(JFrame parentWindow,
    CRUDPanel<EntityCategoryDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("CategoryDeleteAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return TYPE_ACTION_REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<EntityCategoryDescWrapper> selectedWrappers = panel.getSelectedInfos();
        for (EntityCategoryDescWrapper wrapper : selectedWrappers) {
          EntityCategoryDesc category = wrapper.getEntityCategoryDesc();
          admin.removeCategory(category.id);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          panel.removeSelectedInfos();
          panel.getTableModel().fireTableDataChanged();
        }
      }
    };

    task.execute(parentWindow, LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"));
  }
}
