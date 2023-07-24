package convertor;

import java.util.HashMap;
import java.util.Map;

public class Converter {
  private final double SALE_RATE = 0.7;
  private Map<String, Currency> currencies;

  public void showAllRatings() {
    for (Map.Entry<String, Currency> c : currencies.entrySet()) {
      System.out.println(c.getKey() + " / " + c.getValue().getName());
    }
  }

  public Converter() {
    currencies = new HashMap<>();
  }

  public String getCurrencyName(String currencyCode) {
    return currencies.get(currencyCode).getName();
  }

  public void addCurrency(Currency currency) {
    if (!currencies.containsKey(currency.getCode())) {
      currencies.put(currency.getCode(), currency);
    }
  }

  public void setExchangeRate(String currency, double rate) {
    currencies.get(currency).setExchangeRate(rate);
  }

  public void getRateList() {
    for (Map.Entry<String, Currency> currency : currencies.entrySet()) {
      System.out.println(
          currency.getValue().getName().toUpperCase() + "/" + currency.getValue().getCode() + " = "
              + currency.getValue().getExchangeRate() + "||" + SALE_RATE + currency.getValue()
              .getExchangeRate());
    }
  }

  public boolean validateCurrencyPair(String firstCurrency, String secondCurrency) {
    return validateCurrency(firstCurrency) && validateCurrency(secondCurrency);
  }

  public boolean validateCurrency(String firstCurrency) {
    Currency first = currencies.get(firstCurrency);
    return first != null;
  }

  public double byePair(double amount, String firstCurrency, String secondCurrency) {
    Currency byeCurrency = currencies.get(firstCurrency);
    Currency payCurrency = currencies.get(secondCurrency);

    double fromRate = byeCurrency.getExchangeRate();
    double toRate = payCurrency.getExchangeRate();
    double convertedAmount = amount * (toRate / fromRate);

    logBuyTransaction(byeCurrency, payCurrency, amount, convertedAmount);

    return convertedAmount;
  }

  public double salePair(double amount, String firstCurrency, String secondCurrency) {
    Currency payCurrency = currencies.get(firstCurrency);
    Currency saleCurrency = currencies.get(secondCurrency);

    double fromRate = payCurrency.getExchangeRate();
    double toRate = saleCurrency.getExchangeRate();
    double convertedAmount = amount * (SALE_RATE + (toRate / fromRate));

    logSellTransaction(saleCurrency, payCurrency, amount, convertedAmount);

    return convertedAmount;
  }

  private void logBuyTransaction(Currency byeCurrencies, Currency saleCurrencies, double amount,
      double convertedAmount) {
    String logMessage = String.format("Покупка: куплено %s(%s) <%.2f> / заплачено %s(%s) <%.2f>",
        byeCurrencies.getName(), byeCurrencies.getCode(), amount,
        saleCurrencies.getName(), saleCurrencies.getCode(), convertedAmount);
    LogTransactions.addLog(logMessage);
  }

  private void logSellTransaction(Currency byeCurrencies, Currency saleCurrencies, double amount,
      double convertedAmount) {
    String logMessage = String.format("Продажа: продано %s(%s) <%.2f> / получено %s(%s) <%.2f>",
        saleCurrencies.getName(), saleCurrencies.getCode(), amount,
        byeCurrencies.getName(), byeCurrencies.getCode(), convertedAmount);
    LogTransactions.addLog(logMessage);
  }
}
