package busexplorer;

import busexplorer.desktop.dialog.LoginDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.BusExplorerExceptionHandler;
import busexplorer.utils.Utils;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;
import tecgraf.openbus.admin.BusAdminImpl;

import javax.swing.JOptionPane;
import java.awt.EventQueue;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Classe principal da aplicação.
 * 
 * @author Tecgraf
 */
public class Application {

  /**
   * O tratador de exceções padrão da aplicação.
   */
  private static BusExplorerExceptionHandler handler =
    new BusExplorerExceptionHandler();

  /**
   * Informações de login da aplicação.
   */
  private static BusExplorerLogin login;

  private static PropertyChangeSupport loginPcs;

  private static BusAdmin admin = new BusAdminImpl();

  /**
   * Inicializa a aplicação, criando o diálogo de login.
   * 
   * @param args
   */
  public static void main(String[] args) {
    LNG.load("busadminlib.resources.language.idiom", new Locale("pt", "BR"));
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
        MainDialog mainDialog = new MainDialog(properties, admin);
        mainDialog.show();

        loginPcs = new PropertyChangeSupport(this);
        loginPcs.addPropertyChangeListener(mainDialog);
        loginProcess(mainDialog);
      }
    });
  }

  public static void loginProcess(MainDialog mainDialog) {
    login = null;
    LoginDialog loginDialog = new LoginDialog(mainDialog, admin);
    loginDialog.show();
    login = loginDialog.getLogin();
    loginPcs.firePropertyChange("Application.login", null, login);
  }

  /**
   * Recupera o tratador de exceções padrão da aplicação.
   * 
   * @return o tratador de exceções.
   */
  public static BusExplorerExceptionHandler exceptionHandler() {
    return handler;
  }

  /**
   * Recupera as informações de login da aplicação.
   *
   * @return as informações de login da aplicação.
   */
  public static BusExplorerLogin login() {
    return login;
  }

}
