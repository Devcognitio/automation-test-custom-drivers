package co.com.devco.certificacion.customdrivers.exceptions;

public class FailedDriverCreationException extends Exception {

    public static final String FAILED_DRIVER_CREATION = " The driver could not be created. ";
    public static final Throwable NULL_DRIVER = new Throwable("The driver instance is null");

    public FailedDriverCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
