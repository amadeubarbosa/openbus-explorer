package busexplorer.utils.preferences;

public class BusExplorerPrefs {

  private static ApplicationPreferences preferences;

  public static ApplicationPreferences instance() {
    if (preferences == null) {
      preferences = new ApplicationPreferences(PrefName.APPLICATION_NAME);
    }
    return preferences;
  }

}
