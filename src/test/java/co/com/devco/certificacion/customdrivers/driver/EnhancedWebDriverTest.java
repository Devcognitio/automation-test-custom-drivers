package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.exceptions.FailedDriverCreationException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static co.com.devco.certificacion.customdrivers.Browser.*;
import static co.com.devco.certificacion.customdrivers.driver.EnhancedCapabilities.PATH_RESOURCES;

public class EnhancedWebDriverTest {

    private WebDriver driver;

    @Test
    public void testSuccessfulBecauseCreateChromeDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue(CHROME.getName());

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof ChromeDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateFirefoxDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue(FIREFOX.getName());

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateEdgeDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue(EDGE.getName());

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof EdgeDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateDriverInternetExplorer() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue(INTERNET_EXPLORER.getName());
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("ignoreZoomSetting", true);

        driver = EnhancedWebDriver.getWebDriver(desiredCapabilities);

        Assert.assertTrue(driver instanceof InternetExplorerDriver);
    }

    private void addToThePropertyFileTheDriverPropertyWithValue(String propertyValue) throws IOException {
        File propertyFile = FileUtils.getFile(PATH_RESOURCES + co.com.devco.certificacion.customdrivers.PropertiesFileName.WEB.fileName());
        FileUtils.writeStringToFile(propertyFile, String.format("webdriver.customdrivers=%s", propertyValue), StandardCharsets.UTF_8);
    }

    @After
    public void tearDown(){
        EnhancedWebDriver.quit();
    }
}
