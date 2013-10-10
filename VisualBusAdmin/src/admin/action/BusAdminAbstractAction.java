package admin.action;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTable;

import reuse.modified.planref.client.util.crud.CRUDbleActionInterface;
import admin.BusAdmin;

/**
 * A��o que cont�m os principais componentes a serem utilizados na janela
 * principal da aplica��o
 * 
 * @author Tecgraf
 */
public abstract class BusAdminAbstractAction extends AbstractAction implements
  CRUDbleActionInterface {

  protected JTable table;
  protected BusAdmin admin;
  protected JFrame parentWindow;

  /**
   * Construtor.
   * 
   * @param parentWindow
   * @param table tabela que
   * @param admin
   * @param actionName nome da a��o
   */
  public BusAdminAbstractAction(JFrame parentWindow, JTable table,
    BusAdmin admin, String actionName) {
    super(actionName);
    this.table = table;
    this.admin = admin;
    this.parentWindow = parentWindow;

  }
}
