package convertor;

import java.util.Scanner;

public class Main {

  public enum Command {
    EXIT, ADD_CURRENCY, BUY_CURRENCY, SELL_CURRENCY, SHOW_LOG, EDIT_SELL_RATING, EDIT_BYE_RATING, SHOW_RATES
  }

  static final String RESET = "\u001B[0m";
  static final String BLUE = "\u001B[34m";
  static final String AQUA = "\u001B[36m";

  static final String YELLOW = "\u001B[33m";
  static final String RED = "\u001B[31m";

  public static void main(String[] args) {

    int space;
    boolean isCloseMenu = true;
    Scanner scanner = new Scanner(System.in);
    Converter converter = new Converter();
    Command command;
    initialValue(converter);
    int commandInt;
    while (isCloseMenu) {
      mainMenu();

      System.out.println("Выберете пункт меню ^: ");
      if (!scanner.hasNext()) {
        System.out.println("Ошибка ввода команды. Введите целое число.");
        scanner.nextLine();
        continue;
      }

      if (scanner.hasNextInt()) {
        commandInt = scanner.nextInt();
        scanner.nextLine();
      } else {
        System.out.println("Ошибка ввода команды. Введите целое число.");
        scanner.nextLine();
        continue;
      }

      if (commandInt < 0 || commandInt >= Command.values().length) {
        System.out.println("Выберите пункт меню из предложенных, другие пункты в разработке...");
        continue;
      } else {
        command = Command.values()[commandInt];
      }

      switch (command) {
        case ADD_CURRENCY:
          String newCurrencyData;
          String currencyCode;
          String currencyName;
          double quantity;

          System.out.printf("%sВведите код валюты и ее название: ", AQUA);
          newCurrencyData = scanner.nextLine();
          space = newCurrencyData.indexOf(" ");

          if (space == -1) {
            System.out.println("Неверный формат ввода...");
            break;
          }

          currencyName = newCurrencyData.substring(0, space);
          currencyCode = newCurrencyData.substring(space + 1);

          System.out.print("Введите количество: ");
          while (!scanner.hasNextDouble()) {
            System.out.println("Ошибка ввода. Введите действительное число.");
            scanner.nextLine();
          }
          quantity = scanner.nextDouble();
          scanner.nextLine();

          converter.addCurrency(new Currency(currencyName, currencyCode, quantity));
          break;
        case BUY_CURRENCY:
          handleCurrencyExchange("BUY", scanner, converter);
          break;
        case SELL_CURRENCY:
          handleCurrencyExchange("SELL", scanner, converter);
          break;
        case SHOW_LOG:
          LogTransactions.log();
          break;
        case EDIT_SELL_RATING:
          editCurrencyRating("SELL", scanner, converter);
          break;
        case EDIT_BYE_RATING:
          editCurrencyRating("BUY", scanner, converter);
          break;
        case SHOW_RATES:
          converter.getRateList();
          break;
        case EXIT:
          System.out.println(String.format(
              "Завершение работы...%sВсе данные об операциях, переданы в налоговую...%s",
              RED, RESET));
          isCloseMenu = false;
          break;
        default:
      }
    }
  }

  /**
   * Меню для работы с валютами и их парами
   */
  public static void mainMenu() {
    System.out.printf("%s1: %sДобавить валюту%n", RESET, BLUE);
    System.out.printf("%s2: %sКупить валюту%n", RESET, BLUE);
    System.out.printf("%s3: %sПродать валюту%n", RESET, BLUE);
    System.out.printf("%s4: %sВывести лог операций%n", RESET, BLUE);
    System.out.printf("%s5: %sРедактировать рейтинг продажи%n", RESET, BLUE);
    System.out.printf("%s6: %sРедактировать рейтинг покупки%n", RESET, BLUE);
    System.out.printf("%s7: %sВывести обменные курсы%n", RESET, BLUE);
    System.out.printf("%s0: %sЗавершить работу%s%n%n", RESET, BLUE, RESET);
  }

