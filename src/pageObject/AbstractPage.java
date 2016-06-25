package pageObject;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import core.configs.ConfigLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
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

    /** @param driver WebDriver */
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
     * Upload file to FTP (Internal)
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
            client.login(config.SCREEN_CLOUD_LOGIN, config.SCREEN_CLOUD_PASSWORD);
            client.setFileType(FTP.BINARY_FILE_TYPE);

            if (client.isConnected()) {
                File localFile = new File(path+"/"+fileName);
                InputStream inputStream = new FileInputStream(localFile);

                boolean done = client.storeFile(fileName, inputStream);
                if (done) {
                    inputStream.close();
                    client.logout();
                    client.disconnect();
                    return config.SCREENSHARE_URL + "/" + config.SCREEN_CLOUD_LOGIN + "/"+fileName;
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
     * Method Take ScreenShot of current page
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
     * Method take a screenShot of current page (Use for AllureFramework annotations)
     * @return Byte[] of screenshot
     * @throws IOException
     */
    public byte[] takeScreenShot() throws IOException {
        return (byte[]) ((TakesScreenshot) driver).getScreenshotAs((OutputType) OutputType.BYTES);
    }

    /**
     * Method Check if is element present on page
     * @param element WebElement
     * @return true or false
     * NOT TROW ANY EXCEPTION! BE CAREFUL. HARD TO DEBUG WHIT THIS SH*T.
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

    /**
     * Method return boolean is the element is clicked after (int) seconds
     * @param element what needs to be clicked
     * @param seconds wait time
     * @return boolean element is clicked or not
     */
    public boolean waitForElementClickable(WebElement element, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Method return boolean is element is present after (int) seconds
     * @param element whats u want to find
     * @param timeInSeconds time of search
     * @return boolean true -  if find, false - if not find
     */
    public boolean waitForElementPresent(WebElement element, int timeInSeconds){

        try{
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            WebElement result = wait.until(ExpectedConditions.visibilityOf(element));
            return isElementPresent(result);
        }catch (TimeoutException e){
            return false;
        }
    }

    /**
     * Method return boolean is url contains word in url due waitingTime
     * @param contains expected url piece
     * @param timeInSeconds waiting time
     * @return boolean contains or not
     */
    public boolean waitForUrlContains(String contains, int timeInSeconds){
        try{
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            return wait.until(ExpectedConditions.urlContains(contains));
        }catch (TimeoutException e){
            return false;
        }

    }


    /**
     * Method will wait until page will loads(javascript feature -> document.readyState = 4 (complete )
     */
    public void waitForPageLoad() {
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
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
     * Execute Javascript command
     * @param script string javascript code
     */
    public void executeJS(String script){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }

}
