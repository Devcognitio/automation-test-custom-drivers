package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException;
import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import co.com.devco.certificacion.customdrivers.exceptions.WindowsDriverException;
import io.appium.java_client.AppiumDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

import static co.com.devco.certificacion.customdrivers.driver.EnhancedWindowsDriver.MAIN_WINDOW;

public class EnhancedWindowsDriverTest {

    @Test
    public void testSuccessfulBecauseCreateWindowsDriver() throws IOException, FailedDriverCreationException, CloneNotSupportedException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");

        WebDriver driver = EnhancedWindowsDriver.getWindowsDriver(desiredCapabilities);

        Assert.assertTrue(driver instanceof AppiumDriver);
    }

    @Test
    public void testSuccessfulBecausechangeToWindowById() throws IOException, FailedDriverCreationException, CloneNotSupportedException, LoadDriverCapabilitiesException, WindowsDriverException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
        WebDriver driver = EnhancedWindowsDriver.getWindowsDriver(desiredCapabilities);
        driver.manage().window().setPosition(new Point(1000, 10));
        driver.findElement(By.name("One")).click();

        driver = EnhancedWindowsDriver.changeToNewWindow("C:\\Windows\\System32\\notepad.exe", "NOTE");
        driver.findElement(By.className("Edit")).sendKeys("Hola Mundo");

        driver = EnhancedWindowsDriver.getWindowByKey(MAIN_WINDOW);
        WebElement boton7 = driver.findElement(By.name("Seven"));
        boton7.click();
        String result = driver.findElement(By.xpath("//*[@AutomationId='CalculatorResults']")).getText().replace("Display is", "").trim();

        driver = EnhancedWindowsDriver.changeToNewWindow("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App", "CAL2");
        driver.manage().window().setPosition(new Point(10, 10));
        driver.findElement(By.name("Five")).click();

        driver = EnhancedWindowsDriver.changeToNewWindow("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App", "CAL3");
        driver.manage().window().setPosition(new Point(500, 10));
        driver.findElement(By.name("Six")).click();

        driver = EnhancedWindowsDriver.getWindowByKey("CAL2");
        driver.findElement(By.name("Five")).click();

        driver = EnhancedWindowsDriver.getWindowByKey("NOTE");
        String text = driver.findElement(By.className("Edit")).getText();
        driver.findElement(By.className("Edit")).sendKeys(Keys.CONTROL + "a" + Keys.CONTROL);
        driver.findElement(By.className("Edit")).sendKeys(Keys.DELETE);

        Assert.assertEquals("Hola Mundo", text);
        Assert.assertEquals("17", result);
    }

    @After
    public void tearDown(){
        EnhancedWindowsDriver.quit();
    }

}
