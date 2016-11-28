package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.core.lng.LNG;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import exception.handling.ExceptionContext;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CertificateRefreshAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai.
   * @param admin biblioteca de administração.
   */
  public CertificateRefreshAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(CertificateRefreshAction.class.getSimpleName() + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REFRESH;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    BusExplorerTask<List<CertificateWrapper>> task =
      new BusExplorerTask<List<CertificateWrapper>>(
        Application.exceptionHandler(), ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        setResult(CertificateWrapper.convertToInfo(
          admin.getEntitiesWithCertificate()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
