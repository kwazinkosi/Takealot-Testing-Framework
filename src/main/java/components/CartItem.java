package components;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;

/**
 * Represents an item in the shopping cart.
 * Provides methods to interact with and retrieve information about the cart item.
 */
public class CartItem extends BaseComponent {

    @FindBy(className = "remove-item")
    private WebElement removeBtn;

    @FindBy(className = "move-to-wishlist")
    private WebElement moveToWishlistBtn;

    @FindBy(className = "stock-availability-status")
    private WebElement stockAvailability;

    @FindBy(className = "item-price")
    private WebElement priceElement;

    @FindBy(className = "item-name")
    private WebElement productNameElement;

    private BigDecimal price;
    private String productName;

    /**
     * Constructor to initialize the CartItem component.
     * Initializes the WebElements using PageFactory and sets the initial attributes of the cart item.
     * 
     * @param root The root WebElement of the product component.
     */
    public CartItem(WebElement root) {
        super(root);
        PageFactory.initElements(root, this);
        initializeCartItem();
    }

    /**
     * Initializes the CartItem attributes based on the WebElement state.
     * Retrieves the product name and price from the WebElement and handles any parsing or retrieval issues.
     */
    private void initializeCartItem() {
        try {
            this.productName = productNameElement.getText();
            this.price = parsePrice(priceElement.getText());
        } catch (Exception e) {
            LoggingManager.info("Error initializing CartItem: " + e.getMessage());
            this.productName = "Unknown";
            this.price = BigDecimal.ZERO;
        }
    }

    /**
     * Parses the price text to a BigDecimal.
     * Handles different currency formats and locale-specific parsing.
     * 
     * @param priceText The price text to parse.
     * @return The parsed price as a BigDecimal.
     * @throws ParseException If parsing the price text fails.
     */
    private BigDecimal parsePrice(String priceText) throws ParseException {
        
    	NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        Number number = format.parse(priceText);
        return new BigDecimal(number.toString());
    }

    /**
     * Retrieves the name of the product.
     * 
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Retrieves the price of the product.
     * 
     * @return The price of the product as a BigDecimal.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Checks if the item is in stock.
     * 
     * @return true if the item is in stock, otherwise false.
     */
    public boolean isInStock() {
        try {
            return stockAvailability.isDisplayed() && stockAvailability.getText().equalsIgnoreCase("In Stock");
        } catch (Exception e) {
            LoggingManager.info("Error checking stock availability: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sets the attributes of the cart item based on a Product object.
     * 
     * @param product The Product object containing attributes to copy.
     */
    public void setCartAttributes(Product product) {
        this.productName = product.getName();
        this.price = product.getPrice();
    }

    /**
     * Removes the item from the cart.
     * Clicks the remove button to delete the item from the cart.
     */
    public void removeFromCart() {
        try {
            removeBtn.click();
            LoggingManager.info("Removed item from the cart");
        } catch (Exception e) {
            LoggingManager.info("Error removing item from cart: " + e.getMessage());
        }
    }

    /**
     * Moves the item to the wishlist.
     * Clicks the move to wishlist button to transfer the item to the user's wishlist.
     */
    public void moveToWishlist() {
        try {
            moveToWishlistBtn.click();
            LoggingManager.info("Moved item from the cart to wish list.");
        } catch (Exception e) {
            LoggingManager.info("Error moving item to wishlist: " + e.getMessage());
        }
    }
}
