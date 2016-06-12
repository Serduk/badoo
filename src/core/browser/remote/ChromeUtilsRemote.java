package core.browser.remote;

import core.browser.ChromeProperties;
import core.constans.LocaleData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by serdyuk on 6/12/16.
 */
public class ChromeUtilsRemote {



    int wait = 20;
    String host;
    ChromeOptions options;
    DesiredCapabilities capabilities;

    public WebDriver driver;

    /**
     * @param location (Type : String, Site Location (ex. USA, GBR, ESP, etc.))
     * @param hubHost     (Type :String, 'localhost' OR 'grid')
     */
    public ChromeUtilsRemote(String hubHost, String location, int waitInSeconds) {

        System.setProperty("webdriver.chrome.driver", new ChromeProperties().osDetection());

        options = new ChromeOptions();
        options.addArguments("--lang="+ new LocaleData().getLocale(location));
        options.addArguments("--disable-user-media-security");
        options.addArguments("--use-fake-ui-for-media-stream");
        capabilities = DesiredCapabilities.chrome();

        wait = waitInSeconds;
        host = hubHost;

    }

    /**
     * Browser Chrome;
     * Platform Web;
     *
     * @return RemoteWebDriver;
     */
    public WebDriver getWebBrowser() throws MalformedURLException {
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new RemoteWebDriver(setHost(host), capabilities);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;
    }

    /**
     * Browser Chrome;
     * Platform Mob;
     * @return RemoteWebDriver;
     */
    public WebDriver getMobileBrowser() throws MalformedURLException {

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "Google Nexus 5");

        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new RemoteWebDriver(setHost(host), capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;
    }

    /**
     *
     * @param host String
     * @return host URL
     * @throws MalformedURLException
     */
    private URL setHost(String host) throws MalformedURLException {
        switch (host) {
            case "grid":
                host = "http://192.168.12.27:4444/wd/hub";
                break;
            case "localhost":
                host = "http://127.0.0.1:4444/wd/hub";
                break;
        }
        return new URL(host);
    }

}
