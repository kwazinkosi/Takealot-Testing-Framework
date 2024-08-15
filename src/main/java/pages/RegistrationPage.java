package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;

/**
 * RegistrationPage handles actions and validations on the registration page.
 */
public class RegistrationPage extends BasePage {

    @FindBy(name = "first_name")
    private WebElement firstname;

    @FindBy(name = "last_name")
    private WebElement lastname;
    
    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "new_password")
    private WebElement passwordInput;

    @FindBy(id = "registerButton")
    private WebElement registerButton;

    @FindBy(className = "error")
    private List<WebElement> registrationErrors;
    
    @FindBy(xpath = "//button[@class='modal-module_close-button_asjao']")
    private WebElement closeSignupModal;
  
    @FindBy(xpath = "//button[@class='backdrop modal-module_backdrop_1x_BI']")
    private WebElement signupModal;
    
    @FindBy(className ="verify-otp-modal")
    private WebElement otpModal;
    
    // Constructor
    public RegistrationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Types the firstname into the firstname field.
     *
     * @param firstname the firstname to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeFirstname(String firstname) {
    	sendKeys(this.firstname, firstname);
        return this;
    }

    /**
     * Types the lastname into the lastname field.
     *
     * @param lastname the lastname to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeLastname(String lastname) {
    	sendKeys(this.lastname, lastname);
        return this;
    }

    /**
     * Types the email into the email field.
     *
     * @param email the email to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeEmail(String email) {
        sendKeys(emailInput, email);
        return this;
    }

    /**
     * Types the password into the password field.
     *
     * @param password the password to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typePassword(String password) {
        sendKeys(passwordInput, password);
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
     * @param firstname the firstname to enter
     * @param email the email to enter
     * @param password the password to enter
     * @param lastname the lastname to enter
     * @param pageClass the class of the page to navigate to after a successful registration
     * @param <T> the type of the page to return
     * @return the page object representing the page after successful registration
     */
    public <T> T registerAs(String firstname, String lastname, String email, String password, String confirmPassword, Class<T> pageClass) {
        
		typeFirstname(firstname)
							.typeLastname(lastname)
							.typeEmail(email)
							.typePassword(password);
		
		return submitRegistration(pageClass);
    }
    
    /**
     * Retrieves the error messages displayed on the registration page.
     *
     * @return a list of error messages
     */
    public List<String> getErrorMessages() {
        return registrationErrors
				        		.stream()
					            .map(WebElement::getText)
					            .collect(Collectors.toList());
    }

    /**
     * Checks if the registration error is displayed.
     *
     * @return true if the error is displayed, false otherwise
     */
    public boolean isRegistrationErrorDisplayed() {
        return !registrationErrors.isEmpty();
    }

    /**
     * Checks if the OTP modal is displayed.
     *
     * @return true if the OTP modal is visible, false otherwise
     */
    public boolean isOtpModalVisible() {
        try {
            waitUtil.waitForElementToBeVisible(otpModal, 10);
            return otpModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("OTP modal not visible.", e);
            return false;
        }
    }

    public boolean isSignupVisible() {
        try {
            waitUtil.waitForElementToBeVisible(signupModal, 10);
            return signupModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("Signup modal not visible.", e);
            return false;
        }
    }

    public void closeSignup() {
        try {
            waitUtil.waitForElementToBeClickable(closeSignupModal, 20);
            closeSignupModal.click();
            LoggingManager.info("Signup modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close signup modal.", e);
        }
    }

}
