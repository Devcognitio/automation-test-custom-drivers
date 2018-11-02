package co.com.devco.certificacion.customdrivers.habilities;

import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;

public class BrowseAnApp extends BrowseTheWeb {

    public BrowseAnApp(WebDriver browser) {
        super(browser);
    }
}
