package rs.petcom.master.controller.backoffice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.dal.person.PersonCategory;
import rs.petcom.master.dal.person.PersonType;
import rs.petcom.master.dal.person.PersonVatType;

public class PersonsController implements ControlledScreen{
	
	ScreensController controller;
	ObservableList<Person> personsList = FXCollections.observableArrayList();
	ObservableList<PersonType> personTypeList = FXCollections.observableArrayList();
	ObservableList<PersonCategory> personCategoryList = FXCollections.observableArrayList();
	ObservableList<PersonVatType> personVatTypeList = FXCollections.observableArrayList();
	
	Person selectedPerson;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.controller = screenPage;
	}

	@Override
	public void init() {
		personTypeList = PersonType.getList(controller.getSettings().getConnection());
		personCategoryList = PersonCategory.getList(controller.getSettings().getConnection());
		personVatTypeList = PersonVatType.getList(controller.getSettings().getConnection()); 
		cbType.setItems(personTypeList);
		cbCategory.setItems(personCategoryList);
		cbVatType.setItems(personVatTypeList);
		
		loadPersonsList();
	}

	@Override
	public void setParameter(Object parameter) {}

	@Override
	public Object getParameter() {
		return null;
	}
	
	public void initialize(){
		tcName.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
		tcFirstName.setCellValueFactory(new PropertyValueFactory<Person, String>("FirstName"));
		tcLastName.setCellValueFactory(new PropertyValueFactory<Person, String>("LastName"));
		
		 tvPerson.getSelectionModel()
         		 .selectedItemProperty()
         		 .addListener((observableValue, oldValue, newValue) -> {
         			 selectedPerson = newValue;
         			 fillPersonData();
         });
	}
	
	@FXML private TextArea taName;
	@FXML private TextField tfFirstName;
	@FXML private TextField tfLastName;
	@FXML private TextField tfPIB;
	@FXML private TextField tfIdBroj;
	@FXML private TextField tfMB;
	@FXML private TextField tfCode;
	@FXML private ComboBox<PersonType> cbType;
	@FXML private ComboBox<PersonCategory> cbCategory;
	@FXML private ComboBox<PersonVatType> cbVatType;
	@FXML private CheckBox cbActive;
	@FXML private TableView<Person> tvPerson;
	@FXML private TableColumn<Person, String> tcName;
	@FXML private TableColumn<Person, String> tcFirstName;
	@FXML private TableColumn<Person, String> tcLastName;

	@FXML public void personNew(ActionEvent event) {
		selectedPerson = new Person();
		fillPersonData();
	}

	@FXML public void personDelete(ActionEvent event) {
		selectedPerson.delete(controller.getUser(), controller.getSettings().getConnection());
		personsList.remove(selectedPerson);
	}
	
	@FXML public void personCommit(ActionEvent event) {
		if (selectedPerson == null){
			selectedPerson = new Person();
		}
		selectedPerson.setName(taName.getText());
		selectedPerson.setFirstName(tfFirstName.getText());
		selectedPerson.setLastName(tfLastName.getText());
		selectedPerson.setPIB(tfPIB.getText());
		selectedPerson.setIdentificationNumber(tfIdBroj.getText());
		selectedPerson.setMB(tfMB.getText());
		selectedPerson.setCode(tfCode.getText());
		selectedPerson.setPersonTypeID(cbType.getValue().getPersonTypeID());
		selectedPerson.setPersonCategoryID(cbCategory.getValue().getPersonCategoryID());
		selectedPerson.setPersonVatTypeID(cbVatType.getValue().getPersonVatTypeID());
		selectedPerson.setActive(cbActive.isSelected());
		if (selectedPerson.getPersonID() > 0){
			selectedPerson.update(controller.getUser(), controller.getSettings().getConnection());
			personsList.set(tvPerson.getSelectionModel().getSelectedIndex(), selectedPerson);
		}
		else{
			selectedPerson.insert(controller.getUser(), controller.getSettings().getConnection());
			personsList.add(selectedPerson);
		}
	}

	private void loadPersonsList(){
		personsList = Person.getList(true, controller.getSettings().getConnection());
		tvPerson.setItems(personsList);
	}
	
	private void fillPersonData(){
		try{
			taName.setText(selectedPerson.getName());
			tfFirstName.setText(selectedPerson.getFirstName());
			tfLastName.setText(selectedPerson.getLastName());
			tfPIB.setText(selectedPerson.getPIB());
			tfIdBroj.setText(selectedPerson.getIdentificationNumber());
			tfMB.setText(selectedPerson.getMB());
			tfCode.setText(selectedPerson.getCode());
			cbType.setValue(getPersonTypeByIndex(selectedPerson.getPersonTypeID()));
			cbCategory.setValue(getPersonCategoryByIndex(selectedPerson.getPersonCategoryID()));
			cbVatType.setValue(getPersonVatTypeByIndex(selectedPerson.getPersonVatTypeID()));
			cbActive.setSelected(selectedPerson.isActive());
		} catch (NullPointerException e){}
	}
	
	private PersonVatType getPersonVatTypeByIndex(int id){
		for (PersonVatType pc : personVatTypeList){
			if (pc.getPersonVatTypeID() == id)
				return pc;
		}
		return null;
	}	
	private PersonType getPersonTypeByIndex(int id){
		for (PersonType pc : personTypeList){
			if (pc.getPersonTypeID() == id)
				return pc;
		}
		return null;
	}	
	private PersonCategory getPersonCategoryByIndex(int id){
		for (PersonCategory pc : personCategoryList){
			if (pc.getPersonCategoryID() == id)
				return pc;
		}
		return null;
	}
}
