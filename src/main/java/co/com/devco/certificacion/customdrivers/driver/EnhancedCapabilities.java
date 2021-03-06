package co.com.devco.certificacion.customdrivers.driver;

import co.com.devco.certificacion.customdrivers.Platform;
import co.com.devco.certificacion.customdrivers.PropertiesFileName;
import co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static co.com.devco.certificacion.customdrivers.exceptions.LoadDriverCapabilitiesException.ERROR_LOADING_CAPABILITIES;

class EnhancedCapabilities {

    private Optional<Capabilities> capabilities;
    private Platform platform;
    private static final String FILE_SEPARATOR = File.separator;
    public static final String PATH_RESOURCES = String.format("src%stest%sresources%s", FILE_SEPARATOR, FILE_SEPARATOR, FILE_SEPARATOR);

    EnhancedCapabilities(Platform platform) {
        this.platform = platform;
        capabilities = Optional.empty();
    }

    EnhancedCapabilities(Platform platform, Capabilities capabilities) {
        this.platform = platform;
        this.capabilities = Optional.of(capabilities);
    }

    DesiredCapabilities loadCapabilitiesFromPropertiesFile() throws LoadDriverCapabilitiesException {
        String propertiesFileName = PATH_RESOURCES + PropertiesFileName.valueOf(platform.name()).fileName();
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
                if (isThePlatformContainedIn(entry)) {
                    String property = entry.getKey().toString().replace(this.platform.getName() + ".", "");
                    String value = entry.getValue().toString();
                    desiredCapabilities.setCapability(property, value);
                }
            });
            if(this.capabilities.isPresent()){
                desiredCapabilities.merge(capabilities.get());
            }
            return desiredCapabilities;
        } catch (IOException | NullPointerException e) {
            throw new LoadDriverCapabilitiesException(ERROR_LOADING_CAPABILITIES + e.getMessage(), e.getCause());
        }
    }

    private boolean isThePlatformContainedIn(Map.Entry<Object, Object> entry) {
        return entry.getKey().toString().contains(this.platform.getName());
    }
}
