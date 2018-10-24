package co.com.devco.certificacion.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.stream;

public enum Browser {

    CHROME( "chrome", c -> {
            ChromeOptions options = new ChromeOptions();
            options.merge(c);
            return new ChromeDriver(options);
        }),
    INTERNET_EXPLORER( "ie", c -> {
        InternetExplorerOptions options = new InternetExplorerOptions(c);
        return new InternetExplorerDriver(options);
    }),
    FIREFOX( "firefox", c -> {
        FirefoxOptions options = new FirefoxOptions(c);
        return new FirefoxDriver(options);
    }),
    EDGE( "edge", c -> {
        EdgeOptions options = new EdgeOptions();
        options.merge(c);
        return  new EdgeDriver(options);
    });

    private String name;
    private Function<Capabilities, WebDriver> functionToCreateDriver;

    Browser(String name, Function<Capabilities, WebDriver> functionToCreateDriver) {
        this.name = name;
        this.functionToCreateDriver = functionToCreateDriver;
    }

    public static Browser getBrowserByNameOtherwiseChrome(String name){
        Optional<Browser> optionalBrowser = stream(values()).filter(
                n -> n.getName().equalsIgnoreCase(name)).findFirst();

        return optionalBrowser.orElse(CHROME);
    }

    public String getName(){
        return this.name;
    }

    public WebDriver getDriver(DesiredCapabilities capabilities) {
        return functionToCreateDriver.apply(capabilities);
    }

}
