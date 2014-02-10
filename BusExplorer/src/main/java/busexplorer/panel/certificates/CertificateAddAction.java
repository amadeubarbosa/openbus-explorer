package busexplorer.panel.certificates;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import admin.BusAdmin;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

/**
 * Classe de ação para criar uma interface. Esta dispara um diálogo.
 * 
 * 
 * @author Tecgraf
 */
public class CertificateAddAction extends OpenBusAction<CertificateWrapper> {

  /**
   * Construtor da ação.
   * 
   * @param parentWindow janela mãe do diálogo que a ser criado pela ação
   * @param admin biblioteca de administração
   */
  public CertificateAddAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin,
      LNG.get(CertificateAddAction.class.getSimpleName() + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.ADD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    new CertificateInputDialog(parentWindow, getPanelComponent(),
      admin).showDialog();
  }

}
