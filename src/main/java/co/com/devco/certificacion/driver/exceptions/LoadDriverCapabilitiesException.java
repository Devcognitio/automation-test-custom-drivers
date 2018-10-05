package co.com.devco.certificacion.driver.exceptions;

public class LoadDriverCapabilitiesException extends Exception {

    public static final String ERROR_LOADING_CAPABILITIES = " An error loading custom capabilities occurred. Please check " +
            "that properties file is named correctly at the right location and properties are well typed. ";

    public LoadDriverCapabilitiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
