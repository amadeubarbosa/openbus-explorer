package admin.desktop.dialog;

import logistic.logic.common.Identifiable;
import planref.client.util.crud.CRUDPanel;
import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.remote.SimpleWindowRemoteTask;

public abstract class BusAdminAbstractInputDialog<T extends Identifiable<T>>
  extends InputDialog {
  private CRUDPanel<T> panel;
  Exception taskException = null;

  /**
   * Objeto a ser cadastrado no barramento
   */
  private T newRow;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param blockType Modo de Bloqueio da janela mãe.
   */
  public BusAdminAbstractInputDialog(SimpleWindow parentWindow, String title,
    CRUDPanel<T> panel, BusAdmin admin) {
    super(parentWindow, title, admin);
    this.panel = panel;
  }

  /**
   * Método que salva o novo objeto no barramento e atualiza a janela mãe do
   * diálogo com a categoria criada/modificada no diálogo.
   * 
   * @return Verdadeiro, se o objeto não existir e puder ser aplicado com
   *         sucesso.
   */
  protected boolean apply() {

    Thread task =
      new Thread(new SimpleWindowRemoteTask(this, LNG
        .get("AddAction.waiting.title"), LNG.get("AddAction.waiting.msg"),
        new SimpleWindowBlockType(Type.BLOCK_THIS)) {

        @Override
        protected void performTask() throws Exception {
          try {
            openBusCall();
          }
          catch (Exception e) {
            taskException = e;
            throw e;
          }
        }

        @Override
        protected void updateUI() {
          if (hasNoException()) {
            updateTable();
          }

        }
      });

    task.start();

    while (task.isAlive()) {
      try {
        Thread.sleep(200);
      }
      catch (InterruptedException e) {
      }
    }

    if (taskException == null) {
      return true;
    }
    else {
      setErrorMessage(taskException.getMessage());
      getAcceptButton().setEnabled(false);
      return false;
    }
  }

  /**
   * Adiciona uma nova linha na tabela com os dados do novo objeto criado
   */
  protected void updateTable() {
    ObjectTableModel<T> model = panel.getTableModel();
    model.add(newRow);
    model.fireTableDataChanged();
  }

  public T getNewRow() {
    return newRow;

  }

  public void setNewRow(T newRow) {
    this.newRow = newRow;
  }

  /**
   * Chamada ao barramento para cadastrar o novo elemento
   * 
   * @throws Exception
   */
  protected abstract void openBusCall() throws Exception;

}
