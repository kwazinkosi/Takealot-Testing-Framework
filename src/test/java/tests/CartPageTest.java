package tests;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import components.CartItem;
import components.Product;
import config.ConfigReader;
import logging.LoggingManager;
import pages.BasePage;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;
import utilities.AdOverlayListener;
import utilities.DriverFactory;

public class CartPageTest {

    private WebDriver driver;
    private ProductsPage productsPage;
    private HomePage homePage;
    private CartPage cartPage;

    @BeforeClass
    public void setUp() {
        
    	driver = DriverFactory.initDriver();
        // Apply the WebDriverListener
        
        WebDriverListener listener = new AdOverlayListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        driver.get(ConfigReader.getProperty("base_url"));

        // Navigate to the cart page
        homePage = new HomePage(driver);
        productsPage = new ProductsPage(DriverFactory.getDriver());
        homePage.getWait().waitImplicitly(1);
        cartPage = homePage.navigateToCart();
        BasePage.reporter.setDriver(driver); // Inject WebDriver into ReportManager
        LoggingManager.info(" \n\n\n*************** STARTING Cart TESTS **************");
    }

    /**
     * Verifies that the cart is empty by checking the cart badge count and the cart page's state.
     */
    @Test
    public void verifyCartIsEmpty() {
        
    	LoggingManager.info("============ Starting Cart Empty Verification =============");
        int cartBadgeCount = cartPage.getNavBar().getProductsInCartCount();
        Assert.assertEquals(cartBadgeCount, 0, "The cart items count is not 0 as expected.");
        Assert.assertTrue(cartPage.isCartEmpty(), "The cart is not empty.");
        LoggingManager.info("============ Cart Empty Verification -- Passed ============= \n\n");
    }

    /**
     * Verifies that the cart is not empty after adding an item.
     */
    @Test
    public void verifyCartNotEmpty() {
       
    	LoggingManager.info("============ Starting Cart Not Empty Verification =============");
        String searchProduct1 = cartPage.dataUtil.getValue("common info", "search_product");
        Predicate<Product> condition = p -> 
        p.getName().toLowerCase().contains(searchProduct1.toLowerCase()) 
        && p.getPrice().compareTo(new BigDecimal("10000.00")) >= 0;

        homePage.getNavBar().clickNavLink("Home");
        Product product = homePage.searchValidFor(searchProduct1).getProduct(condition);;
         
        boolean isAdded = productsPage.addToCart(product);
        Assert.assertTrue(isAdded, "Product was not added to cart");
        Assert.assertFalse(cartPage.isCartEmpty(), "The cart should not be empty after adding an item.");
        LoggingManager.info("============ Cart Not Empty Verification -- Passed ============= \n\n");
    }

    /**
     * Verifies the details of items in the cart.
     */
    @Test
    public void verifyItemDetailsInCart() {
    	
        LoggingManager.info("============ Starting Item Details Verification in Cart =============");
        String searchProduct1 = cartPage.dataUtil.getValue("common info", "search_product-1");
        String searchProduct2 = cartPage.dataUtil.getValue("common info", "search_product-2");
        List<String> cartItems = Arrays.asList(searchProduct1, searchProduct2);

        for (String item : cartItems) {
            Predicate<Product> condition = p -> p.getName().toLowerCase().contains(item.toLowerCase());
            homePage.getNavBar().clickNavLink("Home");
            productsPage = homePage.searchValidFor(item);
            Product product = productsPage.getProduct(condition);
            boolean isAdded = productsPage.addToCart(product);
            Assert.assertTrue(isAdded, "Product was not added to cart");
        }

        for (String item : cartItems) {
            CartItem cartItem = cartPage.getCartItem(item);

            Assert.assertNotNull(cartItem, "The cart item should be present in the cart.");
            Assert.assertEquals(cartItem.getProductName(), item, "The product name does not match.");
            Assert.assertTrue(cartItem.getPrice().compareTo(BigDecimal.ZERO) > 0, "The price of the cart item is zero.");
        }
        LoggingManager.info("============ Item Details Verification in Cart -- Passed ============= \n\n");
    }

    /**
     * Verifies the total price of the items in the cart.
     */
    @Test
    public void verifyTotalCartPrice() {
        
    	LoggingManager.info("============ Starting Total Cart Price Verification =============");
        BigDecimal expectedTotalPrice = cartPage.getCartItems().stream()
            .map(CartItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal actualTotalPrice = cartPage.getTotalCartPrice();

        Assert.assertEquals(actualTotalPrice, expectedTotalPrice, "The total cart price is incorrect.");
        LoggingManager.info("============ Total Cart Price Verification -- Passed ============= \n\n");
    }

    /**
     * Verifies the removal of an item from the cart.
     */
    @Test
    public void verifyRemovalOfItemFromCart() {

    	LoggingManager.info("============ Starting Removal of Item from Cart Verification =============");
        String searchProduct1 = cartPage.dataUtil.getValue("common info", "search_product-1");

        CartItem cartItem = cartPage.getCartItem(searchProduct1);
        Assert.assertNotNull(cartItem, "The cart item should be present in the cart before removal.");
        cartItem.removeFromCart();
        Assert.assertNull(cartPage.getCartItem(searchProduct1), "The cart item should be removed from the cart.");
        LoggingManager.info("============ Removal of Item from Cart Verification -- Passed ============= \n\n");
    }

    /**
     * Verifies moving an item to the wishlist.
     */
    @Test
    public void verifyMoveItemToWishlist() {
        
    	LoggingManager.info("============ Starting Move Item to Wishlist Verification =============");
        String searchProduct1 = cartPage.dataUtil.getValue("common info", "search_product-1");
        CartItem cartItem = cartPage.getCartItem(searchProduct1);
        Assert.assertNotNull(cartItem, "The cart item should be present in the cart before moving to wishlist.");

        cartItem.moveToWishlist();

        Assert.assertNull(cartPage.getCartItem(searchProduct1), "The cart item should be moved to the wishlist and not in the cart.");
        // Add additional assertions if the item should appear in the wishlist page
        LoggingManager.info("============ Move Item to Wishlist Verification -- Passed ============= \n\n");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
