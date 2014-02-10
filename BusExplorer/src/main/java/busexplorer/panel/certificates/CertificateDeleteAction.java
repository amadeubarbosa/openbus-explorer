package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * Classe de ação para a remoção de uma categoria.
 * 
 * @author Tecgraf
 */
public class CertificateDeleteAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin
   */
  public CertificateDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(CertificateDeleteAction.class.getSimpleName() + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    Task<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
      ExceptionContext.BusCore) {
      @Override
      protected void performTask() throws Exception {
        CertificateWrapper certificate = getPanelComponent().getSelectedElement();
        String entityId = certificate.getEntity();
        admin.removeCertificate(entityId);
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"), getString("waiting.msg"));
  }
}
