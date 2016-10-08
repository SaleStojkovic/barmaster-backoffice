package rs.petcom.master.controller;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;

public class YesNoDialogController implements DialogController{
	
	@FXML private BorderPane rootPane;
	@FXML private Button dialogNo;
	@FXML private Button dialogYes;
	@FXML private Label dialogText;
	@FXML private Label dialogTitle;

	@FXML public void closeAction(ActionEvent event) {
		if (event.getSource() == dialogNo)
			modalResult = ScreensController.MODAL_RESULT_NO;
		else
			modalResult = ScreensController.MODAL_RESULT_YES;
		((Stage) rootPane.getScene().getWindow()).close();
	}

	HashMap<String, Object> parameter;
	int modalResult;
	
	public YesNoDialogController(){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		dialogTitle.setText(parameter.get("title").toString());
		dialogText.setText(parameter.get("text").toString());
		
	}

	@Override
	public HashMap<String, Object> getParameter() {
		return parameter;
	}
	@Override
	public int getModalResult() {
		return modalResult;
	}
	@Override
	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}
}
