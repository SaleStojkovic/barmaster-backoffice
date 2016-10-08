package rs.petcom.master.controller.backoffice;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Description;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

public class OpisniSelectDialogController implements DialogController {
	
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameters;
	Connection connection;
	Description description;
	
	@FXML private BorderPane rootPane;
	@FXML private TextField tfFilter;
	@FXML private TableView<Description> table;
	@FXML private TableColumn<Description, Integer> tcId;
	@FXML private TableColumn<Description, String> tcName;
	
	ObservableList<Description> descriptionList;
	
	

	// Event Listener on Button.onAction
	@FXML
	public void cancel(ActionEvent actionEvent) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	// Event Listener on Button.onAction
	@FXML
	public void confirm(ActionEvent actionEvent) {
		modalResult = ScreensController.MODAL_RESULT_OK;
		description = table.getSelectionModel().getSelectedItem();
		parameters.put("description", description);
		((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	// Event Listener on TableView[#table].onMouseClicked
	@FXML
	public void mouseClick(MouseEvent event) {
		if ((event.getClickCount() == 2)){
			modalResult = ScreensController.MODAL_RESULT_OK;
			description = table.getSelectionModel().getSelectedItem();
			parameters.put("description", description);
			((Stage) rootPane.getScene().getWindow()).close();
		}
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameters = parameter;
		description = (Description) parameter.get("description");
		connection = (Connection) parameter.get("connection");
		populateTable();
		
		tcId.setCellValueFactory(new PropertyValueFactory<Description, Integer>("DescriptionID"));
		tcName.setCellValueFactory(new PropertyValueFactory<Description, String>("Name"));
		tfFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateTable();
			}
		});
		
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
	
	private void populateTable(){
		table.setItems(Description.getObeservableList(tfFilter.getText(),connection));
		
		if (table.getItems().size() > 0)
			table.getSelectionModel().select(0);
		}
	}
