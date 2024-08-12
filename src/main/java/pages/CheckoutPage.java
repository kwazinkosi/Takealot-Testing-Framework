package pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CheckoutPage handles the actions and validations on the checkout page.
 */
public class CheckoutPage extends BasePage {

    @FindBy(id = "proceedToCheckoutButton")
    private WebElement proceedToCheckoutButton;

    @FindBy(id = "loginForm")
    private WebElement loginForm;

    private LoginPage loginPage;

    // Constructor
    public CheckoutPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.loginPage = new LoginPage(driver);
    }

    /**
     * Clicks the "Proceed to Checkout" button.
     * If the login form appears, logs in the user using the LoginPage.
     *
     * @param email    the email to use for login
     * @param password the password to use for login
     * @return CheckoutPage instance if login is successful; otherwise, throws an exception
     */
    public CheckoutPage proceedToCheckout(String email, String password) {
        proceedToCheckoutButton.click();

        if (isVisible(loginForm)) {
            // Login and return the page object that follows successful login
            LoginPage loginPage = new LoginPage(driver);
            CheckoutPage checkoutPage = loginPage.loginAs(email, password, CheckoutPage.class);

            if (checkoutPage == null) {
                throw new RuntimeException("Login failed or redirection to CheckoutPage failed");
            }

            return checkoutPage;
        }

        return this;
    }

}
