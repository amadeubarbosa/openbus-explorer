package admin.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tecgraf.javautils.LNG;
import tecgraf.openbus.assistant.Assistant;
import admin.desktop.SimpleWindow;
import admin.desktop.SimpleWindowBlockType;
import admin.desktop.SimpleWindowBlockType.Type;
import admin.desktop.dialog.LoginDialog;
import admin.remote.SimpleWindowRemoteTask;

public class LogoutAction extends AbstractAction {
  Assistant assistant;
  SimpleWindow parentWindow;

  public LogoutAction(Assistant assistant, SimpleWindow parentWindow) {
    this.assistant = assistant;
    this.parentWindow = parentWindow;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    new SimpleWindowRemoteTask(parentWindow, LNG
      .get("LogoutAction.waiting.title"), LNG.get("LogoutAction.waiting.msg"),
      new SimpleWindowBlockType(Type.BLOCK_THIS)) {
      @Override
      public void performTask() throws Exception {
        assistant.shutdown();
      }

      @Override
      public void updateUI() {
        parentWindow.dispose();
        new LoginDialog().show();
      }

    }.start();

  }
}