package interfaces;


import java.util.List;
import org.openqa.selenium.WebElement;

import pages.BasePage;
import pages.CartPage;

public interface ICart {
    /**
     * Retrieves the list of items in the cart.
     * 
     * @return List of WebElement representing items in the cart.
     */
    List<WebElement> getCartItems();

    /**
     * Proceeds to checkout.
     * 
     * @return A page object representing the next page after proceeding to checkout.
     */
    BasePage proceedToCheckout();

    /**
     * Navigates to the cart page.
     * 
     * @return CartPage instance.
     */
    CartPage goToCartPage();
}

