package rs.petcom.backoffice.controller.document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.document.SravnjenjePoPopisu;
import rs.petcom.master.dal.document.SravnjenjePoPopisuDetailes;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductGroup;
import rs.petcom.master.dal.product.ProductGroupDetaile;
import rs.petcom.master.gui.DoubleTableCellFactory;
import tornadofx.control.DateTimePicker;

public class SravnjenjePoPopisuDetailesController implements DialogController{
	
	ScreensController controller;
	SravnjenjePoPopisu sravnjenjePoPopisu;
	int modalResult;
	ProductGroup noProductGroup = new ProductGroup(-1, "", 2);
	
	ArrayList<SravnjenjePoPopisuDetailes> deletedItems = new ArrayList<>();
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		controller = (ScreensController) parameter.get("controller");
		sravnjenjePoPopisu = (SravnjenjePoPopisu) parameter.get("sravnjenjePoPopisu");
		dcDate.setDateTimeValue(Settings.LocalDateTimefromDate(sravnjenjePoPopisu.getDate()));
		table.setItems(SravnjenjePoPopisuDetailes.getlist(sravnjenjePoPopisu, controller.getSettings().getConnection()));
		deletedItems.clear();
		tfNapomena.setText(sravnjenjePoPopisu.getNote());
		cbProductGroup.setItems(ProductGroup.getList(2, controller.getSettings().getConnection()));
		cbProductGroup.getItems().add(0, noProductGroup);
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
		tcCode.setCellValueFactory(new PropertyValueFactory<SravnjenjePoPopisuDetailes, String>("ProductCode"));
		tcName.setCellValueFactory(new PropertyValueFactory<SravnjenjePoPopisuDetailes, String>("ProductName"));
		
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Object, Double>("Popisano"));
		tcQuantity.setCellFactory(cellFactory);
		tcQuantity.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                	SravnjenjePoPopisuDetailes data = ((SravnjenjePoPopisuDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow()));
	                	data.setPopisano(t.getNewValue());
	                	data.setRazlika(data.getPopisano() - data.getPocetnoStanje());
	                }
	            }
	        );
	}
	
	@FXML private BorderPane rootPane;
	@FXML private DateTimePicker dcDate;
	@FXML private ComboBox<ProductGroup> cbProductGroup;
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	@FXML private TableView<SravnjenjePoPopisuDetailes> table;
	@FXML private TableColumn<SravnjenjePoPopisuDetailes, String> tcCode;
	@FXML private TableColumn<SravnjenjePoPopisuDetailes, String>  tcName;
	@FXML private TableColumn<Object, Double>  tcQuantity;
	@FXML private TextField tfNapomena;

	@FXML public void actionClose(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionSave(ActionEvent event) {
		//Snimanje dokumenta
		boolean ispravnaKolicina=true;
		for (SravnjenjePoPopisuDetailes spd : table.getItems()) {
			if(spd.getPopisano()<0){
				ispravnaKolicina=false;
			}
		}
		
		if(ispravnaKolicina){
			sravnjenjePoPopisu.setNote(tfNapomena.getText());
			sravnjenjePoPopisu.setDate(Settings.DatefromLocalDateTime(dcDate.getDateTimeValue()));
			
			if (sravnjenjePoPopisu.getDocumentID() > 0)
				sravnjenjePoPopisu.update(controller);
			else
				try {
					sravnjenjePoPopisu.insert(controller);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
			// Brisanje uklonjenih
			for (int i=0;i<deletedItems.size();i++){
				deletedItems.get(i).delete(controller.getSettings().getConnection());
			}
			
			// Update izmenjenih i insert novih
			for (int i=0;i<table.getItems().size();i++){
				if (table.getItems().get(i).getSravnjenjePoPopisuDetailesID() > 0)
					table.getItems().get(i).update(controller);
				else{
					table.getItems().get(i).setSravnjenjePoPopisuID(sravnjenjePoPopisu.getDocumentID());
					Long  id = table.getItems().get(i).insert(controller);
					table.getItems().get(i).setSravnjenjePoPopisuDetailesID(id);
				}
			}
			
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		} else {
			controller.alert("Upozorenje", "Količina ne može biti negativna", rootPane.getScene().getWindow());
			return;
		}
		  
		
	}
	@FXML public void actionAdd(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int mr = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			Product product = (Product) hm.get("product");
			
			//Ako je artikal prodajni ne dodaje ga
			if(product.getProductTypeID()==1){				
				return;
			}
			
			boolean postoji = false;
			for (SravnjenjePoPopisuDetailes sppd : table.getItems()){
				if (product.getProductID() == sppd.getProductID()){
					postoji = true;
					break;
				}
			}
			if (!postoji){
				SravnjenjePoPopisuDetailes spd = 
						new SravnjenjePoPopisuDetailes(new Long(-1), 
													   sravnjenjePoPopisu.getDocumentID(), 
													   product.getProductID(), 
													   product.getCode(), 
													   product.getName(), 
													   Product.getCurrentStock(product.getProductID(), 
																			   sravnjenjePoPopisu.getDepartmentID(), 
																			   //Settings.DatefromLocalDateTime(dcDate.getDateTimeValue()),
																			   controller.getSettings().getConnection()), 
													   0, 0);
				spd.setRazlika(spd.getPocetnoStanje());
				table.getItems().add(spd);
				table.getSelectionModel().select(spd);
				table.scrollTo(spd);
				table.requestFocus();
			}
			else{
				HashMap<String, Object> hm2 = new HashMap<>();
				hm2.put("title", "Artikal je već u sravnjenje!");
				hm2.put("text", "Artikal je već u sravnjenje");
				controller.showModalDialog("Artikal je već u sravnjenju!",ScreensController.DIALOG_ALLERT,hm2,rootPane.getScene().getWindow());
			}
		}
	}
	@FXML public void actionRemove(ActionEvent event) {
		if (table.getSelectionModel().getSelectedItem() != null)
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
	}
	@FXML public void productGroupChange(){
		table.getItems().clear();
		
		for (Product product : ProductGroupDetaile.getProductList(cbProductGroup.getSelectionModel().getSelectedItem().getProductGroupID(), 
				   												  controller.getSettings().getConnection())){
			SravnjenjePoPopisuDetailes spd = 
					new SravnjenjePoPopisuDetailes(new Long(-1), 
												   sravnjenjePoPopisu.getDocumentID(), 
												   product.getProductID(), 
												   product.getCode(), 
												   product.getName(), 
												   Product.getCurrentStock(product.getProductID(), 
																		   sravnjenjePoPopisu.getDepartmentID(),
																		   //Settings.DatefromLocalDateTime(dcDate.getDateTimeValue()),
																		   controller.getSettings().getConnection()), 
												   0, 0);
			spd.setRazlika(spd.getPocetnoStanje());
			table.getItems().add(spd);
		}
	}
}
