package busexplorer.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtilities {

  public static List<String> splitOmmitEmpty(String str, String delimiter) {
    return Arrays.asList(str.trim().split(delimiter))
      .stream().filter(item -> !item.isEmpty())
      .collect(Collectors.toList());
  }
}
