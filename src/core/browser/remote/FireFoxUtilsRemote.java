package core.browser.remote;

import core.constans.LocaleData;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by serdyuk on 6/12/16.
 */
public class FireFoxUtilsRemote {


    int wait = 20;
    FirefoxProfile profile;
    DesiredCapabilities capabilities;
    String host;

    public RemoteWebDriver driver;


    /**
     * @param location (Type : String, Site Location (ex. USA, GBR, ESP, etc.))
     * @param hubHost     (Type :String, 'localhost' OR 'grid')
     */
    public FireFoxUtilsRemote(String hubHost, String location, int waitInSeconds) {

        profile         = new FirefoxProfile();
        capabilities    = DesiredCapabilities.firefox();
        profile.setPreference("intl.accept_languages", new LocaleData().getLocale(location));
        wait = waitInSeconds;
        host = hubHost;

    }


    /**
     * Browser Firefox;
     * Platform Web;
     *
     * @return RemoteWebDriver;
     */
    public RemoteWebDriver getWebBrowser() throws MalformedURLException {

        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        driver = new RemoteWebDriver(setHost(host), capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        return driver;

    }

    /**
     * Browser Fiirefox;
     * Platform Mob;
     *
     * @return RemoteWebDriver;
     */
    public RemoteWebDriver getMobileBrowser() throws MalformedURLException {

        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Linux; U; Android 4.2.2; nl-nl; GT-I9505 Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

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
