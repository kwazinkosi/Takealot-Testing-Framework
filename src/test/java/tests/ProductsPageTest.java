package tests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import components.Product;
import config.ConfigReader;
import logging.LoggingManager;
import pages.BasePage;
import pages.HomePage;
import pages.ProductsPage;
import utilities.EventListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

/**
 * Test class for verifying functionalities on the Products Page.
 * This class contains various tests to validate product retrieval, filtering, 
 * and the ability to add products to the cart.
 */
public class ProductsPageTest {

    private WebDriver driver;
    private ProductsPage productsPage;
    private HomePage homePage;
    private DataProviderUtil dataProviderUtil = new DataProviderUtil();
    private String searchProduct;

    /**
     * Setup method that runs before the test class.
     * Initializes the WebDriver, applies event listeners, and navigates to the products page.
     */
    @BeforeClass
    public void setUp() {
        // Initialize WebDriver
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener to handle events during the test
        WebDriverListener listener = new EventListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);

        // Navigate to the base URL
        driver.get(ConfigReader.getProperty("base_url"));

        // Retrieve the product to search for from the data provider
        searchProduct = dataProviderUtil.getValue("common info", "search_product");

        // Navigate to the search results page for the product
        homePage = new HomePage(driver);
        productsPage = homePage.searchValidFor(searchProduct);

        // Set the WebDriver for reporting purposes
        BasePage.reporter.setDriver(driver);
        
