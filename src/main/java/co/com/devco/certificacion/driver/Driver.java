package co.com.devco.certificacion.driver;

import co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Properties;

import static co.com.devco.certificacion.driver.exceptions.LoadDriverCapabilitiesException.ERROR_LOADING_CAPABILITIES;

public abstract class Driver<T> {

    private Platform platform;
    private static final String FILE_SEPARATOR = File.separator;

    protected DesiredCapabilities loadCapabilities(Platform platform) throws LoadDriverCapabilitiesException {
        this.platform = platform;
        String propertiesFileName = PropertiesFileName.valueOf(platform.name()).fileName();
        InputStream inputStream = getInputStream(propertiesFileName);
        return load(inputStream);
    }

    private InputStream getInputStream(String propertiesFileName) throws LoadDriverCapabilitiesException {
        try (InputStream inputStream = new FileInputStream(propertiesFileName)) {
            return inputStream;
        } catch (FileNotFoundException e) {
            return ClassLoader.class.getResourceAsStream(FILE_SEPARATOR + propertiesFileName);
        } catch (IOException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        }
    }

    private DesiredCapabilities load(InputStream inputStream) throws LoadDriverCapabilitiesException {
        try {
            return tryLoad(inputStream);
        } catch (IOException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        }
    }

    private DesiredCapabilities tryLoad(InputStream inputStream) throws IOException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        Properties properties = new Properties();
        properties.load(inputStream);
        properties.entrySet().iterator().forEachRemaining(
            entry -> {
                if (isThePlatformContainedInThe(entry)) {
                    String property = entry.getKey().toString().replace(this.platform.getName() + ".", "");
                    String value = entry.getValue().toString();
                    desiredCapabilities.setCapability(property, value);
                }
            }
        );
        return desiredCapabilities;
    }

    private boolean isThePlatformContainedInThe(Map.Entry<Object, Object> entry) {
        return entry.getKey().toString().contains(this.platform.getName());
    }

    public static void tearDown(WebDriver driver) {
        if(driver != null) {
            driver.quit();
        }
    }

    public abstract T init() throws MalformedURLException;

}
