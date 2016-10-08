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
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.dal.person.PersonCategory;

public class PersonSelectDialogController implements DialogController{

	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameters;
	ScreensController controller;
	Person person;
	PersonCategory personCategory;
	
	public void initialize(){
		tcName.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
		tcFirstName.setCellValueFactory(new PropertyValueFactory<Person, String>("FirstName"));
		tcLastName.setCellValueFactory(new PropertyValueFactory<Person, String>("LastName"));
		tfFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateTable();
			}
		});
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameters = parameter;
		this.controller = (ScreensController) parameter.get("controller");
		this.personCategory = (PersonCategory) parameter.get("personCategory");
		populateTable();
		
		tfFilter.setText("");
		tcName.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
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
	@FXML TableView<Person> table;
	@FXML TableColumn<Person, String> tcName;
	@FXML TableColumn<Person, String> tcFirstName;
	@FXML TableColumn<Person, String> tcLastName;
	
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
		person = table.getSelectionModel().getSelectedItem();
		parameters.put("person", person);
		((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	
	private void populateTable(){
		table.setItems(Person.getList(personCategory, 
									  true,
									  tfFilter.getText(),
									  controller.getSettings().getConnection()));
		if (table.getItems().size() > 0)
			table.getSelectionModel().select(0);
	}
	
	@FXML
	private void mouseClick(MouseEvent event){
		if ((event.getClickCount() == 2)){
			modalResult = ScreensController.MODAL_RESULT_OK;
			person = table.getSelectionModel().getSelectedItem();
			parameters.put("person", person);
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
		}
	}
	
	@FXML void newPerson(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int result = controller.showModalDialog("Dodavanje komitenta",ScreensController.DIALOG_ADD_PERSON, hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			table.getItems().add((Person)hm.get("person"));
			table.getSelectionModel().select((Person)hm.get("person"));
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
