package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.Platform;
import co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import com.microsoft.appcenter.appium.EnhancedIOSDriver;
import com.microsoft.appcenter.appium.Factory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class AppCenterEnhancedIOSDriver extends EnhancedCapabilities implements Driver {

    private DesiredCapabilities capabilities;
    private EnhancedIOSDriver<MobileElement> driver;
    private static AppCenterEnhancedIOSDriver thisInstance;

    private static final String PLATFORM_NAME_CAP = "appium.platformName";
    private static final String APPIUM_URL_CAP = "hub";

    private AppCenterEnhancedIOSDriver() {
        super(Platform.MOBILE);
    }

    public static WebDriver getDriver() throws FailedDriverCreationException {
        if (thisInstance == null) {
            try {
                thisInstance = new AppCenterEnhancedIOSDriver();
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
            driver = Factory.createIOSDriver(url, capabilities);
        }
        return driver;
    }

    @Override
    public void loadCapabilities() throws LoadDriverCapabilitiesException {
        capabilities = super.loadCapabilitiesFromPropertiesFile();
        EnvironmentVariables environmentVariables = Injectors.getInjector().getInstance(EnvironmentVariables.class);
        environmentVariables.setProperty(PLATFORM_NAME_CAP, this.capabilities.getPlatform().name());
    }

    @Override
    public void tearDown() {
        if(thisInstance != null){
            if(thisInstance.driver != null) {
                thisInstance.driver.quit();
            }
            thisInstance = null;
        }
    }

}