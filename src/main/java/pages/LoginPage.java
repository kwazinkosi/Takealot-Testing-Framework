package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;

/**
 * LoginPage handles the actions and validations on the login page.
 */
public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@id='customer_login_password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='customer_login_email']")
    private WebElement emailInput;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    private WebElement loginButton;

    @FindBy(className = "error")
    private List<WebElement> loginErrors;

    @FindBy(xpath = "//div[@data-ref='modal-auth-modal']")
    private WebElement loginModal;

    @FindBy(xpath = "//button[@data-ref='modal-close-button']")
    private WebElement closeLoginButton;

    @FindBy(xpath = "//div[contains(text(),'Incorrect Email or Password. Please try again and ')]")
    private WebElement incorrectEmailPasswordAlert;

    private List<String> errorsList;

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
        errorsList = new ArrayList<>(); // Initialize errorsList
        PageFactory.initElements(driver, this);
    }

    /**
     * Types the email into the email field.
     *
     * @param email The email to be entered. Converts null to an empty string.
     * @return The current LoginPage instance.
     */
    public LoginPage typeEmail(String email) {
        LoggingManager.info("Typing email in the field 'email'");

        if (email == null) {
            email = ""; // Convert null to an empty string
        }

        actionUtil.scrollToElement(emailInput);
        waitUtil.waitFor(driver -> isVisible(emailInput), normalWaitTime); // Wait for the email field to be visible
        sendKeys(emailInput, email);

        return this;
    }

    /**
     * Types the password into the password field.
     *
     * @param password The password to be entered. Converts null to an empty string.
     * @return The current LoginPage instance.
     */
    public LoginPage typePassword(String password) {
        LoggingManager.info("Typing password in the field 'password'");

        if (password == null) {
            password = ""; // Convert null to an empty string
        }

        actionUtil.scrollToElement(passwordInput);
        waitUtil.waitFor(driver -> isVisible(passwordInput), normalWaitTime); // Wait for the password field to be visible
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
            try {
                LoggingManager.info("Clicking on Login Button");
                loginButton.click();
                LoggingManager.info("Clicked on Login Button");

                if (!isErrorAlertDisplayed()) {
                    LoggingManager.info("Logged in successfully");
                    return new HomePage(driver).setLoggedIn(true); // Return the appropriate page after login
                } else {
                    try {
                        String alertText = incorrectEmailPasswordAlert.getText();
                        if (!alertText.isEmpty()) {
                            errorsList.add(alertText);
                        }
                    } catch (Exception e) {
                        LoggingManager.error("Exception occurred while retrieving incorrect email/password alert", e);
                    }
                }
            } catch (Exception e) {
                LoggingManager.error("Exception occurred during login submission", e);
            }
        } else {
            LoggingManager.warn("Login button is disabled. Cannot submit.");
        }
        return this;
    }

    /**
     * Checks if the error alert is displayed.
     *
     * @return True if the alert is visible, otherwise false.
     */
    private boolean isErrorAlertDisplayed() {
        try {
            LoggingManager.info("Checking if error alert is visible");
            waitUtil.waitForElementToBeVisible(incorrectEmailPasswordAlert, normalWaitTime);
            return true;
        } catch (TimeoutException e) {
            LoggingManager.info("Error alert not visible.");
            return false;
        }
    }

    /**
     * Logs in using the provided email and password. This method returns a BasePage
     * instance of the page navigated to after login.
     *
     * @param email    The email for login.
     * @param password The password for login.
     * @return An instance of BasePage or a specific page navigated to after login.
     */
    public BasePage loginAs(String email, String password) {
        typeEmail(email);
        typePassword(password);
        return submitLogin();
    }

    /**
     * Retrieves the error messages displayed on the login page.
     *
     * @return A list of error messages.
     */
    public List<String> getErrorMessages() {
        LoggingManager.info("Retrieving error messages from the login page");

        try {
            // Wait until at least one error message is visible
            waitUtil.waitFor(driver -> !loginErrors.isEmpty() &&
                    loginErrors.stream().anyMatch(WebElement::isDisplayed), fastWaitTime);
        } catch (TimeoutException e) {
            LoggingManager.info("No input errors found");
        }

        // Collect and return the text from the visible error messages
        errorsList.addAll(loginErrors.stream()
                                    .filter(WebElement::isDisplayed) // Ensure the error element is displayed
                                    .map(WebElement::getText)
                                    .collect(Collectors.toList()));

        return errorsList;
    }

    /**
     * Closes the login modal.
     */
    public void closeLogin() {
        try {
            waitUtil.waitForElementToBeClickable(closeLoginButton, normalWaitTime);
            closeLoginButton.click();
            LoggingManager.info("Login modal closed successfully.");
        } catch (Exception e) {
            LoggingManager.error("Failed to close login modal.", e);
        }
    }

    @Override
    public boolean isVisible() {
        try {
            waitUtil.waitForElementToBeVisible(loginModal, fastWaitTime);
            return loginModal.isDisplayed();
        } catch (Exception e) {
            LoggingManager.error("Login modal not visible.", e);
            return false;
        }
    }

    @Override
    public boolean isAlertVisible() {
        // TODO: Implement this method or remove it if not needed.
        return false;
    }
}
