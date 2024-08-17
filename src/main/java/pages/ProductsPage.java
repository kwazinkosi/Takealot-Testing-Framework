package pages;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import components.Product;
import logging.LoggingManager;

/**
 * Represents the Products Page of the application. This class provides methods
 * to interact with and retrieve information from the product listings on the
 * page.
 */
public class ProductsPage extends BasePage {

	@FindBy(css = ".search-product.grid div[data-ref='product-card']")
	private List<WebElement> productElements;

	@FindBy(className = "listings-container-module_listings-container_AC4LI")
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
			productList = productElements.stream().map(el -> new Product(el).initializeProductDetails()).toList();// Initialize
																													// product
																													// details
																													// only
																													// once
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
			int count = Integer.parseInt(textCount);
			return count;

		} catch (NoSuchElementException e) {
			LoggingManager.error("Mini cart icon count element not found", e);
		} catch (NumberFormatException e) {
			LoggingManager.error("Failed to parse the number of products in the cart. Text might not be a valid number",
					e);
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
	 * @param condition A Predicate that defines the condition for selecting the
	 *                  products. For example, you can use it to filter by product
	 *                  name or price.
	 * @return A list of products that match the condition.
	 * @throws IllegalArgumentException if the condition is null.
	 * @throws NoSuchElementException   if no products match the condition.
	 */
	public List<Product> getFilteredProducts(Predicate<Product> condition) {

		if (condition == null) {
			throw new IllegalArgumentException("Condition must not be null.");
		}

		LoggingManager.info("Filtering products based on provided condition.");

		List<Product> filteredProducts = getProducts().stream().filter(condition).collect(Collectors.toList());

		if (filteredProducts.isEmpty()) {
			LoggingManager.warn("No products match the provided condition.");
		}

		return filteredProducts;
	}

	/**
	 * Returns a list of products based on a boolean-valued function (predicate).
	 * This method uses the behavioral Strategy Pattern to filter products.
	 * 
	 * @param condition   A Predicate that defines the condition for selecting the
	 *                    products. For example, you can use it to filter by product
	 *                    name or price.
	 * @param maxProducts The maximum number of products
	 * 
	 * @return A list of products that match the condition and within the maximum
	 *         specified.
	 * @throws IllegalArgumentException if the condition is null.
	 */
	public List<Product> getFilteredProductsMax(Predicate<Product> condition, int maxProducts) {
		if (condition == null) {
			throw new IllegalArgumentException("Condition must not be null.");
		}

		LoggingManager.info("Filtering products based on the provided condition with a maximum limit of " + maxProducts
				+ " products.");

		List<Product> filteredProducts = getProducts().stream().filter(condition).limit(maxProducts).collect(Collectors.toList());// Limits the stream to the specified number of products

		if (filteredProducts.isEmpty()) {
			LoggingManager.warn("No products match the provided condition.");
		}

		return filteredProducts;
	}

	/**
	 * Returns a specific product based on a boolean-valued function (predicate).
	 * This method uses the behavioral Strategy Pattern to filter products.
	 * 
	 * @param condition A Predicate that defines the condition for selecting the
	 *                  product. For example, you can use it to filter by product
	 *                  name or price.
	 * @return The first product that matches the condition.
	 * @throws IllegalArgumentException if the condition is null.
	 * @throws NoSuchElementException   if no product matches the condition.
	 */
	public Product getProduct(Predicate<Product> condition) {
		if (condition == null) {
			throw new IllegalArgumentException("Condition must not be null.");
		}

		LoggingManager.info("Filtering products based on provided condition.");

		Product product = getProducts().stream().filter(condition).findFirst().orElseThrow();
		return product;
	}

	/**
	 * Attempts to add the specified product to the cart.
	 * <p>
	 * This method first checks if the "Add to Cart" button is visible. If it is, the method clicks the button 
	 * and verifies if the product count in the cart has increased, indicating that the product has been added.
	 * If the "Add to Cart" button is not visible, it clicks the "Show All Options" button to reveal additional options,
	 * selects an option, and then attempts to click the "Add to Cart" button again.
	 * <p>
	 * The method handles potential visibility issues with explicit waits and logs relevant information during the process.
	 * 
	 * @param product The {@link Product} object representing the product to be added to the cart.
	 * @return {@code true} if the product was successfully added to the cart; {@code false} otherwise.
	 * 
	 * @throws NoSuchElementException If the required elements are not found within the specified timeout.
	 * @throws TimeoutException       If the specified conditions are not met within the given timeout period.
	 */
	public boolean addToCart(Product product) {
	    boolean isAdded = false;

	    if (product.isVisible(product.addToCartBtn)) {
	        isAdded = clickAddToCartButton(product);
	    } else if (product.isVisible(product.showAllOptions)) {
	        LoggingManager.info("Add to Cart button is not visible, attempting to show all options.");
	        isAdded = handleShowAllOptions(product);
	    } else {
	        LoggingManager.warn("Neither 'Add to Cart' nor 'Show All Options' buttons are visible.");
	    }

	    return isAdded;
	}

	/**
	 * Clicks the "Add to Cart" button and verifies if the product count in the cart has increased.
	 * <p>
	 * This method uses an explicit wait to ensure the "Add to Cart" button is visible before clicking it.
	 * It then waits for the cart count to update and verifies if the product was successfully added.
	 * 
	 * @param product The {@link Product} object representing the product to be added to the cart.
	 * @return {@code true} if the product was successfully added to the cart; {@code false} otherwise.
	 */
	private boolean clickAddToCartButton(Product product) {
	    try {
	        LoggingManager.info("Attempting to click the 'Add to Cart' button.");
	        WebElement addToCartBtn = waitUtil.waitFor(ExpectedConditions.visibilityOf(product.addToCartBtn), normalWaitTime);
	        
	        int initialCount = getProductsInCartCount();
	        addToCartBtn.click();
	        
	        waitUtil.waitImplicitly(1); // Adjust this based on your page's behavior
	        
	        if (getProductsInCartCount() > initialCount) {
	            product.setIsAddedToCart(true);
	            LoggingManager.info("Product added to cart successfully.");
	            return true;
	        } else {
	            LoggingManager.warn("Product was not added to the cart.");
	        }
	    } catch (Exception e) {
	        LoggingManager.info("Error while attempting to add the product to the cart: " + e.getMessage());
	    }
	    return false;
	}

	/**
	 * Handles the scenario where the "Show All Options" button is visible instead of the "Add to Cart" button.
	 * <p>
	 * This method clicks the "Show All Options" button, waits for the options to be displayed, selects an option,
	 * and then attempts to click the "Add to Cart" button to add the product to the cart.
	 * 
	 * @param product The {@link Product} object representing the product to be added to the cart.
	 * @return {@code true} if the product was successfully added to the cart after selecting an option; {@code false} otherwise.
	 */
	private boolean handleShowAllOptions(Product product) {
	    try {
	    	actionUtil.scrollToElement(product.showAllOptions);
	        click(product.showAllOptions);
	        
	        WebElement dropdown = waitUtil.waitFor(ExpectedConditions.elementToBeClickable(product.optionsMenuBy), normalWaitTime);
	        dropdown.click();

	        List<WebElement> options = waitUtil.waitFor(ExpectedConditions.visibilityOfAllElementsLocatedBy(product.optionsListBy), normalWaitTime);
	        if (!options.isEmpty()) {
	            WebElement selectedOption = options.get(1); // Adjust index based on requirement
	            selectedOption.click();

	            return clickAddToCartButton(product); // Attempt to add to cart after selecting an option
	        } else {
	            LoggingManager.warn("No options available to select.");
	        }
	    } catch (Exception e) {
	        LoggingManager.info("Error while handling options: " + e.getMessage());
	    }
	    return false;
	}


	/**
	 * Checks if the product results container is visible.
	 * 
	 * @return True if the product results container is visible, false otherwise.
	 */
	@Override
	public boolean isVisible() {

		boolean isVisible = isVisible(productResultsContainer);
		LoggingManager.info("Product results visibility status: " + isVisible);
		return isVisible;
	}
}
