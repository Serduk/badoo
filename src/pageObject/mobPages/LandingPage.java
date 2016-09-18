package pageObject.mobPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObject.AbstractPage;

/**
 * Created by serdyuk on 9/18/16.
 */
public class LandingPage extends AbstractPage{
    public LandingPage(WebDriver driver) {
        super(driver);
    }

    // /en/new-sign-up page
    @FindBy(xpath = "//*[@data-value= '1']")
    public WebElement maleBtn;

    @FindBy(xpath = "//*[@data-value= '1']")
    public WebElement femaleBtn;

    // and you are here to: /en/new-sign-up/hereto/1
    @FindBy (xpath = "//*[@data-value= '10001']")
    public WebElement makeNewFriendsBtn;

    @FindBy (xpath = "//*[@data-value= '10002']")
    public WebElement chatBtn;

    @FindBy (xpath = "//*[@data-value= '10003']")
    public WebElement dateBtn;

    //signup detail: email/location/birthDate/name
    @FindBy (xpath = "//*[@class = 'registration-details']")
    public WebElement submitForm;

    @FindBy(xpath = "//*[@id = 'signup-email']")
    public WebElement emailInpt;

    @FindBy(xpath = "//*[@id = 'signup-location']")
    public WebElement locationInpt;

    @FindBy(xpath = "//*[@id = 'input-date']")
    public WebElement dateInpt;

    @FindBy(xpath = "//*[@id = 'signup-name']")
    public WebElement nameInpt;

    @FindBy(xpath = "//*[@class= 'form__buttons']")
    public WebElement createAccountBtn;

    //Phone number page
    @FindBy(xpath = "//*[@type = 'tel']")
    public  WebElement phoneNumberInpt;

}
