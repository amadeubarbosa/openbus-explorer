package busexplorer.utils.preferences;

import busexplorer.utils.BusAddress;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OpenBusPreferencesTest {

  static final String APPNAME = "appname";

  static ApplicationPreferences preferences;

  @BeforeAll
  public static void before() {
    preferences = BusExplorerPrefs.instance();
  }

  @Test
  public void busAdressWriteTest() {
    BusAddress bus = BusAddress.toAddress("", "127.0.0.1:2089");
    System.out.println(bus.toString());
  }

}
