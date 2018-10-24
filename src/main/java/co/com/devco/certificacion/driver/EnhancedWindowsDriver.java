package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class EnhancedWindowsDriver extends EnhancedCapabilities implements Driver{

    private static final String PLATFORM_NAME_CAP = "appium.platformName";

    private static EnhancedWindowsDriver thisInstance;
    private DesiredCapabilities capabilities;
    private AppiumDriver<MobileElement> driver;

    private EnhancedWindowsDriver(){
        super(Platform.WINDOWS);
    }

    private EnhancedWindowsDriver(Capabilities capabilities){
        super(Platform.WINDOWS, capabilities);
    }

    public static void quit(){
        if(thisInstance != null){
            thisInstance.tearDown();
        }
    }

    public static WebDriver getWindowsDriver(Capabilities capabilities) throws FailedDriverCreationException {
        EnhancedWindowsDriver enhancedWindowsDriver = getOrCreateEnhancedWindowsDriver(EnhancedWindowsDriver::new, capabilities);
        return enhancedWindowsDriver.driver;
    }

    public static WebDriver getWindowsDriver() throws FailedDriverCreationException {
        EnhancedWindowsDriver enhancedWindowsDriver = getOrCreateEnhancedWindowsDriver(c -> new EnhancedWindowsDriver(), null);
        return enhancedWindowsDriver.driver;
    }


    private static EnhancedWindowsDriver getOrCreateEnhancedWindowsDriver(Function<Capabilities, EnhancedWindowsDriver> functionToCreateEnhancedWindowsDriver,
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
        if(this.driver == null) {
            if (capabilities == null) {
                loadCapabilities();
            }
            URL url = new URL(capabilities.getCapability("hub").toString());
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
        Driver.tearDown(thisInstance.driver);
        thisInstance = null;
    }
}
