package rs.petcom.backoffice.controller.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.DocumentStatus;
import rs.petcom.master.dal.document.Prijemnica;
import rs.petcom.master.dal.document.PrijemnicaDetailes;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.dal.person.PersonCategory;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.gui.AutoCompleteComboBoxListener;
import rs.petcom.master.gui.DoubleTableCellFactory;
import tornadofx.control.DateTimePicker;

public class PrijemnicaDetailesController implements DialogController{
	
	ScreensController controller;
	Prijemnica prijemnica;
	int modalResult;
	
	ArrayList<PrijemnicaDetailes> deletedItems = new ArrayList<>();
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		
		
		controller = (ScreensController) parameter.get("controller");
		
		PersonCategory pc = PersonCategory.getByID(3, controller.getSettings().getConnection());
		cbPerson.setItems(Person.getList(pc,true,"",controller.getSettings().getConnection()));
		
		new AutoCompleteComboBoxListener<>(cbPerson);
		
		
		cbStatus.setItems(DocumentStatus.getList(controller.getSettings().getConnection()));
		
		prijemnica = (Prijemnica) parameter.get("prijemnica");
		tfNumber.setText(String.valueOf(prijemnica.getNumber()));
		tfExternalNumber.setText(prijemnica.getExternalNumber());
		dpDate.setDateTimeValue(Settings.LocalDateTimefromDate(prijemnica.getDate()));
		dpDeliveryDate.setValue(Settings.fromDate(prijemnica.getDeliveryDate()));
		dpCurrencyDate.setValue(Settings.fromDate(prijemnica.getPaymentDate()));
		setDobavljac(prijemnica.getDobavljac());
		tfPlace.setText(prijemnica.getPlace());
		setStatus(prijemnica.getDocumentStatus());
		tfPlace.setText(prijemnica.getPlace());
		tfNapomena.setText(prijemnica.getComment());
		
		
		table.setItems(PrijemnicaDetailes.getlist(prijemnica, controller.getSettings().getConnection()));
		deletedItems.clear();
		tfNapomena.setText(prijemnica.getComment());
		
	}
	@Override
	public HashMap<String, Object> getParameter() {
		return null;
	}
	@Override
	public int getModalResult() {
		return modalResult;
	}
	@Override
	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}
	
	public void initialize(){
		
		DoubleTableCellFactory cellFactory = new DoubleTableCellFactory(4, true);
		DoubleTableCellFactory cellFactory2 = new DoubleTableCellFactory(2, true);
		tcCode.setCellValueFactory(new PropertyValueFactory<PrijemnicaDetailes, String>("ProductCode"));
		tcName.setCellValueFactory(new PropertyValueFactory<PrijemnicaDetailes, String>("ProductName"));
		
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Object, Double>("Quantity"));
		tcQuantity.setCellFactory(cellFactory);
		tcQuantity.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                    ((PrijemnicaDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setQuantity(t.getNewValue());
	                }
	            }
	        );
		
		tcPrice.setCellValueFactory(new PropertyValueFactory<Object, Double>("Price"));
		tcPrice.setCellFactory(cellFactory2);
		tcPrice.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                	PrijemnicaDetailes pd = (PrijemnicaDetailes) t.getTableView().getItems().get(t.getTablePosition().getRow());
	                	pd.setPrice(t.getNewValue());
	                	pd.setPriceWithDiscount(pd.getPrice() * (1-(pd.getDiscount()/100)));
	                	t.getTableColumn().getTableView().refresh();
	                }
	            }
	        );
		
		tcDiscount.setCellValueFactory(new PropertyValueFactory<Object, Double>("Discount"));
		tcDiscount.setCellFactory(cellFactory2);
		tcDiscount.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                	PrijemnicaDetailes pd = (PrijemnicaDetailes) t.getTableView().getItems().get(t.getTablePosition().getRow());
	                	pd.setDiscount(t.getNewValue());
	                	pd.setPriceWithDiscount(pd.getPrice() * (1-(pd.getDiscount()/100)));
	                	t.getTableColumn().getTableView().refresh();
	                }
	            }
	        );
		
		tcPriceWithDiscount.setCellValueFactory(new PropertyValueFactory<Object, Double>("PriceWithDiscount"));
		tcPriceWithDiscount.setCellFactory(cellFactory2);
		tcPriceWithDiscount.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                	PrijemnicaDetailes pd = (PrijemnicaDetailes) t.getTableView().getItems().get(t.getTablePosition().getRow());
	                	pd.setPriceWithDiscount(t.getNewValue());
	                	pd.setPrice(pd.getPriceWithDiscount() / (1-(pd.getDiscount()/100)));
	                	t.getTableColumn().getTableView().refresh();
	                }
	            }
	        );
	}
	
	@FXML private BorderPane rootPane;
	
	@FXML private TextField tfNumber;
	@FXML private TextField tfExternalNumber;
	@FXML private DateTimePicker dpDate;
	@FXML private DatePicker dpDeliveryDate;
	@FXML private DatePicker dpCurrencyDate;
	@FXML private ComboBox<Person> cbPerson;
	@FXML private TextField tfPlace;
	@FXML private ComboBox<DocumentStatus> cbStatus;
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	@FXML private TableView<PrijemnicaDetailes> table;
	@FXML private TableColumn<PrijemnicaDetailes, String> tcCode;
	@FXML private TableColumn<PrijemnicaDetailes, String>  tcName;
	@FXML private TableColumn<Object, Double>  tcQuantity;
	@FXML private TableColumn<Object, Double>  tcPrice;
	@FXML private TableColumn<Object, Double>  tcDiscount;
	@FXML private TableColumn<Object, Double>  tcPriceWithDiscount;
	@FXML private TextArea tfNapomena;

	@FXML public void actionClose(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionSave(ActionEvent event) {
		
		if ((prijemnica.getDocumentID() == -1) &&
			 Prijemnica.exists(tfExternalNumber.getText(), controller.getSettings().getConnection())){
			controller.alert("Prijemnica već postoji", 
							 "Prijemnca sa unetim brojem već postoji" , 
							 rootPane.getScene().getWindow());
			return;
		}
		
		//Snimanje dokumenta
		
		prijemnica.setNumber(Integer.parseInt(tfNumber.getText()));
		prijemnica.setDate(Settings.DatefromLocalDateTime(dpDate.getDateTimeValue()));
		prijemnica.setDeliveryDate(Settings.fromLocalDate(dpDeliveryDate.getValue()));
		prijemnica.setPaymentDate(Settings.fromLocalDate(dpCurrencyDate.getValue()));
		prijemnica.setDobavljac(cbPerson.getItems().get(cbPerson.getSelectionModel().getSelectedIndex()));
		prijemnica.setPlace(tfPlace.getText());
		prijemnica.setDocumentStatus(cbStatus.getSelectionModel().getSelectedItem());
		prijemnica.setComment(tfNapomena.getText());
		prijemnica.setExternalNumber(tfExternalNumber.getText());
		
		if (prijemnica.getDocumentID() > 0)
			prijemnica.update(controller);
		else
			try {
				prijemnica.insert(controller);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// Brisanje uklonjenih
		for (int i=0;i<deletedItems.size();i++){
			deletedItems.get(i).delete(controller.getSettings().getConnection());
		}
		
		// Update izmenjenih i insert novih
		for (int i=0;i<table.getItems().size();i++){
			if (table.getItems().get(i).getDocumentDetailsID() > 0)
				table.getItems().get(i).update(controller);
			else{
				table.getItems().get(i).setDocumentID(prijemnica.getDocumentID());
				Long  id = table.getItems().get(i).insert(controller);
				table.getItems().get(i).setDocumentDetailsID(id);
			}
		}
		
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionAdd(ActionEvent event) {
		HashMap<String,Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor proizvoda",
												ScreensController.DIALOG_SELECT_PRODUCT, 
												hm,
												rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			Product p = (Product) hm.get("product");
			PrijemnicaDetailes pd = new PrijemnicaDetailes();
			pd.setProductID(p.getProductID());
			pd.setProductCode(p.getCode());
			pd.setProductName(p.getName());
			pd.setQuantity(0);
			pd.setPrice(0);
			pd.setDiscount(0);
			pd.setPriceWithDiscount(0);
			table.getItems().add(pd);
		}
	}
	@FXML public void actionRemove(ActionEvent event) {
		if (table.getSelectionModel().getSelectedItem() != null){
			deletedItems.add(table.getSelectionModel().getSelectedItem());
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
		}
			
	}
	
	private void setDobavljac(Person dobavlac){
		if (dobavlac != null){
			for (Person p : cbPerson.getItems()){
				if (p.getPersonID() == prijemnica.getDobavljac().getPersonID()){
					cbPerson.getSelectionModel().select(p);
					break;
				}
			}
		}
	}
	
	private void setStatus(DocumentStatus status){
		if (prijemnica.getDocumentStatus() == null){
			prijemnica.setDocumentStatus(cbStatus.getItems().get(0));
		}
		for (DocumentStatus ds : cbStatus.getItems()){
			if (ds.getDocumentStatusID() == prijemnica.getDocumentStatus().getDocumentStatusID()){
				cbStatus.getSelectionModel().select(ds);
				break;
			}
		}
	}
	
	
	@FXML private void actionImport(){
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setGroupingUsed(false);
		
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Import prijemnice");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (file != null) {
        	DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder;
    		
    		String greska = "";
    		try {
    			builder = fact.newDocumentBuilder();
    			InputSource is = new InputSource(new FileInputStream(file));
    			org.w3c.dom.Document doc = builder.parse(is);
    						
    			NodeList nodes = doc.getElementsByTagName("Doc");
    			
    			NodeList listaNodova = nodes.item(0).getChildNodes();
    			int brojNodova = listaNodova.getLength();
    			for (int i=0; i<brojNodova;i++){
    				
    				if (listaNodova.item(i).getNodeName().equals("DocType")){
    					if ((!listaNodova.item(i).getTextContent().equals("IntPren")) &&
    						(prijemnica.getDocumentID() == -1)){
    						greska = "Neispravan tip dokumenta";
    						break;
    					}
    					if ((!listaNodova.item(i).getTextContent().equals("IntPrenFinal")) &&
    						(prijemnica.getDocumentID() != -1)){
    						greska = "Neispravan tip dokumenta";
    						break;
    					}
    				}
    				if (listaNodova.item(i).getNodeName().equals("DocNumber")){
    					if ((prijemnica.getDocumentID() > 0) &&
    						((tfExternalNumber.getText() == null) ||
    						 (!tfExternalNumber.getText().equals(listaNodova.item(i).getTextContent()))
    						)
    					   ){
    						greska = "Finalna prijemnica nije za aktuelni dokument";
    					}
    					else{
    						tfExternalNumber.setText(listaNodova.item(i).getTextContent());
    					}
    				}
    				
    				if (listaNodova.item(i).getNodeName().equals("DocDate")){
    					dpDeliveryDate.setValue(LocalDate.parse(listaNodova.item(i).getTextContent(),dtf));
    					dpCurrencyDate.setValue(LocalDate.parse(listaNodova.item(i).getTextContent(),dtf));
    					try{
	    					dpDate.setDateTimeValue(WorkDay.getFirstStart(prijemnica.getDepartmentID(),
	    																  dpDeliveryDate.getValue(), 
	    																  controller.getSettings().getConnection()));
    					} catch (NullPointerException e){
    						greska = "Za datum otpremnice ne postoji otovoren radni dan!";
    					}
    				}
    				
    				if (listaNodova.item(i).getNodeName().equals("ObjectOutCode")){
    					try{
    						Person p = Person.getByCode(listaNodova.item(i).getTextContent(),
    													controller.getSettings().getConnection());
	    					for (int j=0;j<cbPerson.getItems().size();j++){
	    						if ((cbPerson.getItems().get(j).getCode() != null) &&
	    							cbPerson.getItems().get(j).getCode().equals(p.getCode())){
	    							cbPerson.getSelectionModel().select(cbPerson.getItems().get(j));
	    						}
	    					}
    					} catch (NullPointerException e){
    						greska = "Dobavljač nije nađen u bazi podataka!";
    					}
    				}
    				
    				if (listaNodova.item(i).getNodeName().equals("ObjectInCode")){
    					Department dp = Department.getByCode(listaNodova.item(i).getTextContent(), 
    														 controller.getSettings().getConnection());
    					if ((dp == null) || (prijemnica.getDepartmentID() != dp.getDepartmentID())){
    						greska = "XML nije namenjen objektu!";
    					}
    				}
    				
    				if (listaNodova.item(i).getNodeName().equals("Items")){
    					NodeList listaNodova1 = listaNodova.item(i).getChildNodes();
	    				
    					for (int j=0;j<listaNodova1.getLength();j++){
    						if (listaNodova1.item(j).getNodeName().equals("Item")){ 
    							PrijemnicaDetailes pd = new PrijemnicaDetailes();
    							
    							NodeList listaNodova2 = listaNodova1.item(j).getChildNodes();
    							for(int k=0;k<listaNodova2.getLength();k++){
									Product p;
									if (listaNodova2.item(k).getNodeName().equals("ProductCode")){
										pd.setProductCode(listaNodova2.item(k).getTextContent());
										p = Product.getByCode(listaNodova2.item(k).getTextContent(),
															  controller.getSettings().getConnection());
										if (p == null){
											greska = "Neki od proizvoda nisu nađeni u bazi podataka!";
										}
										else{
											pd.setProductID(p.getProductID());
										}
									}
									if (listaNodova2.item(k).getNodeName().equals("ProductName"))
										pd.setProductName(listaNodova2.item(k).getTextContent());
									if (listaNodova2.item(k).getNodeName().equals("Quantity"))
										pd.setQuantity(nf.parse(listaNodova2.item(k).getTextContent()).doubleValue());
									if (listaNodova2.item(k).getNodeName().equals("Price")){
										pd.setPrice(nf.parse(listaNodova2.item(k).getTextContent()).doubleValue());
										pd.setDiscount(0);
										pd.setPriceWithDiscount(pd.getPrice());
									}
    							}
    							if (greska.equals(""))
    								if (prijemnica.getDocumentID() == -1)
    									table.getItems().add(pd);
    								else{
    									for(PrijemnicaDetailes pdp : table.getItems()){
    										if (pdp.getProductID() == pd.getProductID()){
    											pdp.setPrice(pd.getPrice());
    											pdp.setDiscount(pd.getDiscount());
    											pdp.setPriceWithDiscount(pd.getPriceWithDiscount());
    											table.refresh();
    										}
    									}
    								}
    									
    						}
    					}
    					if (!greska.equals("")){
    						table.getItems().clear();
    					}
    				}
    			}		
    			
    			if (!greska.equals("")){
    				controller.alert("Greška pri učitavanju XML-a", greska, rootPane.getScene().getWindow());
    			}
    			
    		} catch (ParserConfigurationException | SAXException | IOException | DOMException | ParseException e) {
    			controller.alert("Greška pri učitavanju XML-a", e.getMessage(), rootPane.getScene().getWindow());
    		}
        }
	}
}
