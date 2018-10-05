package co.com.devco.certificacion.driver;

public enum Platform {

    MOBILE("appium"),
    WEB("webdriver"),
    WINDOWS("appium");

    private final String driver;

    Platform(String driver) {
        this.driver = driver;
    }

    public String driver() {
        return driver;
    }
}
