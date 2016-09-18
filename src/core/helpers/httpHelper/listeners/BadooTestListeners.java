package core.helpers.httpHelper.listeners;

import core.configs.ConfigLoader;
import core.random.RandomUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

/**
 * Created by serdyuk on 9/18/16.
 */

public class BadooTestListeners implements ITestListener{

    private RandomUtils rand = new RandomUtils();
    private ConfigLoader cfg = new ConfigLoader();
    private static WebDriver driver;
    public static boolean  showLog = false;
    public static boolean  takeScreenShotOnFail = false;
    /**
     * Set instance of current webDriver, for handle supported WebDriver features
     * **************** INIT ONLY AFTER  OBJECT CALL IN YOUR CLASS***************
     * @param driver current instance of WebDriver
     */
    public static void setDriver(WebDriver driver) {
        BadooTestListeners.driver = driver;
    }

    /**
     * This method fill run before each method;
     * @param iTestResult Internal test param from your testNG test instance
     */
    @Override
    public void onTestStart(ITestResult iTestResult) {
        if(showLog){
            System.out.println("\033[36m[TEST] "+getTestMethodName(iTestResult) +"\033[0m ");
        }
    }

    /**
     * If test result is success (iTestResult.isSuccess()) this method will be run;
     * @param iTestResult Internal test param from your testNG test instance
     */
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        if(showLog){
            System.out.println("\033[1;32m[SUCCESS] " + getTestMethodName(iTestResult)+ "\033[0m");
        }
    }

    /**
     *
     * @param iTestResult Internal test param from your testNG test instance
     */
    @Override
    public void onTestFailure(ITestResult iTestResult) {

        if(showLog){
            System.out.println("\033[1;31m[FAIL] "+getTestMethodName(iTestResult) +" is failed \033[0m");
        }
        if(takeScreenShotOnFail){
            String name = getTestMethodName(iTestResult) + "_FAILED_" + rand.getTime("-");
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File(cfg.REPORTS_DIR + "/" + name + ".png"));
                System.out.println("\033[1;31m[FAIL] Screen shot saved:  " + cfg.REPORTS_DIR +"/"+ name + ".png \033[0m");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *
     * @param iTestResult Internal test param from your testNG test instance
     */
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if(showLog){
            System.out.println("\033[1;33m[SKIPPED] "+getTestMethodName(iTestResult)+ "\033[0m");
        }
    }

    /**
     *
     * @param iTestResult Internal test param from your testNG test instance
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    /**
     * Global Test Class params
     * @param iTestContext data test param from your testNG test instance
     *                     ( for example, data from dataProvider can be handled from here);
     */
    @Override
    public void onStart(ITestContext iTestContext) {

        System.out.println("[START] " + iTestContext.getStartDate());
    }

    /**
     * Global Test Class params
     * @param iTestContext data test param from your testNG test instance
     *                     ( for example, data from dataProvider can be handled from here);
     */
    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("[FINISH] " + iTestContext.getEndDate());
    }

    /**
     * Coverage-method for get string of current executed test-method;
     * @param result internal test result data;
     * @return method string name
     */
    private static String getTestMethodName(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getName();
    }

}
