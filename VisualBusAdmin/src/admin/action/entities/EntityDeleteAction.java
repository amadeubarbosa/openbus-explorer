package admin.action.entities;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import planref.client.util.crud.CRUDPanel;
import planref.client.util.crud.CRUDbleActionInterface;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.RegisteredEntityDesc;
import admin.BusAdmin;
import admin.action.BusAdminAbstractAction;
import admin.desktop.SimpleWindow;
import admin.wrapper.RegisteredEntityDescWrapper;

/**
 * Classe de ação para a remoção de uma entidade.
 *
 * @author Tecgraf
 */
public class EntityDeleteAction extends BusAdminAbstractAction {

  /** Painel com o CRUD de entidades */
  private CRUDPanel<RegisteredEntityDescWrapper> panel;

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param panel painel de CRUD
   * @param admin 
   */
  public EntityDeleteAction(SimpleWindow parentWindow,
    CRUDPanel<RegisteredEntityDescWrapper> panel, BusAdmin admin) {
    super(parentWindow, panel.getTable(), admin, LNG
      .get("EntityDeleteAction.name"));
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int crudActionType() {
    return CRUDbleActionInterface.TYPE_ACTION_REMOVE;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        List<RegisteredEntityDescWrapper> selectedWrappers = panel.getSelectedInfos();
        for (RegisteredEntityDescWrapper wrapper : selectedWrappers) {
          RegisteredEntityDesc entity = wrapper.getRegisteredEntityDesc();
          admin.removeEntity(entity.id);
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
