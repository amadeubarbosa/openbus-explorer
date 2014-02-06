package busexplorer;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.BusExplorerExceptionHandler;
import busexplorer.utils.Utils;

/**
 * Classe principal da aplica��o.
 * 
 * @author Tecgraf
 */
public class Application {

  /**
   * O tratador de exce��es padr�o da aplica��o.
   */
  private static BusExplorerExceptionHandler handler =
    new BusExplorerExceptionHandler();

  /**
   * Inicializa a aplica��o, criando o di�logo de login.
   * 
   * @param args
   */
  public static void main(String[] args) {
    LNG.load("busexplorer.resources.language.idiom", new Locale("pt", "BR"));
    LNG.load("reuse.modified.logistic.client.resources.language.idiom",
      new Locale("pt", "BR"));

    final Properties properties = new Properties();
    try {
      InputStream in =
        MainDialog.class
          .getResourceAsStream("/busexplorer/resources/client.properties");
      properties.load(in);
      in.close();
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null, Utils.getString(MainDialog.class,
        "error.properties.file"), Utils.getString(MainDialog.class,
        "error.properties.title"), JOptionPane.ERROR_MESSAGE);
    }

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainDialog mainDialog = new MainDialog(properties);
        mainDialog.show();
      }
    });
  }

  /**
   * Recupera o tratador de exce��es padr�o da aplica��o.
   * 
   * @return o tratador de exce��es.
   */
  public static BusExplorerExceptionHandler exceptionHandler() {
    return handler;
  }

}
