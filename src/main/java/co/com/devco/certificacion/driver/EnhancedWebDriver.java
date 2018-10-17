package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.util.function.Function;

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

    private EnhancedWebDriver(Capabilities capabilities) {
        super(Platform.WEB, capabilities);
    }

    public static WebDriver getDriver(Capabilities capabilities) throws FailedDriverCreationException {
        EnhancedWebDriver enhancedWebDriver = getOrCreateEnhancedWebDriver(EnhancedWebDriver::new, capabilities);
        return enhancedWebDriver.driver;
    }

    public static WebDriver getDriver() throws FailedDriverCreationException {
        EnhancedWebDriver enhancedWebDriver = getOrCreateEnhancedWebDriver(c -> new EnhancedWebDriver(), null);
        return enhancedWebDriver.driver;
    }

    private static EnhancedWebDriver getOrCreateEnhancedWebDriver(Function<Capabilities, EnhancedWebDriver> functionToCreateEnhancedWebDriver, Capabilities capabilities) throws FailedDriverCreationException{
        if (thisInstance == null) {
            try {
                thisInstance = functionToCreateEnhancedWebDriver.apply(capabilities);
                thisInstance.createDriver();
                return thisInstance;
            } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
                throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
            }
        }
        return thisInstance;
    }

    public static void quit(){
        if(thisInstance != null){
            thisInstance.tearDown();
        }
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
        this.capabilities = super.loadCapabilitiesFromPropertiesFile();
    }

    private String checkBrowserName(DesiredCapabilities capabilities) {
        return capabilities.getCapability(BROWSER_NAME_CAPABILITY).toString();
    }

    @Override
    public void tearDown() {
        Driver.tearDown(thisInstance.driver);
        thisInstance = null;
    }

}
