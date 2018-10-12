package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import com.microsoft.appcenter.appium.Factory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class AppCenterEnhancedAndroidDriver extends EnhancedCapabilities implements Driver {

    private DesiredCapabilities capabilities;
    private EnhancedAndroidDriver<MobileElement> driver;
    private static AppCenterEnhancedAndroidDriver thisInstance;

    private static final String PLATFORM_NAME_CAP = "appium.platformName";
    private static final String APPIUM_URL_CAP = "hub";

    private AppCenterEnhancedAndroidDriver() throws LoadDriverCapabilitiesException {
        loadCapabilities();
    }

    public static WebDriver getDriver() throws FailedDriverCreationException {
        if (thisInstance == null) {
            try {
                thisInstance = new AppCenterEnhancedAndroidDriver();
                return thisInstance.createDriver();
            } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
                throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
            }
        }
        return thisInstance.driver;
    }

    public static void takeScreenshot(String screenshotSubject) {
        if (thisInstance.driver != null) {
            thisInstance.driver.label(screenshotSubject);
        }
    }

    @Override
    public AppiumDriver createDriver() throws MalformedURLException, LoadDriverCapabilitiesException {
        if (driver == null) {
            if (capabilities == null) {
                loadCapabilities();
            }
            URL url = new URL(capabilities.getCapability(APPIUM_URL_CAP).toString());
            driver = Factory.createAndroidDriver(url, capabilities);
        }
        return driver;
    }

    @Override
    public void loadCapabilities() throws LoadDriverCapabilitiesException {
        capabilities = super.loadCapabilities(Platform.MOBILE);
        EnvironmentVariables environmentVariables = Injectors.getInjector().getInstance(EnvironmentVariables.class);
        environmentVariables.setProperty(PLATFORM_NAME_CAP, this.capabilities.getPlatform().name());
    }

}
