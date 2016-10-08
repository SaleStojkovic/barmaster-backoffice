package rs.petcom.master.controller;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;

public class AlertScreenController implements ControlledScreen{
	
	ScreensController controller;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void setParameter(Object parameter) {
		// TODO Auto-generated method stub
	}
	@Override
	public Object getParameter() {
		return null;
	}
	@Override
	public void init() {
		ResourceBundle bundle = ResourceBundle.getBundle("locale");
		dialogTitle.setText(bundle.getString(controller.getDialogTitle()));
		dialogText.setText(bundle.getString(controller.getDialogText()));
	}
	
	@FXML BorderPane rootPane;
	@FXML Label dialogTitle;
	@FXML Label dialogText;
	@FXML Button dialogButton1;
	
	@FXML
	private void closeAction(){
		controller.getScreen(controller.getSender(), controller.getRoot(),ScreensController.SCREEN_ALLERT);
	}

	
}
