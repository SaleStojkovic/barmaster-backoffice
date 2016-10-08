package rs.petcom.backoffice.controller;

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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.KitchenDisplay;
import rs.petcom.master.dal.NormativeItem;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.PointOfSaleProductDescription;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.product.Description;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.GroupPointOfSale;
import rs.petcom.master.dal.product.GroupPointOfSaleProduct;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductCategory;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.dal.product.ProductType;

public class ProductsController implements ControlledScreen{
	
	ScreensController controller;
	Product oldValue = null;
	Product selectedProduct = null;
	NormativeItem selectedNormative = null;
	PointOfSaleProductDescription selectedPointOfSaleProductDescription = null;
	Description selectedDescription = null;
	
	rs.petcom.master.dal.Object sviObjekti = new rs.petcom.master.dal.Object(-1,"Svi objekti");
	KitchenDisplay nemaDisplaya = new KitchenDisplay(-1,-1,"");
	ObservableList<Product> productList;
	ObservableList<Vat> vatList;
	ObservableList<ProductCategory> productCategoryList;
	ObservableList<ProductType> productTypeList;

	ObservableList<NormativeItem> normativeList;
	ObservableList<NormativeItem> normativeKopirajList;
	ObservableList<PointOfSaleProductDescription> descriptionIdList;
	ObservableList<GroupPointOfSaleProduct> additionalList;
	
	@FXML BorderPane rootPane;
	@FXML TableView<Product> productTable;
	@FXML TableColumn<Product, String> codeCol;
	@FXML TableColumn<Product, String> nameCol;
	@FXML TableColumn<Product, String> shortNameCol;
	
	// Filteri
	@FXML ComboBox<rs.petcom.master.dal.Object> cbObjekat;
	@FXML ComboBox<Department> cbOdeljenje;
	@FXML ComboBox<PointOfSale> cbPos;
	@FXML TextField tfFilter;
	
	@FXML Tab tabGrupe;
	@FXML Tab tabNormativ;
	@FXML Tab tabKopirajNormativ;
	@FXML Tab tabDodatni;
	@FXML Tab tabOpisni;
	
	
	@FXML TextField tfCode;
	@FXML TextField tfName;
	@FXML TextField tfShortName;
	@FXML CheckBox cbActive;
	@FXML ComboBox<Vat> cbVat;
	@FXML TextField tfNutritional;
	@FXML TextField tfEnergy;
	@FXML ComboBox<ProductCategory> cbCategory;
	@FXML ComboBox<ProductType> cbType;
	@FXML TextField tfPrepTime;
	@FXML ComboBox<KitchenDisplay> cbKitchenDisplay;
	@FXML AnchorPane apGrupe;
	@FXML AnchorPane apNormativ;
	@FXML HBox hbGrupeNormativ;
	
	//Normative
	@FXML TableView<NormativeItem> normativTable;
	@FXML TableColumn<NormativeItem, String> ntColCode;
	@FXML TableColumn<NormativeItem, String> ntColName;
	@FXML TableColumn<NormativeItem, String> ntColShortName;
	@FXML TableColumn<NormativeItem, Double> ntColNormative;
	@FXML TableColumn<NormativeItem, Integer> ntColOrder;
	@FXML TextField tfNormative;
	@FXML TextField tfOrder;
	
	//KopirajNormative
	@FXML TableView<NormativeItem> normativKopirajTable;
	@FXML TableColumn<NormativeItem, String> ntKopirajColCode;
	@FXML TableColumn<NormativeItem, String> ntKopirajColName;
	@FXML TableColumn<NormativeItem, String> ntKopirajColShortName;
	@FXML TableColumn<NormativeItem, Double> ntKopirajColNormative;
	@FXML TableColumn<NormativeItem, Integer> ntKopirajColOrder;
	@FXML TextField tfKopirajNormative;
	@FXML TextField tfKopirajOrder;
	
	// Group
	@FXML TableView<GroupPointOfSaleProduct> tableGroup;
	@FXML TableColumn<GroupPointOfSaleProduct, String> grColName;
	@FXML TextField tfGroupSort;
	@FXML TextField tfGroupPosition;
	@FXML TextField tfGroupColor;
	@FXML CheckBox cbGroupActive;
	
