package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.Platform;
import co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import co.com.devco.certificacion.customdrivers.exceptions.WindowsDriverException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;
import static co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException.NULL_DRIVER;
import static co.com.devco.certificacion.customdrivers.exceptions.WindowsDriverException.*;

public class EnhancedWindowsDriver extends EnhancedCapabilities implements Driver, Cloneable {

    private static final String PLATFORM_NAME_CAP = "appium.platformName";
    private static final String APPIUM_URL_CAP = "hub";
    private static final String APP_CAP = "cap";

    public static final String MAIN_WINDOW = "MAIN_WINDOW";
    private static EnhancedWindowsDriver thisInstance;
    private DesiredCapabilities capabilities;
    private AppiumDriver<MobileElement> driver;
    private static Map<String, EnhancedWindowsDriver> windows;

    private EnhancedWindowsDriver(){
        super(Platform.WINDOWS);
        windows = new HashMap<>();
    }

    private EnhancedWindowsDriver(Capabilities capabilities){
        super(Platform.WINDOWS, capabilities);
        windows = new HashMap<>();
    }

    public static WebDriver getWindowByKey(String key) throws FailedDriverCreationException, WindowsDriverException {
        if(windows.isEmpty()){
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION, NULL_DRIVER);
        }

        if(windows.containsKey(key)){
            throw new WindowsDriverException(GET_WINDOW_BY_KEY_ERROR + key, NO_ASSOCIATED_DRIVER_FOR_KEY);
        }
        return windows.get(key).driver;
    }

    public static WebDriver changeToNewWindow(String idApp, String key) throws WindowsDriverException {
        try {
            return changeToNewWindow(idApp, key, null);
        } catch ( CloneNotSupportedException | MalformedURLException | WindowsDriverException | FailedDriverCreationException | LoadDriverCapabilitiesException e) {
            throw new WindowsDriverException(CHANGE_TO_NEW_WINDOW_ERROR, e.getCause());
        }
    }

    public static WebDriver changeToNewWindow(String idApp, String key, Capabilities capabilities) throws CloneNotSupportedException, MalformedURLException, FailedDriverCreationException, WindowsDriverException, LoadDriverCapabilitiesException {
        if(thisInstance == null) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION, NULL_DRIVER);
        }

        if(windows.containsKey(key)){
          return getWindowByKey(key);
        }

        thisInstance.loadCapabilities();
        if(capabilities != null){
            thisInstance.capabilities.merge(capabilities);
        }
        thisInstance.capabilities.setCapability(APP_CAP, idApp);
        thisInstance.setNullDriver();
        thisInstance.createDriver();
        windows.put(key, (EnhancedWindowsDriver) thisInstance.clone());
        return thisInstance.driver;
    }

    public void setNullDriver(){
        this.driver = null;
    }

    public static void quit(){
        if(thisInstance != null){
            thisInstance.tearDown();
        }
    }

    public static WebDriver getWindowsDriver(Capabilities capabilities) throws FailedDriverCreationException, CloneNotSupportedException {
        EnhancedWindowsDriver enhancedWindowsDriver = getOrCreateEnhancedWindowsDriver(EnhancedWindowsDriver::new, capabilities);
        return enhancedWindowsDriver.driver;
    }

    public static WebDriver getWindowsDriver() throws FailedDriverCreationException, CloneNotSupportedException {
        EnhancedWindowsDriver enhancedWindowsDriver = getOrCreateEnhancedWindowsDriver(c -> new EnhancedWindowsDriver(), null);
        return enhancedWindowsDriver.driver;
    }

    private static EnhancedWindowsDriver getOrCreateEnhancedWindowsDriver(Function<Capabilities, EnhancedWindowsDriver> functionToCreateEnhancedWindowsDriver,
                                                                          Capabilities capabilities) throws FailedDriverCreationException, CloneNotSupportedException {
        if (thisInstance == null) {
            try {
                thisInstance = functionToCreateEnhancedWindowsDriver.apply(capabilities);
                thisInstance.createDriver();
                windows.put(MAIN_WINDOW, (EnhancedWindowsDriver) thisInstance.clone());
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
        Driver.tearDown(thisInstance.driver);
        windows.forEach(
                (k, d) -> {
                    Driver.tearDown(d.driver);
                    d = null;
                }
        );
        thisInstance = null;
    }

    @Override
    public EnhancedWindowsDriver clone() throws CloneNotSupportedException {
        return (EnhancedWindowsDriver) super.clone();
    }
}
