package components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import logging.LoggingManager;
import utilities.AdOverlayListener;
import utilities.DriverFactory;
import wait.WaitUtil;

/**
 * Represents a product card component on the page.
 * This class provides methods to interact with the product card's elements.
 */
public class Product extends BaseComponent {

    @FindBy(className = "product-title")
    private WebElement productName;
    
    @FindBy (className ="product-thumb-container")
    private WebElement thumbImage;
    
    @FindBy(css = "[data-ref='price'] span.currency.currency-module_currency_29IIm")
    private WebElement productPrice;


    By sponsoredProduct = By.className("product-card-module_sponsored-listing-badge_1nHiP");
   
    @FindBy(css = "button.add-to-cart-button-module_add-to-cart-button_1a9gT[data-ref='add-to-cart-button']")
    public WebElement addToCartBtn;

    @FindBy(css = "div .shop-all-options")
    public WebElement showAllOptions;

    @FindBy(css = ".select-dropdown-module_select-dropdown_3Rysq")
    public WebElement selectColorOptionExpanded;
    
    @FindBy(className ="select-dropdown-module_list-item_2kHtk")
    public List<WebElement> options;
	

    @FindBy(css = ".badge-button-module_badge-count-wrapper_2buZm")
    public WebElement miniCartIconCount;

    @FindBy(css = "div.buybox-button button.wishlist-button")
    public WebElement addToWishlistBtn;

    @FindBy(className = "rating-module_rating-wrapper_3Cogb")
    private WebElement rating;

    @FindBy(className = "rating-module_review-count_3g6zO")
    private WebElement reviews;

    @FindBy(css = "a.wish")
    private WebElement goToWishlist;
    By cartBtn = By.cssSelector("button.add-to-cart-button-module_add-to-cart-button_1a9gT");
    

    WebElement cartBtnElement =DriverFactory.getDriver().findElement(cartBtn);
    
    private boolean isAddedToCart = false;
    private int itemsInCart = 0;
    private String product_name = "";
    private String product_type = "search";
    private BigDecimal price;
    private RatingDetails ratingDetails;
    private WaitUtil waitUtil = new WaitUtil(DriverFactory.getDriver());

    /**
     * Constructor to initialize the product component.
     * 
     * @param root The root WebElement of the product component.
     */
    public Product(WebElement root) {
        super(root);
        PageFactory.initElements(root, this);
    }

    /**
     * Initializes product details.
     */
    public Product initializeProductDetails() {
        AdOverlayListener.closeAdOverlay();
        setName();
        LoggingManager.info("Done initializing product name");
        setPrice();
        LoggingManager.info("Done initializing product price");
        setRatingDetails();
        LoggingManager.info("Done initializing product details");
        setProductType("sponsored");
        return this;
    }

    /**
     * Sets the product name.
     * 
     * @return The current Product instance.
     */
    public Product setName() {
    	
        try {
            waitUtil.waitForElementToBeVisible(thumbImage);
            String nameText = this.productName.getText();
            LoggingManager.info("Product name extracted: '" + nameText + "'");
            if (nameText != null && !nameText.isEmpty()) {
                this.product_name = nameText;
            } else {
                LoggingManager.warn("Product name element is empty or null.");
                this.product_name = "Unknown"; // Or handle as needed
            }
        } catch (Exception e) {
            LoggingManager.error("Failed to retrieve product name.", e);
            
        }
        return this;
    }


    /**
     * Gets the price of the product.
     * 
     * @return The product price as a BigDecimal.
     */
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * Gets the rating details of the product.
     * 
     * @return An instance of RatingDetails containing the rating and the number of raters.
     */
    public RatingDetails getRatingDetails() {
        return this.ratingDetails;
    }

    /**
     * Gets the name of the product.
     * 
     * @return The product name as a String.
     */
    public String getName() {
        return this.product_name;
    }

    /**
     * Gets the number of items in the cart.
     * 
     * @return The number of items in the cart.
     */
    public int getItemsInCart() {
        return this.itemsInCart;
    }

    /**
     * Checks if the product has been added to the cart.
     * 
     * @return True if the product is added to the cart, false otherwise.
     */
    public boolean isAddedToCart() {
        return this.isAddedToCart;
    }

