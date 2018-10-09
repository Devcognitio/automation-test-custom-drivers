package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static co.com.devco.certificacion.driver.Browser.getBrowserByNameOtherwiseChrome;
import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class OwnWebDriver extends Driver {

    private static final String BROWSER_NAME_CAPABILITY = "driver";
    private final DesiredCapabilities capabilities;
    private WebDriver driver;
    private Browser browser;

    private OwnWebDriver() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilities(Platform.WEB);
        String browserName = checkBrowserName(this.capabilities);
        defineBrowser(browserName);
    }

    private String checkBrowserName(DesiredCapabilities capabilities) {
        return capabilities.getCapability(BROWSER_NAME_CAPABILITY).toString();
    }

    private void defineBrowser(String browserName) {
        this.browser = getBrowserByNameOtherwiseChrome(browserName);
    }

    @Override
    public WebDriver init() throws MalformedURLException {
        if (this.driver == null) {
            this.driver = this.browser.getDriver(this.capabilities);
        }
        return this.driver;
    }

    public static WebDriver theBrowser() throws FailedDriverCreationException {
        try {
            Driver customDriver = new OwnWebDriver();
            return (WebDriver) customDriver.init();
        } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
        }
    }
}
