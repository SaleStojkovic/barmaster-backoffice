package rs.petcom.master.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.util.Duration;
import rs.petcom.master.dal.product.ProductDepartment;

public class ProductButton extends Button{
	
	private Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {  
	     public void handle(ActionEvent event) {   
	    	 if (productDepartemnt != null)
	    		 setDisable(!productDepartemnt.isEnabled());
	    	 else
	    		 setDisable(false);
	     }  
	}));
	
	public ProductButton(){
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
	private ProductDepartment productDepartemnt;

	public ProductDepartment getProductDepartemnt() {
		return productDepartemnt;
	}

	public void setProductDepartemnt(ProductDepartment productDepartemnt) {
		this.productDepartemnt = productDepartemnt;
		if (productDepartemnt != null) 
			setDisable(!productDepartemnt.isEnabled());
		else
			setDisable(false);
	}
	

}
