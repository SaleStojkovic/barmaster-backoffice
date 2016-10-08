package rs.petcom.backoffice.controller.popis;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.ControllData;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class ControllListController implements ControlledScreen{
	
	ScreensController controller;
	
	public void initialize(){
		cbObject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<rs.petcom.master.dal.Object>() {

			@Override
			public void changed(ObservableValue<? extends rs.petcom.master.dal.Object> observable,
					rs.petcom.master.dal.Object oldValue, rs.petcom.master.dal.Object newValue) {
				loadDepartment();
				loadList();
			}
		});
		
		
		
		dcDateFrom.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				loadList();
			}
		});
		dcDateTo.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				loadList();
			}
		});
		
		tcTime.setCellValueFactory(new PropertyValueFactory<ControllData, Date>("ControllTime"));
		tcTime.setCellFactory((TableColumn<ControllData, Date> column) -> {
			   return new TableCell<ControllData, Date>() {
				      @Override
				      protected void updateItem(Date item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getDateTimeFromatter().format(item));
				         }
				      }
				   };
				});
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void init() {
		cbObject.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		if(controller.getPointOfSale() == null){
			cbObject.getSelectionModel().select(0);
		}
		else{
			for(rs.petcom.master.dal.Object object : cbObject.getItems()){
				if (object.getObjectID() == controller.getPointOfSale().getObjectID()){
					cbObject.getSelectionModel().select(object);
					break;
				}
			}
		}
		dcDateFrom.setValue(LocalDate.ofYearDay(LocalDate.now().getYear(), 1));
		dcDateTo.setValue(LocalDate.now());
	}
	@Override
	public void setParameter(Object parameter) {
		cbObject.setDisable(false);
		if (controller.getPointOfSale() != null){
			for (rs.petcom.master.dal.Object o : cbObject.getItems()){
				if (o.getObjectID() == controller.getPointOfSale().getObjectID()){
					cbObject.getSelectionModel().select(o);
					cbObject.setDisable(true);
					break;
				}
			}
		}
	}
	@Override
	public Object getParameter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@FXML private BorderPane rootPane;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private ComboBox<Department> cbDepartment;
	@FXML private DatePicker dcDateFrom;
	@FXML private DatePicker dcDateTo;
	@FXML private Button btnPrint;
	@FXML private TableView<ControllData> tbTime;
	@FXML private TableColumn<ControllData, Date> tcTime;
	
	@FXML public void createNew(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("title", "Presek smene");
		hm.put("text", "Da li ste sigurni da želite da uradite presek smene?");
		int result = controller.showModalDialog("Presek smene",ScreensController.DIALOG_YES_NO,hm,rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_YES){
			Timestamp controllDate = new Timestamp(new Date().getTime());
			hm = new HashMap<>();	
			hm.put("date", controllDate);
			hm.put("controller", controller);
			hm.put("object", cbObject.getSelectionModel().getSelectedItem());
			hm.put("department", cbDepartment.getSelectionModel().getSelectedItem());
			int result2 = controller.showModalDialog("Presek smene", ScreensController.SCREEN_BACKOFFICE_POPIS_PRESEK_SMENE, hm, rootPane.getScene().getWindow());
			if (result2 == ScreensController.MODAL_RESULT_OK){
				loadList();
			}
		}
	}
	@FXML public void print(ActionEvent event) {
		String reportFileName = "rs/petcom/backoffice/jasper/PresekSmene.jasper";
		try {
			HashMap<String, Object> hm = new HashMap<>();
			Timestamp controllDate;
			controllDate = new Timestamp(tbTime.getSelectionModel().getSelectedItem().getControllTime().getTime());
			hm.put("time", controllDate);
			hm.put("object", controller.getObject());
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	private void loadList(){
		try {
			if (dcDateFrom.getValue() != null && 
				dcDateTo.getValue() != null &&
				cbObject.getSelectionModel().getSelectedItem() != null)
			tbTime.setItems(ControllData.getControllList(dcDateFrom.getValue(), 
														 dcDateTo.getValue(), 
														 cbObject.getSelectionModel().getSelectedItem().getObjectID(), 
														 controller.getSettings().getConnection()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML private void dateFromChange(){
		loadList();
	}
	
	private void loadDepartment(){
		cbDepartment.getItems().clear();
		for (Department d : Department.getList(cbObject.getSelectionModel().getSelectedItem(), 
						   					   controller.getSettings().getConnection())){
			cbDepartment.getItems().add(d);
		}
		if (cbDepartment.getItems().size() == 1){
			cbDepartment.getSelectionModel().select(0);
		}
	}
}
