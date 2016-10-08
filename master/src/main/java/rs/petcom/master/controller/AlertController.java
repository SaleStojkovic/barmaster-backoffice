package rs.petcom.master.controller;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;

public class AlertController implements DialogController{

	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		labelTitle.setText(parameter.get("title").toString());
		taText.setText(parameter.get("text").toString());
	}

	@Override
	public HashMap<String, Object> getParameter() {
		return null;
	}

	@Override
	public int getModalResult() {
		return 0;
	}

	@Override
	public void setModalResult(int modalResult) {}
	
	@FXML private Label labelTitle;
	@FXML private TextArea taText;
	
	@FXML private void confirm(ActionEvent event){
	    ((Stage)(((Node) event.getSource()).getScene().getWindow())).close();
	}
}
