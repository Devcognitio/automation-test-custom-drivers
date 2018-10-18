package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import io.appium.java_client.AppiumDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

public class EnhancedWindowsDriverTest {

    @Test
    public void testSuccessfulBecauseCreateWindowsDriver() throws IOException, FailedDriverCreationException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("driver", "apium");

        WebDriver driver = EnhancedWindowsDriver.getWindowsDriver(desiredCapabilities);

        Assert.assertTrue(driver instanceof AppiumDriver);
    }

    @After
    public void tearDown(){
        EnhancedWindowsDriver.quit();
    }

}
