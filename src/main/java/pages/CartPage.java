package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import components.CartItem;
import logging.LoggingManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object Model for the Cart Page.
 * This class provides methods to interact with the cart page elements.
 */
public class CartPage extends BasePage {

    // Locators for the cart items
    @FindBy(xpath ="//div[@data-ref='cart']")
    private WebElement cart;

    @FindBy(className = "cart-item-module_product-anchor_g4hEN")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//a[normalize-space()='Proceed to Checkout']")
    private WebElement checkoutButton;

    @FindBy(css = "p[class='cart-summary-module_cart-summary-items-cost_2gFl4'] span[class='currency plus currency-module_currency_29IIm']")
    private WebElement totalPrice;

    // Constructor to initialize the CartPage with the WebDriver instance
    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets the list of products in the cart.
     *
     * @return a list of Product components representing the items in the cart.
     */
    public List<CartItem> getCartItems() {
        return cartItems.stream()
                        .map(CartItem::new)
                        .collect(Collectors.toList());
    }

    /**
     * Clicks the checkout button to proceed to checkout.
     *
     * @return the CheckoutPage object for further interactions.
     */
    public CheckoutPage proceedToCheckout() {
    	
    	LoggingManager.info("going to checkout.");
        waitUtil.waitForElementToBeClickable(checkoutButton, normalWaitTime);
        checkoutButton.click();
        return new CheckoutPage(driver);
    }

    /**
     * Gets the total price of the items in the cart.
     *
     * @return the total price as a BigDecimal.
     */
    public BigDecimal getTotalCartPrice() {
        return getCartItems().stream()
                             .map(CartItem::getPrice)
                             .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifies if the cart is empty.
     *
     * @return true if the cart is empty, otherwise false.
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Checks if the cart is visible.
     *
     * @return true if the cart is visible, otherwise false.
     */
    @Override
    public boolean isVisible() {
        try {
        	LoggingManager.info("Checking cart visibility.");
            waitUtil.waitForElementToBeVisible(cart, normalWaitTime);
            return cart.isDisplayed();
            
        } catch (Exception e) {
            LoggingManager.error("Cart not visible.", e);
            return false;
        }
    }
}
