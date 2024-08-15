package pages;

import java.util.List;
import java.util.function.Function;
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

    @FindBy(name = "mobile_national_number")
    private WebElement mobileNumber;
    
    @FindBy(xpath = "//button[normalize-space()='Continue']")
    private WebElement registerButton;

    @FindBy(className = "error")
    private List<WebElement> registrationErrors;
    
    @FindBy(xpath = "//button[@class='modal-module_close-button_asjao']")
    private WebElement closeSignupModal;
  
    @FindBy(xpath = "//button[@class='backdrop modal-module_backdrop_1x_BI']")
    private WebElement signupModal;
    
    @FindBy(className = "verify-otp-modal")
    private WebElement otpModal;
    
    @FindBy(className = "modal-module_close-button_asjao")
    private WebElement closeOtp;
    
    @FindBy(xpath = "//button[@data-ref='modal-primary-button']")
    private WebElement closeOtpConfirm;

    /**
     * Constructor for RegistrationPage.
     *
     * @param driver WebDriver instance to be used by the page
     */
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
    public RegistrationPage typeFirstname(String firstName) {
    	
        LoggingManager.info("Typing first name in the field 'first_name'");
        if (firstName == null) {
            firstName = ""; // Convert null to empty string
        }
        actionUtil.scrollToElement(firstname);
        sendKeys(firstname, firstName);
        return this;
    }

    /**
     * Types the lastname into the lastname field.
     *
     * @param lastname the lastname to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeLastname(String lastName) {
        
    	LoggingManager.info("Typing last name in the field 'last_name'");
        if (lastName == null) {
            lastName = ""; // Convert null to empty string
        }
        actionUtil.scrollToElement(lastname);
        sendKeys(lastname, lastName);
        return this;
    }

    /**
     * Types the email into the email field.
     *
     * @param email the email to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeEmail(String email) {
        
    	LoggingManager.info("Typing email in the field 'email'");
        if (email == null) {
            email = ""; // Convert null to empty string
        }
        actionUtil.scrollToElement(emailInput);
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
        
    	LoggingManager.info("Typing password in the field 'new_password'");
    	if (password == null) {
            password = ""; // Convert null to empty string
        }
    	actionUtil.scrollToElement(passwordInput);
    	sendKeys(passwordInput, password);
        return this;
    }

    /**
     * Types the mobile number into the mobile number field.
     *
     * @param mobile the mobile number to enter
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage typeMobileNumber(String mobile) {
        
    	LoggingManager.info("Typing mobile number in the field 'mobile_national_number'");
        if (mobile == null) {
            mobile = ""; // Convert null to empty string
        }
        actionUtil.scrollToElement(mobileNumber);
        sendKeys(mobileNumber, mobile);
        return this;
    }

    /**
     * Clicks the register button to submit the registration form.
     *
     * @param <T>       the type of the page to return
     * @param pageClass the class of the page to navigate to after a successful registration
     * @return a page object representing the next page after registration
     */
    public <T> T submitRegistration(Class<T> pageClass) {
        LoggingManager.info("Clicking on the register button to submit the registration form");
        registerButton.click();
        // Add logic to wait for the next page or handle redirection if necessary
        return PageFactory.initElements(driver, pageClass);
    }

    /**
     * Submits the registration form and expects registration to fail.
     *
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage submitRegistration() {
        
    	LoggingManager.info("Submitting the registration form expecting failure");
        actionUtil.scrollToElement(registerButton);
        registerButton.click();
        // Add logic to wait for the error message or handle redirection if necessary
        return this;
    }

    /**
     * Performs the registration with the provided user details.
     *
     * @param firstname the firstname to enter
     * @param lastname  the lastname to enter
     * @param email     the email to enter
     * @param password  the password to enter
     * @param pageClass the class of the page to navigate to after a successful registration
     * @param <T>       the type of the page to return
     * @return the page object representing the page after successful registration
     */
    public <T> T registerAs(String firstname, String lastname, String email, String password, Class<T> pageClass) {
        LoggingManager.info(String.format("Performing registration with firstname: %s, lastname: %s, email: %s", firstname, lastname, email));
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
        LoggingManager.info("Retrieving error messages from the registration page");

        LoggingManager.info("Waiting for error messages to be visible on the registration page");
        
        // Use the waitFor method to wait until the registration errors are visible
        waitUtil.waitFor(driver -> registrationErrors.stream().allMatch(WebElement::isDisplayed), 10, 600);
        
        LoggingManager.info("Retrieving error messages from the registration page");
        
        // Collect and return the text from the visible error messages
        return registrationErrors.stream()
                                 .map(WebElement::getText)
                                 .collect(Collectors.toList());
    }


    /**
     * Checks if the registration error is displayed.
     *
     * @return true if the error is displayed, false otherwise
     */
    public boolean isRegistrationErrorDisplayed() {
        LoggingManager.info("Checking if registration error is displayed");
        return !registrationErrors.isEmpty();
    }

    /**
     * Checks if the OTP modal is displayed.
     *
     * @return true if the OTP modal is visible, false otherwise
     */
    public boolean isOtpModalVisible() {
        try {
            LoggingManager.info("Checking if OTP modal is visible");
            waitUtil.waitForElementToBeVisible(otpModal, normalWaitTime);
            return true;
        } catch (Exception e) {
            LoggingManager.error("OTP modal not visible.", e);
            return false;
        }
    }

    /**
     * Checks if the signup modal is displayed.
     *
     * @return true if the signup modal is visible, false otherwise
     */
    public boolean isSignupVisible() {
        try {
            LoggingManager.info("Checking if signup modal is visible");
            waitUtil.waitForElementToBeVisible(signupModal, normalWaitTime);
            return signupModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("Signup modal not visible.", e);
            return false;
        }
    }

    /**
     * Closes the signup modal.
     */
    public void closeSignup() {
        try {
            LoggingManager.info("Attempting to close signup modal");
            waitUtil.waitForElementToBeClickable(closeSignupModal, normalWaitTime);
            closeSignupModal.click();
            LoggingManager.info("Signup modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close signup modal.", e);
        }
    }

    /**
     * Closes the OTP modal.
     */
    public void closeOtpModal() {
        try {
            LoggingManager.info("Attempting to close OTP modal");
            waitUtil.waitForElementToBeClickable(closeOtp, normalWaitTime);
            closeOtp.click();
            waitUtil.waitForElementToBeClickable(closeOtpConfirm, normalWaitTime);
            closeOtpConfirm.click();
            LoggingManager.info("OTP modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close OTP modal.", e);
        }
    }
}
