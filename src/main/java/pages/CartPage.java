package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;

import components.CartItem;
import logging.LoggingManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Page Object Model for the Cart Page. This class provides methods to interact
 * with the cart page elements.
 */
public class CartPage extends BasePage{

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
    
    @FindBy(xpath = "//div[@class='Toastify__toast Toastify__toast--success toast cursor-default']")
    private WebElement successAlert;

    private List<CartItem> cartItemsList;

    // Constructor to initialize the CartPage with the WebDriver instance
    public CartPage(WebDriver driver) {
        super(driver);
        this.cartItemsList = new ArrayList<>();
    }

    /**
     * Initializes or refreshes the cart items list by fetching current elements from the page.
     */
    private void initUpdateCartItems() {
    	
    	LoggingManager.info("Initializing or updating cart items list.");
        if (cartItems == null || cartItems.isEmpty()) {
            cartItemsList.clear();
            throw new NoSuchElementException("No cart items found.");
        } else {
        	
            cartItemsList = cartItems.stream()
                .map(element -> {
                    CartItem cartItem = new CartItem(element);
                    cartItem.initializeCartItem(); // Initialize the CartItem with its data
                    return cartItem;
                })
                .collect(Collectors.toList());
        }
    }

    /**
     * Gets the list of products in the cart, initializing or updating the list if necessary.
     *
     * @return a list of CartItem components representing the items in the cart.
     */
    public List<CartItem> getCartItems() {
    	LoggingManager.info("Retrieving cart items.");
        try {
        	initUpdateCartItems();
        }
        catch (NoSuchElementException e) {
        	
        }
        return cartItemsList;
    }

    /**
     * Gets the cart component.
     *
     * @param condition The condition to filter the cart item.
     * @return The cart item with the first matching condition, or null if none match.
     */
    public CartItem getCartItem(Predicate<CartItem> condition) {
        return getCartItems().stream().filter(condition).findFirst().orElse(null);
    }

    /**
     * Gets the item specified, from the cart.
     *
     * @param productName the name of the product.
     * @return The CartItem matching the specified product name, or null if not found.
     */
    public CartItem getCartItem(String productName) {
        
    	if (!isVisible()) {
            getNavBar().clickNavLink("Cart");
            waitUtil.waitForElementToBeVisible(cart, normalWaitTime);
        }

        String normalizedProductName = productName.trim().toLowerCase();

        // Log the entire list before filtering
        List<CartItem> items = getCartItems();
        LoggingManager.info("Total Cart Items: " + items.size());
        
        items.forEach(item -> 
            LoggingManager.info("Cart Item: " + item.getProductName().trim().toLowerCase())
        );

        // Filtering with normalized product names
        CartItem foundItem = items.stream()
                .filter(item -> item.getProductName().trim().toLowerCase().contains(normalizedProductName))
                .findFirst()
                .orElse(null);
        
        if (foundItem == null) {
        	LoggingManager.warn("Product not found in the cart: " + productName);
        } else {
            LoggingManager.info("Found Product: " + foundItem.getProductName());
        }
        
        return foundItem;
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
        } catch (NoSuchElementException e) {
        	LoggingManager.info("Cart empty element not found: " + e.getMessage());
            return true; // Consider the cart empty if the element is not found
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
            waitUtil.waitForElementToBeVisible(cart, fastWaitTime);
            return cart.isDisplayed();
        } catch (Exception e) {
        	LoggingManager.info("Error checking cart visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a cart item by a given condition.
     *
     * @param condition The condition to filter the cart item.
     * @return An Optional containing the cart item if found, or empty if not found.
     */
    public Optional<CartItem> findCartItem(Predicate<CartItem> condition) {
        return getCartItems().stream().filter(condition).findFirst();
    }

    /**
     * Gets the CartItem by its name.
     *
     * @param productName The name of the product.
     * @return The CartItem matching the specified product name, or null if not found.
     */
    public CartItem getCartItemByName(String productName) {
        return findCartItem(item -> productName.equalsIgnoreCase(item.getProductName())).orElse(null);
    }

	@Override
	public boolean isAlertVisible() {
		
		try {
        	LoggingManager.info("Checking success alert visibility.");
            waitUtil.waitForElementToBeVisible(successAlert, normalWaitTime);
            return successAlert.isDisplayed();
        } catch (Exception e) {
        	LoggingManager.info("Error checking alert visibility: " + e.getMessage());
            return false;
        }
	}

	
}
