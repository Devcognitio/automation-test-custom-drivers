package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class OwnWebDriver extends Driver {

    private static final String BROWSER_NAME_CAPABILITY = "driver";
    private final DesiredCapabilities capabilities;
    private WebDriver driver;
    private Browser browser;

    private OwnWebDriver() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilities(Platform.WEB);
        String browserName = capabilities.getCapability(BROWSER_NAME_CAPABILITY).toString();
        this.browser = Browser.getBrowserByName(browserName);
    }

    private OwnWebDriver(Browser browser) throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilities(Platform.WEB);
        this.browser = browser;
    }

    @Override
    public WebDriver init() throws MalformedURLException {
        if (this.driver == null) {
            this.driver = browser.getDriver(this.capabilities);
        }
        return this.driver;
    }

    public static WebDriver theBrowser(Browser browser) throws FailedDriverCreationException {
        Driver customDriver;
        try {
            customDriver = new OwnWebDriver(browser);
            return (WebDriver) customDriver.init();
        } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
        }
    }

    public static WebDriver theBrowser() throws FailedDriverCreationException {
        Driver customDriver;
        try {
            customDriver = new OwnWebDriver();
            return (WebDriver) customDriver.init();
        } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
        }
    }

}
