package test;

import core.utils.CSVUtils;
import core.helpers.listeners.BadooTestNGListener;
import core.random.RandomUtils;
import exceptions.BadooException;
import mySQL.DatabaseUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Abstract init class most used tools;
 * Created by serdyuk on 6/25/16.
 */
@Listeners(value = BadooTestNGListener.class)
public abstract class AbstractTestScenario {

    protected RandomUtils random;
    protected CSVUtils csvWorker;
    protected DatabaseUtils db;
    protected String bufferFile;
    private static Map<Runnable, Long> scheduledTasks = Collections.synchronizedMap(new HashMap<>());
    private static final int scheduledExecutorsLoop = 5;

    public AbstractTestScenario() {
        csvWorker = new CSVUtils();
        random = new RandomUtils();
        db = new DatabaseUtils();
    }

    public WebDriver switchTab(WebDriver driver, int tabNumber) throws BadooException, InterruptedException {

        List<String> tabs = driver.getWindowHandles().stream().collect(Collectors.toList());
        int totalTabs = tabs.size();
        if (tabNumber > totalTabs) {
            throw new BadooException("Cant switch to tab " + tabNumber + ", there only " + totalTabs + " exist");
        } else {
            Keys key = null;
            switch (tabNumber) {
                case 1:
                    key = Keys.NUMPAD1;
                    break;
                case 2:
                    key = Keys.NUMPAD2;
                    break;
                case 3:
                    key = Keys.NUMPAD3;
                    break;
                case 4:
                    key = Keys.NUMPAD4;
                    break;
                case 5:
                    key = Keys.NUMPAD5;
                    break;
                case 6:
                    key = Keys.NUMPAD6;
                    break;
            }
            driver.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.CONTROL, key));
            WebDriver window = driver.switchTo().window(tabs.get(tabNumber - 1));
            Thread.sleep(1000);
            return window;
        }
    }

    public void closeTab(WebDriver driver, int tab) throws InterruptedException, BadooException {

        switchTab(driver, tab);
        List<String> tabs = driver.getWindowHandles().stream().collect(Collectors.toList());
        String tabForClose = tabs.get(tab - 1);

        driver.getWindowHandles()
                .stream()
                .filter(handle -> handle.equals(tabForClose))
                .forEach(handle -> driver.close());
        tabs = driver.getWindowHandles().stream().collect(Collectors.toList());
        driver.switchTo().window(tabs.get(0));
    }

    public static void addScheduledTask(Runnable task, int delayInMs) {
        scheduledTasks.put(task, (long) delayInMs);
    }


    public static void runScheduledTasks() {
        if (scheduledTasks.size() > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(scheduledExecutorsLoop);
            scheduledTasks.entrySet()
                    .forEach(taskSet -> scheduler.scheduleAtFixedRate(taskSet.getKey(), scheduledExecutorsLoop,
                            taskSet
                                    .getValue(), TimeUnit.MILLISECONDS
                    ));
        }

    }

    public void addNewTab(WebDriver driver) throws Exception {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open()");
    }

    public String urlToString(String url) {

        String[] urlArr = url.split("://");
        return urlArr[1].replace(".", "_");
    }

    /**
     * kill process
     *
     * @param driver;
     */
    public void tearDown(WebDriver driver) {
        driver.quit();
    }


    /**
     * Need to set environment in allure props.
     *
     * @param driver current WebDriver
     * @return Current WebDriver instance capabilities
     */
    public Capabilities getCapabilities(WebDriver driver) {
        return ((RemoteWebDriver) driver).getCapabilities();
    }
}
