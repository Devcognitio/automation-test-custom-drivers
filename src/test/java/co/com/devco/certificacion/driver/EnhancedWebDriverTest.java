package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
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

public class EnhancedWebDriverTest {

    private WebDriver driver;

    @Test
    public void testSuccessfulBecauseCreateChromeDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("chrome");

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof ChromeDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateFirefoxDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("firefox");

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateEdgeDriver() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("edge");

        driver = EnhancedWebDriver.getWebDriver();

        Assert.assertTrue(driver instanceof EdgeDriver);
    }

    @Test
    public void testSuccessfulBecauseCreateDriverInternetExplorer() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("ie");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("ignoreZoomSetting", true);

        driver = EnhancedWebDriver.getWebDriver(desiredCapabilities);

        Assert.assertTrue(driver instanceof InternetExplorerDriver);
    }

    private void addToThePropertyFileTheDriverPropertyWithValue(String propertyValue) throws IOException {
        //File propertyFile = FileUtils.getFile("src/test/resources/" + PropertiesFileName.WEB.fileName());
        File propertyFile = FileUtils.getFile(PropertiesFileName.WEB.fileName());
        FileUtils.writeStringToFile(propertyFile, String.format("webdriver.driver=%s", propertyValue), StandardCharsets.UTF_8);
    }

    @After
    public void tearDown(){
        EnhancedWebDriver.quit();
    }
}
