package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;

import components.CartItem;
import logging.LoggingManager;
import wait.WaitUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Page Object Model for the Cart Page. This class provides methods to interact
 * with the cart page elements.
 */
public class CartPage extends BasePage {

	@FindBy(xpath = "//div[@data-ref='cart']")
	private WebElement cart;

	@FindBy(className = "cart-item-module_item-body_23ZTr")
	private List<WebElement> cartItems;

	@FindBy(className = "empty-list-message-module_panel_EdYDK")
	private WebElement emptyCart;

	@FindBy(xpath = "//a[normalize-space()='Proceed to Checkout']")
	private WebElement checkoutButton;

	@FindBy(xpath = "//p[contains(@data-ref,'cart-summary-items-cost')]//span")
	private WebElement totalPrice;

	public List<CartItem> cartItemsList;

	// Constructor to initialize the CartPage with the WebDriver instance
	public CartPage(WebDriver driver) {
		super(driver);

		cartItemsList = getCartProducts();
	}

	/**
	 * Gets the cart component.
	 *
	 * @param condition
	 * @return The cart component with the first matching condition
	 */
	public CartItem getCartItem(Predicate<CartItem> condition) {
		return cartItemsList.stream().filter(condition).findFirst().orElse(null);
	}

	/**
	 * Gets the list of products in the cart.
	 *
	 * @return a list of CartItem components representing the items in the cart.
	 */
	public List<CartItem> getCartProducts() {
		LoggingManager.info("Retrieving cart items.");

		return cartItems.stream().map(CartItem::new).collect(Collectors.toList());
	}

	/**
	 * Gets the list of products in the cart.
	 *
	 * @return a list of CartItem components representing the items in the cart.
	 */
	public List<CartItem> getCartItems() {
		LoggingManager.info("Retrieving cart items.");

		return cartItemsList.isEmpty() ? cartItems.stream().map(CartItem::new).collect(Collectors.toList())
				: cartItemsList;
	}

	/**
	 * Gets the item specified, from the cart.
	 *
	 * @param productName the name of the product
	 * @return a list of CartItem components representing the items in the cart.
	 */
	public CartItem getCartItem(String productName) {

		CartItem cartItem = getCartItems().stream().filter(item -> productName.equals(item.getProductName()))
				.findFirst().orElse(null);

		return cartItem;
	}

	/**
	 * Clicks the checkout button to proceed to checkout.
	 *
	 * @return the CheckoutPage object for further interactions.
	 */
	public CheckoutPage proceedToCheckout() {

		LoggingManager.info("Clicking 'Proceed to Checkout' button.");
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
		LoggingManager.info("Calculating total cart price.");
		return getCartItems().stream()
				.map(cartItem -> cartItem.getPrice() != null ? cartItem.getPrice() : BigDecimal.ZERO)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * Verifies if the cart is empty.
	 *
	 * @return true if the cart is empty, otherwise false.
	 */
	public boolean isCartEmpty() {
		try {
			LoggingManager.info("Checking if the cart is empty.");
			return isVisible(emptyCart);
		} catch (Exception e) {
			LoggingManager.info("Error checking if the cart is empty: " + e.getMessage());
		}
		return false;
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
			LoggingManager.info("Error checking cart visibility: " + e.getMessage());
			return false;
		}
	}
}
