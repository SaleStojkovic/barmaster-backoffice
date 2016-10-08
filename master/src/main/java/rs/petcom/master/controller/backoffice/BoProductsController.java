package rs.petcom.master.controller.backoffice;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.NormativeItem;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.GroupPointOfSaleProduct;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductCategory;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.dal.product.ProductType;

public class BoProductsController  implements ControlledScreen{
	
	ScreensController controller;
	Product oldValue = null;
	Product selectedProduct = null;
	NormativeItem selectedNormative = null;
	
	ObservableList<Product> productList;
	ObservableList<Vat> vatList;
	ObservableList<ProductCategory> productCategoryList;
	ObservableList<ProductType> productTypeList;

	ObservableList<NormativeItem> normativeList;
	
	@FXML BorderPane rootPane;
	@FXML TableView<Product> productTable;
	@FXML TableColumn<Product, String> codeCol;
	@FXML TableColumn<Product, String> nameCol;
	@FXML TableColumn<Product, String> shortNameCol;
	
	@FXML TextField tfCode;
	@FXML TextField tfName;
	@FXML TextField tfShortName;
	@FXML CheckBox cbActive;
	@FXML ComboBox<Vat> cbVat;
	@FXML TextField tfNutritional;
	@FXML TextField tfEnergy;
	@FXML ComboBox<ProductCategory> cbCategory;
	@FXML ComboBox<ProductType> cbType;
	
	@FXML ComboBox<Department> cbFilterDepartment;
	
	
	//Normative
	@FXML TableView<NormativeItem> normativTable;
	@FXML TableColumn<NormativeItem, String> ntColCode;
	@FXML TableColumn<NormativeItem, String> ntColName;
	@FXML TableColumn<NormativeItem, String> ntColShortName;
	@FXML TableColumn<NormativeItem, Double> ntColNormative;
	@FXML TableColumn<NormativeItem, Integer> ntColOrder;
	@FXML TextField tfNormative;
	@FXML TextField tfOrder;
	
	// Group
	@FXML TableView<GroupPointOfSaleProduct> tableGroup;
	@FXML TableColumn<GroupPointOfSaleProduct, String> grColName;
	@FXML TextField tfGroupSort;
	@FXML TextField tfGroupPosition;
	@FXML TextField tfGroupColor;
	@FXML CheckBox cbGroupActive;
	
	public void initialize(){
		
		
	}
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void setParameter(Object parameter) {}
	@Override
	public Object getParameter() {
		return null;
	}
	@Override
	public void init() {
		cbFilterDepartment.setItems(Department.getList(controller.getObject(), "", controller.getSettings().getConnection()));
		cbFilterDepartment.getSelectionModel().select(0);
		productList = Product.getObeservableList(null,
												 "",
												 false,
												 cbFilterDepartment.getSelectionModel().getSelectedItem(),
												 controller.getSettings().getConnection());
		vatList = Vat.getObeservableList(controller.getSettings().getConnection());
		productCategoryList = ProductCategory.getObeservableList(controller.getSettings().getConnection());
		productTypeList = ProductType.getObeservableList(true, controller.getSettings().getConnection());
				
		productTable.setItems(productList);
		
		codeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		shortNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("ShortName"));

		productTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Product>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Product> c) {
				showProduct(productTable.getSelectionModel().getSelectedItem());
			}
		});
		
		tfCode.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedProduct.setCode(newValue);
			}
		});
		
		tfName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedProduct.setName(newValue);
			}
		});
		
		tfShortName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedProduct.setShortName(newValue);
			}
		});
		
		cbVat.setItems(vatList);
		cbVat.valueProperty().addListener(new ChangeListener<Vat>() {
			@Override
			public void changed(ObservableValue<? extends Vat> observable, Vat oldValue, Vat newValue) {
				try{
					selectedProduct.setVatID(newValue.getVatID());
				}catch (NullPointerException e){}
			}
		});
		
		cbCategory.setItems(productCategoryList);
		cbCategory.valueProperty().addListener(new ChangeListener<ProductCategory>() {
			@Override
			public void changed(ObservableValue<? extends ProductCategory> observable, ProductCategory oldValue, ProductCategory newValue) {
				try{
					selectedProduct.setProductCategoryID(newValue.getProductCategoryID());
				}catch (NullPointerException e){}
			}
		});
		
		
		cbType.setItems(productTypeList);
		cbType.valueProperty().addListener(new ChangeListener<ProductType>() {
			@Override
			public void changed(ObservableValue<? extends ProductType> observable, ProductType oldValue, ProductType newValue) {
				try{
					selectedProduct.setProductTypeID(newValue.getProductTypeID());
				} catch (NullPointerException e){}
			}
		});
		
		
		
		cbActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				selectedProduct.setActive(newValue);
			}
		});
		
		tfNutritional.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
		tfNutritional.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					selectedProduct.setNutritionalValue(Settings.getNumberFormat(4, true).parse(newValue).doubleValue());
				} catch (ParseException e) {}
			}
		}); 
		
		tfEnergy.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
		tfEnergy.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					selectedProduct.setEnergyValue(Settings.getNumberFormat(4, true).parse(newValue).doubleValue());
				} catch (ParseException e) {}
			}
		});
		
		
		// Normative 
		normativTable.setItems(normativeList);
		normativTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<NormativeItem>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends NormativeItem> c) {
				showNormative(normativTable.getSelectionModel().getSelectedItem());
			}
		});
		ntColCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
		ntColName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		ntColShortName.setCellValueFactory(new PropertyValueFactory<NormativeItem, String>("ShortName"));
		ntColNormative.setCellValueFactory(new PropertyValueFactory<NormativeItem, Double>("Normative"));
		ntColOrder.setCellValueFactory(new PropertyValueFactory<NormativeItem, Integer>("Order"));
		ntColOrder.setOnEditCommit(
			    new EventHandler<CellEditEvent<NormativeItem, Integer>>() {
			        @Override
			        public void handle(CellEditEvent<NormativeItem, Integer> t) {
			            ((NormativeItem) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).setOrder(t.getNewValue());
			        }
			    }
			);
		tfNormative.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
		tfNormative.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					selectedNormative.setNormative(Settings.getNumberFormat(4, true).parse(newValue).doubleValue());
				} catch (ParseException e) {}
			}
		}); 
		tfOrder.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
		tfOrder.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					selectedNormative.setOrder(Settings.getNumberFormat(0, true).parse(newValue).intValue());
				} catch (ParseException e) {}
			}
		}); 
		
		
		// Group 
		grColName.setCellValueFactory(new PropertyValueFactory<GroupPointOfSaleProduct, String>("GroupName"));
		tableGroup.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<GroupPointOfSaleProduct>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends GroupPointOfSaleProduct> c) {
				showGroupData(tableGroup.getSelectionModel().getSelectedItem());
			}
		});
	}
	
	private void showProduct(Product p){
		try{
			oldValue = p.getCopy();
			selectedProduct = p;
			
			tfCode.setText(selectedProduct.getCode());
			tfName.setText(selectedProduct.getName());
			tfShortName.setText(selectedProduct.getShortName());
			cbActive.setSelected(selectedProduct.isActive());
			cbVat.setValue(getVatByIndex(selectedProduct.getVatID()));
			cbCategory.setValue(getProductCategoryByIndex(selectedProduct.getProductCategoryID()));
			cbType.setValue(getProductTypeByIndex(selectedProduct.getProductTypeID()));
			tfNutritional.setText(Settings.getNumberFormat(4, true).format(selectedProduct.getNutritionalValue()));
			tfEnergy.setText(Settings.getNumberFormat(4, true).format(selectedProduct.getEnergyValue()));
			
			loadNormative(p.getProductDepartmentID());
			loadGroup();
		} catch (NullPointerException e){
			tfCode.setText("");
			tfName.setText("");
			tfShortName.setText("");
			cbActive.setSelected(false);
			cbVat.getSelectionModel().select(0);
			cbCategory.getSelectionModel().select(0);
			cbType.getSelectionModel().select(0);
			tfNutritional.setText("");
			tfEnergy.setText("");
		}
	}
	
	private Vat getVatByIndex(int id){
		for (Vat v : vatList){
			if (v.getVatID() == id)
				return v;
		}
		return null;
	}
	private ProductCategory getProductCategoryByIndex(int id){
		for (ProductCategory pc : productCategoryList){
			if (pc.getProductCategoryID() == id)
				return pc;
		}
		return null;
	}
	private ProductType getProductTypeByIndex(int id){
		for (ProductType pt : productTypeList){
			if (pt.getProductTypeID() == id)
				return pt;
		}
		return null;
	}
	
	@FXML
	private void comitAction(){
		if (selectedProduct.getProductID() != new Long(0))
			try {
				selectedProduct.update(controller.getUser(), controller.getSettings().getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else 
			selectedProduct.insert(controller.getUser(), controller.getSettings().getConnection());
	}
	
	@FXML
	private void cancelAction(){
		selectedProduct = oldValue.getCopy();
		showProduct(selectedProduct);
	}
	
	@FXML
	private void copyAction(){
		System.out.println("Copy");
	}
	
	@FXML private void deleteAction(){
		try {
			ProductDepartment pd = new ProductDepartment();
			pd.setProductDepartmentID(selectedProduct.getProductDepartmentID());
			pd.delete(controller.getSettings().getConnection());
			productList.remove(selectedProduct);
		} catch (SQLException e) {
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("title", "Greška prilikom brisanja");
			hm.put("text", e.toString());
			controller.showModalDialog("Greška prilikom brisanja",ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
		}
	}
	
	@FXML
	private void newAction(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		hm.put("department", cbFilterDepartment.getSelectionModel().getSelectedItem());
		int mr = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
		if (mr == ScreensController.MODAL_RESULT_OK){
			try{
			ProductDepartment pd = new ProductDepartment();
			pd.setProductID(((Product) hm.get("product")).getProductID());
			pd.setSort(0);
			pd.setActive(true);
			pd.setFavorite(false);
			pd.setMin(0);
			pd.setMax(0);
			pd.setWarehouseID(0);
			pd.setPrinterID(0);
			pd.setDepartmentID(cbFilterDepartment.getSelectionModel().getSelectedItem().getDepartmentID());
			pd.insert(controller.getSettings().getConnection());
			productList = Product.getObeservableList(null,
													 "",
													 false,
													 cbFilterDepartment.getSelectionModel().getSelectedItem(),
													 controller.getSettings().getConnection());
			productTable.setItems(productList);
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	private void loadNormative(long productId){
		normativeList = NormativeItem.getObeservableList(productId, 
														 cbFilterDepartment.getSelectionModel().getSelectedItem(),
														 controller.getSettings().getConnection());
		normativTable.setItems(normativeList);
	}
	
	private void showNormative(NormativeItem ni){
		selectedNormative = ni;
		try{
			tfNormative.setText(Settings.getNumberFormat(4,true).format(ni.getNormative()));
		}catch (NullPointerException e){
			tfNormative.setText("");
		}
		try{
			tfOrder.setText(Settings.getNumberFormat(0, true).format(ni.getOrder()));
		}catch (NullPointerException e){
			tfOrder.setText("");
		}
		
	}
	@FXML
	private void normativeComit(){
		if (selectedNormative != null){
			try {
				selectedNormative.setNormative(Settings.getNumberFormat(4, true).parse(tfNormative.getText()).doubleValue());
				selectedNormative.setOrder(Settings.getNumberFormat(0, true).parse(tfOrder.getText()).intValue());
				selectedNormative.update(controller.getSettings().getConnection());
				loadNormative(selectedProduct.getProductDepartmentID());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void normativeNew(){
		if (selectedProduct != null){
			Product p = new Product();
			p.setProductID(selectedProduct.getProductID());
			
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("product", p);
			hm.put("controller", controller);
			hm.put("department", cbFilterDepartment.getSelectionModel().getSelectedItem());
			int result = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_NORMATIVE_PRODUCT,hm,rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK){
				NormativeItem ni = new NormativeItem();
				ni.setArticleProductDepartmenID( selectedProduct.getProductDepartmentID());
				ni.setProduct((Product)hm.get("product"));
				ni.setNormative(1);
				int maxOrder = 0;
				for (int i=0;i<normativeList.size();i++){
					if (maxOrder < normativeList.get(i).getOrder())
						maxOrder = normativeList.get(i).getOrder();
				}
				ni.setOrder(maxOrder+1);
				ni.insert(controller.getSettings().getConnection());
				
				loadNormative(selectedProduct.getProductDepartmentID());
				normativTable.getSelectionModel().select(normativeList.size()-1);
				showNormative(normativTable.getSelectionModel().getSelectedItem());
				
			}
		}
	}
	@FXML
	private void normativeDelete(){
		if (selectedNormative != null){
			selectedNormative.delete(controller.getSettings().getConnection());
			loadNormative(selectedProduct.getProductDepartmentID());
		}
	}

	private void loadGroup(){
		tableGroup.setItems(GroupPointOfSaleProduct.getList(controller.getPointOfSale(),selectedProduct, "",controller.getSettings().getConnection()));
		if (tableGroup.getItems().size() > 0){
			tableGroup.getSelectionModel().select(0);
			showGroupData(tableGroup.getItems().get(0));
		}
	}
	@FXML
	private void groupNew(){
		if (selectedProduct != null){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("user",controller.getUser());
			hm.put("PointOfSale", controller.getPointOfSale());
			hm.put("connection",controller.getSettings().getConnection());
			hm.put("product", selectedProduct);
			Group g = new Group();
			hm.put("group", g);
			int result = controller.showModalDialog("Izbor grupe",ScreensController.DIALOG_SELECT_GROUP,hm,rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK){
				GroupPointOfSaleProduct gpp = new GroupPointOfSaleProduct();
				gpp.setGroupID(((Group) hm.get("group")).getGroupID());
				gpp.setProductID(selectedProduct.getProductID());
				gpp.setActive(true);
				gpp.setSysUserAddID(controller.getUser().getUserID());
				gpp.setPosition(g.getPosition());
				gpp.setColor(g.getColor());
				gpp.setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
				
				gpp.insert(controller.getSettings().getConnection());
				loadGroup();
			}
		}
	}
	@FXML
	private void groupDelete(){
		try{
			tableGroup.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			loadGroup();
		} catch (NullPointerException e){}
	}
	@FXML
	private void groupPost(){
		tableGroup.getSelectionModel().getSelectedItem().setActive(cbGroupActive.isSelected());
		tableGroup.getSelectionModel().getSelectedItem().setSort(Integer.parseInt(tfGroupSort.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setPosition(Integer.parseInt(tfGroupPosition.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setColor(tfGroupColor.getText());
		
		tableGroup.getSelectionModel().getSelectedItem().setProductID(selectedProduct.getProductID());
		tableGroup.getSelectionModel().getSelectedItem().setSysUserEditID(controller.getUser().getUserID());
		tableGroup.getSelectionModel().getSelectedItem().setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
				
		tableGroup.getSelectionModel().getSelectedItem().update(controller.getSettings().getConnection());
	}
	private void showGroupData(GroupPointOfSaleProduct group){
		try{
			tfGroupSort.setText(String.valueOf(group.getSort()));
		} catch (NullPointerException e){
			tfGroupSort.setText("");
		}
		try{
			tfGroupPosition.setText(String.valueOf(group.getPosition()));
		} catch (NullPointerException e){
			tfGroupPosition.setText("");
		}
		try{
			tfGroupColor.setText(group.getColor());
		} catch (NullPointerException e){
			tfGroupColor.setText("");
		}
		try{
			cbGroupActive.setSelected(group.isActive());
		}  catch (NullPointerException e){
			cbGroupActive.setSelected(false);
		}
	}
	
	@FXML private void departmentChange(){
		productList = Product.getObeservableList(null,
												 "",
												 false,
												 cbFilterDepartment.getSelectionModel().getSelectedItem(),
												 controller.getSettings().getConnection());
		productTable.setItems(productList);
	}
}
