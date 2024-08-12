package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * RegistrationPage handles actions and validations on the registration page.
 */
public class RegistrationPage extends BasePage {

    @FindBy(id = "usernameInput")
    private WebElement usernameInput;

    @FindBy(id = "emailInput")
    private WebElement emailInput;

    @FindBy(id = "passwordInput")
    private WebElement passwordInput;

    @FindBy(id = "confirmPasswordInput")
    private WebElement confirmPasswordInput;

    @FindBy(id = "registerButton")
    private WebElement registerButton;

    @FindBy(id = "registrationError")
    private WebElement registrationError;

    // Constructor
    public RegistrationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Types the username into the username field.
     *
     * @param username the username to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    /**
     * Types the email into the email field.
     *
     * @param email the email to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeEmail(String email) {
        emailInput.sendKeys(email);
        return this;
    }

    /**
     * Types the password into the password field.
     *
     * @param password the password to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typePassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    /**
     * Types the password into the confirm password field.
     *
     * @param password the password to confirm
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeConfirmPassword(String password) {
        confirmPasswordInput.sendKeys(password);
        return this;
    }

    /**
     * Clicks the register button to submit the registration form.
     *
     * @return a page object representing the next page after registration
     */
    public <T> T submitRegistration(Class<T> pageClass) {
        registerButton.click();
        // Add logic to wait for the next page or handle redirection if necessary
        return PageFactory.initElements(driver, pageClass);
    }

    /**
     * Submits the registration form and expects registration to fail.
     *
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage submitRegistrationExpectingFailure() {
        registerButton.click();
        // Add logic to wait for the error message or handle redirection if necessary
        return this;
    }

    /**
     * Performs the registration with the provided user details.
     *
     * @param username the username to enter
     * @param email the email to enter
     * @param password the password to enter
     * @param confirmPassword the password to confirm
     * @param pageClass the class of the page to navigate to after a successful registration
     * @param <T> the type of the page to return
     * @return the page object representing the page after successful registration
     */
    public <T> T registerAs(String username, String email, String password, String confirmPassword, Class<T> pageClass) {
        typeUsername(username);
        typeEmail(email);
        typePassword(password);
        typeConfirmPassword(confirmPassword);
        return submitRegistration(pageClass);
    }
    
    /**
     * Checks if the registration error is displayed.
     *
     * @return true if the error is displayed, false otherwise
     */
    public boolean isRegistrationErrorDisplayed() {
        return registrationError.isDisplayed();
    }
}