        // Log the start of the products tests
        LoggingManager.info("\n\n*************** STARTING PRODUCTS TESTS **************");
    }

    /**
     * Test to verify that product results are visible on the page.
     * This test checks the visibility of the product results container.
     */
    @Test(priority = 5, groups = {"products", "search"})
    public void verifyIsResultsVisible() {
        LoggingManager.info("============== Starting test to check if product results are visible. ==============");

        // Check if product results are visible
        boolean resultsVisible = productsPage.isVisible();

        // Validate that the product results container is visible
        Assert.assertTrue(resultsVisible, "Product results container should be visible.");
        LoggingManager.info("Product results container visibility status: " + resultsVisible);
        LoggingManager.info("Test for search product results -- PASSED!\n\n");
    }

    /**
     * Test to verify the retrieval of products.
     * This test ensures that the list of products is not empty.
     */
    @Test(priority = 6, groups = {"products", "retrieval"})
    public void verifyProductsRetrieval() {
        LoggingManager.info("============== Starting test for product retrieval. ==============");

        // Retrieve products from the products page
        List<Product> products = productsPage.getProducts();

        // Validate that the product list is not null or empty
        Assert.assertNotNull(products, "Product list should not be null.");
        Assert.assertFalse(products.isEmpty(), "Product list should not be empty.");

        LoggingManager.info("Retrieved " + products.size() + " products.");
        LoggingManager.info("Test for product retrieval -- PASSED!\n\n");
    }

    /**
     * Test to verify the filtering of products.
     * This test filters products based on a rating and name criteria.
     */
    @Test(priority = 7, groups = {"products", "filtering"})
    public void verifyFilteredProducts() {
        LoggingManager.info("============== Starting test for filtered products. ==============");

        // Filter products by rating and name
        List<Product> products = productsPage.getFilteredProducts(
                p -> p.getRatingDetails().getRating() >= 4.5 && p.getName().toLowerCase().contains(searchProduct.toLowerCase()));

        // Validate that the filtered list is not empty
        Assert.assertFalse(products.isEmpty(), "No products found with a high rating.");

        LoggingManager.info("Test for filtered products -- PASSED!\n\n");
    }

    /**
     * Test to verify the retrieval of a product by price.
     * This test uses a predicate to find a product with a specific price.
     */
    @Test(priority = 8, groups = {"products", "price"})
    public void verifyGetProductByPrice() {
        LoggingManager.info("============== Starting test to get a product by price. ==============");

        // Define a predicate to find a product by price
        Predicate<Product> productByPrice = product -> product.getPrice().compareTo(new BigDecimal("7999")) == 0;

        // Get the product that matches the price
        Product product = productsPage.getProduct(productByPrice);

        // Validate that the product was found and the price matches
        Assert.assertNotNull(product, "Product should be found.");
        Assert.assertEquals(product.getPrice(), new BigDecimal("7999.00"), "Product price should match.");

        LoggingManager.info("Product with price '7999' was found, " + product.getName());
        LoggingManager.info("Product price filter -- Passed!\n\n");
    }

    /**
     * Test to verify the details of a product.
     * This test is data-driven and verifies product name, price, rating, and reviews.
     *
     * @param productName         The expected name of the product.
     * @param vendorName          The expected vendor name (optional).
     * @param price               The expected price of the product.
     * @param rating              The expected rating of the product.
     * @param reviews             The expected number of reviews.
     * @param category            The expected category of the product (optional).
     * @param executionRequired   Indicates whether this test case should be executed.
     */
    @Test(priority = 9, dataProvider = "productSearchData", dataProviderClass = DataProviderUtil.class, groups = {"products", "details"})
    public void verifyProductDetails(String productName, String vendorName, String price, String rating, String reviews, String category, String executionRequired) {
        LoggingManager.info("============= Starting test for verifyProductDetails ================");

        // Check if execution is required for this test case
        if ("No".equalsIgnoreCase(executionRequired)) {
            throw new SkipException("Skipping this test case as execution is not required");
        }

        // Convert price, rating, and reviews to appropriate data types
        BigDecimal expectedPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        float expectedRating = (rating != null) ? Float.parseFloat(rating) : 0.0f;
        int expectedReviews = (reviews != null) ? (int) Float.parseFloat(reviews) : 0;

        // Search for the product by name
        Product product = homePage.searchValidFor(productName).getProduct(
                p -> p.getName().toLowerCase().equals(productName.toLowerCase()));

        // Assertions to verify product details
        Assert.assertEquals(product.getName().toLowerCase(), productName.toLowerCase(), "Product name does not match");
        Assert.assertEquals(product.getPrice(), expectedPrice, "Product price does not match");
        Assert.assertEquals(product.getRatingDetails().getRating(), expectedRating, "Product rating does not match");
        Assert.assertTrue(product.getRatingDetails().getRatersCount() >= expectedReviews, "Number of reviews does not match");

        LoggingManager.info("Test for " + productName + " availability Passed!\n\n");

        // TODO: Add additional assertions based on vendorName, category, etc.
    }

    /**
     * Test to verify that a product can be added to the cart.
     * This test searches for a product by name and price range, then adds it to the cart.
     */
    @Test(priority = 10, groups = {"products", "cart"})
    public void verifyProductAddToCart() {
        LoggingManager.info("============= Starting test to verify adding a product to the cart ===============");

        // Retrieve the product name from the data file
        String cartProduct = homePage.dataUtil.getValue("common info", "cart_product");

        // Define the condition for filtering the product by name and price range
        Predicate<Product> condition = p -> p.getName().toLowerCase().contains(cartProduct.toLowerCase())
                && p.getPrice().compareTo(new BigDecimal("15000.00")) >= 0
                && p.getPrice().compareTo(new BigDecimal("20000.00")) <= 0;

        // Search for the product, apply the filter, and add it to the cart
        ProductsPage products = homePage.searchValidFor(cartProduct);
        Product product = products.getProduct(condition);
        boolean isAdded = products.addToCart(product);

        // Verify if the product is added to the cart
        Assert.assertTrue(isAdded, "Product '" + cartProduct + "' was not added to the cart");
        LoggingManager.info("Product '" + cartProduct + "' was successfully added to the cart.");
    }

    /**
     * Tear down method that runs after the test class.
     * Closes the WebDriver instance to end the session.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            LoggingManager.info("Driver quit successfully.");
        }
    }
}
