package convertor;

public class Currency {
  private String name;

  private String code;
  private double exchangeRate;


  public double getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(double exchangeRates) {
    this.exchangeRate = exchangeRates;
  }

  public Currency(String name, String code, double rate) {
    this.name = name.toLowerCase();
    this.code = code.toLowerCase();
    setExchangeRate(rate);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
