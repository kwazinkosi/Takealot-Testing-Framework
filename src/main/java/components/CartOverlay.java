package components;

import pages.BasePage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import interfaces.ICart;
import utilities.DriverFactory;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartOverlay extends BaseComponent implements ICart {

    @FindBy(xpath = "//div[@class='cart-module_item_3MErs'] | //div[@class ='cart-item-container-module_item_3Vkqc']")
    private List<WebElement> cartItems;

    @FindBy(css = ".checkout-button[data-ref='checkout-button']")
    private WebElement checkoutBtn;

    @FindBy(name = "login-form")
    private WebElement loginForm;

    @FindBy(css = ".button.cart.pay")
    private WebElement goToCart;

    @FindBy(css = ".drawer-module_drawer-outer_3hLuY[data-ref='drawer-outer']")
    private WebElement cartOverlay;

    public CartOverlay(WebElement root) {
        super(root);
    }

    @Override
    public List<WebElement> getCartItems() {
        return cartItems;
    }

    @Override
    public CartPage goToCartPage() {
        
    	goToCart.click();
        return new CartPage(DriverFactory.getDriver()); // Get the current WebDriver from DriverFactory
    }

    @Override
    public BasePage proceedToCheckout() {
        
    	checkoutBtn.click();
        if (isVisible(loginForm)) {
            // Handle login form if it appears, returning a LoginPage
            return new LoginPage(DriverFactory.getDriver());
        }
        // If no login form appears, continue to the checkout process
        return new CheckoutPage(DriverFactory.getDriver());
    }

    public boolean isOverlayVisible() {
        return isVisible(cartOverlay);
    }
}
