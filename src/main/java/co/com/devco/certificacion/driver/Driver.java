package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Properties;

import static co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException.ERROR_LOADING_CAPABILITIES;

public abstract class Driver<T> {

    private Platform platform;
    private static final String FILE_SEPARATOR = File.separator;

    DesiredCapabilities loadCapabilities(Platform platform) throws LoadDriverCapabilitiesException {
        this.platform = platform;
        String propertiesFileName = PropertiesFileName.valueOf(platform.name()).fileName();
        try (InputStream inputStream = new FileInputStream(propertiesFileName)) {
            return load(inputStream);
        } catch (FileNotFoundException e) {
            return load(ClassLoader.class.getResourceAsStream(FILE_SEPARATOR + propertiesFileName));
        } catch (IOException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        }
    }

    private DesiredCapabilities load(InputStream inputStream) throws LoadDriverCapabilitiesException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            properties.entrySet().iterator().forEachRemaining(entry -> {
            if (entry.getKey().toString().contains(this.platform.driver())) {
                String property = entry.getKey().toString().replace(this.platform.driver() + ".", "");
                String value = entry.getValue().toString();
                desiredCapabilities.setCapability(property, value);
            }
        });
        return desiredCapabilities;
        } catch (IOException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        }
    }

    public static void tearDown(WebDriver driver) {
        if(driver != null) {
            driver.quit();
        }
    }

    public abstract T init() throws MalformedURLException;

}
