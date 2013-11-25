package busexplorer.desktop.dialog;

import java.util.List;

import javax.swing.JFrame;

import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.javautils.gui.table.ObjectTableModel;
import admin.BusAdmin;

public abstract class BusAdminAbstractInputDialog<T> extends InputDialog {
  private ObjectTableModel<T> model;
  Exception taskException = null;

  /**
   * Objeto a ser cadastrado no barramento
   */
  private T newRow;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo
   * @param title T�tulo do Di�logo.
   * @param blockType Modo de Bloqueio da janela m�e.
   */
  public BusAdminAbstractInputDialog(JFrame parentWindow, String title,
    ObjectTableModel<T> model, BusAdmin admin) {
    super(parentWindow, title, admin);
    this.model = model;
  }

  /**
   * M�todo que salva o novo objeto no barramento e atualiza a janela m�e do
   * di�logo com a categoria criada/modificada no di�logo.
   * 
   * @return Verdadeiro, se o objeto n�o existir e puder ser aplicado com
   *         sucesso.
   */
  protected boolean apply() {
    Task<?> task = new Task() {
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
    task.execute(this, LNG.get("AddAction.waiting.title"), LNG
      .get("AddAction.waiting.msg"));

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
      return false;
    }
  }

  @Override
  public void showDialog() {
    // Emulando tratamento de modalidade
    getOwner().setEnabled(false);
    super.showDialog();
  }

  @Override
  public void dispose() {
    // Emulando tratamento de modalidade
    getOwner().setEnabled(true);
    super.dispose();
  }

  /**
   * Adiciona uma nova linha na tabela com os dados do novo objeto criado
   */
  protected void updateTable() {
    model.add(newRow);
    model.fireTableDataChanged();
  }

  public List<T> getRows() {
    return model.getRows();
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
