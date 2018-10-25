package co.com.devco.certificacion.customdrivers;

public enum PropertiesFileName {

    MOBILE("mobile.properties"),
    WEB("web.properties"),
    WINDOWS("windows.properties");

    private final String fileName;

    PropertiesFileName(String propertiesFileName) {
        this.fileName = propertiesFileName;
    }

    public String fileName() {
        return fileName;
    }
}
