package rs.petcom.backoffice.controller.popis;

import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Controll;
import rs.petcom.master.dal.ControllProduct;
import rs.petcom.master.dal.product.Product;

public class ControllEditController implements ControlledScreen{
	
	ScreensController controller;
	
	ObservableList<Controll> controllList;
	ObservableList<ControllProduct> controllProductList; 
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.controller = screenPage;
	}
	@Override
	public void init() {
		
	}
	@Override
	public void setParameter(Object parameter) {
		controllList = Controll.getList((rs.petcom.master.dal.Object) parameter, 
										 controller.getSettings().getConnection());
		tableControll.setItems(controllList);
		tableControll.getSelectionModel()
		 			 .selectedItemProperty()
		 			 .addListener((observableValue, oldValue, newValue) -> {
		 				loadControllProduct();
					});
	}
	
	@Override
	public Object getParameter() {
		return null;
	}
	
	public void initialize(){
		tableControll.setItems(controllList);

		tableControll.setItems(controllList);
		tcControllGroup.setCellValueFactory(new PropertyValueFactory<Controll, String>("Group"));
		tcControllCode.setCellValueFactory(new PropertyValueFactory<Controll, String>("Code"));
		tcControllName.setCellValueFactory(new PropertyValueFactory<Controll, String>("Name"));

		tcProductCode.setCellValueFactory(new PropertyValueFactory<ControllProduct, String>("Code"));
		tcProductNaziv.setCellValueFactory(new PropertyValueFactory<ControllProduct, String>("Name"));
	}
	@FXML private BorderPane rootPane;
	@FXML private TableView<Controll> tableControll;
	@FXML private TableColumn<Controll, String> tcControllGroup;
	@FXML private TableColumn<Controll, String> tcControllCode;
	@FXML private TableColumn<Controll, String> tcControllName;
	
	@FXML private TableView<ControllProduct> tableControllProduct;
	@FXML private TableColumn<ControllProduct, String> tcProductCode;
	@FXML private TableColumn<ControllProduct, String> tcProductNaziv;
	
	@FXML private void productAdd(){
		HashMap<String,Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT, hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			Product p = (Product) hm.get("product");
			ControllProduct.insert(tableControll.getSelectionModel().getSelectedItem(), 
								   p, 
								   controller.getSettings().getConnection());
			loadControllProduct();
		}
	}
	@FXML private void productRemove(){
		tableControllProduct.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
		loadControllProduct();
	}
	
	
	private void loadControllProduct(){
		controllProductList = ControllProduct.getList(tableControll.getSelectionModel().getSelectedItem(), 
													  controller.getSettings().getConnection());
		tableControllProduct.setItems(controllProductList);
	}
}
