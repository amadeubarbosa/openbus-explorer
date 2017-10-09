package busexplorer.panel.certificates;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CertificateEditAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor da ação.
   *  @param parentWindow janela mãe do diálogo que a ser criado pela ação
   *
   */
  public CertificateEditAction(JFrame parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.EDIT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    CertificateInputDialog dialog = new CertificateInputDialog(parentWindow,
      getTablePanelComponent());
    dialog.showDialog();
    CertificateWrapper certificate = getTablePanelComponent().getSelectedElement();
    dialog.setEditionMode(certificate);
  }

}
