package pages;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
    	
    	if(product.isVisible(product.addToCartBtn)) {
    		LoggingManager.info("cart button is visible");
    		int c1 = getProductsInCartCount();
    		product.addToCartBtn.click();
    		if(getProductsInCartCount() - c1 >0) {
    			product.setIsAddedToCart(true);
        		isAdded = true;
    		}
    	}
    	else if(product.isVisible(product.showAllOptions)) {
    		
    		LoggingManager.info("cart button is not visible, the shoAll buttons is visible");
    		click(product.showAllOptions);
        	waitUtil.waitForElementsToBePresent(DriverFactory.getDriver().findElements(By.cssSelector(".select-dropdown-module_select-dropdown_3Rysq"))).click();
        	waitUtil.waitImplicitly(1);
    		List<WebElement> options = DriverFactory.getDriver().findElements(By.className("select-dropdown-module_list-item_2kHtk"));
        	WebElement clickOption = options.get(1);
        	waitUtil.waitImplicitly(1);
        	clickOption.click();
        	
        	if(product.isVisible(DriverFactory.getDriver().findElement(By.cssSelector("button.add-to-cart-button-module_add-to-cart-button_1a9gT[data-ref='add-to-cart-button']")))) {
        		LoggingManager.info("cart button is visible");
        		int c1 = getProductsInCartCount();
        		DriverFactory.getDriver().findElement(By.cssSelector("button.add-to-cart-button-module_add-to-cart-button_1a9gT[data-ref='add-to-cart-button']")).click();

        		if(getProductsInCartCount() - c1 >0) {
        			product.setIsAddedToCart(true);
            		isAdded = true;
        		}
        	}
        	else {
        		LoggingManager.info("uhhmmm, button not located");
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
