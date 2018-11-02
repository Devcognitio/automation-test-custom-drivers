package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public interface Driver<T> {

    T createDriver() throws MalformedURLException, LoadDriverCapabilitiesException;
    void loadCapabilities() throws LoadDriverCapabilitiesException;
    void tearDown();

    static void tearDown(WebDriver driver) {
        if(driver != null) {
            driver.quit();
        }
    }
}