    public void setIsAddedToCart(boolean added) {
    	this.isAddedToCart = added;
    }
    /**
     * Initializes the price of the product.
     * 
     * @return The current Product instance.
     */
    public Product setPrice() {
        try {
        	waitUtil.waitForElementToBeVisible(productPrice, 1);
            if (productPrice != null) {
                String priceText = productPrice.getText().replace("R", "").replace(",", "").trim();
                LoggingManager.info("Extracted price text: '" + priceText + "'");

                // Check if the priceText is not empty and a valid number
                if (!priceText.isEmpty() && isNumeric(priceText)) {
                    this.price = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);
                    LoggingManager.info("Successfully set the price");
                } else {
                    LoggingManager.warn("Price text is empty or not a valid number: '" + priceText + "'");
                }
            } else {
                LoggingManager.warn("Product price element is not displayed or null.");
            }
        } catch (NumberFormatException e) {
            LoggingManager.error("Failed to parse price: '" + productPrice.getText() + "'", e);
        }
        LoggingManager.info("Returning the Product..");
        return this;
    }

    // Utility method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public Product setRatingDetails() {
        try {
            if (rating != null && rating.isDisplayed()) {
            	LoggingManager.info("getting the rating");
                String ratingText = rating.getAttribute("innerText").trim();
                LoggingManager.info("Raw rating text: " + ratingText);
                
                String[] parts = ratingText.split("\\s*\\(");
                if (parts.length == 2) {
                    float ratingValue = Float.parseFloat(parts[0].trim());
                    int reviewsValue = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim());
                    ratingDetails = new RatingDetails(ratingValue, reviewsValue);
                } else {
                    LoggingManager.warn("Unexpected rating text format: " + ratingText);
                    ratingDetails = new RatingDetails(0.0f, 0); // Default values if format is unexpected
                }
            } else {
                LoggingManager.warn("Rating element is null or not displayed.");
                ratingDetails = new RatingDetails(0.0f, 0); // Default values if element is not present
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException  e) {
            
        	LoggingManager.error("Failed to parse rating details.", e);
            ratingDetails = new RatingDetails(0.0f, 0); // Default values on exception
        }catch (NoSuchElementException e) {
            
        	LoggingManager.warn("Rating or review element not found. This product might not have a rating.");
            ratingDetails = new RatingDetails(0.0f, 0); // Or set to a default value
        }
        
        return this;
    }


    /**
     * Adds the product to the cart.
     * If the "Add to Cart" button is not directly visible, clicks the "Show All Options" button first.
     * 
     * @return The current Product instance.
     */
    public Product addToCartOnList() {
        int retryCount = 2;
        boolean addedToCart = false;
        boolean allOptions = false;
        if(isVisible(this.addToCartBtn)) {
        	
        	
        }
        else {
        	AddToCart();
        }
/*        String whand1 =DriverFactory.getDriver().getWindowHandle();

        for (int attempt = 0; attempt < retryCount; attempt++) {
            try {
                int productsInCartBefore = getProductsInCartCount();
                LoggingManager.info("Attempting to add product to cart");
                
                if(allOptions) {
                	DriverFactory.getDriver().switchTo().defaultContent();
                	DriverFactory.getDriver().findElement(By.cssSelector(".select-dropdown-module_select-dropdown_3Rysq")).click();
                	waitUtil.waitImplicitly(1);
                	List<WebElement> options = DriverFactory.getDriver().findElements(By.className("select-dropdown-module_list-item_2kHtk"));
                	WebElement clickOption = options.get(1);
                	clickOption.click();
//                	addToCartBtn.get(0).click();
                }
                if (waitUtil.waitForElementToBeVisible(this.cartBtnElement, 1) != null) {
//                    scrollToElement(addToCartBtn.get(0));
                	this.cartBtnElement.click();
                    waitUtil.waitImplicitly(1);

                    WebElement miniCartIconCountElement = DriverFactory.getDriver().findElement(By.className("badge-button-module_badge-count-wrapper_2buZm"));
                    int productsInCartAfter = Integer.parseInt(miniCartIconCountElement.getText().trim());

                    if (productsInCartAfter > productsInCartBefore) {
                        isAddedToCart = true;
                        this.itemsInCart = productsInCartAfter - productsInCartBefore;
                        LoggingManager.info("Product added to cart successfully");
                        addedToCart = true;
                        break;
                    } else {
                        LoggingManager.warn("Product was not added to cart");
                    }
                } else {
                    LoggingManager.info("Add to Cart button is not visible, clicking 'Show All Options'");
                    scrollToElement(showAllOptions);
                    showAllOptions.click();
                    allOptions =true;
                    waitUtil.waitImplicitly(1);
                }
            } catch (Exception e) {
                LoggingManager.error("Error adding product to cart: " + e.getMessage(), e);
                scrollToElement(showAllOptions);
                showAllOptions.click();
                allOptions =true;
                waitUtil.waitImplicitly(1);
            }
        }

        if (!addedToCart) {
            LoggingManager.warn("Failed to add product to cart after " + retryCount + " attempts.");
        }
*/
        return this;
    }

    private void AddToCart() {
		
	}

	/**
     * Retrieves the current count of products in the cart.
     * 
     * @return The count of products in the cart. Defaults to 0 if there's an error.
     */
    private int getProductsInCartCount() {
        try {
            // Get text from the element
            String textCount = DriverFactory.getDriver().findElement(By.className("badge-button-module_badge-count-wrapper_2buZm")).getText().trim();
            
            // Parse the text to an integer
            int count =Integer.parseInt(textCount);
            return count;
            
        } catch (NoSuchElementException e) {
            LoggingManager.error("Mini cart icon count element not found", e);
        } catch (NumberFormatException e) {
            LoggingManager.error("Failed to parse the number of products in the cart. Text might not be a valid number", e);
        } catch (Exception e) {
            LoggingManager.error("Unexpected error occurred while getting the cart product count", e);
        }
        
        // Return default value if an error occurs
        return 0;
    }

    
	public String getProductType() {
		return product_type;
	}

	public Product setProductType(String product_type) {
		
		boolean isPresent = waitUtil.isElementPresent(sponsoredProduct, 1);
	    if (isPresent) {
	        this.product_type = product_type;
	    } else {
	        LoggingManager.warn("Sponsored product element is not present.");
	    }
	    return this;
	}
}
