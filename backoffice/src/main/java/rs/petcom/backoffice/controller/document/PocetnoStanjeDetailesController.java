package rs.petcom.backoffice.controller.document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import rs.petcom.master.dal.document.PocetnoStanje;
import rs.petcom.master.dal.document.PocetnoStanjeDetailes;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.gui.DoubleTableCellFactory;
import tornadofx.control.DateTimePicker;

public class PocetnoStanjeDetailesController implements DialogController{
	
	ScreensController controller;
	PocetnoStanje pocetnoStanje;
	int modalResult;
	
	ArrayList<PocetnoStanjeDetailes> deletedItems = new ArrayList<>();
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		controller = (ScreensController) parameter.get("controller");
		pocetnoStanje = (PocetnoStanje) parameter.get("pocetnoStanje");
		dcDate.setDateTimeValue(Settings.LocalDateTimefromDate(pocetnoStanje.getDate()));
		table.setItems(PocetnoStanjeDetailes.getlist(pocetnoStanje, controller.getSettings().getConnection()));
		deletedItems.clear();
		tfNapomena.setText(pocetnoStanje.getNote());
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
		tcCode.setCellValueFactory(new PropertyValueFactory<PocetnoStanjeDetailes, String>("ProductCode"));
		tcName.setCellValueFactory(new PropertyValueFactory<PocetnoStanjeDetailes, String>("ProductName"));
		
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Object, Double>("Quantity"));
		tcQuantity.setCellFactory(cellFactory);
		tcQuantity.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                    ((PocetnoStanjeDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setQuantity(t.getNewValue());
	                }
	            }
	        );
	}
	
	@FXML private BorderPane rootPane;
	@FXML private DateTimePicker dcDate;
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	@FXML private TableView<PocetnoStanjeDetailes> table;
	@FXML private TableColumn<PocetnoStanjeDetailes, String> tcCode;
	@FXML private TableColumn<PocetnoStanjeDetailes, String>  tcName;
	@FXML private TableColumn<Object, Double>  tcQuantity;
	@FXML private TextField tfNapomena;

	@FXML public void actionClose(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionSave(ActionEvent event) {
		//Snimanje dokumenta
		pocetnoStanje.setNote(tfNapomena.getText());
		pocetnoStanje.setDate(Settings.DatefromLocalDateTime(dcDate.getDateTimeValue()));
		
		if (pocetnoStanje.getDocumentID() > 0)
			pocetnoStanje.update(controller);
		else
			try {
				pocetnoStanje.insert(controller);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// Brisanje uklonjenih
		for (int i=0;i<deletedItems.size();i++){
			deletedItems.get(i).delete(controller.getSettings().getConnection());
		}
		
		// Update izmenjenih i insert novih
		for (int i=0;i<table.getItems().size();i++){
			if (table.getItems().get(i).getPocetnoStanjeDetailesID() > 0)
				table.getItems().get(i).update(controller);
			else{
				table.getItems().get(i).setPocetnoStanjeID(pocetnoStanje.getDocumentID());
				Long  id = table.getItems().get(i).insert(controller);
				table.getItems().get(i).setPocetnoStanjeDetailesID(id);
			}
		}
		
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionAdd(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int mr = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			Product product = (Product) hm.get("product");
			
			boolean postoji = false;
			for (PocetnoStanjeDetailes pd : table.getItems()){
				if (product.getProductID() == pd.getProductID()){
					postoji = true;
					break;
				}
			}
			if (!postoji){
				PocetnoStanjeDetailes pd = 
						new PocetnoStanjeDetailes(new Long(-1), 
													   pocetnoStanje.getDocumentID(), 
													   product.getProductID(), 
													   product.getCode(), 
													   product.getName(), 
													   0);
				table.getItems().add(pd);
				table.getSelectionModel().select(pd);
				table.scrollTo(pd);
				table.requestFocus();
			}
			else{
				controller.alert("Artikal je već unet u listu!", 
								 "Artikal je već unet u listu!", 
								 rootPane.getScene().getWindow());
			}
		}
	}
	@FXML public void actionRemove(ActionEvent event) {
		// TODO Autogenerated
	}
	
}
