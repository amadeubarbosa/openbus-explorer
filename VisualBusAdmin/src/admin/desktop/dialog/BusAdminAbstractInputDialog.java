package admin.desktop.dialog;

import java.util.List;

import javax.swing.JFrame;

import reuse.modified.logistic.logic.common.Identifiable;
import reuse.modified.planref.client.util.crud.CRUDPanel;
import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;

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
  public BusAdminAbstractInputDialog(JFrame parentWindow, String title,
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
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        openBusCall();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          updateTable();
        }
      }

      @Override
      protected void handleError(Exception exception) {
        taskException = exception;
      }
    };

    Thread taskThread = new Thread(task);
    task.execute(this,LNG.get("AddAction.waiting.title"),
      LNG.get("AddAction.waiting.msg"));


    while (taskThread.isAlive()) {
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

  public List<T> getRows() {
      return panel.getTableModel().getRows();
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
