package pageObject;

import core.configs.ConfigLoader;
import core.random.RandomUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Describe all base methods for PageObject
 * <p>
 * Created by serdyuk on 6/12/16.
 */
public class AbstractPage {

    /**
     * @param driver WebDriver
     */
    public WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(tagName = "html")
    public static WebElement html;

    @FindBy(tagName = "head")
    public static WebElement head;

    @FindBy(tagName = "body")
    public static WebElement body;


    /**
     * Override webDriver.get()
     *
     * @param url
     */
    public void get(String url) {
        this.driver.get(url);
    }

    public void open(String site) {
        driver.get(site);
    }

    /**
     * Upload file to FTP (Internal)
     *
     * @param fileName full name of file
     * @param path     directory path to file
     * @return absolute path to file
     * @throws IOException
     */
    public static String uploadFile(String fileName, String path) throws IOException {
        FTPClient client = new FTPClient();

        try {
            client.connect(ConfigLoader.FTP_URL);
//            client.login(ConfigLoader.IPA_LOGIN_AG, ConfigLoader.IPA_PASSWORD_AG);
            client.setFileType(FTP.BINARY_FILE_TYPE);

            if (client.isConnected()) {
                File localFile = new File(path + "/" + fileName);
                InputStream inputStream = new FileInputStream(localFile);

                boolean done = client.storeFile(fileName, inputStream);
                if (done) {
                    inputStream.close();
                    client.logout();
                    client.disconnect();
                    return ConfigLoader.SCREENSHARE_URL + "/" + fileName;
                }
            }
        } catch (IOException e) {
            System.out.println("Error_1");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param element     WebDriver Element, screenshot that you want to save. Firefox Only.
     * @param screenName  Name of screenshot
     * @param uploadToFTP is need to upload to ftp
     * @return absolute path of screenShot
     * @throws IOException
     */
    public String takeScreenShotElement(WebElement element, String screenName, boolean uploadToFTP) throws IOException {
        screenName = screenName + ".png";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String absolutePath = ConfigLoader.REPORTS_DIR + "/" + screenName;
        BufferedImage fullImg = ImageIO.read(screenshot);
        Point point = element.getLocation();

        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();

        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);

        FileUtils.copyFile(screenshot, new File(absolutePath));
        if (uploadToFTP) {
            absolutePath = uploadFile(screenName, ConfigLoader.REPORTS_DIR);
        }
        return absolutePath;
    }

    /**
     * Method Take ScreenShot of current page
     *
     * @param screenName  name of screenShot
     * @param uploadToFTP bool. Upload to ftp
     * @return absolute path of screenShot
     * @throws IOException
     */
    public String takeScreenShot(String screenName, boolean uploadToFTP) throws IOException {

        screenName = screenName + ".png";

        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String absolutePath = ConfigLoader.REPORTS_DIR + "/" + screenName;
        FileUtils.copyFile(screenShot, new File(absolutePath));
        if (uploadToFTP) {
            absolutePath = uploadFile(screenName, ConfigLoader.REPORTS_DIR);
        }
        return absolutePath;
    }

    /**
     * Method take a screenShot of current page (Use for AllureFramework annotations)
     *
     * @return Byte[] of screenshot
     * @throws IOException
     */
    public byte[] takeScreenShot() throws IOException {
        return (byte[]) ((TakesScreenshot) driver).getScreenshotAs((OutputType) OutputType.BYTES);
    }

    /**
     * Method Check if is element present on page
     *
     * @param element WebElement
     * @return true or false
     * NOT TROW ANY EXCEPTION! BE CAREFUL. HARD TO DEBUG WHIT THIS SH*T.
     */
    public boolean isElementPresent(WebElement element) {
        boolean isPresent = false;
        try {
            if (element.isDisplayed()) {
                isPresent = true;
            }
        } catch (NoSuchElementException e) {
            isPresent = false;
        }
        return isPresent;
    }

    /**
     * @param element          WebElement
     * @param poolInSeconds    int seconds
     * @param timeOutInSeconds int seconds
     * @return false
     * @deprecated not done yet
     */
    public boolean waitPool(WebElement element, int poolInSeconds, int timeOutInSeconds) {

        List<WebElement> lElement = new ArrayList<>();
        lElement.add(element);
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(poolInSeconds, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
        return false;
    }

    /**
     * Method return boolean is the element is clicked after (int) seconds
     *
     * @param element what needs to be clicked
     * @param seconds wait time
     * @return boolean element is clicked or not
     */
    public boolean waitForElementClickable(WebElement element, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitForElementInvisibility(String xpath, int waitInSecondsBefore) {
        driver.manage().timeouts().implicitlyWait(waitInSecondsBefore, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, waitInSecondsBefore);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForElementVisibility(By by, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public By findElementByXpath(String xpath, Object... args) {
        return By.xpath(String.format(xpath, args));
    }

    /**
     * Method return boolean is element is present after (int) seconds
     *
     * @param element       whats u want to find
     * @param timeInSeconds time of search
     * @return boolean true -  if find, false - if not find
     */
    public boolean waitForElementPresent(WebElement element, int timeInSeconds) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForElementPresent(By by, int timeInSeconds) {
        boolean $isFound = driver.findElements(by).size() > 0;
        int timer = 0;
        while (!$isFound && timer < timeInSeconds) {
            $isFound = driver.findElements(by).size() > 0;
            timer++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return $isFound;
    }

    /**
     * Method return boolean is element is present after (int) seconds
     *
     * @param elementsList  List<WebElement> elements list, whats u want to find
     * @param timeInSeconds time of search
     * @return boolean true -  if find, false - if not find
     */
    public boolean waitForElementPresent(List<WebElement> elementsList, int timeInSeconds) throws InterruptedException {


        for (WebElement element : elementsList) {
            if (waitForElementPresent(element, timeInSeconds))
                return true;
        }
        return false;
    }

    /**
     * Method return boolean is url contains word in url due waitingTime
     *
     * @param contains      expected url piece
     * @param timeInSeconds waiting time
     * @return boolean contains or not
     */
    public boolean waitForUrlContains(String contains, int timeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            return wait.until(ExpectedConditions.urlContains(contains));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForUrl(String url, int timeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (TimeoutException e) {
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
     * Method return bool of page complete load status
     * true === complete
     */
    public boolean pageLoadComplete() {
        return new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * loop-based iterator method: Trying to find element every 500 ms, and return true, if it found.
     * ****WARNING *****
     * THIS IS NOT END-TIME METHOD, AND WILL BE EXECUTED, UNLIMITED TIME...USE IN ON YOUR RISK.
     * *****************
     *
     * @param elementWasDisplayed WebElement what we waiting for.
     * @return boolean True, when element was find. (best practice for javascript dynamic page layout)
     * @throws InterruptedException
     */
    public boolean waitUntil(WebElement elementWasDisplayed) throws InterruptedException {
        boolean displayed = isElementPresent(elementWasDisplayed);
        try {
            while (!displayed) {
                Thread.sleep(500);
                displayed = isElementPresent(elementWasDisplayed);
            }
        } catch (WebDriverException e) {
            e.getStackTrace();
        }
        return displayed;
    }

    /**
     * Method set hard wait.
     * Based on default Threads methods and add try/Catch
     * This Method set hard wait for tests.
     * Set sleep in Thread
     * Time in ms (milliseconds)
     */
    public void sleep(int seconds) {
        int ms = seconds * 1000;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("Trying to set hard sleep in Thread, but something wrong");
        }
    }

    public void sleepMs(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            System.out.println("Trying to set hard sleep in Thread, but something wrong");
        }
    }

    /**
     * Advanced WebDriver webElement.click() action,
     * is verify, is element is present, and if true, click on it. Or throw exception.
     *
     * @param element WebElement what need to be clicked
     * @throws WebDriverException
     */
    public void click(WebElement element) {
        if (isElementPresent(element)) {
            element.click();
        } else {
            throw new WebDriverException(element.getTagName() + "\t" + element.getAttribute("class") + " is not clickable");
        }
    }

    public void clickAndCatchStaleReference(WebElement element) {
        try {
            click(element);
        } catch (StaleElementReferenceException e) {
            sleep(2);
            click(element);
        }

    }

    /**
     * Execute Javascript command
     *
     * @param script string javascript code
     */
    public JavascriptExecutor executeJS(String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
        return js;
    }


    public WebElement getParentElementOf(WebElement element) {
        return element.findElement(By.xpath(".."));
    }


    /**
     * Wait for text present inside element
     *
     * @param element           WebElement where text needs to be present
     * @param textMustBePresent String text which need to be present in element
     * @param waitTimeInSeconds max waiting time in seconds
     * @return boolean, is text present inside element
     */
    public boolean waitForTextPresent(WebElement element, String textMustBePresent, int waitTimeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
            return wait.until(ExpectedConditions.textToBePresentInElement(element, textMustBePresent));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Wait for title contains some text
     *
     * @param titleText         String text whats need contains
     * @param waitTimeInSeconds waiting time in seconds
     * @return boolean, is title contains titleText
     */
    public boolean titleContains(String titleText, int waitTimeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
            return wait.until(ExpectedConditions.titleContains(titleText));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementPresentXpath(String xpath, Object... args) {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        List<WebElement> elements = driver.findElements(By.xpath(String.format(xpath, args)));
        return elements.size() > 0;
    }


    protected void waitForElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(element));

        //wait until element will be at the same place (for moving elements: for Chrome and IE)
        Point currLocation, newLocation;
        long startTime = System.currentTimeMillis();
        long delta;

        newLocation = new Point(-1, -1);
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }   //the element may move veeery slowly. It's better to wait some time
            currLocation = newLocation;
            newLocation = element.getLocation();
            delta = System.currentTimeMillis() - startTime;
        } while ((currLocation.getX() - newLocation.getX() != 0 && currLocation.getY() - newLocation.getY() != 0)
                && (delta <= 5 * 1000));

        if (delta > 5 * 1000) {
            throw new InvalidElementStateException("Element did not stand at the same place for " + 5 + " seconds");
        }
        if (System.getProperty("browser", "firefox").equalsIgnoreCase("firefox")) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
        }
    }

    protected int selectDropDownListRandomOption(int firstIndex, int lastIndex, WebElement locator) {

        Select dropDownList = new Select(locator);
        int size = dropDownList.getOptions().size();
        int selectedIndex = (lastIndex > 0)
                ? RandomUtils.genInt(firstIndex - 1, lastIndex - 1)
                : RandomUtils.genInt(firstIndex - 1, size - 1);
        dropDownList.selectByIndex(selectedIndex);
        return selectedIndex;
    }

    public boolean fluentWait(int timeOut, int poolingDelay, WebElement element) {

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOut, TimeUnit.SECONDS)
                .pollingEvery(poolingDelay, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        return wait.until(input -> isElementPresent(element));
    }

    public void waitForElementToBeClickable(By by, int timeInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(by));

        //wait until element will be at the same place (for moving elements: for Chrome and IE)
        Point currLocation, newLocation;
        long startTime = System.currentTimeMillis();
        long delta;

        newLocation = new Point(-1, -1);
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
            //the element may move veeery slowly. It's better to wait some time
            currLocation = newLocation;
            newLocation = driver.findElement(by).getLocation();
            delta = System.currentTimeMillis() - startTime;
        } while ((currLocation.getX() - newLocation.getX() != 0 && currLocation.getY() - newLocation.getY() != 0)
                && (delta <= timeInSeconds * 1000));

        if (delta > timeInSeconds * 1000) {
            throw new InvalidElementStateException("Element did not stand at the same place for " + timeInSeconds + " seconds");
        }
    }

    public boolean waitForMethodReturnTrue(Callable calledMethod, int waitTime) throws Exception {
        int timer = 0;
        boolean result = false;
        while (waitTime > timer) {

            result = (boolean) calledMethod.call();
            if (result) {
                return true;
            }
            sleep(1);
            timer++;
        }
        return false;
    }


    public void scrollToTop() {
        executeJS("window.scroll(0,0);");
    }
}
