package co.com.devco.certificacion.customdrivers;

public enum Platform {

    MOBILE("appium"),
    WEB("webdriver"),
    WINDOWS("appium");

    private final String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
