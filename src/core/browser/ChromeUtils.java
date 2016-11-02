package core.browser;


import core.constans.ConfirmAuthDataForProxy;
import core.constans.LocaleData;
import core.constans.LocationsProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
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

    /*
* Create browser instans with paid proxy (Adclarity)
* */
    public WebDriver getMobWithProxy(String location) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "Google Nexus 5");

        ConfirmAuthDataForProxy dataProxy = new ConfirmAuthDataForProxy(location);
        dataProxy.setProxy();
        options.addArguments("load-extension=" + dataProxy.saveFile);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);


        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);


        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;
    }

    public WebDriver getWEBWithProxy(String location) {
        ConfirmAuthDataForProxy dataProxy = new ConfirmAuthDataForProxy(location);
        dataProxy.setProxy();
        options.addArguments("load-extension=" + dataProxy.saveFile);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();

        return driver;
    }

    public WebDriver getWEBFreeProxy(String location) {
        LocationsProxy country = LocationsProxy.valueOf(location.toUpperCase());
        String proxyLocation = country.getProxy() + ":" + country.getPort();

        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(proxyLocation)
                .setSslProxy(proxyLocation)
                .setSocksProxy(proxyLocation);
        capabilities.setCapability(CapabilityType.PROXY, proxy);


        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;
    }
}
