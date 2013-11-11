package admin;

import java.util.Locale;

import tecgraf.javautils.LNG;
import admin.desktop.dialog.LoginDialog;

/**
 * Classe principal da aplica��o.
 * 
 * @author Tecgraf
 */
public class VisualBusAdmin {

  /**
   * Inicializa a aplica��o, criando o di�logo de login.
   * 
   * @param args
   */
  public static void main(String[] args) {
    LNG.load("admin.resources.language.idiom", new Locale("pt", "BR"));
    LNG.load("reuse.modified.logistic.client.resources.language.idiom",
      new Locale("pt", "BR"));
    LNG.load("reuse.modified.planref.client.resources.language.idiom",
      new Locale("pt", "BR"));

    LoginDialog loginDialog = new LoginDialog();
    loginDialog.show();
  }

}
