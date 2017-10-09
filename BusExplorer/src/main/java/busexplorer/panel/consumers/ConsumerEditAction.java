package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class ConsumerEditAction extends OpenBusAction<ConsumerWrapper> {

  public ConsumerEditAction(JFrame parentWindow) {
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
    ConsumerInputDialog dialog =
      new ConsumerInputDialog(ConsumerEditAction.this.parentWindow,
        getTablePanelComponent());
    dialog.showDialog();
    ConsumerWrapper entity = getTablePanelComponent().getSelectedElement();
    dialog.setEditionMode(entity);
  }
}
