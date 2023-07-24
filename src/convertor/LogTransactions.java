package convertor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogTransactions {

  private static Map<String, LocalDateTime> log = new HashMap<>();
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void log() {
    if (log.size() == 0) {
      System.out.println("Журнал операций пуст.");
    } else {
      for (Map.Entry<String, LocalDateTime> entry : log.entrySet()) {
        String formattedDateTime = entry.getValue().format(formatter);
        System.out.printf("\u001B[32m%s\u001B[0m -> %s%n", entry.getKey(), formattedDateTime);
      }
    }
  }

  public static void addLog(String currencyPair) {
    log.put(currencyPair, LocalDateTime.now());
  }
}
