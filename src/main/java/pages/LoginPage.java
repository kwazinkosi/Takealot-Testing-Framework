package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;

/**
 * LoginPage handles the actions and validations on the login page.
 */
public class LoginPage extends BasePage {

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(name="email")
    private WebElement emailInput;

    @FindBy(xpath ="//button[normalize-space()='Login']")
    private WebElement loginButton;

    @FindBy(className = "error")
    private List<WebElement> loginErrors;
    
    @FindBy(xpath = "//div[@data-ref='modal-auth-modal']")
    private WebElement loginModal;
    
    @FindBy(xpath = "//button[@data-ref='modal-close-button']")
    private WebElement closeLoginButton;

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Types the email into the email field.
     *
     * @param email The email to be entered.
     * @return The current LoginPage instance.
     */
    public LoginPage typeUsername(String email) {
        sendKeys(emailInput, email);
        return this;
    }

    /**
     * Types the password into the password field.
     *
     * @param password The password to be entered.
     * @return The current LoginPage instance.
     */
    public LoginPage typePassword(String password) {
        
    	sendKeys(passwordInput, password);
        return this;
    }

    /**
     * Submits the login form.
     * This method may navigate to a different page, so it returns a BasePage or specific page type.
     *
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage submitLogin() {
        
    	if (!loginButton.getAttribute("class").contains("disabled")) {
            loginButton.click();
            return new HomePage(driver); //TODO: return the signedInHomepage
        } else {
            LoggingManager.warn("Login button is disabled. Cannot submit.");
            return this;
        }
    }


    /**
     * Submits the login form and expects a failure.
     *
     * @return The current LoginPage instance.
     */
    public LoginPage submitLoginExpectingFailure() {
        
    	loginButton.submit();
        return this;
    }

    /**
     * Retrieves the error messages displayed on the login page.
     *
     * @return a list of error messages
     */
    public List<String> getErrorMessages() {
        return loginErrors
		        		.stream()
			            .map(WebElement::getText)
			            .collect(Collectors.toList());
    }
    /**
     * Logs in using the provided email and password.
     * This method returns a BasePage instance of the page navigated to after login.
     *
     * @param email The email for login.
     * @param password The password for login.
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage loginAs(String email, String password) {
        
    	typeUsername(email);
        typePassword(password);
        return submitLogin();
    }

	public boolean isLoginVisible() {
		
		
		try {
            waitUtil.waitForElementToBeVisible(loginModal, 10);
            return loginModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("login modal not visible.", e);
            return false;
        }
	}
	
	public void closeLogin() {
		
        try {
            waitUtil.waitForElementToBeClickable(closeLoginButton, 20);
            closeLoginButton.click();
            LoggingManager.info("Login modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close login modal.", e);
        }
    }
}
