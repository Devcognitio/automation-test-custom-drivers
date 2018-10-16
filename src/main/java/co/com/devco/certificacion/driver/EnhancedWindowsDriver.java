package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class EnhancedWindowsDriver extends EnhancedCapabilities implements Driver{

    private static EnhancedWindowsDriver thisInstance;
    private DesiredCapabilities capabilities;
    private WindowsDriver<WebElement> driver;

    public EnhancedWindowsDriver(){
        super(Platform.WINDOWS);
    }

    public static WebDriver windows() throws FailedDriverCreationException {
        if (thisInstance == null) {
            try {
                thisInstance = new EnhancedWindowsDriver();
                return thisInstance.createDriver();
            } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
                throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
            }
        }
        return thisInstance.driver;
    }

    @Override
    public WebDriver createDriver() throws MalformedURLException, LoadDriverCapabilitiesException {
        if(this.driver == null) {
            if (capabilities == null) {
                loadCapabilities();
            }
            this.driver = new WindowsDriver<>(capabilities);
        }
        return this.driver;
    }

    @Override
    public void loadCapabilities() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilitiesFromPropertyFile();
    }
}
