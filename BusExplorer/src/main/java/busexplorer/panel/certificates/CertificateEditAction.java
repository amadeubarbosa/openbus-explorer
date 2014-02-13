package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

/**
 * Classe de a��o para criar uma interface. Esta dispara um di�logo.
 * 
 * 
 * @author Tecgraf
 */
public class CertificateEditAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin biblioteca de administra��o
   */
  public CertificateEditAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(CertificateEditAction.class.getSimpleName() + ".name"));
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
      getPanelComponent(), admin);
    dialog.showDialog();
    CertificateWrapper certificate = getPanelComponent().getSelectedElement();
    dialog.setEditionMode(certificate);
  }

}
