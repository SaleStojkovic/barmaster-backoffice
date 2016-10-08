package rs.petcom.backoffice.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.NormativeItem;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.Unit;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.GroupPointOfSaleProduct;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductCategory;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.dal.product.ProductType;
import rs.petcom.master.gui.PointOfSaleCheckBoxCell;
import rs.petcom.master.gui.TreeGroupCell;

public class ProductNewDialogController implements DialogController{
	
	ScreensController controller;
	ObservableList<NormativeItem> normativeList = FXCollections.observableArrayList();
	
	TreeItem<Group> treeRoot = new TreeItem<>();
	TreeItem<PointOfSale> treeRootD = new TreeItem<>();
	
	public void initialize(){
		treeGroup.setCellFactory(g -> new TreeGroupCell());
		treeGroup.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<Group>>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends TreeItem<Group>> c) {
				tfGroupColor.setText(treeGroup.getSelectionModel().getSelectedItem().getValue().getColor());
				tfGroupPosition.setText(String.valueOf(treeGroup.getSelectionModel().getSelectedItem().getValue().getPosition()));
			}
		});
		treeGroup.setShowRoot(false);
		
		tfGroupPosition.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (treeGroup.getSelectionModel().getSelectedItem() != null){
		    	Group group = treeGroup.getSelectionModel().getSelectedItem().getValue();
		    	group.setPosition(Integer.parseInt(newValue));
		    }
		});
		
		tfGroupColor.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (treeGroup.getSelectionModel().getSelectedItem() != null){
		    	treeGroup.getSelectionModel().getSelectedItem().getValue().setColor(newValue);
		    }
		});
		treeDepartment.setCellFactory(g -> new PointOfSaleCheckBoxCell());
		treeDepartment.setShowRoot(false);
		
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
		
	}
	
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameter;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		cbVat.setItems(Vat.getObeservableList(controller.getSettings().getConnection()));
		cbVat.getSelectionModel().select(0);
		cbCategory.setItems(ProductCategory.getObeservableList(controller.getSettings().getConnection()));
		cbCategory.getSelectionModel().select(0);
		cbType.setItems(ProductType.getObeservableList(true, controller.getSettings().getConnection()));
		cbType.getSelectionModel().select(0);
		cbUnit.setItems(Unit.getList(controller.getSettings().getConnection()));
		cbUnit.getSelectionModel().select(0);
		
		// Učitavanje grupa
		treeRoot = new CheckBoxTreeItem<>();
		treeRoot.setValue(new Group(-1,"Grupe"));
		for (Group g : Group.getList(0, controller.getSettings().getConnection())){
			TreeItem<Group> groupItem = new TreeItem<>();
			groupItem.setValue(g);
		    treeRoot.getChildren().add(groupItem);
		    for(Group g1 : Group.getList(g.getGroupID(), controller.getSettings().getConnection())){
		    	TreeItem<Group> groupItem1 = new TreeItem<>();
				groupItem1.setValue(g1);
				groupItem.getChildren().add(groupItem1);
		    }
		    groupItem.setExpanded(true);
		}
		treeRoot.setExpanded(true);
		treeGroup.setRoot(treeRoot);
		
		// Učitavanje odeljena
		treeRootD = new TreeItem<PointOfSale>();
		treeRootD.setValue(new PointOfSale(-1,"Objekti"));
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			TreeItem<PointOfSale> objekatItem = new TreeItem<PointOfSale>();
			objekatItem.setValue(new PointOfSale(o.getObjectID(), o.getName()));
			for (PointOfSale d : PointOfSale.getPosList(o, controller.getSettings().getConnection())){
				TreeItem<PointOfSale> departmentItem = new TreeItem<PointOfSale>();
				departmentItem.setValue(d);
				objekatItem.getChildren().add(departmentItem);
				objekatItem.setExpanded(true);
			}
			treeRootD.getChildren().add(objekatItem);
		}
		treeRootD.setExpanded(true);
		treeDepartment.setRoot(treeRootD);
		
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
	
	@FXML private TreeView<Group> treeGroup;
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
	
	@FXML private TreeView<PointOfSale> treeDepartment;
	
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
	
			long productId = p.insert(controller.getUser(), controller.getSettings().getConnection());
			p.setProductID(productId);
			
			ArrayList<Group> izabraneGrupe = new ArrayList<>();
			for (TreeItem<Group> g : treeRoot.getChildren()){
				if (g.getValue().isSelected()) izabraneGrupe.add(g.getValue());
				for (TreeItem<Group> g1 : g.getChildren()){
					if (g1.getValue().isSelected()) izabraneGrupe.add(g1.getValue());
				}
			}
			
			ArrayList<PointOfSale> izabraniPosovi = new ArrayList<>();
			for (TreeItem<PointOfSale> pos : treeRootD.getChildren()){
				for (TreeItem<PointOfSale> pos1 : pos.getChildren()){
					if (pos1.getValue().isSelected()) izabraniPosovi.add(pos1.getValue());
				}
			}
			
			ArrayList<Department> odeljenjaZaDodavanje = new ArrayList<>();
			for (PointOfSale pos : izabraniPosovi){
				for (Department d : pos.getDepartmentList(controller.getSettings().getConnection())){
					boolean postoji = false;
					for (Department dp : odeljenjaZaDodavanje){
						if (dp.getDepartmentID() == d.getDepartmentID()){
							postoji = true;
							break;
						}
					}
					if (!postoji) odeljenjaZaDodavanje.add(d);
				}
			}
			
			//Ubacivanje ProductDepartment-a
			ArrayList<ProductDepartment> dodatiProductDepartment = new ArrayList<>();
			for (Department d : odeljenjaZaDodavanje){
				ProductDepartment pd = new ProductDepartment();
				pd.setProductID(productId);
				pd.setSort(0);
				pd.setActive(true);
				pd.setFavorite(false);
				pd.setDepartmentID(d.getDepartmentID());
				pd.insert(controller.getSettings().getConnection());
				dodatiProductDepartment.add(pd);
			}
			
			// Ubacivanje u grouppointofsaleproduct
			
			ArrayList<Group> grupeZaDodavanje = new ArrayList<>();
			
			for (PointOfSale pos : izabraniPosovi){
				grupeZaDodavanje.clear();
				for (ProductDepartment pdp : dodatiProductDepartment){
					for (Group gr : izabraneGrupe){
						boolean dodato = false;
						for (Group g : grupeZaDodavanje){
							if (g.getGroupID() == gr.getGroupID()){
								dodato = true;
								break;
							}
						}
						if (!dodato){
							GroupPointOfSaleProduct g = new GroupPointOfSaleProduct();
							g.setGroupID(gr.getGroupID());
							g.setProductID(productId);					 
							g.setProductDepartmentID(pdp.getProductDepartmentID());
							g.setActive(true);
							g.setSysUserAddID(controller.getUser().getUserID());
							g.setPointOfSaleID(pos.getPointOfSaleID());
							g.setPosition(gr.getPosition());
							g.setColor(gr.getColor());
							g.insert(controller.getSettings().getConnection());
							grupeZaDodavanje.add(gr);
						}
					}
				}
			}
					
			// Ubacivane u productcomponent
			for (ProductDepartment pdd : dodatiProductDepartment){
				for (NormativeItem ni : normativTable.getItems()){
					ni.setArticleProductDepartmenID(pdd.getProductDepartmentID());
					ni.getProduct().setProductDepartmentID(ProductDepartment.getFromDB(ni.getProduct().getProductID(), 
																				  	   pdd.getDepartmentID(), 
																				  	   controller.getSettings().getConnection()).getProductDepartmentID());
					ni.insert(controller.getSettings().getConnection());
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
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
		//hm.put("product", new Product());
		hm.put("connection", controller.getSettings().getConnection());
		hm.put("controller", controller);
		int result = controller.showModalDialog("Izbor proizvoda",ScreensController.DIALOG_SELECT_PRODUCT,hm,rootPane.getScene().getWindow());
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
