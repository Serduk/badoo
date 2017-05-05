package test.exapmles;

import core.browser.ChromeUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Example, Haw we can init browser with already workable proxy
 * Created by sergey on 10/25/16.
 */

public class ChromeWithProxyRun {

    WebDriver driver;

    @Test
    public void webProxyTest() {
        ChromeUtils chromeBrowser = new ChromeUtils("USA", 10);
        driver = chromeBrowser.getWEBWithProxy("USA");

        driver.get("https://2ip.ru");
    }

    @Test
    public void mobProxyTest() throws IOException {
        ChromeUtils chromeBrowser = new ChromeUtils("USA", 10);
        driver = chromeBrowser.getMobWithProxy("USA");

        driver.get("https://2ip.ru");
    }

    @AfterTest
    public void cleanUP() {
        driver.quit();
    }
}
