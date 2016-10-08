package rs.petcom.kitchenmonitor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.document.Order;

public class KitchenOrder extends BorderPane{
	
	private Order order;
	private PorudzbinaController orderController = new PorudzbinaController();
	
	public KitchenOrder(ScreensController controller){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ScreensController.SCREEN_KITCHEN_PORUDZBINA),
																	  controller.getBundle());
		try {
			setCenter((Parent)fxmlLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		orderController = fxmlLoader.getController();
		orderController.setScreenParent(controller);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
		orderController.setOrder(order);
		
	}
	
	public PorudzbinaController getController(){
		return orderController;
	}
	
}
