package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class ConsumerEditAction extends OpenBusAction<ConsumerWrapper> {

  public ConsumerEditAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(ConsumerEditAction.class.getSimpleName()
      + ".name"));
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
    ConsumerInputDialog dialog =
      new ConsumerInputDialog(ConsumerEditAction.this.parentWindow,
        getTablePanelComponent(), admin);
    dialog.showDialog();
    ConsumerWrapper entity = getTablePanelComponent().getSelectedElement();
    dialog.setEditionMode(entity);
  }
}
