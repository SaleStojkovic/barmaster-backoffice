package rs.petcom.backoffice.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.NormativeItem;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.gui.PointOfSaleCheckBoxCell;

public class NormativCopyController implements DialogController{
	ScreensController controller;
	ObservableList<NormativeItem> normativeList = FXCollections.observableArrayList();
	
	TreeItem<PointOfSale> treeRootD = new TreeItem<>();
	
	public void initialize(){
		
		treeDepartment.setCellFactory(g -> new PointOfSaleCheckBoxCell());
		treeDepartment.setShowRoot(false);
		
		
	}
	
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	HashMap<String, Object> parameter;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		
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
	
	@FXML private TreeView<PointOfSale> treeDepartment;
	
	@FXML public void cancel(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
	@FXML private void commit(){
		try{
			
			Product p = new Product();
			p.setActive(true);
			p.setSysDTCreated(new Date());
		
			p.setSysUserAddID(controller.getUser().getUserID());

			long productId = p.insert(controller.getUser(), controller.getSettings().getConnection());
			p.setProductID(productId);
			
			
			ArrayList<Department> odeljenjaZaDodavanje = new ArrayList<>();
			
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
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		modalResult = ScreensController.MODAL_RESULT_OK;
		((Stage) rootPane.getScene().getWindow()).close();
	}

}
