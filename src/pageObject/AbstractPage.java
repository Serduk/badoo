package pageObject;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import core.configs.ConfigLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by serdyuk on 6/12/16.
 */
public class AbstractPage {


    /**
     * Constructor
     *
     * @param driver
     */
    public WebDriver driver;

    public AbstractPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(tagName = "html")
    public static WebElement html;

    @FindBy(tagName = "head")
    public static WebElement head;

    @FindBy(tagName = "body")
    public static WebElement body;

    /**
     * Upload file to FTP
     * @param fileName full name of file
     * @param path directory path to file
     * @return absolute path to file
     * @throws IOException
     */
    protected String uploadFile(String fileName, String path) throws IOException {
        ConfigLoader config = new ConfigLoader();
        FTPClient client = new FTPClient();

        try {
            client.connect(config.FTP_URL);
//            client.login(config.IPA_LOGIN_AG, config.IPA_PASSWORD_AG);
            client.setFileType(FTP.BINARY_FILE_TYPE);

            if (client.isConnected()) {
                File localFile = new File(path+"/"+fileName);
                InputStream inputStream = new FileInputStream(localFile);

                boolean done = client.storeFile(fileName, inputStream);
                if (done) {
                    inputStream.close();
                    client.logout();
                    client.disconnect();
//                    return config.SCREENSHARE_URL + "/" + config.IPA_LOGIN_AG + "/"+fileName;
                }
            }
        } catch (IOException e) {
            System.out.println("Error_1");
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param element Webdriver Element, screenshot that you want to save. Firefox Only.
     * @param screenName Name of screenshot
     * @param uploadToFTP is need to upload to ftp
     * @return absolute path of screenShot
     * @throws IOException
     */
    public String takeScreenShotElement(WebElement element, String screenName, boolean uploadToFTP) throws IOException {
        ConfigLoader config = new ConfigLoader();
        screenName = screenName  + ".png";
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String absolutePath = config.REPORTS_DIR + "/" +screenName;
        BufferedImage fullImg = ImageIO.read(screenshot);
        Point point = element.getLocation();

        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();

        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);

        FileUtils.copyFile(screenshot, new File(absolutePath));
        if(uploadToFTP)
        {
            absolutePath = uploadFile(screenName, config.REPORTS_DIR);
        }
        return absolutePath;
    }

    /**
     * Take ScreenShot of current page
     * @param screenName name of screenShot
     * @param uploadToFTP bool. Upload to ftp
     * @return absolute path of screenShot
     * @throws IOException
     */
    public String takeScreenShot(String screenName, boolean uploadToFTP) throws IOException {
        ConfigLoader config = new ConfigLoader();

        screenName = screenName  + ".png";

        File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String absolutePath = config.REPORTS_DIR + "/" +screenName;
        FileUtils.copyFile(screenShot, new File(absolutePath));
        if(uploadToFTP)
        {
            absolutePath = uploadFile(screenName, config.REPORTS_DIR);
        }
        return absolutePath;
    }

    /**
     * Take ScreenShot of current page
     * @param screenName name of screenShot
     * @return absolute path of screenShot
     * @throws IOException
     */
    public byte[] takeScreenShot(String screenName) throws IOException {
        ConfigLoader config = new ConfigLoader();

        screenName = screenName  + ".png";

        return (byte[]) ((TakesScreenshot) driver).getScreenshotAs((OutputType) OutputType.BYTES);
    }

    /**
     * Check if is element present on page
     * @param element WebElement
     * @return true or false
     * NOT TROW ANY EXCEPTION ! BE CAREFUL. HARD TO DEBUG WHIT THIS SH*T.
     *
     */
    public boolean isElementPresent(WebElement element){
        boolean isPresent = false;
        try{
            if(element.isDisplayed()){
                isPresent = true;
            }
        }catch (NoSuchElementException  e) {
            isPresent =  false;
        }
        return isPresent;
    }


    public boolean waitForClickable(WebElement element, int seconds) {
        boolean result;
        try {
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     *
     * @param element whats u want to find
     * @param timeInSeconds time of search
     * @return boolean true -  if find, false - if not find
     */
    public boolean waitForElement(WebElement element, int timeInSeconds){

        try{
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            WebElement result = wait.until(ExpectedConditions.visibilityOf(element));
            return isElementPresent(result);
        }catch (TimeoutException e){
            return false;
        }
    }


    /**
     * loop-based iterator method: Trying to find element every 500 ms, and return true, if it found.
     * ****WARNING *****
     * THIS IS NOT END-TIME METHOD, AND WILL BE EXECUTED, UNLIMITED TIME...USE IN ON YOUR RISK.
     * *****************
     * @param elementWasDisplayed WebElement what we waiting for.
     * @return boolean True, when element was find. (best practice for javascript dynamic page layout)
     * @throws InterruptedException
     */
    public boolean waitUntil(WebElement elementWasDisplayed) throws InterruptedException {
        boolean displayed = isElementPresent(elementWasDisplayed);
        try{
            while (!displayed){
                Thread.sleep(500);
                displayed = isElementPresent(elementWasDisplayed);
            }
        }catch (WebDriverException e){
            e.getStackTrace();
        }
        return displayed;
    }

    public boolean waitUntil(WebElement elementWasDisplayed, int seconds) throws InterruptedException {

        boolean displayed = isElementPresent(elementWasDisplayed);
        int cc =  0;

        try{
            while (!displayed && cc < seconds){
                Thread.sleep(1000);
                displayed = isElementPresent(elementWasDisplayed);
                cc++;
            }
            if(cc >= seconds){
                return  false;
            }
        }catch (WebDriverException e){
            e.getStackTrace();
        }
        return displayed;
    }
    /**
     * Create new tab in current Browser window
     */
    public List<String> openNewTab(){
        List<String>handles = new ArrayList<>();
        body.sendKeys(Keys.CONTROL + "t");
        handles.addAll(driver.getWindowHandles().stream().collect(Collectors.toList()));
        return handles;
    }


    /**
     * Switch to concrete window Tab in current WebDriver instance.
     * @param tabHandleName String, name of window what you need to switch
     */
    public void switchToTab(String tabHandleName){
        driver.switchTo().window(tabHandleName);
    }


    /**
     * Advanced WebDriver webElement.click() action,
     * is verify, is element is present, and if true, click on it. Or throw exception.
     * @param element WebElement what need to be clicked
     * @throws ElementNotFoundException
     */
    public void click(WebElement element){
        if(isElementPresent(element)){
            element.click();
        }else{
            throw new ElementNotFoundException(element.toString(), "","");
        }
    }

    /**
     * Execute Javascript
     * @param script string javascript code
     */
    public void executeJS(String script){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }
}
