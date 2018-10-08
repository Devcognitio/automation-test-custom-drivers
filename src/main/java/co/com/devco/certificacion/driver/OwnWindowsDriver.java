package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

import static co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException.FAILED_DRIVER_CREATION;

public class OwnWindowsDriver extends Driver {

    private final DesiredCapabilities capabilities;
    private WindowsDriver<WebElement> driver;

    public OwnWindowsDriver() throws LoadDriverCapabilitiesException {
        this.capabilities = super.loadCapabilities(Platform.WINDOWS);
    }

    @Override
    public WindowsDriver<WebElement> init() {
        if(this.driver == null) {
            this.driver = new WindowsDriver<>(capabilities);
        }
        return this.driver;
    }

    public static WebDriver windows() throws FailedDriverCreationException {
        Driver customDriver;
        try {
            customDriver = new OwnWindowsDriver();
            return (WebDriver) customDriver.init();
        } catch (LoadDriverCapabilitiesException | MalformedURLException e) {
            throw new FailedDriverCreationException(FAILED_DRIVER_CREATION + e.getMessage(), e.getCause());
        }
    }
}
