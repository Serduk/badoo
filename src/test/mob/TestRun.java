package test.mob;

import core.browser.ChromeUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created by serdyuk on 6/3/16.
 */
public class TestRun {
    WebDriver driver;

    @Test
    public void startTest() {
        ChromeUtils chromeBrowser = new ChromeUtils("FRA", 20);
        this.driver = chromeBrowser.getMobileBrowser();

        driver.get("https://google.com");
    }
}
