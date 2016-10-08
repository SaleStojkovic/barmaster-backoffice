package rs.petcom.master.controller.backoffice;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentDetails;
import rs.petcom.master.dal.document.DocumentStatus;
import rs.petcom.master.dal.document.DocumentType;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.dal.person.PersonCategory;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.gui.EditingTableCellDouble;

public class DocumentDetailesController implements DialogController{
	
	ScreensController controller;
	HashMap<String, Object> parameter; 
	Document document;
	ObservableList<DocumentStatus> statusList = FXCollections.observableArrayList();
	int modalResult;
	
	Person person;
	
	Callback<TableColumn<Object, Double>, TableCell<Object, Double>> doubleCellFactory =
            new Callback<TableColumn<Object, Double>, TableCell<Object, Double>>() {
                public TableCell<Object, Double> call(TableColumn<Object, Double> p) {
                    return new EditingTableCellDouble(2, true);
                }
            };
	Callback<TableColumn<Object, Double>, TableCell<Object, Double>> doubleCellFactory4 =
	        new Callback<TableColumn<Object, Double>, TableCell<Object, Double>>() {
	            public TableCell<Object, Double> call(TableColumn<Object, Double> p) {
	                return new EditingTableCellDouble(4, true);
	            }
	        };
	        
	public void initialize(){
		tcProduct.setCellValueFactory(new PropertyValueFactory<DocumentDetails, String>("ProductName"));
		
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Object, Double>("Quantity"));
		tcQuantity.setCellFactory(doubleCellFactory4);
		tcQuantity.setOnEditCommit(
            (CellEditEvent<Object, Double> t) -> {
            	DocumentDetails dd = detailsTable.getSelectionModel().getSelectedItem();
            	dd.setQuantity(t.getNewValue());
            	dd.setValue(dd.getQuantity() * dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	dd.setPriceWithDiscount(dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	detailsTable.getEditingCell().getColumn();
            	detailsTable.getItems().set(detailsTable.getSelectionModel().getSelectedIndex(), dd);
        });
		
		tcPrice.setCellValueFactory(new PropertyValueFactory<Object, Double>("Price"));
		tcPrice.setCellFactory(doubleCellFactory);
		tcPrice.setOnEditCommit(
            (CellEditEvent<Object, Double> t) -> {
            	DocumentDetails dd = detailsTable.getSelectionModel().getSelectedItem();
            	dd.setPrice(t.getNewValue());
            	dd.setValue(dd.getQuantity() * dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	dd.setPriceWithDiscount(dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	detailsTable.getEditingCell().getColumn();
            	detailsTable.getItems().set(detailsTable.getSelectionModel().getSelectedIndex(), dd);
        });
		
		tcDiscount.setCellValueFactory(new PropertyValueFactory<Object, Double>("Discount"));
		tcDiscount.setCellFactory(doubleCellFactory);
		tcDiscount.setOnEditCommit(
            (CellEditEvent<Object, Double> t) -> {
            	DocumentDetails dd = detailsTable.getSelectionModel().getSelectedItem();
            	dd.setDiscount(t.getNewValue());
            	dd.setValue(dd.getQuantity() * dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	dd.setPriceWithDiscount(dd.getPrice() * (1 - dd.getDiscount() / 100) );
            	detailsTable.getEditingCell().getColumn();
            	detailsTable.getItems().set(detailsTable.getSelectionModel().getSelectedIndex(), dd);
        });
		
		tcValue.setCellValueFactory(new PropertyValueFactory<Object, Double>("Value"));
		
		detailsTable.getSelectionModel().setCellSelectionEnabled(true);
		detailsTable.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(Event event) {
				@SuppressWarnings("rawtypes")
				TablePosition focusedCellPosition = detailsTable.getFocusModel().getFocusedCell();
				detailsTable.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
		        
			}
		});
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		statusList = DocumentStatus.getList(controller.getSettings().getConnection());
		cbStatus.setItems(statusList);
		if (parameter.get("document") != null){
			this.document = (Document) parameter.get("document");
		}
		else{
			this.document = new Document();
			this.document.setDate(new Date());
			this.document.setDeliveryDate(new Date());
			this.document.setCurrencyDate(new Date());
			this.document.setPersonID(0);
			this.document.setPersonName("");
			this.document.setDocumentStatusID(0);
			this.document.setDocumentStatusID(1);
			this.document.setDepartmentID(controller.getSettings().getDepartmentId());
		}
		fillDocData();
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

	@FXML private BorderPane rootPane;
	@FXML private TextField tfNumber;
	@FXML private TextField tfExternalNumber;
	@FXML private DatePicker dpDate;
	@FXML private DatePicker dpDeliveryDate;
	@FXML private DatePicker dpPaymentDate;
	@FXML private TextField tfPerson;
	@FXML private TextField tfMesto;
	@FXML private ComboBox<DocumentStatus> cbStatus;
	@FXML private TextArea taNote;
	
	@FXML private TableView<DocumentDetails> detailsTable;
	@FXML private TableColumn<DocumentDetails, String> tcProduct;
	@FXML private TableColumn<Object, Double> tcQuantity;
	@FXML private TableColumn<Object, Double> tcPrice;
	@FXML private TableColumn<Object, Double> tcDiscount;
	@FXML private TableColumn<Object, Double> tcValue;
	
	@FXML private void comit(ActionEvent event){
		
		modalResult = ScreensController.MODAL_RESULT_OK;
		document.setDocumentTypeID(((DocumentType) parameter.get("documentType")).getDocumentTypeID() );
		document.setDocumentStatusID(cbStatus.getSelectionModel().getSelectedItem().getDocumentStatusID());
		if (controller.getPointOfSale() != null){
			document.setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
			document.setNumber(Document.getNextNumber(((DocumentType) parameter.get("documentType")).getDocumentTypeID(),
					  		   						  controller.getPointOfSale().getPointOfSaleID(),
					  		   						  controller));
		}		
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		document.setYear(c.get(Calendar.YEAR));
		LocalDateTime ldt = LocalDateTime.of(dpDate.getValue(), LocalTime.NOON);
		document.setDate(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
		document.setDeliveryDate(Settings.fromLocalDate(dpDeliveryDate.getValue()));
		document.setCurrencyDate(Settings.fromLocalDate(dpPaymentDate.getValue()));
		document.setNote(taNote.getText());
		document.setPlace(tfMesto.getText());
		document.setPersonID(person.getPersonID());
		document.setExternalNumber(tfExternalNumber.getText());
		document.setSysUserAddID(controller.getUser().getUserID());
		
		double Sum = 0;
		double SumWithDiscount = 0;
		for (DocumentDetails docD : detailsTable.getItems()){
			Sum += docD.getQuantity() * docD.getPrice();
			SumWithDiscount += docD.getQuantity() * docD.getPrice() * (1 - docD.getDiscount() / 100);
		}
		
		document.setSum(Sum);
		document.setSumWithDiscount(SumWithDiscount);
		document.setPaid(Sum);
		
		if (document.getDocumentID() == 0){
			long docId = document.insert(controller.getSettings().getConnection());
			
			for (DocumentDetails docD : detailsTable.getItems()){
				docD.setDocumentID(docId);
				docD.setSysUserAddID(controller.getUser().getUserID());
				docD.insert(controller.getSettings().getConnection());
			}
			
			((Stage)rootPane.getScene().getWindow()).close(); 
		}
		else{
			DocumentDetails.deleteDetailes(document, controller.getSettings().getConnection());
			for (DocumentDetails docD : detailsTable.getItems()){
				docD.setDocumentID(document.getDocumentID());
				docD.setSysUserAddID(controller.getUser().getUserID());
				docD.insert(controller.getSettings().getConnection());
			}
			
			((Stage)rootPane.getScene().getWindow()).close(); 
		}
	}
	@FXML private void cancel(ActionEvent event){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage)((Node) event.getSource()).getScene().getWindow()).close(); 
	}
	@FXML private void delete(ActionEvent event){
		/*
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage)((Node) event.getSource()).getScene().getWindow()).close(); 
		*/
	}
	@FXML private void selectPerson(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		hm.put("personCategory", PersonCategory.getByID(3,controller.getSettings().getConnection()));
		int result = controller.showModalDialog("IZbor komitenta",ScreensController.DIALOG_SELECT_PERSON, hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			person = (Person) hm.get("person");
			tfPerson.setText(person.toString());
			document.setPersonID(person.getPersonID());
			document.setPersonName(person.toString());
		}
	}
	
	@FXML private void tableMuseClick(){
		/*
		if (detailsTable.getItems().size() == 0){
			DocumentDetails ndd = new DocumentDetails();
			detailsTable.getItems().add(ndd);
		}
		*/
	}
	
	@FXML private void detaileAdd(){
		HashMap<String,Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT, hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			Product p = (Product) hm.get("product");
			DocumentDetails dd = new DocumentDetails();
			dd.setProductID(p.getProductID());
			dd.setProductName(p.getName());
			dd.setProductShortName(p.getShortName());
			dd.setUnitID(p.getUnitID());
			dd.setVatID(p.getVatID());
			dd.setQuantity(0);
			dd.setPrice(0);
			detailsTable.getItems().add(dd);
		}
		
		for(DocumentDetails dd1 : detailsTable.getItems()/* detailesList*/){
			System.out.println(dd1.getProductName() + " > " +
							   dd1.getQuantity() + " > " +
							   dd1.getPrice());
		}
	}
	@FXML private void detaileRemove(){
		detailsTable.getItems().remove(detailsTable.getSelectionModel().getSelectedItem());
	}
	
	private void fillDocData(){
		if (person == null){
			person = new Person();
			person.setPersonID(document.getPersonID());
			person.setName(document.getPersonName());
		}
		if (document.getNumber() != 0)
			tfNumber.setText(String.valueOf(document.getNumber()));
		else 
			tfNumber.setText("");
		tfExternalNumber.setText(document.getExternalNumber());
		dpDate.setValue(Settings.fromDate(document.getDate()));
		dpDeliveryDate.setValue(Settings.fromDate(document.getDeliveryDate()));
		dpPaymentDate.setValue(Settings.fromDate(document.getCurrencyDate()));
		tfPerson.setText(document.getPersonName());
		
		tfMesto.setText(document.getPlace());
		cbStatus.getSelectionModel().select(getStatusByID(document.getDocumentStatusID()));
		
		if (document.getDocumentID() != 0)
			detailsTable.setItems(DocumentDetails.getObeservableList(document.getDocumentID(),controller.getSettings().getConnection()));
			//detailesList = DocumentDetails.getObeservableList(document.getDocumentID(),controller.getSettings().getConnection());
		else
			detailsTable.setItems(FXCollections.observableArrayList());
			//detailesList.clear();
		//detailsTable.setItems(detailesList);
	}
	
	private DocumentStatus getStatusByID(int id){
		for (int i=0;i<statusList.size();i++){
			if (statusList.get(i).getDocumentStatusID() == id){
				return statusList.get(i);
			}
		}
		return null;
	}
}
