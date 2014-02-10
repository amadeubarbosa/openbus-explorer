package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

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
    Task<List<CertificateWrapper>> task = new Task<List<CertificateWrapper>>() {

      @Override
      protected void performTask() throws Exception {
        setResult(CertificateWrapper.convertToInfo(
          admin.getEntitiesWithCertificate()));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().setElements(getResult());
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }

}
