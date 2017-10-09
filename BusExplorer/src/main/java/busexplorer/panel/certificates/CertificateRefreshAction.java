package busexplorer.panel.certificates;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ação que atualiza a tabela de categorias
 * 
 * @author Tecgraf
 * 
 */
public class CertificateRefreshAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor.
   *  @param parentWindow janela pai.
   *
   */
  public CertificateRefreshAction(JFrame parentWindow) {
    super(parentWindow);
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
      new BusExplorerTask<List<CertificateWrapper>>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        setResult(CertificateWrapper.convertToInfo(
          Application.login().admin.getEntitiesWithCertificate()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getTablePanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }

}
