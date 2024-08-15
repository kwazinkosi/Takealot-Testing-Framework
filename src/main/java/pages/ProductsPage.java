package pages;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import components.Product;
import logging.LoggingManager;
import utilities.DriverFactory;

/**
 * Represents the Products Page of the application.
 * This class provides methods to interact with and retrieve information from the product listings on the page.
 */
public class ProductsPage extends BasePage {

    @FindBy(css = ".search-product.grid div[data-ref='product-card']")
    private List<WebElement> productElements;
    
    @FindBy(className ="listings-container-module_listings-container_AC4LI")
    private WebElement productResultsContainer;

    @FindBy(className = "badge-button-module_badge-count-wrapper_2buZm")
    private WebElement badgeCount;
    
	private List<Product> productList;

    /**
     * Constructor to initialize the ProductsPage.
     * 
     * @param driver The WebDriver instance to interact with the browser.
     */
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns a list of Product components representing each product on the page.
     * 
     * @return List of Product objects.
     */
    public List<Product> getProducts() {
        LoggingManager.info("Retrieving products from the product list.");

        if (productElements == null || productElements.isEmpty()) {
            LoggingManager.warn("No product elements found.");
            return Collections.emptyList();
        }

        // Initialize productList if it hasn't been initialized yet
        if (productList == null) {
            productList = productElements
                    .stream()
                    .map(el -> new Product(el).initializeProductDetails()) // Initialize product details only once
                    .toList();
        }

        return productList;
    }

    /**
     * Retrieves the current count of products in the cart.
     * 
     * @return The count of products in the cart. Defaults to 0 if there's an error.
     */
    private int getProductsInCartCount() {
        try {
            // Get text from the element
            String textCount = badgeCount.getText().trim();
            
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
    
    /**
     * Returns a list of products based on a boolean-valued function (predicate).
     * This method uses the behavioral Strategy Pattern to filter products.
     * 
     * @param condition A Predicate that defines the condition for selecting the products.
     *                  For example, you can use it to filter by product name or price.
     * @return A list of products that match the condition.
     * @throws IllegalArgumentException if the condition is null.
     * @throws NoSuchElementException if no products match the condition.
     */
    public List<Product> getFilteredProducts(Predicate<Product> condition) {

    	if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }

        LoggingManager.info("Filtering products based on provided condition.");

        List<Product> filteredProducts = getProducts()
									                .stream()
									                .filter(condition)
									                .collect(Collectors.toList());

        if (filteredProducts.isEmpty()) {
            LoggingManager.warn("No products match the provided condition.");
        }

        return filteredProducts;
    }

    /**
     * Returns a list of products based on a boolean-valued function (predicate).
     * This method uses the behavioral Strategy Pattern to filter products.
     * 
     * @param condition A Predicate that defines the condition for selecting the products.
     *                  For example, you can use it to filter by product name or price.
     *  @param maxProducts The maximum number of products
     *  
     * @return A list of products that match the condition and within the maximum specified.
     * @throws IllegalArgumentException if the condition is null.
     */
    public List<Product> getFilteredProductsMax(Predicate<Product> condition, int maxProducts) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }

        LoggingManager.info("Filtering products based on the provided condition with a maximum limit of " + maxProducts + " products.");

        List<Product> filteredProducts = getProducts()
                                            .stream()
                                            .filter(condition)
                                            .limit(maxProducts) // Limits the stream to the specified number of products
                                            .collect(Collectors.toList());

        if (filteredProducts.isEmpty()) {
            LoggingManager.warn("No products match the provided condition.");
        }

