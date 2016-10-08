package rs.petcom.master.controller.backoffice;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.dal.person.PersonCategory;
import rs.petcom.master.dal.person.PersonType;

public class PersonNewDialogController implements DialogController{
	
	ScreensController controller;
	int modalResult;
	HashMap<String, Object> parameter;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		
		cbVatType.setItems(Vat.getObeservableList(controller.getSettings().getConnection()));
		cbCategory.setItems(PersonCategory.getList(controller.getSettings().getConnection()));
		cbType.setItems(PersonType.getList(controller.getSettings().getConnection()));
		
		cbVatType.getSelectionModel().select(0);
		cbCategory.getSelectionModel().select(0);
		cbType.getSelectionModel().select(0);
		cbActive.setSelected(true);
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
	
	
	@FXML private TextArea taName;
	@FXML private TextField tfFirstName;
	@FXML private TextField tfLastName;
	@FXML private TextField tfMB;
	@FXML private TextField tfPIB;
	@FXML private TextField tfCode;
	@FXML private ComboBox<Vat> cbVatType;
	@FXML private ComboBox<PersonCategory> cbCategory;
	@FXML private ComboBox<PersonType> cbType;
	@FXML private TextField tfIdBroj;
	@FXML private CheckBox cbActive;
	@FXML private TextField tfAddress;
	@FXML private TextField tfZip;
	@FXML private TextField tfCity;

	// Event Listener on Button.onAction
	@FXML
	public void cancel(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}
	// Event Listener on Button.onAction
	@FXML
	public void post(ActionEvent event) {
		Person p = new Person();
		
		p.setPersonTypeID(cbType.getSelectionModel().getSelectedItem().getPersonTypeID());
		p.setPersonCategoryID(cbCategory.getSelectionModel().getSelectedItem().getPersonCategoryID());
		p.setPersonVatTypeID(cbVatType.getSelectionModel().getSelectedItem().getVatID());
		p.setFirstName(tfFirstName.getText());
		p.setLastName(tfLastName.getText());
		p.setIdentificationNumber(tfIdBroj.getText());
		p.setName(taName.getText());
		p.setPIB(tfPIB.getText());
		p.setMB(tfMB.getText());
		p.setActive(true);
		p.setSysUserAddID(controller.getUser().getUserID());
		p.setCode(tfCode.getText());
		long personID = p.insert(controller.getUser(), controller.getSettings().getConnection());
		p.setPersonID(personID);
		
		Address a = new Address();
		a.setAddressTypeID(1);
		a.setPersonID(personID);
		a.setAddress(tfAddress.getText());
		a.setCity(tfCity.getText());
		a.setZip(tfZip.getText());
		a.setIsPrimary(true);
		a.setActive(true);
		a.setSysUserAddID(controller.getUser().getUserID());
		a.setPersonID(a.insert(controller));
		
		parameter.put("person", p);
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}
	
}
