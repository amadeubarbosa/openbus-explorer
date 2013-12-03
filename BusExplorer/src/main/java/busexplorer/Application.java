package busexplorer;

import java.awt.EventQueue;
import java.util.Locale;

import tecgraf.javautils.LNG;
import busexplorer.desktop.dialog.MainDialog;

/**
 * Classe principal da aplicação.
 * 
 * @author Tecgraf
 */
public class Application {

  /**
   * Inicializa a aplicação, criando o diálogo de login.
   * 
   * @param args
   */
  public static void main(String[] args) {
    LNG.load("busexplorer.resources.language.idiom", new Locale("pt", "BR"));
    LNG.load("reuse.modified.logistic.client.resources.language.idiom",
      new Locale("pt", "BR"));
    LNG.load("reuse.modified.planref.client.resources.language.idiom",
      new Locale("pt", "BR"));

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainDialog mainDialog = new MainDialog();
        mainDialog.show();
      }
    });
  }

}
