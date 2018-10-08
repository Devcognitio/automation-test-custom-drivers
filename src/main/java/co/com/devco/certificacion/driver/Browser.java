package co.com.devco.certificacion.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public enum Browser {
    CHROME( "chrome", c -> {
            ChromeOptions options = new ChromeOptions();
            options.merge(c);
            return new ChromeDriver(options);
        }),
    INTERNET_EXPLORER( "explorer", c -> {
        InternetExplorerOptions options = new InternetExplorerOptions(c);
        return new InternetExplorerDriver(options);
    }),
    FIREFOX( "firefox", c -> {
        FirefoxOptions options = new FirefoxOptions(c);
        return new FirefoxDriver(options);
    });

    private String name;
    private Function<Capabilities, WebDriver> function;

    Browser(String name, Function<Capabilities, WebDriver> function) {
        this.name = name;
        this.function = function;
    }

    public WebDriver getDriver(DesiredCapabilities capabilities) {
        return function.apply(capabilities);
    }

    public String getName(){
        return this.name;
    }

    public static Browser getBrowserByName(String name){
        Browser Browser = CHROME;
        List<Browser> browsers = Arrays.stream(values()).filter(
                n -> n.getName().equalsIgnoreCase(name)
        ).collect(toList());
        if(!browsers.isEmpty()){
            Browser = browsers.get(0);
        }
        return Browser;
    }
}