        return filteredProducts;
    }

    
    /**
     * Returns a specific product based on a boolean-valued function (predicate).
     * This method uses the behavioral Strategy Pattern to filter products.
     * 
     * @param condition A Predicate that defines the condition for selecting the product.
     *                  For example, you can use it to filter by product name or price.
     * @return The first product that matches the condition.
     * @throws IllegalArgumentException if the condition is null.
     * @throws NoSuchElementException if no product matches the condition.
     */
    public Product getProduct(Predicate<Product> condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }
        
        LoggingManager.info("Filtering products based on provided condition.");

        Product product = getProducts()
						            .stream()
						            .filter(condition)
						            .findFirst()
						            .orElseThrow();
        return product;
    }

    public boolean addToCart(Product product) {
    	
    	boolean isAdded = false;
    	
    	if (product.isVisible(product.addToCartBtn)) {
    	    LoggingManager.info("Add to Cart button is visible");
    	    // Define the condition for waiting for the Add to Cart button to be visible
    	    Function<WebDriver, WebElement> addToCartBtnCondition = driver -> {
    	        WebElement btn = driver.findElement(product.cartAddButtonBy);
    	        return btn.isDisplayed() ? btn : null; // Return the button if visible, or null if not
    	    };
    	    
    	    // Wait for the Add to Cart button to be visible and clickable
    	    WebElement addToCartBtn = waitUtil.waitFor(addToCartBtnCondition, 10, 500);
    	    
    	    if (addToCartBtn != null) {
    	        LoggingManager.info("Add to Cart button is visible");
    	        int c1 = getProductsInCartCount();
    	        addToCartBtn.click();
    	        
    	        // Wait for the cart count to update and verify the product was added
    	        waitUtil.waitImplicitly(1); // Adjust this as needed based on your page's behavior
    	        if (getProductsInCartCount() - c1 > 0) {
    	            product.setIsAddedToCart(true);
    	            isAdded = true;
    	            LoggingManager.info("Product added to cart successfully");
    	        } else {
    	            LoggingManager.warn("Product was not added to cart");
    	        }
    	    } else {
    	        LoggingManager.info("Add to Cart button not found or not visible");
    	    }
    	}

    	else if(product.isVisible(product.showAllOptions)) {
    		
    		LoggingManager.info("cart button is not visible, the shoAll buttons is visible");
    		// Click the "Show All Options" button
    	    click(product.showAllOptions);
    	    // Define the condition for waiting
    	    Function<WebDriver, WebElement> dropdownCondition = driver -> {
    	        List<WebElement> elements = driver.findElements(product.optionsMenuBy);
    	        return elements.isEmpty() ? null : elements.get(0); // Return the first element or null if not found
    	    };
    	    // Wait for the dropdown to be present and clickable
    	    WebElement dropdown = waitUtil.waitFor(dropdownCondition, 10, 500);
    	    dropdown.click();
    	    
    	    // Define the condition for waiting for the options
    	    Function<WebDriver, List<WebElement>> optionsCondition = driver -> driver.findElements(product.optionsListBy);
    	    // Wait for the options to be present
    	    List<WebElement> options = waitUtil.waitFor(optionsCondition, 10, 500);
    	    WebElement clickOption = options.get(1);
    	    waitUtil.waitImplicitly(2);
    	    clickOption.click();
        	
    	    // Define the condition for waiting for the add-to-cart button to be visible
    	    Function<WebDriver, WebElement> cartButtonCondition = driver -> driver.findElement(product.cartAddButtonBy);
    	    
    	    // Wait for the add-to-cart button to be visible and clickable
    	    WebElement cartButton = waitUtil.waitFor(cartButtonCondition, 10, 500);
    	    if (cartButton != null)  {
    	        LoggingManager.info("Cart button is visible");
    	        int c1 = getProductsInCartCount();
    	        waitUtil.waitImplicitly(2);
        	    cartButton.click();
    	        
    	        if (getProductsInCartCount() - c1 > 0) {
    	            product.setIsAddedToCart(true);
    	            isAdded = true;
    	        }
    	    } else {
    	        LoggingManager.info("Cart button not located");
    	    }
        	
    	}
    	else {
    		LoggingManager.info("Could not locat the buttons");
    	}
    	return isAdded;
    }
    
    /**
     * Checks if the product results container is visible.
     * 
     * @return True if the product results container is visible, false otherwise.
     */
    public boolean isResultsVisible() {
        
    	boolean isVisible = isVisible(productResultsContainer);
        LoggingManager.info("Product results visibility status: " + isVisible);
        return isVisible;
    }
}
