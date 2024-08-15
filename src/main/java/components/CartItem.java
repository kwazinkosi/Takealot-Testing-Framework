package components;

import java.math.BigDecimal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartItem extends BaseComponent {

    @FindBy(className = "remove-item")
    public WebElement removeBtn;

    @FindBy(className = "move-to-wishlist")
    public WebElement moveToWishlistBtn;

    @FindBy(className = "stock-availability-status")
    public WebElement stockAvailability;
    
    @FindBy(className = "item-price")
    public WebElement priceElement;

    @FindBy(className = "item-name")
    public WebElement productNameElement;

    private BigDecimal price;
    private String productName;

    /**
     * Constructor to initialize the CartItem component.
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
     */
    private void initializeCartItem() {
        this.productName = productNameElement.getText();
        this.price = new BigDecimal(priceElement.getText().replace("$", ""));
        // Additional initializations as needed
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isInStock() {
        return stockAvailability.isDisplayed() && stockAvailability.getText().equalsIgnoreCase("In Stock");
    }

    /**
     * Sets CartItem attributes from the provided Product object.
     * 
     * @param product The Product object to copy attributes from.
     */
    public void setCartAttributes(Product product) {
        
    	this.productName = product.getName();
        this.price = product.getPrice();
    }

    /**
     * Removes the item from the cart.
     */
    public void removeFromCart() {
        removeBtn.click();
    }

    /**
     * Moves the item to the wishlist.
     */
    public void moveToWishlist() {
        moveToWishlistBtn.click();
    }
}
