package rs.petcom.master.controller.backoffice;

import java.sql.Connection;
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
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.Product;

public class GroupSelectDialogController implements DialogController{

	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameters;
	Connection connection;
	Product product;
	Group group;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameters = parameter;
		group = (Group) parameter.get("group");
		product = (Product) parameter.get("product");
		connection = (Connection) parameter.get("connection");
		populateTable();
		
		tfFilter.setText("");
		tcName.setCellValueFactory(new PropertyValueFactory<Group, String>("Name"));
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
	@FXML TableView<Group> table;
	@FXML TableColumn<Group, String> tcName;
	
	@FXML TextField tfFilter;
	
	@FXML
	private void cancel(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	
	@FXML
	private void confirm(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_OK;
		group = table.getSelectionModel().getSelectedItem();
		parameters.put("group", group);
		((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	
	private void populateTable(){
		table.setItems(Group.getInsertList((PointOfSale) parameters.get("PointOfSale"),
										   product,
										   tfFilter.getText(), 
										   connection));
		if (table.getItems().size() > 0)
			table.getSelectionModel().select(0);
	}
	
	@FXML
	private void mouseClick(MouseEvent event){
		if ((event.getClickCount() == 2)){
			modalResult = ScreensController.MODAL_RESULT_OK;
			group = table.getSelectionModel().getSelectedItem();
			parameters.put("group", group);
			((Stage) rootPane.getScene().getWindow()).close();
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
