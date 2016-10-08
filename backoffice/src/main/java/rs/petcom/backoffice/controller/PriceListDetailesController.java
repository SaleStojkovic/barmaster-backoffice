package rs.petcom.backoffice.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PriceList;
import rs.petcom.master.dal.PriceListDetailes;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.gui.DoubleTableCellFactory;

public class PriceListDetailesController implements DialogController{
	
	ScreensController controller;
	PriceList priceList;
	int modalResult;
	
	ArrayList<PriceListDetailes> deletedItems = new ArrayList<>();
	
	EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()){
			case DELETE:
				productRemove(null);
				break;
			case INSERT:
				productAdd(null);
				break;
			default:
			}
		};
	};
	
	public void initialize(){		
		rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
		
		DoubleTableCellFactory cellFactory = new DoubleTableCellFactory(2, true);
		 
		tcCode.setCellValueFactory(new PropertyValueFactory<PriceListDetailes, String>("Code"));
		
		tcName.setCellValueFactory(new PropertyValueFactory<PriceListDetailes, String>("Name"));
		
		tcPrice.setCellValueFactory(new PropertyValueFactory<Object, Double>("Price"));
		tcPrice.setCellFactory(cellFactory);
		tcPrice.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                    ((PriceListDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setPrice(t.getNewValue());
	                }
	            }
	        );
		
		tcDiscount.setCellValueFactory(new PropertyValueFactory<Object, Double>("Discount"));
		tcDiscount.setCellFactory(cellFactory);
		tcDiscount.setOnEditCommit(
	            new EventHandler<CellEditEvent<Object, Double>>() {
	                @Override
	                public void handle(CellEditEvent<Object, Double> t) {
	                    ((PriceListDetailes) t.getTableView().getItems().get(
	                            t.getTablePosition().getRow())
	                            ).setDiscount(t.getNewValue());
	                }
	            }
	        );
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		controller = (ScreensController) parameter.get("controller");
		priceList = (PriceList) parameter.get("priceList");
		table.setItems(PriceListDetailes.getList(priceList, controller.getSettings().getConnection()));
		deletedItems.clear();
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
	
	@FXML private BorderPane rootPane;
	@FXML private TableView<PriceListDetailes> table;
	@FXML private TableColumn<PriceListDetailes, String> tcCode;
	@FXML private TableColumn<PriceListDetailes, String> tcName;
	@FXML private TableColumn<Object, Double> tcPrice;
	@FXML private TableColumn<Object, Double> tcDiscount;
		
	@FXML public void productAdd(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int mr = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			Product product = (Product) hm.get("product");
			
			boolean postoji = false;
			for (PriceListDetailes pld : table.getItems()){
				if (product.getProductID() == pld.getProductID()){
					postoji = true;
					break;
				}
			}
			if (!postoji){
				PriceListDetailes pld = new PriceListDetailes();
				pld.setPriceListDetailsID(new Long(-1));
				pld.setProductID(product.getProductID());
				pld.setCode(product.getCode());
				pld.setName(product.getName());
				pld.setPrice(0.0);
				pld.setDiscount(0.0);
				pld.setPriceListID(priceList.getPriceListID());
				
				table.getItems().add(pld);
				table.getSelectionModel().select(pld);
				table.scrollTo(pld);
				table.requestFocus();
			}
			else{
				HashMap<String, Object> hm2 = new HashMap<>();
				hm2.put("title", "Artikal je već u cenovniku!");
				hm2.put("text", "Artikal je već unet u cenovnik");
				controller.showModalDialog("Artikal je već unet u cenovnik!",ScreensController.DIALOG_ALLERT,hm2,rootPane.getScene().getWindow());
			}
		}
	}
	
	@FXML public void productRemove(ActionEvent event) {
		deletedItems.add(table.getSelectionModel().getSelectedItem());
		table.getItems().remove(table.getSelectionModel().getSelectedItem());
	}
	
	@FXML public void close(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void save(ActionEvent event) {
		// Brisanje uklonjenih
		for (int i=0;i<deletedItems.size();i++){
			deletedItems.get(i).delete(controller.getSettings().getConnection());
		}
		
		// Update izmenjenih i insert novih
		for (int i=0;i<table.getItems().size();i++){
			if (table.getItems().get(i).getPriceListDetailsID() > 0)
				table.getItems().get(i).update(controller.getSettings().getConnection());
			else{
				Long  id = table.getItems().get(i).insert(controller.getSettings().getConnection());
				table.getItems().get(i).setPriceListDetailsID(id);
			}
		}

		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
	}
}
