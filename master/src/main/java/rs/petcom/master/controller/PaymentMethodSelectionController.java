package rs.petcom.master.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PaymentMethod;

public class PaymentMethodSelectionController implements DialogController{

	private ScreensController controller;
	HashMap<String, Object> parameter;
	int modalResult;
	
	ArrayList<PaymentMethod> methodList;
	ArrayList<RadioButton> buttonList = new ArrayList<RadioButton>();
	
	@FXML BorderPane rootPane;
	@FXML VBox listPane;
	@FXML GridPane napomenaPane;
	@FXML TextArea taNapomena;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		controller = (ScreensController) parameter.get("controller");
	
		listPane.getChildren().clear();
		methodList = PaymentMethod.getSubList(controller.getCurrentBill().getPaymetnMethodID(),controller.getSettings().getConnection());
		
		ToggleGroup tg = new ToggleGroup();
		buttonList.clear();
		taNapomena.setText("");
		napomenaPane.setVisible(false);
		for(PaymentMethod pm : methodList){
			RadioButton rb = new RadioButton(pm.getName());
			rb.setMinWidth(300);
			rb.setPrefWidth(300);
			rb.setMaxWidth(300);
			rb.setToggleGroup(tg);
			rb.getStyleClass().add("paymentRadioButton");
			rb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(rb.isSelected() && (rb.getText().equals("Ostali otpisi") || rb.getText().equals("Ostala davanja"))){
						napomenaPane.setVisible(true);
					} else {
						napomenaPane.setVisible(false);
						taNapomena.setText("");
					}
			
				}
			});
			listPane.getChildren().add(rb);
			buttonList.add(rb);
		}
		buttonList.get(0).setSelected(true);
	}
	@Override
	public HashMap<String, Object> getParameter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getModalResult() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setModalResult(int modalResult) {
		// TODO Auto-generated method stub
		
	}
	

	@FXML
	private void comitAction(){
		for(int i =0;i<buttonList.size();i++){
			if (buttonList.get(i).isSelected()){
				controller.getCurrentBill().setPaymentMethod(methodList.get(i));
				controller.getCurrentBill().setNapomena(taNapomena.getText());
				break;
			}
		}
		
		modalResult = ScreensController.MODAL_RESULT_YES;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
	@FXML
	private void cancelAction(){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
}
