package admin.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.assistant.Assistant;
import admin.desktop.dialog.LoginDialog;

public class LogoutAction extends AbstractAction {
  Assistant assistant;
  JFrame parentWindow;

  public LogoutAction(Assistant assistant, JFrame parentWindow) {
    this.assistant = assistant;
    this.parentWindow = parentWindow;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Task task = new Task() {
      @Override
      protected void performTask() throws Exception {
        assistant.shutdown();
      }

      @Override
      protected void afterTaskUI() {
        if (getError() == null) {
          parentWindow.dispose();
          new LoginDialog().show();
        }
      }
    };

    task.execute(parentWindow, LNG.get("LogoutAction.waiting.title"),
      LNG.get("LogoutAction.waiting.msg"));
  }
}
