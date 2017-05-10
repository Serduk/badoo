package core.helpers.listeners;

import core.configs.ConfigLoader;
import core.helpers.jettyLogger.Log;
import core.random.RandomUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import pageObject.AbstractPage;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;

/**
 * Listener for WebDriver WebElement Actions  e.g (findBy, click, etc)
 *
 * Created by serdyuk on 5/10/17.
 */
public class BadooWebDriverListener  extends AbstractWebDriverEventListener {


    WebDriver d_driver;
    By listeningElement;
    Runnable  callableMethodForElement, methodOnException, methodOnValueChange;
    RandomUtils rand;

    public BadooWebDriverListener(WebDriver driver) {

        this.d_driver = driver;
        rand = new RandomUtils();

    }

    public void setMethodForListeningElement(By by, Runnable runnable) {

        listeningElement = by;
        callableMethodForElement = runnable;

    }

    public void setMethodOnElementValueChange(Runnable runnable){
        methodOnValueChange = runnable;
    }

    public void setMethodOnException(Runnable runnable) {
        methodOnException = runnable;
    }

    public void beforeClickOn(WebElement element, WebDriver driver) {
        if (listeningElement != null &&
                callableMethodForElement != null
                ) {
            boolean isFound = driver.findElements(listeningElement).size() > 0;
            if (isFound) {
                try {
                    new Thread(callableMethodForElement).start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver) {

        Log.info("element value has change");
        if(methodOnValueChange != null){
            try {
                new Thread(methodOnValueChange)
                        .start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void beforeNavigateTo(String url, WebDriver driver) {
        if (listeningElement != null &&
                callableMethodForElement != null
                ) {
            boolean isFound = driver.findElements(listeningElement).size() > 0;
            if (isFound) {
                try {
                    new Thread(callableMethodForElement)
                            .start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void takeAShot(String viewName) {

        String currentTestName = BadooTestNGListener.getMethodName(BadooTestNGListener.getCurrentTestResult());
        String name            = viewName + "_" + currentTestName + "_FAILED_" + rand.getTime("-");
        try {
            File srcFile = makeScreenShot();
            FileUtils.copyFile(srcFile, new File(ConfigLoader.REPORTS_DIR + "/" + name + ".png"));
            String link = AbstractPage.uploadFile(name + ".png", ConfigLoader.REPORTS_DIR);
            makeScreenShotAllure();
            Throwable throwable       = BadooTestNGListener.getCurrentTestResult().getThrowable();
            String    originalMessage = throwable.getMessage();
            String    newMessage      = "[TEST]: " + currentTestName + "[FAILED]\n [SCREENSHOT]: " + link+"\n";
            BadooTestNGListener.printTestStatus("[SCREENSHOT]", link, "\033[1;31m [FAILED] \033[0m");

            try {
                FieldUtils.writeField(throwable, "detailMessage", newMessage+originalMessage, true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onException(Throwable throwable, WebDriver driver) {

        if (methodOnException != null) {
            try {
                new Thread(methodOnException).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Attachment("Failed Test ScreenShot")
    public byte[] makeScreenShotAllure() {
        return ((TakesScreenshot) d_driver).getScreenshotAs(OutputType.BYTES);
    }

    public File makeScreenShot() {
        return ((TakesScreenshot) d_driver).getScreenshotAs(OutputType.FILE);
    }

}
