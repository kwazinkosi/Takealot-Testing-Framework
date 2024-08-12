package components;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartOverlay extends BaseComponent {

	
	@FindBy(xpath = "//div[@class='cart-module_item_3MErs'] | //div[@class ='cart-item-container-module_item_3Vkqc']")
	List<WebElement> cartItems; 
	
	@FindBy(css = ".checkout-button[data-ref='checkout-button']")
	WebElement checkoutBtn; 
	
	@FindBy(css =".button.cart.pay")
	WebElement goToCart;
	
	@FindBy(css =".drawer-module_drawer-outer_3hLuY[data-ref='drawer-outer']")
	WebElement cartOverlay;
	
	public CartOverlay(WebElement root) {
		
		super(root);
		
	}
	
	
	// TODO: perhaps should return an Overlay object or cart object
	public void overlayGoToCart() {
		
	}
	
	// TODO: perhaps should return the loginPage if the login modal is diplayed after clicking
	public goToCheckout() {
		
	}
	
	public List<WebElement> getCartItems(){
		
	}
	
}
