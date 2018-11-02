package co.com.devco.certificacion.customdrivers.exceptions;

public class WindowsDriverException extends Throwable {

    public static final String CHANGE_TO_NEW_WINDOW_ERROR = " The driver failed to change to a new window ";
    public static final String GET_WINDOW_BY_KEY_ERROR = " There is no existing driver with the key ";
    public static final Throwable NO_ASSOCIATED_DRIVER_FOR_KEY = new Throwable("The key has no associated diver");

    public WindowsDriverException(String message, Throwable cause) {
        super(message, cause);
    }
}
