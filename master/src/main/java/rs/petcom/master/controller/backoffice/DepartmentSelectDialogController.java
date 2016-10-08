package rs.petcom.master.controller.backoffice;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;

public class DepartmentSelectDialogController implements DialogController{

	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameters;
	ScreensController controller;
	Department department;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameters = parameter;
		controller = (ScreensController) parameter.get("controller");
		populateTable();
		
		tfFilter.setText("");
		tcName.setCellValueFactory(new PropertyValueFactory<Department, String>("Name"));
		tfFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateTable();
			}
		});
		tfFilter.requestFocus();
	}
	
	public HashMap<String, Object> getParameters(){
		return parameters;
	}

	@FXML BorderPane rootPane;
	@FXML TableView<Department> table;
	@FXML TableColumn<Department, String> tcName;
	
	@FXML TextField tfFilter;
	
	@FXML
	private void cancel(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		Node  source = (Node)  actionEvent.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	private void confirm(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_OK;
		department = table.getSelectionModel().getSelectedItem();
		parameters.put("department", department);
		Node  source = (Node)  actionEvent.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	
	private void populateTable(){
		table.setItems(Department.getList(controller.getObject(),
										  tfFilter.getText(), 
										  controller.getSettings().getConnection()));
		if (table.getItems().size() > 0)
			table.getSelectionModel().select(0);
	}
	
	@FXML
	private void mouseClick(MouseEvent event){
		if ((event.getClickCount() == 2)){
			modalResult = ScreensController.MODAL_RESULT_OK;
			department = table.getSelectionModel().getSelectedItem();
			parameters.put("department", department);
			Node  source = (Node)  event.getSource(); 
		    Stage stage  = (Stage) source.getScene().getWindow();
		    stage.close();
		}
	}

	@Override
	public HashMap<String, Object> getParameter() {
		return parameters;
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
