package co.com.devco.certificacion.driver;

import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;

public class BrowseAnApp extends BrowseTheWeb {

    public BrowseAnApp(WebDriver browser) {
        super(browser);
    }
}
