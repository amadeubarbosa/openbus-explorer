package busexplorer;

import busexplorer.desktop.dialog.LoginDialog;
import busexplorer.desktop.dialog.MainDialog;
import busexplorer.exception.BusExplorerExceptionHandler;
import busexplorer.utils.Language;
import tecgraf.javautils.core.lng.LNG;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import static busexplorer.desktop.dialog.MainDialog.COMPATIBILITY_FOREGROUND;
import static busexplorer.desktop.dialog.MainDialog.TABBED_PANE_DISABLED_TEXT;

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

  private static PropertyChangeSupport propertyChangeSupport;

  public static final String APPLICATION_LOGIN = "Application.login";

  static {
    LNG.load("busadminlib.resources.language.idiom", new Locale("pt", "BR"));
    LNG.load("busexplorer.resources.language.idiom", new Locale("pt", "BR"));
    System.setProperty("apple.laf.useScreenMenuBar", "true");

    LookAndFeel javaRuntimeDefault = UIManager.getLookAndFeel();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e1) {
        try {
          // restore java runtime default
          UIManager.setLookAndFeel(javaRuntimeDefault);
        } catch (UnsupportedLookAndFeelException e2) {
          e2.printStackTrace();
        }
      }
    }
    // hack to override foreground color on disabled tabs in some platforms like MacOSX
    // pick the most compliant property over known platforms = Label.disabledForeground
    //   http://nadeausoftware.com/articles/2008/11/all_ui_defaults_names_common_java_look_and_feels_windows_mac_os_x_and_linux
    Color disabledTextColor =
      UIManager.getColor(TABBED_PANE_DISABLED_TEXT) != null
        ? UIManager.getColor(TABBED_PANE_DISABLED_TEXT)
        : UIManager.getColor(COMPATIBILITY_FOREGROUND);
    // ugly but some component is testing color references instead of equals method!
    UIManager.put(TABBED_PANE_DISABLED_TEXT, new Color(disabledTextColor.getRGB()));
  }

  /**
   * Inicializa a aplicação, criando o diálogo de login.
   *
   * @param args parâmetro da linha de comando serão <strong>descartados</strong>
   */
  public static void main(String[] args) {
    final Properties properties = new Properties();
    try {
      InputStream in =
        MainDialog.class
          .getResourceAsStream("/busexplorer/resources/client.properties");
      properties.load(in);
      in.close();
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null, Language.get(MainDialog.class,
        "error.properties.file"), Language.get(MainDialog.class,
        "error.properties.title"), JOptionPane.ERROR_MESSAGE);
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainDialog mainDialog = new MainDialog(properties);
        mainDialog.setVisible(true);

        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(mainDialog);
        showLoginDialog(mainDialog);
      }
    });
  }

  public static void showLoginDialog(MainDialog mainDialog) {
    BusExplorerLogin oldLogin = login;
    login = null;
    LoginDialog loginDialog = new LoginDialog(mainDialog);
    loginDialog.setVisible(true);
    login = loginDialog.getLogin();
    propertyChangeSupport.firePropertyChange(APPLICATION_LOGIN, oldLogin, login);
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
