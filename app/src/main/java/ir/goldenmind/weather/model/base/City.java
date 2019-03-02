package ir.goldenmind.weather.model.base;

public class City {

    public City(String countryCode, String countryName, String cityName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.cityName = cityName;
    }

    private String countryCode;
    private String countryName;
    private String cityName;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
