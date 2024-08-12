package components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a product card component on the page.
 * This class provides methods to interact with the product card's elements.
 */
public class Product extends BaseComponent {

    // Locator for the product name
    @FindBy(xpath = "//h4[@class='product-title'] | //*[@class='product-title']/div/div/h1")
    private WebElement productName;

    // Locator for the product price
    @FindBy(css = "[data-ref='price'] span.currency-module_currency_29IIm")
    private WebElement productPrice;

    // Locator for the "Add to Cart" button
    @FindBy(css = "button.add-to-cart-button-module_add-to-cart-button_1a9gT")
    private WebElement addToCartBtn;

    // Locator for the "Show All Options" button
    @FindBy(css = "div .shop-all-options")
    private WebElement showAllOptions;

    // Locator for the expanded color selection dropdown
    @FindBy(xpath = "//div[@data-ref='select-dropdown']")
    private WebElement selectColorOptionExpanded;

    // Locator for the mini cart icon count
    @FindBy(css = "div.mini-cart div[data-ref='badge-count']")
    private WebElement miniCartIconCount;

    // Locator for the "Add to Wishlist" button in the buybox
    @FindBy(css = "div.buybox-button button.wishlist-button")
    private WebElement addToWishlistBtn;

    // Locator for the product rating wrapper
    @FindBy(className ="rating-module_rating-wrapper_3Cogb")
    private WebElement rating;

    // Locator for the number of raters
    @FindBy(className ="rating-module_review-count_3g6zO")
    private WebElement ratersCount;

    // Locator for the wish list button
    @FindBy(css = "a.wish")
    private WebElement goToWishlist;

    
//    private boolean isAddedToCart = false;
    /**
     * Constructor to initialize the product component.
     * 
     * @param root The root WebElement of the product component.
     */
    public Product(WebElement root) {
        super(root);
    }

    /**
     * Gets the name of the product.
     * 
     * @return The product name as a String.
     */
    public String getName() {
        return productName.getText();
    }

    /**
     * Gets the price of the product.
     * 
     * @return The product price as a BigDecimal.
     */
    public BigDecimal getPrice() {
        String priceText = productPrice.getText().replace("R", ""); // Replace currency symbol
        return new BigDecimal(priceText).setScale(2, RoundingMode.UNNECESSARY); // Convert to BigDecimal
    }

    /**
     * Gets the rating details of the product.
     * 
     * @return An instance of RatingDetails containing the rating and the number of raters.
     */
    public RatingDetails getRatingDetails() {
        // Extract and convert the rating and raters count
        String ratingText = rating.getText().replace('"', ' ').trim();
        float ratingValue = Float.parseFloat(ratingText);
        int ratersCountValue = Integer.parseInt(ratersCount.getText().replaceAll("[^0-9]", ""));

        return new RatingDetails(ratingValue, ratersCountValue);
    }

    /**
     * Adds the product to the cart.
     * If the "Add to Cart" button is not directly visible, clicks the "Show All Options" button first.
     */
    public Product addToCart() {
    	
//    	int currentProductsInCart = Integer.parseInt(miniCartIconCount.getText().trim());
        if(isVisible(addToCartBtn)) {
            addToCartBtn.click();
            
            return this;
        }
        
        showAllOptions.click();
        return this;         
    }
}
