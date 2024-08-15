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
import utilities.AdOverlayListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class ProductsPageTest {

    private WebDriver driver;
    private ProductsPage productsPage;
    private HomePage homePage;
    private DataProviderUtil dataProviderUtil = new DataProviderUtil();

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new AdOverlayListener(driver);
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        driver.get(ConfigReader.getProperty("base_url"));

        String searchProduct = dataProviderUtil.getValue("common info", "search_product");
        // Navigate to a particular products page
        homePage = new HomePage(driver);
        productsPage = homePage.searchValidFor(searchProduct);
        productsPage.getWait().waitImplicitly(0,5);
        BasePage.reporter.setDriver(driver);  // Inject WebDriver into ReportManager
    }

    @Test(priority = 5, enabled =false)
    public void verifyIsResultsVisible() {
        
    	LoggingManager.info("==============Starting test to check if product results are visible. ==============");
        // Check if product results are visible
        boolean resultsVisible = productsPage.isResultsVisible();

        // Validate that the product results container is visible
        Assert.assertTrue(resultsVisible, "Product results container should be visible.");
        LoggingManager.info("Product results container visibility status: " + resultsVisible);
        LoggingManager.info("Test for search product results  -- PASSED!\n\n");
    }

    @Test(priority = 6, enabled =false)
    public void verifyProductsRetrieval() {
        LoggingManager.info("==============Starting test for product retrieval.==============");

        // Retrieve products
        List<Product> products = productsPage.getProducts();

        // Validate that the list is not empty
        Assert.assertNotNull(products, "Product list should not be null.");
        Assert.assertFalse(products.isEmpty(), "Product list should not be empty.");

        LoggingManager.info("Retrieved " + products.size() + " products.");
        LoggingManager.info("Test for product retrieval -- PASSED!\n\n");
    }

    @Test(priority =7, enabled =false) 
    public void verifyFilteredProducts() {
        
    	LoggingManager.info("==============Starting test for filtered products.============== ");
        List<Product> product = productsPage.getFilteredProducts(
        		p -> p.getRatingDetails().getRating() >= 4.5 && p.getName().toLowerCase().contains("iphone"));


        Assert.assertFalse(product.isEmpty(), "No products found with a high rating.");

       
        LoggingManager.info("Test for filtered products -- PASSED!\n\n");
    }

    @Test(priority = 8, enabled =false)
    public void verifyGetProductByPrice() {
        LoggingManager.info("============== Starting test to get a product by price. ==============");

        // Define a predicate to find a product by price
        Predicate<Product> productByPrice = product -> product.getPrice().compareTo(new BigDecimal("7999")) == 0;

        // Get the product
        Product product = productsPage.getProduct(productByPrice);

        // Validate that the product was found
        Assert.assertNotNull(product, "Product should be found.");
        Assert.assertEquals(product.getPrice(), new BigDecimal("7999.00"), "Product price should match.");

        LoggingManager.info("Product with price '7999' was found, "+product.getName());
        LoggingManager.info("Product price filter -- Passed!\n\n");
    }

    @Test(priority = 9, enabled =false, dataProvider = "productSearchData", dataProviderClass = DataProviderUtil.class)
    public void verifyProductDetails(String productName, String vendorName, String price,
                                   String rating, String reviews, String category, String executionRequired) {
    	
        // Check if execution is required for this test case
        if ("No".equalsIgnoreCase(executionRequired)) {
            throw new SkipException("Skipping this test case as execution is not required");
        }
        // Convert price, rating, and reviews to appropriate data types if needed
        BigDecimal expectedPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        float expectedRating = (price != null)?Float.parseFloat(rating) :0.0f;
        int expectedReviews = (reviews !=null)?(int)Float.parseFloat(reviews):0;

        // Apply filters or search criteria on the page if necessary
        LoggingManager.info("Successfully got sheet values - now geting products");
        Product product = homePage.searchValidFor(productName).getProduct(
        		p -> p.getName().toLowerCase().equals(productName.toLowerCase()));

        // Assertions to verify product details
        Assert.assertEquals(product.getName().toLowerCase(), productName.toLowerCase(), "Product name does not match");
        Assert.assertEquals(product.getPrice(), expectedPrice, "Product price does not match");
        Assert.assertEquals(product.getRatingDetails().getRating(), expectedRating, "Product rating does not match");
        Assert.assertTrue(product.getRatingDetails().getRatersCount() >= expectedReviews, "Number of reviews does not match");
        LoggingManager.info("Test for "+ productName+ " avaibility Passed!\n\n");
        // TODO: Might add additional assertions based on vendorName, category, etc
    }

    @Test(priority = 10)
    public void verifyProductAddToCart() {
        
    	// Retrieve the product name from data file
        String cartProduct = homePage.dataUtil.getValue("common info", "cart_product");

        // Define the condition for filtering the product
        Predicate<Product> condition = p -> p.getName().toLowerCase().contains(cartProduct.toLowerCase())
                && p.getPrice().compareTo(new BigDecimal("15000.00")) >= 0
                && p.getPrice().compareTo(new BigDecimal("20000.00")) <= 0;

        // Search for the product, apply the filter, and add it to the cart
              ProductsPage products = homePage.searchValidFor(cartProduct);
              Product product = products.getProduct(condition);
              boolean isAdded = products.addToCart(product);

        // Verify if the product is added to the cart
        Assert.assertTrue(isAdded, "Product '" + cartProduct + "' was not added to the cart");
    }

    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
