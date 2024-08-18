package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;

/**
 * RegistrationPage handles actions and validations on the registration page.
 */
public class RegistrationPage extends BasePage {

    @FindBy(xpath = "//input[@id='register_customer_first_name']")
    private WebElement firstname;

    @FindBy(xpath = "//input[@id='register_customer_last_name']")
    private WebElement lastname;
    
    @FindBy(xpath = "//input[@id='register_customer_email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@id='register_customer_new_password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='register_customer_mobile_national_number']")
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
    }

    /**
     * Types the first name into the first name field.
     *
     * @param firstName The first name to enter.
     * @return The current instance of RegistrationPage.
     */
    public RegistrationPage typeFirstname(String firstName) {
        LoggingManager.info("Typing first name in the field 'first_name'");
        
        if (firstName == null) {
            firstName = " "; // Convert null to empty string
        }
        try {
            actionUtil.scrollToAndClick(firstname);
            waitUtil.waitFor(driver -> isVisible(firstname), fastWaitTime); // Wait for the firstname field to be visible
            sendKeys(firstname, firstName);
        } catch (NoSuchElementException e) {
            LoggingManager.error("The element for first name could not be found.", e);
            actionUtil.scrollBy(-100);
            sendKeys(firstname, firstName);
        }
        return this;
    }

    /**
     * Types the last name into the last name field.
     *
     * @param lastName The last name to enter.
     * @return The current instance of RegistrationPage.
     */
    public RegistrationPage typeLastname(String lastName) {
        LoggingManager.info("Typing last name in the field 'last_name'");
        
        if (lastName == null) {
            lastName = " "; // Convert null to empty string
        }
        
        actionUtil.scrollToElement(lastname);
        waitUtil.waitFor(driver -> isVisible(lastname), fastWaitTime); // Wait for the lastname field to be visible
        sendKeys(lastname, lastName);
        
        return this;
    }

    /**
     * Types the email into the email field.
     *
     * @param email The email to enter.
     * @return The current instance of RegistrationPage.
     */
    public RegistrationPage typeEmail(String email) {
        LoggingManager.info("Typing email in the field 'email'");
        
        if (email == null) {
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
     * @param password The password to enter.
     * @return The current instance of RegistrationPage.
     */
    public RegistrationPage typePassword(String password) {
        LoggingManager.info("Typing password in the field 'new_password'");
        
        if (password == null) {
            password = " "; // Convert null to empty string
        }
        
        actionUtil.scrollToElement(passwordInput);
        waitUtil.waitFor(driver -> isVisible(passwordInput), fastWaitTime); // Wait for the password field to be visible
        sendKeys(passwordInput, password);
        
        return this;
    }

    /**
     * Types the mobile number into the mobile number field.
     *
     * @param mobile The mobile number to enter.
     * @return The current instance of RegistrationPage.
     */
    public RegistrationPage typeMobileNumber(String mobile) {
        LoggingManager.info("Typing mobile number in the field 'mobile_number'");
        
        if (mobile == null) {
            mobile = " "; // Convert null to empty string
        }
        
        actionUtil.scrollToElement(mobileNumber);
        waitUtil.waitFor(driver -> isVisible(mobileNumber), fastWaitTime); // Wait for the mobile number field to be visible
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
        
        return PageFactory.initElements(driver, pageClass);
    }

    /**
     * Submits the registration form and expects registration to fail.
     *
     * @return the current instance of RegistrationPage
     */
    public RegistrationPage submitRegistration() {
        LoggingManager.info("Attempting to submit the registration form");

        // Scroll to the button
        actionUtil.scrollToElement(registerButton);

        // Check if the button class does not contain "disabled"
        String classAttribute = registerButton.getAttribute("class");
        if (classAttribute != null && !classAttribute.contains("disabled")) {
            // Click the button if it's not disabled
            registerButton.click();
            LoggingManager.info("Registration form submitted successfully");
        } else {
            LoggingManager.info("Registration button is disabled, cannot submit the form");
        }

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

        // Wait until at least one error message is visible
        waitUtil.waitFor(driver -> !registrationErrors.isEmpty() &&
                registrationErrors.stream().anyMatch(WebElement::isDisplayed), fastWaitTime);

        // Collect and return the text from the visible error messages
        return registrationErrors.stream()
                                 .filter(WebElement::isDisplayed) // Ensure the element is displayed
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
            return otpModal.isDisplayed();
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
    @Override
    public boolean isVisible() {
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

    @Override
    public boolean isAlertVisible() {
        // Placeholder for alert visibility check logic
        return false;
    }
}
