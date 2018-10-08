package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class OwnAppiumDriver extends Driver {

    private final DesiredCapabilities capabilities;
    private AppiumDriver<MobileElement> driver;

    private static final String PLATFORM_NAME_CAP = "appium.platformName";
    private static final String APPIUM_URL_CAP = "hub";

    private OwnAppiumDriver() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilities(Platform.MOBILE);
        EnvironmentVariables environmentVariables = Injectors.getInjector().getInstance(EnvironmentVariables.class);
        environmentVariables.setProperty(PLATFORM_NAME_CAP, this.capabilities.getPlatform().name());
    }

    @Override
    public AppiumDriver init() throws MalformedURLException {
        if (this.driver == null) {
            URL url = new URL(capabilities.getCapability(APPIUM_URL_CAP).toString());
            this.driver = new AppiumDriver<>(url, capabilities);
        }
        return this.driver;
    }

    public static WebDriver getDriver() throws FailedDriverCreationException {
        Driver customDriver;
        try {
            customDriver = new OwnAppiumDriver();
            return (WebDriver) customDriver.init();
        } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
        }

    }

}
