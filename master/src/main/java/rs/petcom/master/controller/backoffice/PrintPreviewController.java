package rs.petcom.master.controller.backoffice;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;

public class PrintPreviewController implements DialogController{
	private int modalResult;
	HashMap<String, Object> parameter;

	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		BorderPane report = (BorderPane)parameter.get("report") ;
		rootPane.setCenter(report);
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

	@FXML BorderPane rootPane;
	
	@FXML private void close(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
	}
}
