package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.FailedDriverCreationException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EnhancedWebDriverTest {

    private WebDriver driver;

    @Test
    public void createOwnWebDriverChrome() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("chrome");

        driver = EnhancedWebDriver.getDriver();

        Assert.assertTrue(driver instanceof ChromeDriver);
    }

    @Test
    public void createOwnWebDriverFirefox() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("firefox");
        System.setProperty("webdriver.gecko.driver", "D:\\firefox64\\geckodriver.exe");
        driver = EnhancedWebDriver.getDriver();

        Assert.assertTrue(driver instanceof FirefoxDriver);
    }

    @Ignore
    @Test
    public void createOwnWebDriverInternetExplorer() throws IOException, FailedDriverCreationException {
        addToThePropertyFileTheDriverPropertyWithValue("ie");

        driver = EnhancedWebDriver.getDriver();

        Assert.assertTrue(driver instanceof InternetExplorerDriver);
    }

    private void addToThePropertyFileTheDriverPropertyWithValue(String propertyValue) throws IOException {
        File propertyFile = FileUtils.getFile(PropertiesFileName.WEB.fileName());
        FileUtils.writeStringToFile(propertyFile, String.format("webdriver.driver=%s", propertyValue), StandardCharsets.UTF_8);
    }

    @After
    public void tearDown(){
        Driver.tearDown(driver);
        driver = null;
    }
}
