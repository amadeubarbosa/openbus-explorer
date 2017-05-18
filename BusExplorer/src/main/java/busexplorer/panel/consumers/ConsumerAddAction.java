package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;


public class ConsumerAddAction extends OpenBusAction<ConsumerWrapper> {

  public ConsumerAddAction(JFrame parentWindow, BusAdminFacade admin) {
    super(parentWindow, admin);
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
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    new ConsumerInputDialog(parentWindow, getTablePanelComponent(),
      admin).showDialog();
  }

}
