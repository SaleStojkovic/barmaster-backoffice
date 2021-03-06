package rs.petcom.backoffice.controller.document;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.PocetnoStanje;
import rs.petcom.master.dal.person.Person;
import rs.petcom.print.JRPrintPreviewPane;

public class PocetnoStanjeController implements ControlledScreen{
	ScreensController controller;
	
	rs.petcom.master.dal.Object sviObjekti = new rs.petcom.master.dal.Object(-1,"Svi objekti");
	Department svaOdeljenja = new Department(-1,"Sva odeljenja");
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void init() {
		cbObject.getItems().add(sviObjekti);
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			cbObject.getItems().add(o);
		}
		if (cbObject.getItems().size() == 2){
			cbObject.getSelectionModel().select(1);
		}
		else{
			cbObject.getSelectionModel().select(sviObjekti);
		}
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
		if ((controller.getVrstaPrograma() == ScreensController.BACKOFFICE) && 
				(controller.getObject() != null)){
				for (rs.petcom.master.dal.Object o : cbObject.getItems()){
					if (o.getObjectID() == controller.getObject().getObjectID()){
						cbObject.getSelectionModel().select(o);
						cbObject.setDisable(true);
						break;
					}
				}
			}
	}
	@Override
	public Object getParameter() {
		return null;
	}
	
	public void initialize(){
		cbObject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<rs.petcom.master.dal.Object>() {
			@Override
			public void changed(ObservableValue<? extends rs.petcom.master.dal.Object> observable,
					rs.petcom.master.dal.Object oldValue, rs.petcom.master.dal.Object newValue) {
				loadDepartment();				
			}
		});
		cbDepartment.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Department>() {
			@Override
			public void changed(ObservableValue<? extends Department> observable, Department oldValue, Department newValue) {
				loadDocumentList();				
			}
		});
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PocetnoStanje>() {
			@Override
			public void changed(ObservableValue<? extends PocetnoStanje> observable, PocetnoStanje oldValue,
					PocetnoStanje newValue) {
				if (newValue == null){
					btnDelete.setDisable(true);
					btnPrint.setDisable(true);
					btnEdit.setDisable(true);
				}
				else{
					btnDelete.setDisable(false);
					btnPrint.setDisable(false);
					btnEdit.setDisable(false);
				}
			}
		});
		tcBroj.setCellValueFactory(new PropertyValueFactory<PocetnoStanje, Integer>("Number"));
		tcDate.setCellValueFactory(new PropertyValueFactory<PocetnoStanje, Date>("Date"));
		tcDate.setCellFactory((TableColumn<PocetnoStanje, Date> column) -> {
			   return new TableCell<PocetnoStanje, Date>() {
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
		tcNapomena.setCellValueFactory(new PropertyValueFactory<PocetnoStanje, String>("Note"));
	}
	
	@FXML private BorderPane rootPane;
	
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private ComboBox<Department> cbDepartment;
	
	@FXML private TableView<PocetnoStanje> table;
	@FXML private TableColumn<PocetnoStanje, Integer> tcBroj;
	@FXML private TableColumn<PocetnoStanje, Date> tcDate;
	@FXML private TableColumn<PocetnoStanje, String> tcNapomena;
	
	@FXML private Button btnNew;
	@FXML private Button btnEdit;
	@FXML private Button btnPrint;
	@FXML private Button btnDelete;

	@FXML public void actionNew(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("controller", controller);
		hm.put("pocetnoStanje", new PocetnoStanje(-1, -1, new Date(), 
												  cbDepartment.getSelectionModel().getSelectedItem().getDepartmentID(),
												  ""));
		
		int result = controller.showModalDialog("Početno stanje - Detalji", 
								   ScreensController.SCREEN_BACKOFFICE_DOC_POCETNO_STANJE_DETAILES, hm, 
								   rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_OK){
			loadDocumentList();
		}
	}

	@FXML public void actionEdit(ActionEvent event) {
		if (table.getSelectionModel().getSelectedItem() != null){
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("controller", controller);
			hm.put("pocetnoStanje", table.getSelectionModel().getSelectedItem());
			
			controller.showModalDialog("Početno stanje - Detalji", 
									   ScreensController.SCREEN_BACKOFFICE_DOC_POCETNO_STANJE_DETAILES, hm, 
									   rootPane.getScene().getWindow());
		}
	}
	
	@FXML public void actionPrint(ActionEvent event) {
		
		String reportFileName = "rs/petcom/backoffice/jasper/PocetnoStanje.jasper";
		Map<String, Object> hm = new HashMap<>();
		
		Document document = Document.getByID(table.getSelectionModel().getSelectedItem().getDocumentID(), 
											 controller.getSettings().getConnection());
		
		hm.put("memorandum", controller.getSettings().getSetting("company.memorandum", null));
		hm.put("person",Person.getByCode("1", controller.getSettings().getConnection()));
		hm.put("documentId", document.getDocumentID());
		hm.put("document", document);
				
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Početno stanje",
									   ScreensController.DIALOG_PRINT_PREVIEW, 
									   hm2,
									   rootPane.getScene().getWindow());
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@FXML public void actionDelete(ActionEvent event) {
		try{
			table.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
		} catch (SQLException e){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("title", "Brisanje početnog stanja nije moguće!");
			hm.put("text", "\nPoruka o grešci:\n\n" + e.getLocalizedMessage());
			controller.showModalDialog("Brisanje početnog stanja nije moguće!",
									   ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
		}
	}
	
	private void loadDepartment(){
		cbDepartment.getItems().clear();
		cbDepartment.getItems().add(svaOdeljenja);
		for (Department d : Department.getList(cbObject.getSelectionModel().getSelectedItem(), 
						   					   controller.getSettings().getConnection())){
			cbDepartment.getItems().add(d);
		}
		if (cbDepartment.getItems().size() == 2){
			cbDepartment.getSelectionModel().select(1);
		}
		else{
			cbDepartment.getSelectionModel().select(svaOdeljenja);
		}
	}
	
	private void loadDocumentList(){
		table.getItems().clear();
		try{
			if (cbDepartment.getSelectionModel().getSelectedItem().getDepartmentID() > 0){
				table.setItems(PocetnoStanje.getlist(cbDepartment.getSelectionModel().getSelectedItem(), 
													 controller.getSettings().getConnection()));
			}
		} catch (NullPointerException e){}
	}
}