  /**
   * Инициализируем несколько валют для начала работы
   *
   * @param converter
   */
  public static void initialValue(Converter converter) {
    Currency rub = new Currency("Rubble", "rub", 90);
    Currency usd = new Currency("US Dollar", "usd", 1);
    Currency eur = new Currency("Euro", "eur", 0.8);

    converter.addCurrency(rub);
    converter.addCurrency(usd);
    converter.addCurrency(eur);
  }

  private static void handleCurrencyExchange(String action, Scanner scanner, Converter converter) {
    String pair;
    String firstCurrency;
    String secondCurrency;
    double amount;
    int space;
    boolean isSucceed = false;
    String buyAction = "покупки";
    boolean isBuyAction = "BUY".equals(action);
    String actionText = isBuyAction ? buyAction : "продажи";

    while(!isSucceed) {
      System.out.printf("%sВведите пару валют для %s: ", AQUA, actionText);
      pair = scanner.nextLine().trim();
      space = pair.indexOf(" ");

      if (space == -1) {
        System.out.printf("%sНеверный формат валютной пары %n", RED);
        continue;
      }

      firstCurrency = pair.substring(0, space);
      secondCurrency = pair.substring(space + 1);

      if (!converter.validateCurrencyPair(firstCurrency, secondCurrency)) {
        System.out.printf("Одной из пар не существует: %s или %s %n", firstCurrency, secondCurrency);
        continue;
      }

      do {
        System.out.printf(String.format("Введите количество %s для %s: ", firstCurrency, actionText));

        while (!scanner.hasNextDouble()) {
          System.out.printf("%sОшибка ввода суммы. Введите положительное число! %s%n", RED, RESET);
          System.out.printf(String.format("%sВведите количество %s для %s: ", AQUA, firstCurrency, actionText));
          scanner.nextLine();
        }
        amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
          System.out.printf("%sОшибка ввода суммы. Введите положительное число! %s%n", RED, RESET);
        }
      } while (amount <= 0);

      if ("BUY".equals(action)) {
        String firstCurrencyName = converter.getCurrencyName(firstCurrency);
        String secondCurrencyName = converter.getCurrencyName(secondCurrency);
        double byeResult = converter.byePair(amount, firstCurrency, secondCurrency);

        System.out.printf("%sРезультат: %sвы купили %.2f %s и заплатили: %.2f %s%n", YELLOW, RESET,
            amount, firstCurrencyName, byeResult, secondCurrencyName);
        isSucceed = true;
      } else if ("SELL".equals(action)) {
        System.out.printf("%sРезультат: %sвы продали %.2f %s и получили: %.2f %s%n", YELLOW, RESET,
            amount, converter.getCurrencyName(firstCurrency),
            converter.salePair(amount, firstCurrency, secondCurrency),
            converter.getCurrencyName(secondCurrency));
        isSucceed = true;
      }
    }
  }

  private static void editCurrencyRating(String action, Scanner scanner, Converter converter) {
    String currencyForEditing;
    double rating;

    System.out.print(String.format("%sРейтинг какой валюты вы хотите изменить?: %n ", AQUA));
    converter.showAllRatings();

    System.out.print(String.format("%sУкажите название редактируемой валюты: ", AQUA));
    currencyForEditing = scanner.nextLine();
    if (!converter.validateCurrency(currencyForEditing)) {
      System.out.printf("Валюты не существует: %s: %n", currencyForEditing);
      return;
    }

    System.out.print(String.format("%sУкажите новый рейтинг: ", AQUA));

    while (!scanner.hasNextDouble()) {
      System.out.println("Ошибка ввода рейтинга. Введите действительное число.");
      scanner.nextLine();
    }
    rating = scanner.nextDouble();
    scanner.nextLine();

    if ("SELL".equals(action)) {
      converter.setExchangeRate(currencyForEditing, rating);
    } else if ("BUY".equals(action)) {
      converter.setExchangeRate(currencyForEditing, rating);
    }
  }
}
