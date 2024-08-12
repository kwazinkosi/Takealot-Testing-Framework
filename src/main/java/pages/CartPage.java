package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import components.Product;

import java.util.List;

/**
 * Page Object Model for the Cart Page.
 * This class provides methods to interact with the cart page elements.
 */
public class CartPage extends BasePage {

    private WebDriver driver;

    // Constructor to initialize the CartPage with the WebDriver instance
    public CartPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
        
    // Locators for the cart items
    @FindBy(className = "cart-item-container-module_item_3Vkqc")
    private List<WebElement> cartItems;

    @FindBy(css = "button.checkout-button")
    private WebElement checkoutButton;

    @FindBy(css = "div.total-price")
    private WebElement totalPrice;

    @FindBy(css = "button.clear-cart-button")
    private WebElement clearCartButton;

    /**
     * Gets the list of products in the cart.
     *
     * @return a list of Product components representing the items in the cart.
     */
    public List<Product> getCartItems() {
        return cartItems.stream()
                .map(Product::new)
                .toList();
    }

    /**
     * Clicks the checkout button to proceed to checkout.
     *
     * @return the CheckoutPage object for further interactions.
     */
    public CheckoutPage proceedToCheckout() {
        checkoutButton.click();
        return new CheckoutPage(driver);
    }

    /**
     * Gets the total price of the items in the cart.
     *
     * @return the total price as a String.
     */
    public String getTotalPrice() {
        return totalPrice.getText();
    }

    /**
     * Clears all items in the cart.
     */
    public void clearCart() {
        clearCartButton.click();
    }

    /**
     * Verifies if the cart is empty.
     *
     * @return true if the cart is empty, otherwise false.
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}

