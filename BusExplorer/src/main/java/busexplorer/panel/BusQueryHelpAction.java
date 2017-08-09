package busexplorer.panel;

import busexplorer.ApplicationIcons;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;

public class BusQueryHelpAction extends OpenBusAction<String> {

  public BusQueryHelpAction(Window parent) {
    super(parent);
    putValue(SHORT_DESCRIPTION, getString("tooltip"));
    putValue(SMALL_ICON, ApplicationIcons.ICON_HELP_16);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    JOptionPane.showMessageDialog(parentWindow, getString("message"),
      getString("title"), JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public ActionType getActionType() {
    return ActionType.OTHER;
  }
}
