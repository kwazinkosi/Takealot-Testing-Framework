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
     * This method may navigate to a different page, so it returns a generic Page object.
     *
     * @return An instance of the page navigated to after login.
     */
    public <T> T submitLogin(Class<T> pageClass) {
        loginButton.click();
        // Optionally wait for the new page to load and return the page object
        return PageFactory.initElements(driver, pageClass);
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
     * This method returns a page object of the page navigated to after login.
     *
     * @param username The username for login.
     * @param password The password for login.
     * @param pageClass The class of the page expected after login.
     * @param <T> The type of the page expected after login.
     * @return An instance of the page navigated to after login.
     */
    public <T> T loginAs(String username, String password, Class<T> pageClass) {
        typeUsername(username);
        typePassword(password);
        return submitLogin(pageClass);
    }
}
