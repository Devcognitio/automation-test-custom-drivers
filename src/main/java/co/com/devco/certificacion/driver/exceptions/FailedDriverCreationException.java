package co.com.devco.certificacion.driver.exceptions;

public class FailedDriverCreationException extends Exception {

    public static final String FAILED_DRIVER_CREATION = " The co.com.devco.certificacion.getName could not be created. ";

    public FailedDriverCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
