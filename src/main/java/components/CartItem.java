package components;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    @FindBy(css = ".cart-item-module_total_hR9wF span.currency.plus.currency-module_currency_29IIm")
    private WebElement priceElement;

    @FindBy(className = "cart-item-module_item-title_1M9cq")
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
    }
    
    /**
     * Initializes the CartItem attributes based on the WebElement state.
     * Retrieves the product name and price from the WebElement and handles any parsing or retrieval issues.
     */
    public void initializeCartItem() {
    	if(this.productName !=null && this.price !=null) {
    		return;
    	}
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
     */
    private BigDecimal parsePrice(String priceText) {
        String cleanedPrice = priceText.replace("R", "").replace(",", "").trim();
        LoggingManager.info("Extracted price text: '" + cleanedPrice + "'");

        if (!cleanedPrice.isEmpty() && isNumeric(cleanedPrice)) {
            return new BigDecimal(cleanedPrice).setScale(2, RoundingMode.HALF_UP);
        } else {
            LoggingManager.warn("Price text is empty or not a valid number: '" + cleanedPrice + "'");
            return BigDecimal.ZERO;
        }
    }

    private boolean isNumeric(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
            click(moveToWishlistBtn);
            waitUtil.waitImplicitly(5);
            LoggingManager.info("Moved item from the cart to wish list.");
        } catch (Exception e) {
            LoggingManager.info("Error moving item to wishlist: " + e.getMessage());
        }
    }
}
