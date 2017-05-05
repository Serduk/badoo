package test.mob;

import core.browser.ChromeUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Test run example
 *
 * Created by serdyuk on 6/3/16.
 */
public class TestRun {
    private WebDriver driver;

    @BeforeTest
    public void startTest() {
        ChromeUtils chromeBrowser = new ChromeUtils("FRA", 20);
        this.driver = chromeBrowser.getMobileBrowser();
    }

    @Test
    public void test() {
        driver.get("https://google.com");
        System.out.println(driver.getPageSource());
    }

    @Test
    public void test2() {
        driver.get("https://ya.ru");
        System.out.println(driver.getPageSource());
    }

    @AfterMethod
    public void checkURL() {
        System.out.println("I'm current at " + driver.getCurrentUrl() + " URL");
        System.out.println(driver.getTitle());
    }

    @AfterTest
    public void cleanUp() {
        driver.quit();
    }
}
