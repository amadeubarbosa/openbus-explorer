package admin.action;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import planref.client.util.crud.CRUDbleActionInterface;
import admin.BusAdmin;
import admin.desktop.SimpleWindow;

/**
 * Ação que contém os principais componentes a serem utilizados na janela
 * principal da aplicação
 * 
 * @author Tecgraf
 */
public abstract class BusAdminAbstractAction extends AbstractAction implements
  CRUDbleActionInterface {

  protected JTable table;
  protected BusAdmin admin;
  protected SimpleWindow parentWindow;

  /**
   * Construtor.
   * 
   * @param parentWindow
   * @param table tabela que
   * @param admin
   * @param actionName nome da ação
   */
  public BusAdminAbstractAction(SimpleWindow parentWindow, JTable table,
    BusAdmin admin, String actionName) {
    super(actionName);
    this.table = table;
    this.admin = admin;
    this.parentWindow = parentWindow;

  }
}
