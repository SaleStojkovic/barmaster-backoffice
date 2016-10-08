package rs.petcom.master.controller.backoffice;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.product.Product;

public class NormativeProductSelectDialogController implements DialogController{

	ScreensController controller;
	HashMap<String, Object> parameters;
	Product product;
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	
	ObservableList<Product> productList = FXCollections.observableArrayList();
	
	public void initialize(){
		tcCode.setCellValueFactory(new PropertyValueFactory<Product, String>("Code"));
		tcName.setCellValueFactory(new PropertyValueFactory<Product, String>("Name"));
		tcShortName.setCellValueFactory(new PropertyValueFactory<Product, String>("ShortName"));
		
		tfFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				populateTable();
			}
		});	
	}
	
	@Override
	public void setParameter(HashMap<String, Object> parameters) {
		this.parameters = parameters;
		product = (Product) parameters.get("product");
		this.controller = (ScreensController) parameters.get("controller");
		tfFilter.setText("");
		tfFilter.requestFocus();
		populateTable();
		
	}
	
	public HashMap<String, Object> getParameter(){
		return parameters;
	}

	@FXML BorderPane rootPane;
	@FXML TableView<Product> table;
	@FXML TableColumn<Product, String> tcCode;
	@FXML TableColumn<Product, String> tcName;
	@FXML TableColumn<Product, String> tcShortName;
	
	@FXML TextField tfFilter;
	
	@FXML
	private void cancel(ActionEvent actionEvent){
		setModalResult(ScreensController.MODAL_RESULT_CANCEL);
		Node  source = (Node)  actionEvent.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	private void confirm(ActionEvent actionEvent){
		parameters.put("product",table.getSelectionModel().getSelectedItem());
		
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
	}
	
	private void populateTable(){
		table.setItems(Product.getObeservableList(product,
												  tfFilter.getText(), 
												  true,  
												  (Department) parameters.get("department"),
												  controller.getSettings().getConnection()));
		
		if (table.getItems().size() > 0)
			table.getSelectionModel().select(0);
	}
	
	@FXML
	private void mouseClick(MouseEvent event){
		if (event.getClickCount() == 2){
			setModalResult(0);
			parameters.put("product",table.getSelectionModel().getSelectedItem());			
			Node  source = (Node)  event.getSource(); 
		    Stage stage  = (Stage) source.getScene().getWindow();
		    stage.close();
		}
	}
	
	@FXML private void newProduct(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int mr = controller.showModalDialog("Izbor proizvoda",
											ScreensController.DIALOG_BACKOFFICE_ADD_PRODUCT,
											hm,
											rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			populateTable();
		}
	}

	@Override
	public int getModalResult() {
		return modalResult;
	}

	@Override
	public void setModalResult(int modalResult) {
		this.modalResult= modalResult;
	}
}
