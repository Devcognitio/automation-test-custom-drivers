package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.Platform;
import co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

import static co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class EnhancedAppiumDriver extends EnhancedCapabilities implements Driver {

    private DesiredCapabilities capabilities;
    private AppiumDriver<MobileElement> driver;
    private static EnhancedAppiumDriver thisInstance;

    private static final String PLATFORM_NAME_CAP = "appium.platformName";
    private static final String APPIUM_URL_CAP = "hub";

    private EnhancedAppiumDriver(){
        super(Platform.MOBILE);
    }

    private EnhancedAppiumDriver(Capabilities capabilities){
        super(Platform.MOBILE, capabilities);
    }

    public static AppiumDriver getAppiumDriver(Capabilities capabilities) throws FailedDriverCreationException {
        EnhancedAppiumDriver enhancedAppiumDriver = getOrCreateEnhancedAppiumDriver(EnhancedAppiumDriver::new, capabilities);
        return enhancedAppiumDriver.driver;
    }

    public static AppiumDriver getAppiumDriver() throws FailedDriverCreationException {
        EnhancedAppiumDriver enhancedAppiumDriver = getOrCreateEnhancedAppiumDriver(c -> new EnhancedAppiumDriver(), null);
        return enhancedAppiumDriver.driver;
    }

    private static EnhancedAppiumDriver getOrCreateEnhancedAppiumDriver(Function<Capabilities, EnhancedAppiumDriver> functionToCreateEnhancedWindowsDriver,
                                                                Capabilities capabilities) throws FailedDriverCreationException {
        if (thisInstance == null) {
            try {
                thisInstance = functionToCreateEnhancedWindowsDriver.apply(capabilities);
                thisInstance.createDriver();
                return thisInstance;
            } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
                throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
            }
        }
        return thisInstance;
    }

    @Override
    public AppiumDriver createDriver() throws MalformedURLException, LoadDriverCapabilitiesException {
        if (this.driver == null) {
            if (capabilities == null) {
                loadCapabilities();
            }
            URL url = new URL(capabilities.getCapability(APPIUM_URL_CAP).toString());
            this.driver = new AppiumDriver<>(url, capabilities);
        }
        return this.driver;
    }

    @Override
    public void loadCapabilities() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilitiesFromPropertiesFile();
        EnvironmentVariables environmentVariables = Injectors.getInjector().getInstance(EnvironmentVariables.class);
        environmentVariables.setProperty(PLATFORM_NAME_CAP, this.capabilities.getPlatform().name());
    }

    @Override
    public void tearDown() {
        if(thisInstance != null){
            Driver.tearDown(thisInstance.driver);
            thisInstance = null;
        }
    }

}
