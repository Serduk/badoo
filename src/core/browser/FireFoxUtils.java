package core.browser;

import core.constans.LocaleData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by serdyuk on 6/12/16.
 */
public class FireFoxUtils {


    int wait = 20;
    FirefoxProfile profile;
    DesiredCapabilities capabilities;
    public WebDriver driver;


    /**
     * @param location (Type : String, Site Location (ex. USA, GBR, ESP, etc.))
     */
    public FireFoxUtils(String location, int waitInSeconds) {

        profile         = new FirefoxProfile();
        capabilities    = DesiredCapabilities.firefox();

        profile.setPreference("intl.accept_languages", new LocaleData().getLocale(location));
        profile.setPreference ("media.navigator.permission.disabled", true);

        wait = waitInSeconds;

    }

    /**
     * Browser Firefox;
     * Platform Web;
     * @return WebDriver;
     */
    public WebDriver getWebBrowser() {

        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        driver = new FirefoxDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        return driver;

    }

    /**
     * Browser Fiirefox;
     * Platform Mob;
     * @return WebDriver ;
     */
    public WebDriver getMobileBrowser() {

        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Linux; U; Android 4.2.2; nl-nl; GT-I9505 Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        driver = new FirefoxDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);

        return driver;

    }
}
