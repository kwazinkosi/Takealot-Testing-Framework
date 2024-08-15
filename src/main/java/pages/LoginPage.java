package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * LoginPage handles the actions and validations on the login page.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "usernameInput")
    private WebElement usernameInput;

    @FindBy(id = "passwordInput")
    private WebElement passwordInput;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    @FindBy(id = "loginError")
    private WebElement loginError;

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Types the username into the username field.
     *
     * @param username The username to be entered.
     * @return The current LoginPage instance.
     */
    public LoginPage typeUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    /**
     * Types the password into the password field.
     *
     * @param password The password to be entered.
     * @return The current LoginPage instance.
     */
    public LoginPage typePassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    /**
     * Submits the login form.
     * This method may navigate to a different page, so it returns a BasePage or specific page type.
     *
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage submitLogin() {
        
    	loginButton.click();
        // Optionally wait for the new page to load
        // Return a new instance of the expected page here or a generic BasePage
        // For now, let's assume you navigate to a Page that extends BasePage
//        return PageFactory.initElements(driver, BasePage.class); // Adjust if you know specific pages
        return new BasePage(driver);
    }

    /**
     * Submits the login form and expects a failure.
     *
     * @return The current LoginPage instance.
     */
    public LoginPage submitLoginExpectingFailure() {
        
    	loginButton.click();
        return this;
    }

    /**
     * Logs in using the provided username and password.
     * This method returns a BasePage instance of the page navigated to after login.
     *
     * @param username The username for login.
     * @param password The password for login.
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage loginAs(String username, String password) {
        
    	typeUsername(username);
        typePassword(password);
        return submitLogin();
    }
}
