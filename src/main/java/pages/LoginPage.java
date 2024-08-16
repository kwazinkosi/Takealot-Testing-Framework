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

    
    @FindBy(xpath = ("//div[contains(text(),'Incorrect Email or Password. Please try again and ')]"))
    private WebElement incorrectEmailPasswordAlert;
    
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
    public LoginPage typeEmail(String email) {
    	LoggingManager.info("Typing email in the field 'email'");
        
        if (email ==null) {
            email = " "; // Convert null to empty string
        }
        
        actionUtil.scrollToElement(emailInput);
        waitUtil.waitFor(driver -> isVisible(emailInput), 10, 500); // Wait for the email field to be visible
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
        
    	LoggingManager.info("Typing password in the field 'new_password'");
        if (password==null) {
            password = " "; // Convert null to empty string
        }
        
        actionUtil.scrollToElement(passwordInput);
        waitUtil.waitFor(driver -> isVisible(passwordInput), 10, 500); // Wait for the password field to be visible
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
        
    	actionUtil.scrollToElement(loginButton);
    	if (!loginButton.getAttribute("class").contains("disabled")) {
            
    		loginButton.click();
            if(getErrorMessages().isEmpty() && !errorAlertDisplay()) {
            	return new HomePage(driver); //TODO: return the signedInHomepage
            }
        }
        LoggingManager.warn("Login button is disabled. Cannot submit.");
        return this;

    }


	/**
     *  Checks if the error alert is displayed or not
     *
     * @return returns true if the alert is visilbe
     */
    private boolean errorAlertDisplay() {
		
    	try {
            LoggingManager.info("Checking if error alert is visible");
            waitUtil.waitForElementToBeVisible(incorrectEmailPasswordAlert, normalWaitTime);
            return true;
        } catch (Exception e) {
            LoggingManager.error("Error alert not visible.", e);
            return false;
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
     * Logs in using the provided email and password.
     * This method returns a BasePage instance of the page navigated to after login.
     *
     * @param email The email for login.
     * @param password The password for login.
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage loginAs(String email, String password) {
        
    	typeEmail(email);
        typePassword(password);
        return submitLogin();
    }
      
    /**
     * Retrieves the error messages displayed on the registration page.
     *
     * @return a list of error messages
     */
    public List<String> getErrorMessages() {
        LoggingManager.info("Retrieving error messages from the registration page");

        // Wait until at least one error message is visible
        waitUtil.waitFor(driver -> !loginErrors.isEmpty() &&
        		loginErrors.stream().anyMatch(WebElement::isDisplayed), 10, 800);

        // Collect and return the text from the visible error messages
        return loginErrors.stream()
                                 .filter(WebElement::isDisplayed) // Ensure the error element is displayed
                                 .map(WebElement::getText)
                                 .collect(Collectors.toList());
    }

    @Override
	public boolean isVisible() {
		
		
		try {
            waitUtil.waitForElementToBeVisible(loginModal, normalWaitTime);
            return loginModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("login modal not visible.", e);
            return false;
        }
	}
	
	public void closeLogin() {
		
        try {
            waitUtil.waitForElementToBeClickable(closeLoginButton, normalWaitTime);
            closeLoginButton.click();
            LoggingManager.info("Login modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close login modal.", e);
        }
    }
}
