package admin.action.categories;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.openbus.core.v2_0.services.offer_registry.EntityCategoryDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;
import admin.wrapper.EntityCategoryDescWrapper;

/**
 * Classe de a��o para a remo��o de uma categoria.
 *
 * @author Tecgraf
 */
public class CategoryDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de categorias */
  private CRUDPanel<EntityCategoryDescWrapper> panel;

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param panel painel de CRUD
   * @param admin 
   */
  public CategoryDeleteAction(SimpleWindow parentWindow,
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
    new SimpleWindowRemoteTask(parentWindow,
      LNG.get("ListAction.waiting.title"),
      LNG.get("ListAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {

      @Override
      public void performTask() throws Exception {
        List<EntityCategoryDescWrapper> selectedWrappers = panel.getSelectedInfos();
        for (EntityCategoryDescWrapper wrapper : selectedWrappers) {
          EntityCategoryDesc category = wrapper.getEntityCategoryDesc();
          admin.removeCategory(category.id);
        }
      }

      @Override
      public void updateUI() {
        if (hasNoException()) {
          panel.removeSelectedInfos();
          panel.getTableModel().fireTableDataChanged();
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
