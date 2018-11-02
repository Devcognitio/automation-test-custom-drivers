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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class EnhancedWindowsDriver extends EnhancedCapabilities implements Driver, Cloneable {

    private static final String PLATFORM_NAME_CAP = "appium.platformName";

    private static EnhancedWindowsDriver thisInstance;
    private DesiredCapabilities capabilities;
    private AppiumDriver<MobileElement> driver;
    private static Map<String, EnhancedWindowsDriver> windows;

    public static final String MAIN_WINDOW = "MAIN_WINDOW";

    private EnhancedWindowsDriver(){
        super(Platform.WINDOWS);
        windows = new HashMap<>();
    }

    private EnhancedWindowsDriver(Capabilities capabilities){
        super(Platform.WINDOWS, capabilities);
        windows = new HashMap<>();
    }

    public static WebDriver getWindowByKey(String key){
        if(windows.isEmpty()){
            // TODO lanzar excepcion no se ha creado ningun driver
        }

        if(windows.containsKey(key)){
            // TODO lanzar excepcion no existe ningun driver con ese key
        }
        return windows.get(key).driver;
    }

    public static WebDriver changeToNewWindow(String idApp, String key) throws  CloneNotSupportedException, MalformedURLException, LoadDriverCapabilitiesException {
        return changeToNewWindow(idApp, key, null);
    }

    public static WebDriver changeToNewWindow(String idApp, String key, Capabilities capabilities) throws  CloneNotSupportedException, MalformedURLException, LoadDriverCapabilitiesException {
        if(thisInstance == null) {
            // TODO lanzar excepcion no se ha creado ningun driver
        }

        if(windows.containsKey(key)){
          return getWindowByKey(key);
        }

        thisInstance.loadCapabilities();
        if(capabilities != null){
            thisInstance.capabilities.merge(capabilities);
        }
        thisInstance.capabilities.setCapability("app", idApp);
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
