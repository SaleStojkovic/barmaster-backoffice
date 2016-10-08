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
import rs.petcom.master.dal.document.Presifriranje;
import rs.petcom.master.dal.document.PresifriranjeDetailes;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.gui.DoubleTableCellFactory;
import tornadofx.control.DateTimePicker;

public class PresifriranjeDetailesController implements DialogController{
	
	ScreensController controller;
	Presifriranje presifriranje;
	int modalResult;
	
	ArrayList<PresifriranjeDetailes> deletedItems = new ArrayList<>();
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		controller = (ScreensController) parameter.get("controller");
		presifriranje = (Presifriranje) parameter.get("presifriranje");
		dcDate.setDateTimeValue(Settings.LocalDateTimefromDate(presifriranje.getDate()));
		table.setItems(PresifriranjeDetailes.getList(presifriranje, controller.getSettings().getConnection()));
		deletedItems.clear();
		tfNapomena.setText(presifriranje.getNote());
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
		tcCode.setCellValueFactory(new PropertyValueFactory<PresifriranjeDetailes, String>("ProductCode"));
		tcName.setCellValueFactory(new PropertyValueFactory<PresifriranjeDetailes, String>("ProductName"));
		
		tcQuantity.setCellValueFactory(new PropertyValueFactory<Object, Double>("Quantity"));
		tcQuantity.setCellFactory(cellFactory);
		tcQuantity.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                	PresifriranjeDetailes data = ((PresifriranjeDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow()));
	                	data.setQuantity(t.getNewValue());
	                }
	            }
	        );
	}
	
	@FXML private BorderPane rootPane;
	@FXML private DateTimePicker dcDate;
	@FXML private Button btnAdd;
	@FXML private Button btnRemove;
	@FXML private TableView<PresifriranjeDetailes> table;
	@FXML private TableColumn<PresifriranjeDetailes, String> tcCode;
	@FXML private TableColumn<PresifriranjeDetailes, String>  tcName;
	@FXML private TableColumn<Object, Double>  tcQuantity;
	@FXML private TextField tfNapomena;

	@FXML public void actionClose(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void actionSave(ActionEvent event) {
		try{
			//Snimanje dokumenta
			presifriranje.setNote(tfNapomena.getText());
			presifriranje.setDate(Settings.DatefromLocalDateTime(dcDate.getDateTimeValue()));
			
			if (presifriranje.getDocumentID() > 0)
				presifriranje.update(controller);
			else
				presifriranje.insert(controller);
			
			// Brisanje uklonjenih
			for (int i=0;i<deletedItems.size();i++){
				deletedItems.get(i).delete(controller.getSettings().getConnection());
			}
			
			// Update izmenjenih i insert novih
			for (int i=0;i<table.getItems().size();i++){
				if (table.getItems().get(i).getPresifriranjeDetailesID() > 0)
					table.getItems().get(i).update(controller);
				else{
					table.getItems().get(i).setPresifriranjeID(presifriranje.getDocumentID());
					Long  id = table.getItems().get(i).insert(controller);
					table.getItems().get(i).setSravnjenjePoPopisuDetailesID(id);
				}
			}
			
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		} catch (SQLException e){
			controller.alert("Greška pri snimanju u bazu!", 
							 "Došlo je do greške prilikom snimanja u bazu!\n" + e.getMessage(), 
							 rootPane.getScene().getWindow());
		}
	}
	@FXML public void actionAdd(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int mr = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			Product product = (Product) hm.get("product");
			
			boolean postoji = false;
			for (PresifriranjeDetailes pd : table.getItems()){
				if (product.getProductID() == pd.getProductID()){
					postoji = true;
					break;
				}
			}
			if (!postoji){
				PresifriranjeDetailes pd = 
						new PresifriranjeDetailes(new Long(-1), 
													   presifriranje.getDocumentID(), 
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
		if (table.getSelectionModel().getSelectedItem() != null)
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
	}
}
