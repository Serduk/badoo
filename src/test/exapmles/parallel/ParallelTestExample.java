package test.exapmles.parallel;

import core.browser.remote.ChromeUtilsRemote;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Example for parallel test
 * Created by serdyuk on 5/10/17.
 */
public class ParallelTestExample {

    /**@Method return instance of WebDriver */
    public WebDriver setDriver() throws MalformedURLException {
        ChromeUtilsRemote fox = new ChromeUtilsRemote("grid", "USA", 30);
        return fox.getWebBrowser();
    }

    /**@Method return Array of sites domains*/
    @DataProvider(name = "dp", parallel = true) //parallel = true is important, if false - test will be run in one thread;
    public Object[][] parseLocaleData() {
        return new Object[][]{
                {"https://ya.ru"},
                {"https://google.com"},
                {"https://duck.com"},
                {"https://facebook.com"},
                {"https://badoo.com"}
        };
    }

    @Test(dataProvider = "dp")
    public void testProvider(String domain) throws IOException, InterruptedException {
        WebDriver driver = setDriver(); //call webdriver instance;
        driver.get(domain); // get domain from dataprovider;
        System.out.println("Domain : " + domain + "\n Title: " + driver.getTitle() +"\n URL:  "+driver.getCurrentUrl()); //print result
        driver.quit(); // close driver instance.
    }

}
