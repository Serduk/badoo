package test;

import core.configs.ConfigLoader;
import core.csvUtils.WorkWithCSV;
import core.random.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract init class most used tools;
 * Created by serdyuk on 6/25/16.
 */
public abstract class AbstractTestScenario {


    protected ConfigLoader config;
    protected RandomUtils random;
    protected WorkWithCSV csvWorker;
//    protected PhoenixHelper helper;
//    protected DatabaseUtils db;

    public AbstractTestScenario(){

        config      = new ConfigLoader();
        csvWorker   = new WorkWithCSV();
        random      = new RandomUtils();
//        helper      = new PhoenixHelper();
//        db          = new DatabaseUtils();

    }


    public void switchTab(WebDriver driver, int tabNumber) throws Exception {

        List<String> tabs = driver.getWindowHandles().stream().collect(Collectors.toList());
        int totalTabs = tabs.size();
        if(tabNumber > totalTabs){
            throw new Exception("Cant switch to tab " + tabNumber+ ", there only " + totalTabs + " exist");
        }else{
            Keys key = null;
            switch (tabNumber){
                case 1 : key = Keys.NUMPAD1;break;
                case 2 : key = Keys.NUMPAD2;break;
                case 3 : key = Keys.NUMPAD3;break;
                case 4 : key = Keys.NUMPAD4;break;
            }
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, key));
            driver.switchTo().window(tabs.get(tabNumber-1));
        }
    }

    public void addNewTab(WebDriver driver) throws Exception {
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        Thread.sleep(1000);
    }

    public String urlToString(String url){

        String[] urlArr = url.split("://");
        return urlArr[1].replace(".","_");
    }

    public void tearDown(WebDriver driver){
        driver.quit();
    }

}
