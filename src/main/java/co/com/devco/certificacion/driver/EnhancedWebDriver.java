package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static co.com.devco.certificacion.driver.Browser.getBrowserByNameOtherwiseChrome;
import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class EnhancedWebDriver extends EnhancedCapabilities implements Driver {

    private static final String BROWSER_NAME_CAPABILITY = "driver";
    private DesiredCapabilities capabilities;
    private WebDriver driver;
    private static EnhancedWebDriver thisInstance;

    private EnhancedWebDriver() {
        super(Platform.WEB);
    }

    public static WebDriver getDriver() throws FailedDriverCreationException {
        if (thisInstance == null) {
            try {
                thisInstance = new EnhancedWebDriver();
                return thisInstance.createDriver();
            } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
                throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
            }
        }
        return thisInstance.driver;
    }

    @Override
    public WebDriver createDriver() throws MalformedURLException, LoadDriverCapabilitiesException {
        if (this.driver == null) {
            if (capabilities == null) {
                loadCapabilities();
            }
            String browserName = checkBrowserName(this.capabilities);
            Browser browser = getBrowserByNameOtherwiseChrome(browserName);
            this.driver = browser.getDriver(this.capabilities);
        }
        return this.driver;
    }

    @Override
    public void loadCapabilities() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilitiesFromPropertyFile();
    }

    private String checkBrowserName(DesiredCapabilities capabilities) {
        return capabilities.getCapability(BROWSER_NAME_CAPABILITY).toString();
    }

}
