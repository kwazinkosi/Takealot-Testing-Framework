package components;

import pages.BasePage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import interfaces.ICart;
import utilities.DriverFactory;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the cart overlay component that appears when the cart is accessed from the navigation bar.
 * Provides methods to interact with cart items, proceed to checkout, and navigate to the cart page.
 */
public class CartOverlay extends BaseComponent implements ICart {

    @FindBy(xpath = "//div[@class='cart-module_item_3MErs'] | //div[@class ='cart-item-container-module_item_3Vkqc']")
    private List<WebElement> cartItems;

    @FindBy(css = ".checkout-button[data-ref='checkout-button']")
    private WebElement checkoutBtn;

    @FindBy(name = "login-form")
    private WebElement loginForm;

    @FindBy(css = ".button.cart.pay")
    private WebElement goToCart;

    @FindBy(css = ".drawer-module_drawer-outer_3hLuY[data-ref='drawer-outer']")
    private WebElement cartOverlay;

    /**
     * Constructor to initialize the cart overlay component.
     *
     * @param root The root WebElement of the cart overlay component.
     */
    public CartOverlay(WebElement root) {
        super(root);
    }

    /**
     * Retrieves the list of cart items currently in the cart overlay.
     * 
     * @return A list of WebElements representing the cart items.
     */
    @Override
    public List<WebElement> getCartItemElements() {
        return cartItems;
    }

    /**
     * Navigates to the Cart Page by clicking the 'Go to Cart' button in the cart overlay.
     *
     * @return A new instance of the CartPage.
     */
    @Override
    public CartPage goToCartPage() {
        goToCart.click();
        return new CartPage(DriverFactory.getDriver());
    }

    /**
     * Proceeds to the checkout process by clicking the 'Checkout' button.
     * 
     * @return A new instance of the LoginPage if the login form appears, otherwise a new instance of the CheckoutPage.
     */
    @Override
    public BasePage proceedToCheckout() {
        checkoutBtn.click();
        if (isVisible(loginForm)) {
            // If login form is visible, return a LoginPage
            return new LoginPage(DriverFactory.getDriver());
        }
        // If no login form is present, proceed to the CheckoutPage
        return new CheckoutPage(DriverFactory.getDriver());
    }

    /**
     * Checks if the cart overlay is currently visible on the page.
     *
     * @return true if the cart overlay is visible, false otherwise.
     */
    public boolean isOverlayVisible() {
        return isVisible(cartOverlay);
    }
}
