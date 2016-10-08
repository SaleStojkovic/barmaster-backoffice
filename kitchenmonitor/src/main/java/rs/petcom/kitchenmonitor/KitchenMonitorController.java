package rs.petcom.kitchenmonitor;

import java.net.URL;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.document.Order;

public class KitchenMonitorController implements ControlledScreen{
	
	ScreensController controller;
	
	private ArrayList<KitchenOrder> orderList = new ArrayList<KitchenOrder>();
	
	final Timeline timerCheckOrders = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {  
	     public void handle(ActionEvent event) {    
	          checkOrders();
	     }  
	}));
	
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	public void init() {
		timerCheckOrders.setCycleCount(Animation.INDEFINITE);
		timerCheckOrders.play();
		
		scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        workPane.setMinWidth(scrollPane.getWidth() - 20);
		    }
		});
	}

	public void setParameter(Object parameter) {}

	public Object getParameter() {
		return null;
	}

	@FXML BorderPane borderPane;
	@FXML TilePane workPane;
	@FXML ScrollPane scrollPane;
	
	private void checkOrders(){
		ArrayList<Order> lista = Order.getList(controller);
		
		// Update porudžbina i brisanje završenih
		for( int i=orderList.size()-1;i>=0;i--){
			boolean postoji = false;
			KitchenOrder ko = orderList.get(i);
			for(Order o : lista){
				if (o.getOrderID() == ko.getOrder().getOrderID()){
					ko.setOrder(o);
					postoji = true;
					break;
				}
			}
			if (!postoji){
				orderList.remove(ko);
				workPane.getChildren().remove(ko);
			}
		}
		
		// Dodavanje novih porudžbina
		for(Order o : lista){
			boolean postoji = false;
			for (KitchenOrder ko : orderList){
				if (o.getOrderID() == ko.getOrder().getOrderID()){
					postoji = true;
					break;
				}
			}
			if (!postoji){
				KitchenOrder newKitchenOrder = new KitchenOrder(controller);
				newKitchenOrder.setOrder(o);
				orderList.add(newKitchenOrder);
				workPane.getChildren().add(newKitchenOrder);
				
				 URL resource = getClass().getResource("/audio/bikehorn.mp3");
				 Media media = new Media(resource.toString());
				 MediaPlayer mediaPlayer = new MediaPlayer(media);
				 mediaPlayer.play();
			}
		}
	}
}
