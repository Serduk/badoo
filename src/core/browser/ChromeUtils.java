package core.browser;


import core.constans.LocaleData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by serdyuk on 6/3/16.
 */
public class ChromeUtils {

    int wait;
    DesiredCapabilities capabilities;
    ChromeOptions options;
    public WebDriver driver;

    /**
     * @param location (Type : String, Site Location (ex. USA, GBR, ESP, etc.))
     */
    public ChromeUtils(String location, int waitInSeconds) {

        System.setProperty("webdriver.chrome.driver", new ChromeProperties().osDetection());


        options = new ChromeOptions();
        options.addArguments("--lang=" + new LocaleData().getLocale(location));
        options.addArguments("--disable-user-media-security");
        options.addArguments("--use-fake-ui-for-media-stream");

        capabilities = DesiredCapabilities.chrome();
        wait = waitInSeconds;

    }

    /**
     * Browser Chrome;
     * Platform Web;
     *
     * @return ChromeDriver;
     */
    public WebDriver getWebBrowser() {

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;

    }

    /**
     * Browser Chrome;
     * Platform Mob;
     *
     * @return ChromeDriver;
     */
    public WebDriver getMobileBrowser() {

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "Google Nexus 5");

        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;
    }
}
