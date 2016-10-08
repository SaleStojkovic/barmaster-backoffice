package rs.petcom.master.controller.backoffice;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.NormativeItem;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.Unit;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.GroupPointOfSaleProduct;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductCategory;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.dal.product.ProductType;

public class ProductNewDialogController implements DialogController{
	
	Connection connection;
	ScreensController controller;
	
	ObservableList<GroupPointOfSaleProduct> groupList = FXCollections.observableArrayList();
	ObservableList<NormativeItem> normativeList = FXCollections.observableArrayList();
	ObservableList<Department> departmentList = FXCollections.observableArrayList();
	
	public void initialize(){
		tableGroup.setItems(groupList);
		tableGroup.setPlaceholder(new Label(""));
		normativTable.setItems(normativeList);		
		normativTable.setPlaceholder(new Label(""));
		grColName.setCellValueFactory(new PropertyValueFactory<GroupPointOfSaleProduct, String>("GroupName"));
		tableGroup.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<GroupPointOfSaleProduct>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends GroupPointOfSaleProduct> c) {
				showGroupData(tableGroup.getSelectionModel().getSelectedItem());
			}
		});
		
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
		
		tfNormative.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter()));
		tfOrder.setTextFormatter(new TextFormatter<>(Settings.getDoubleFilter())); 
		
		lvDepartment.setItems(departmentList);
	}
	
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameter;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		connection = (Connection) parameter.get("connection");
		cbVat.setItems(Vat.getObeservableList(connection));
		cbVat.getSelectionModel().select(0);
		cbCategory.setItems(ProductCategory.getObeservableList(connection));
		cbCategory.getSelectionModel().select(0);
		cbType.setItems(ProductType.getObeservableList(true, connection));
		cbType.getSelectionModel().select(0);
		cbUnit.setItems(Unit.getList(connection));
		cbUnit.getSelectionModel().select(0);
	}

	@Override
	public HashMap<String, Object> getParameter() {
		return parameter;
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
	@FXML private TextField tfName;
	@FXML private TextField tfShortName;
	@FXML private TextField tfCode;
	@FXML private TextField tfColor;
	@FXML private TextField tfNutrition;
	@FXML private TextField tfEnergy;
	@FXML private ComboBox<Vat> cbVat;
	@FXML private ComboBox<ProductCategory> cbCategory;
	@FXML private ComboBox<ProductType> cbType;
	@FXML private ComboBox<Unit> cbUnit;
	
	@FXML private TableView<GroupPointOfSaleProduct> tableGroup;
	@FXML private TableColumn<GroupPointOfSaleProduct, String> grColName;
	@FXML private TextField tfGroupSort;
	@FXML private TextField tfGroupPosition;
	@FXML private TextField tfGroupColor;
	
	@FXML private TableView<NormativeItem> normativTable;
	@FXML private TableColumn<NormativeItem, String> ntColCode;
	@FXML private TableColumn<NormativeItem, String> ntColName;
	@FXML private TableColumn<NormativeItem, String> ntColShortName;
	@FXML private TableColumn<NormativeItem, Double> ntColNormative;
	@FXML private TableColumn<NormativeItem, Integer> ntColOrder;
	@FXML private TextField tfNormative;
	@FXML private TextField tfOrder;
	
	@FXML private ListView<Department> lvDepartment;
	
	@FXML public void cancel(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
	@FXML private void commit(){
		try{
			Product p = new Product();
			p.setProductTypeID(cbType.getSelectionModel().getSelectedItem().getProductTypeID());
			p.setProductCategoryID(cbCategory.getSelectionModel().getSelectedItem().getProductCategoryID());
			p.setVatID(cbVat.getSelectionModel().getSelectedItem().getVatID());
			p.setUnitID(cbUnit.getSelectionModel().getSelectedItem().getUnitID());
			p.setActive(true);
			p.setSysDTCreated(new Date());
			
			p.setCode(tfCode.getText());
			p.setName(tfName.getText());
			p.setShortName(tfShortName.getText());
			p.setSysUserAddID(controller.getUser().getUserID());
			try {
				p.setNutritionalValue(Settings.getNumberFormat(4, true).parse(tfNutrition.getText()).doubleValue());
			} catch (ParseException e) {
				p.setNutritionalValue(0);
			}
			try {
				p.setEnergyValue(Settings.getNumberFormat(4, true).parse(tfEnergy.getText()).doubleValue());
			} catch (ParseException e) {
				p.setEnergyValue(0);
			}
			p.setColor(tfColor.getText());
	
			long productId = p.insert(controller.getUser(), connection);
			p.setProductID(productId);
			
			// Ubacivanje grupa
			for (GroupPointOfSaleProduct g : groupList){
				g.setProductID(productId);					 
				g.setActive(true);
				g.setSysUserAddID(controller.getUser().getUserID());
				g.setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
				g.insert(connection);
			}
			
			for (Department d : departmentList){
				ProductDepartment pd = new ProductDepartment();
				pd.setProductID(productId) ;
				pd.setSort(0);
				pd.setActive(true);
				pd.setFavorite(false);
				pd.setMin(0);
				pd.setMax(0);	
				pd.setDepartmentID(d.getDepartmentID());
				long departmentId = pd.insert(connection);
				p.setProductID(departmentId);
				
				//Ubacivanje normativa
				for(NormativeItem n : normativeList){
					n.setArticleProductDepartmenID(departmentId);
					n.insert(connection);
				}
			}
		} catch (Exception e){
			controller.setDialogTitle("Greška prilikom upis u bazu");
			controller.setDialogText(e.getMessage());
			String[] buttons = {"OK"};
			controller.setDialogButtonText(buttons);
			
			controller.getScreen(ScreensController.SCREEN_ALLERT, 
								 controller.getRoot(),
								 ScreensController.SCREEN_LOGIN);
		}
	
		
		
			
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
	@FXML private void groupNew(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("user",controller.getUser());
		hm.put("PointOfSale", controller.getPointOfSale());
		hm.put("connection",controller.getSettings().getConnection());
		hm.put("controller", controller);
		hm.put("product", new Product());
		Group g = new Group();
		hm.put("group", g);
		int result = controller.showModalDialog("Izbor grupe",ScreensController.DIALOG_SELECT_GROUP,hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			GroupPointOfSaleProduct gps = new GroupPointOfSaleProduct();
			gps.setGroupID(((Group) hm.get("group")).getGroupID());
			gps.setGroupName(((Group) hm.get("group")).getName());
			gps.setColor(((Group) hm.get("group")).getColor());
			gps.setPosition(((Group) hm.get("group")).getPosition());
			
			groupList.add(gps);
		}
	}
	@FXML private void groupDelete(){
		groupList.remove(tableGroup.getSelectionModel().getSelectedItem());
	}
	@FXML private void groupPost(){
		tableGroup.getSelectionModel().getSelectedItem().setSort(Integer.parseInt(tfGroupSort.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setPosition(Integer.parseInt(tfGroupPosition.getText()));
		tableGroup.getSelectionModel().getSelectedItem().setColor(tfGroupColor.getText());
	}
	
	@FXML private void normativePost(){
		try {
			NormativeItem ni = normativTable.getSelectionModel().getSelectedItem();
			ni.setNormative(Settings.getNumberFormat(4, true).parse(tfNormative.getText()).doubleValue());
			ni.setOrder(Settings.getNumberFormat(0, true).parse(tfOrder.getText()).intValue());
			normativeList.set(normativTable.getSelectionModel().getSelectedIndex(),ni);
		} catch (ParseException e) {}
	}
	@FXML private void normativeNew(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("product", new Product());
		hm.put("connection", controller.getSettings().getConnection());
		hm.put("PointOfSale", controller.getPointOfSale());
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_NORMATIVE_PRODUCT,hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			NormativeItem ni = new NormativeItem();
			ni.setProduct((Product)hm.get("product"));

			ni.setNormative(1);
			int maxOrder = 0;
			for (int i=0;i<normativeList.size();i++){
				if (maxOrder < normativeList.get(i).getOrder())
					maxOrder = normativeList.get(i).getOrder();
			}
			ni.setOrder(maxOrder+1);
			normativeList.add(ni);
			normativTable.getSelectionModel().select(normativeList.size()-1);
			showNormative(normativTable.getSelectionModel().getSelectedItem());
		}
	}
	@FXML private void normativeDelete(){
		normativeList.remove(normativTable.getSelectionModel().getSelectedItem());
	}
	
	@FXML private void departmentNew(){
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor odeljenja",ScreensController.DIALOG_SELECT_DEPARTMENT,hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			departmentList.add((Department)hm.get("department"));
		}
	}
	@FXML private void departmentDelete(){
		departmentList.remove(lvDepartment.getSelectionModel().getSelectedItem());
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
	}
	
	private void showNormative(NormativeItem ni){
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
}
