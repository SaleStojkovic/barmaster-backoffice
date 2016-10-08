package rs.petcom.backoffice.controller.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.Presifriranje;
import rs.petcom.master.dal.person.Person;
import rs.petcom.print.JRPrintPreviewPane;

public class PresifriranjeController implements ControlledScreen{
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
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Presifriranje>() {
			@Override
			public void changed(ObservableValue<? extends Presifriranje> observable, Presifriranje oldValue,
					Presifriranje newValue) {
				if (newValue == null){
					btnDelete.setDisable(true);
					btnPrint.setDisable(true);
					btnEdit.setDisable(true);
					btnExport.setDisable(true);
				}
				else{
					btnDelete.setDisable(false);
					btnPrint.setDisable(false);
					btnEdit.setDisable(false);
					btnExport.setDisable(false);
				}
			}
		});
		tcBroj.setCellValueFactory(new PropertyValueFactory<Presifriranje, Integer>("Number"));
		tcDate.setCellValueFactory(new PropertyValueFactory<Presifriranje, Date>("Date"));
		tcDate.setCellFactory((TableColumn<Presifriranje, Date> column) -> {
			   return new TableCell<Presifriranje, Date>() {
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
		tcNapomena.setCellValueFactory(new PropertyValueFactory<Presifriranje, String>("Note"));
	}
	
	@FXML private BorderPane rootPane;
	
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private ComboBox<Department> cbDepartment;
	
	@FXML private TableView<Presifriranje> table;
	@FXML private TableColumn<Presifriranje, Integer> tcBroj;
	@FXML private TableColumn<Presifriranje, Date> tcDate;
	@FXML private TableColumn<Presifriranje, String> tcNapomena;
	
	@FXML private Button btnNew;
	@FXML private Button btnEdit;
	@FXML private Button btnPrint;
	@FXML private Button btnDelete;
	@FXML private Button btnExport;

	@FXML public void actionNew(ActionEvent event) {
		try{
			// Uradi rekalkulaciju utrošaka za trenutni dan
			LocalDate currentDate = LocalDate.now();
			for (WorkDay wd : WorkDay.getList(currentDate,currentDate, 
											  controller.getObject().getObjectID(),
											  controller.getSettings().getConnection())){
				wd.saveRecap(controller);
			}
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("controller", controller);
			hm.put("presifriranje", new Presifriranje(-1, -1, new Date(), 
													  cbDepartment.getSelectionModel().getSelectedItem().getDepartmentID(),
													  ""));
			
			int result = controller.showModalDialog("Prešifriranje - Detalji", 
									   ScreensController.SCREEN_BACKOFFICE_DOC_PRESIFRIRANJE_DETAILES, hm, 
									   rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK){
				loadDocumentList();
			}
		} catch (SQLException e ){
			controller.alert("Greška prilikom upisa u bazu!", "Došlo je do greške prilikom upisa u bazu", rootPane.getScene().getWindow());
		}
	}

	@FXML public void actionEdit(ActionEvent event) {
		if (table.getSelectionModel().getSelectedItem() != null){
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("controller", controller);
			hm.put("presifriranje", table.getSelectionModel().getSelectedItem());
			
			controller.showModalDialog("Prešifriranje - Detalji", 
									   ScreensController.SCREEN_BACKOFFICE_DOC_PRESIFRIRANJE_DETAILES, hm, 
									   rootPane.getScene().getWindow());
		}
	}
	
	@FXML public void actionPrint(ActionEvent event) {
		
		String reportFileName = "rs/petcom/backoffice/jasper/Presifriranje.jasper";
		Map<String, Object> hm = new HashMap<>();
		
		Document document = Document.getByID(table.getSelectionModel().getSelectedItem().getDocumentID(), 
											 controller.getSettings().getConnection());
		
		hm.put("memorandum", controller.getSettings().getSetting("company.memorandum", null));
		hm.put("person",Person.getByCode("1", controller.getSettings().getConnection()));
		hm.put("documentId", document.getDocumentID());
		hm.put("document", document);
		hm.put("object", cbObject.getSelectionModel().getSelectedItem());
		hm.put("department", cbDepartment.getSelectionModel().getSelectedItem());
				
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
				table.setItems(Presifriranje.getlist(cbDepartment.getSelectionModel().getSelectedItem(), 
													 controller.getSettings().getConnection()));
			}
		} catch (NullPointerException e){}
	}
	
	@FXML private void actionExport(){
		//SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		Presifriranje p = table.getSelectionModel().getSelectedItem();
		Department d = cbDepartment.getSelectionModel().getSelectedItem();
		// p.setDepartmentID(d.getDepartmentID());
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Export Prešifravanja");
        fileChooser.setInitialFileName("Presifravanje-"  + 
        							   d.getCode() + "-" +
        							   p.getNumber() + ".xml");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (file != null) {
        	try {
            	if (file.getAbsolutePath().toUpperCase().endsWith(".XML"))
						p.exportToXml(file.getAbsolutePath(), controller.getSettings().getConnection());	
				else
            		p.exportToXml(file.getAbsolutePath()+".xml", controller.getSettings().getConnection());
        	} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
	}
}