	// Dodatni
	@FXML TableView<GroupPointOfSaleProduct> tableDodatni;
	@FXML TableColumn<GroupPointOfSaleProduct, String> dodatniColName;
	@FXML TextField tfDodatniSort;
	@FXML TextField tfDodatniPosition;
	@FXML TextField tfDodatniColor;
	@FXML CheckBox cbDodatniActive;
		
	// Opisni
	@FXML TableView<PointOfSaleProductDescription> tableOpisni;
	@FXML TableColumn<PointOfSaleProductDescription, Integer> opisniColId;
	@FXML TableColumn<PointOfSaleProductDescription, String> opisniColName;
	@FXML TextField tfOpisniSort;
	@FXML TextField tfOpisniPosition;
	@FXML TextField tfOpisniColor;
	@FXML CheckBox cbOpisniActive;
	@FXML ColorPicker colorPickerOpisni;
	
	public void initialize(){
		tfFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				loadProductList();
			}
		});
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	
	@Override
	public void setParameter(Object parameter) {
		
	}
	
	@Override
	public Object getParameter() {
		return null;
	}
	
	@Override
	public void init() {
		if(controller.getUser().hasRole("izmena.normativa")){
			hbGrupeNormativ.setDisable(false);
			apGrupe.setDisable(false);
			apNormativ.setDisable(false);
		}else{
			hbGrupeNormativ.setDisable(true);
			apGrupe.setDisable(true);
			apNormativ.setDisable(true);
		}
		cbObjekat.getItems().add(sviObjekti);
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			cbObjekat.getItems().add(o);
		}
		cbObjekat.getSelectionModel().select(sviObjekti);
		cbOdeljenje.setDisable(true);
		cbPos.setDisable(true);
		
		tabGrupe.setDisable(true);
		tabNormativ.setDisable(true);
		tabDodatni.setDisable(true);
		tabOpisni.setDisable(true);
		tabKopirajNormativ.setDisable(true);
		
		popuniFilterOdeljene();
		popuniFilterPos();
		
		tfFilter.setText("");
		
		loadProductList();
		
		vatList = Vat.getObeservableList(controller.getSettings().getConnection());
		productCategoryList = ProductCategory.getObeservableList(controller.getSettings().getConnection());
		productTypeList = ProductType.getObeservableList(true, controller.getSettings().getConnection());
		
		
		codeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		shortNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("ShortName"));

		productTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Product>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Product> c) {
				try {
					if(!cbOdeljenje.getSelectionModel().isSelected(0)) {
						int ProductTypeId = productTable.getSelectionModel().getSelectedItem().getProductTypeID();
						if((ProductTypeId == 3) || (ProductTypeId == 4) || 
						   (ProductTypeId == 6) || (ProductTypeId == 7)) {
							tabKopirajNormativ.setDisable(false);
							tabNormativ.setDisable(false);
						} else {
							tabNormativ.setDisable(true);
							tabKopirajNormativ.setDisable(true);
						}
					}
				} catch (NullPointerException e) {
					tabNormativ.setDisable(true);
					tabKopirajNormativ.setDisable(true);				
				}
				
				loadOpisni();
				loadDodatni();
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
		
		// Dodatni 
			tableDodatni.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<GroupPointOfSaleProduct>(){
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends GroupPointOfSaleProduct> c) {
					showDodatniData(tableDodatni.getSelectionModel().getSelectedItem());
				}
			});
		
		// Opisni	
		tableOpisni.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<PointOfSaleProductDescription>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends PointOfSaleProductDescription> c) {
				showOpisniData(tableOpisni.getSelectionModel().getSelectedItem());
			}			
		});
		
		// Kopiraj Normative 
		normativKopirajTable.setItems(normativeKopirajList);
		normativKopirajTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<NormativeItem>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends NormativeItem> c) {
				showNormative(normativTable.getSelectionModel().getSelectedItem());
				showKopirajNormative(normativKopirajTable.getSelectionModel().getSelectedItem());
			}
		});
		ntKopirajColCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
		ntKopirajColName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		ntKopirajColShortName.setCellValueFactory(new PropertyValueFactory<NormativeItem, String>("ShortName"));
		ntKopirajColNormative.setCellValueFactory(new PropertyValueFactory<NormativeItem, Double>("Normative"));
		ntKopirajColOrder.setCellValueFactory(new PropertyValueFactory<NormativeItem, Integer>("Order"));
		ntKopirajColOrder.setOnEditCommit(
			    new EventHandler<CellEditEvent<NormativeItem, Integer>>() {
			        @Override
			        public void handle(CellEditEvent<NormativeItem, Integer> t) {
			            ((NormativeItem) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOrder(t.getNewValue());
			        }
			    }
			);
				tfKopirajNormative.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
				tfKopirajNormative.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							selectedNormative.setNormative(Settings.getNumberFormat(4, true).parse(newValue).doubleValue());
						} catch (ParseException e) {}
					}
				}); 
				tfKopirajOrder.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
				tfKopirajOrder.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							selectedNormative.setOrder(Settings.getNumberFormat(0, true).parse(newValue).intValue());
						} catch (ParseException e) {}
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
			
			if (selectedProduct.getPreparationTime() != null)
				tfPrepTime.setText(Settings.getTimeFromatter().format(selectedProduct.getPreparationTime()));
			else
				tfPrepTime.setText("");
			getKitchenDisplayByIndex(selectedProduct.getKitchenDisplayID());
			
			loadNormative(p.getProductDepartmentID());
			loadKopirajNormative(p.getProductDepartmentID());
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
	
	private void getKitchenDisplayByIndex(int id){
		boolean selected = false;
		for(int i=0;i<cbKitchenDisplay.getItems().size();i++){
			if (cbKitchenDisplay.getItems().get(i).getKitchenDisplayID() == id){
				cbKitchenDisplay.getSelectionModel().select(i);
				selected = true;
				break;
			}
		}
		if (!selected){
			cbKitchenDisplay.getSelectionModel().select(0);
		}
	}
	
	@FXML
	private void comitAction(){
		if (selectedProduct.getProductID() != new Long(0))
			try{
				selectedProduct.update(controller.getUser(), controller.getSettings().getConnection());
			} catch (SQLException e){
				controller.alert("Greška prilikom snimanja izmena", e.getMessage(), rootPane.getScene().getWindow());
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
		if ((cbOdeljenje.getSelectionModel().getSelectedItem() == null) ||
			(cbOdeljenje.getSelectionModel().getSelectedItem().getDepartmentID() == -1)){
			try {
				productTable.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
				productTable.getItems().remove(productTable.getSelectionModel().getSelectedItem());
			} catch (SQLException e) {
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("title", "Greška prilikom brisanja");
				hm.put("text", e.toString());
				controller.showModalDialog("Greška prilikom brisanja",ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
			}
		}
		else{
			try {
				ProductDepartment pd = new ProductDepartment();
				pd.setProductDepartmentID(selectedProduct.getProductDepartmentID());
				pd.delete(controller.getSettings().getConnection());
				productTable.getItems().remove(selectedProduct);
			} catch (SQLException e) {
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("title", "Greška prilikom brisanja");
				hm.put("text", e.toString());
				controller.showModalDialog("Greška prilikom brisanja",ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
			}
		}
	}
	
	@FXML
	private void newAction(){
		if (cbObjekat.getSelectionModel().getSelectedItem() != sviObjekti){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("controller", controller);
			hm.put("department", cbOdeljenje.getSelectionModel().getSelectedItem());
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
				pd.setDepartmentID(cbOdeljenje.getSelectionModel().getSelectedItem().getDepartmentID());
				pd.insert(controller.getSettings().getConnection());
				productList = Product.getObeservableList(null,
														 "",
														 false,
														 cbOdeljenje.getSelectionModel().getSelectedItem(),
														 controller.getSettings().getConnection());
				productTable.setItems(productList);
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
		else{
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("controller", controller);
			int result =
				controller.showModalDialog("Dodavanje artikla", 
										   ScreensController.DIALOG_BACKOFFICE_ADD_PRODUCT, 
										   hm2, 
										   rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK)
				loadProductList();
		}
	}
	
	private void loadNormative(long productId){
		try{
			normativeList = NormativeItem.getObeservableList(productId, 
															 cbOdeljenje.getSelectionModel().getSelectedItem(),
															 controller.getSettings().getConnection());
			normativTable.setItems(normativeList);
		}catch (NullPointerException e){}
	}
	
	private void loadKopirajNormative(long productId){
		try{
			normativeKopirajList = NormativeItem.getObeservableList(productId, 
															 cbOdeljenje.getSelectionModel().getSelectedItem(),
															 controller.getSettings().getConnection());
			normativKopirajTable.setItems(normativeKopirajList);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
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
	
	private void showKopirajNormative(NormativeItem ni){
		selectedNormative = ni;
		try{
			tfKopirajNormative.setText(Settings.getNumberFormat(4,true).format(ni.getNormative()));
		}catch (NullPointerException e){
			tfKopirajNormative.setText("");
		}
		try{
			tfKopirajOrder.setText(Settings.getNumberFormat(0, true).format(ni.getOrder()));
		}catch (NullPointerException e){
			tfKopirajOrder.setText("");
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
			hm.put("department", cbOdeljenje.getSelectionModel().getSelectedItem());
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
	
	@FXML 
	private void normativeKopirajComit(){
		HashMap<String, Object> hm2 = new HashMap<>();
		hm2.put("controller", controller);
		//hm2.put(key, value)
		controller.showModalDialog("Dodavanje artikla", 
									   ScreensController.DIALOG_COPY_NORMATIV, 
									   hm2, 
									   rootPane.getScene().getWindow());
		
	}

	private void loadGroup(){
		try{
			tableGroup.setItems(GroupPointOfSaleProduct.getList(cbPos.getSelectionModel().getSelectedItem(),
																selectedProduct, 
																"",
																controller.getSettings().getConnection()));
			if (tableGroup.getItems().size() > 0){
				tableGroup.getSelectionModel().select(0);
				showGroupData(tableGroup.getItems().get(0));
			}   
		} catch (NullPointerException e){}
	}
	
	private void loadOpisni(){
		try{
			opisniColId.setCellValueFactory(new PropertyValueFactory<PointOfSaleProductDescription, Integer>("DescriptionID"));
			opisniColName.setCellValueFactory(new PropertyValueFactory<PointOfSaleProductDescription, String>("Name"));
			descriptionIdList = PointOfSaleProductDescription.getListLoadTableOpisni(cbPos.getSelectionModel().getSelectedItem(), 
					productTable.getSelectionModel().getSelectedItem(),
					controller.getSettings().getConnection());
			tableOpisni.setItems(descriptionIdList);
		} catch (NullPointerException e){}
	}
	
	private void loadDodatni(){
		try{
			dodatniColName.setCellValueFactory(new PropertyValueFactory<GroupPointOfSaleProduct, String>("DodatniName"));
			additionalList = GroupPointOfSaleProduct.getAdditionalList(cbPos.getSelectionModel().getSelectedItem(), 
															 productTable.getSelectionModel().getSelectedItem().getProductID(),
															 controller.getSettings().getConnection());
			tableDodatni.setItems(additionalList);
		} catch (NullPointerException e){}
	}
	
	
	@FXML private void opisniNew(){
		if (selectedProduct != null){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("user",controller.getUser());
			hm.put("PointOfSale", cbPos.getSelectionModel().getSelectedItem());
			hm.put("connection",controller.getSettings().getConnection());
			hm.put("product", selectedProduct);
			hm.put("description", selectedDescription);
			Group g = new Group();
			hm.put("group", g);
			int result = controller.showModalDialog("Izbor grupe",ScreensController.DIALOG_SELECT_OPISNI,hm,rootPane.getScene().getWindow());
			
			if (result == ScreensController.MODAL_RESULT_OK){
				Description d = (Description) hm.get("description");
				PointOfSaleProductDescription pospd = new PointOfSaleProductDescription();
				pospd.setColor(tfOpisniColor.getText());
				pospd.setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
				pospd.setProductID(productTable.getSelectionModel().getSelectedItem().getProductID());
				pospd.setDescriptionID(d.getDescriptionID());
				pospd.setActive(d.isActive());
				
				pospd.insert(controller.getSettings().getConnection());
				loadOpisni();
			}
		}
	}
	
	
	@FXML private void opisniDelete(){
		try{
			tableOpisni.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			loadOpisni();
		} catch (NullPointerException e){}
	}
	
	
	@FXML private void opisniPost(){

			tableOpisni.getSelectionModel().getSelectedItem().setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
			tableOpisni.getSelectionModel().getSelectedItem().setProductID(selectedProduct.getProductID());
			tableOpisni.getSelectionModel().getSelectedItem().setDescriptionID(tableOpisni.getSelectionModel().getSelectedItem().getDescriptionID());
			tableOpisni.getSelectionModel().getSelectedItem().setColor("#"+Integer.toHexString(colorPickerOpisni.getValue().hashCode()).substring(0, 6).toUpperCase());
			tableOpisni.getSelectionModel().getSelectedItem().setActive(cbOpisniActive.isSelected());
			tableOpisni.getSelectionModel().getSelectedItem().setPositionBtn(Integer.parseInt(tfOpisniPosition.getText()));
			tableOpisni.getSelectionModel().getSelectedItem().setPointOfSaleProductDescriptionID(tableOpisni.getSelectionModel().getSelectedItem().getPointOfSaleProductDescriptionID());
			tableOpisni.getSelectionModel().getSelectedItem().update(controller.getSettings().getConnection());

				
		
	}
	
	@FXML private void groupNew(){
		if (selectedProduct != null){
			try{
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("user",controller.getUser());
			hm.put("PointOfSale", cbPos.getSelectionModel().getSelectedItem());
			hm.put("connection",controller.getSettings().getConnection());
			hm.put("product", selectedProduct);
			Group g = new Group();
			hm.put("group", g);
			int result = controller.showModalDialog("Izbor grupe",ScreensController.DIALOG_SELECT_GROUP,hm,rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK){
				GroupPointOfSaleProduct gpp = new GroupPointOfSaleProduct();
				GroupPointOfSale gps = 
				GroupPointOfSale.getFromDb(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID(), 
										   ((Group) hm.get("group")).getGroupID(), 
										   controller.getSettings().getConnection());
				gpp.setGroupID(gps.getGroupID());
				gpp.setProductID(selectedProduct.getProductID());
				gpp.setProductDepartmentID(selectedProduct.getProductDepartmentID());
				gpp.setActive(true);
				gpp.setSysUserAddID(controller.getUser().getUserID());
				gpp.setPosition(g.getPosition());
				gpp.setColor(g.getColor());
				gpp.setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
				
				gpp.insert(controller.getSettings().getConnection());
				loadGroup();
			}
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	@FXML private void groupDelete(){
		try{
			tableGroup.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			loadGroup();
		} catch (NullPointerException e){}
	}
	
	@FXML private void groupPost(){
		tableGroup.getSelectionModel().getSelectedItem().setActive(cbGroupActive.isSelected());
		tableGroup.getSelectionModel().getSelectedItem().setSort(Integer.parseInt(tfGroupSort.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setPosition(Integer.parseInt(tfGroupPosition.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setColor(tfGroupColor.getText());
		
		tableGroup.getSelectionModel().getSelectedItem().setProductID(selectedProduct.getProductID());
		tableGroup.getSelectionModel().getSelectedItem().setSysUserEditID(controller.getUser().getUserID());
		tableGroup.getSelectionModel().getSelectedItem().setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
				
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
	
	private void showDodatniData(GroupPointOfSaleProduct dodatni){
		try{
			tfDodatniSort.setText(String.valueOf(dodatni.getSort()));
		} catch (NullPointerException e){
			tfDodatniSort.setText("");
		}
		try{
			tfDodatniPosition.setText(String.valueOf(dodatni.getPositionDodatni()));
		} catch (NullPointerException e){
			tfDodatniPosition.setText("");
		}
		try{
			tfDodatniColor.setText(dodatni.getColorDodatni());
		} catch (NullPointerException e){
			tfDodatniColor.setText("");
		}
		try{
			cbDodatniActive.setSelected(dodatni.isActiveDodatni());
		}  catch (NullPointerException e){
			cbDodatniActive.setSelected(false);
		}
	}
	
	private void showOpisniData(PointOfSaleProductDescription point){
		try{
			if(point.getColor() != null || point.getColor().equals("")) {
				colorPickerOpisni.setValue(Color.valueOf(point.getColor()));
			} else {
				colorPickerOpisni.setValue(Color.AZURE);
			}
		} catch (NullPointerException e){
			colorPickerOpisni.setValue(Color.ALICEBLUE);
		}
		
		try{
			tfOpisniPosition.setText(""+point.getPositionBtn());
		}  catch (NullPointerException e){
			tfOpisniPosition.setText("");
		}
		
		try{
			cbOpisniActive.setSelected(point.isActive());
		}  catch (NullPointerException e){
			cbOpisniActive.setSelected(false);
		}
	}
	
	@FXML private void objekatChange(){
		if (cbObjekat.getSelectionModel().getSelectedItem().getObjectID() == -1){
			cbOdeljenje.getSelectionModel().select(0);
			cbPos.getSelectionModel().select(0);
			
			cbOdeljenje.setDisable(true);
			cbPos.setDisable(true);
			
			tabNormativ.setDisable(true);
			tabGrupe.setDisable(true);
		}
		else{
			popuniFilterOdeljene();
			cbOdeljenje.setDisable(false);
			popuniFilterPos();
			cbPos.setDisable(false);
		}
		loadProductList();
	}
	
	@FXML private void departmentChange(){
		if  ((cbOdeljenje.getSelectionModel().getSelectedItem() == null) ||
			 (cbOdeljenje.getSelectionModel().getSelectedItem().getDepartmentID() == -1)){
			cbPos.getSelectionModel().select(0);
			cbPos.setDisable(true);
			
			tabNormativ.setDisable(true);
			tabKopirajNormativ.setDisable(true);
			
			tfPrepTime.setText("");
			tfPrepTime.setDisable(true);
			cbKitchenDisplay.getSelectionModel().select(-1);
			cbKitchenDisplay.setDisable(true);
		}
		else{
			tabDodatni.setDisable(false);
			cbPos.setDisable(false);
			tfPrepTime.setDisable(false);
			cbKitchenDisplay.setItems(KitchenDisplay.getList(cbObjekat.getSelectionModel().getSelectedItem(), 
															 controller.getSettings().getConnection()));
			if (cbKitchenDisplay.getItems().size() > 0)
				cbKitchenDisplay.getSelectionModel().select(0);
			cbKitchenDisplay.setDisable(false);
		}
		loadProductList();
	}
	
	@FXML private void posChange(){
		if ((cbPos.getSelectionModel().getSelectedItem() == null) ||
			(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID() == -1)){
			tabGrupe.setDisable(true);
			tabOpisni.setDisable(true);
		} else {
			tabGrupe.setDisable(false);
			tabOpisni.setDisable(false);
		}
		loadProductList();
		loadGroup();
	}
	
	private void popuniFilterOdeljene(){
		cbOdeljenje.getItems().clear();
		Department svaOdeljenja = new Department();
		svaOdeljenja.setDepartmentID(-1);
		svaOdeljenja.setName("Sva odeljenja");
		cbOdeljenje.getItems().add(svaOdeljenja);
		for (Department d : Department.getList(cbObjekat.getSelectionModel().getSelectedItem(),
											   "", controller.getSettings().getConnection())){
		cbOdeljenje.getItems().add(d);										   
		}
		cbOdeljenje.getSelectionModel().select(svaOdeljenja);
		cbPos.getSelectionModel().select(0);
		cbPos.setDisable(true);
	}
	private void popuniFilterPos(){
		cbPos.getItems().clear();
		PointOfSale sviPosovi = new PointOfSale();
		sviPosovi.setPointOfSaleID(-1);
		sviPosovi.setName("Sva prodajna mesta");
		cbPos.getItems().add(sviPosovi);
		for (PointOfSale p : PointOfSale.getPosList(cbObjekat.getSelectionModel().getSelectedItem(),controller.getSettings().getConnection())){
			cbPos.getItems().add(p);
		}
		
		cbPos.getSelectionModel().select(sviPosovi);
	}
	
	private void loadProductList(){
		long productId = -1;
		try{
			productId = productTable.getSelectionModel().getSelectedItem().getProductID();
		} catch (NullPointerException e){}
		productTable.getItems().clear();
		productTable.setItems(Product.getObeservableList(controller.getObject(),
							  cbPos.getSelectionModel().getSelectedItem(),
							  cbOdeljenje.getSelectionModel().getSelectedItem(),
							  tfFilter.getText(),
							  controller.getSettings().getConnection()));
		if (productId > 0){
			for (Product p : productTable.getItems()){
				if (p.getProductID() == productId)
					productTable.getSelectionModel().select(p);
			}
		}
	}
}
