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

    public Driver(Platform platform) {
        this.platform = platform;
    }

    protected DesiredCapabilities loadCapabilities() throws LoadDriverCapabilitiesException {
        InputStream inputStream = null;
        try {
            inputStream = getInputstreamFromPropertiesFile();
            return tryLoad(inputStream);
        } catch (IOException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        } finally {
            closeInputStream(inputStream);
        }
    }

    private InputStream getInputstreamFromPropertiesFile() {
        String propertiesFileName = PropertiesFileName.valueOf(platform.name()).fileName();
        try {
            return new FileInputStream(propertiesFileName);
        } catch (FileNotFoundException e) {
            return ClassLoader.class.getResourceAsStream(FILE_SEPARATOR + propertiesFileName);
        }
    }

    private DesiredCapabilities tryLoad(InputStream inputStream) throws IOException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        Properties properties = new Properties();
        properties.load(inputStream);
        properties.entrySet().iterator().forEachRemaining(
            entry -> {
                if (isThePlatformContainedInThe(entry)) {
                    String property = entry.getKey().toString().replace(platform.getName() + ".", "");
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

    private void closeInputStream(InputStream inputStream) throws LoadDriverCapabilitiesException {
        try {
            if(inputStream != null){
                inputStream.close();
            }
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
