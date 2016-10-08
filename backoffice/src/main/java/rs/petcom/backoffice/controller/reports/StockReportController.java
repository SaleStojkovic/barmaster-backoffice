package rs.petcom.backoffice.controller.reports;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class StockReportController  implements ControlledScreen{
	
	ScreensController controller;
	
	StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = 
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };       
    
    public void initialize(){
		cbObject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<rs.petcom.master.dal.Object>() {
			@Override
			public void changed(ObservableValue<? extends rs.petcom.master.dal.Object> observable,
					rs.petcom.master.dal.Object oldValue, rs.petcom.master.dal.Object newValue) {
				loadDepartment();				
			}
		});
    }
	
    @FXML private BorderPane rootPane;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private ComboBox<Department> cbDepartment;
	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private BorderPane reportPane;
	@FXML private CheckBox cbSamoSaPromenama;
	@FXML private RadioButton rbIzborCene1;
		
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
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
	
	@Override
	public void init() {
		dateFrom.setConverter(converter);		
		dateFrom.setValue(LocalDate.now());
		dateTo.setConverter(converter);		
		dateTo.setValue(LocalDate.now());
		
		cbObject.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		
		if (cbObject.getItems().size()>0)
			cbObject.getSelectionModel().select(0);
	}

	@FXML 
	private void showReport(){
		
		String reportFileName = "rs/petcom/backoffice/jasper/StockReport.jasper";
		
		String uslovi = controller.getBundle().getString("dateFrom") + converter.toString(dateFrom.getValue());

		Map<String, Object> hm = new HashMap<>();
		
		hm.put("danOd", new java.sql.Date(Settings.fromLocalDate(dateFrom.getValue()).getTime()));
		hm.put("danDo", new java.sql.Date(Settings.fromLocalDate(dateTo.getValue()).getTime()));
		hm.put("objekatId", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("departmentId", cbDepartment.getSelectionModel().getSelectedItem().getDepartmentID());
		hm.put("objekat", cbObject.getSelectionModel().getSelectedItem());
		hm.put("uslovi", uslovi);
		hm.put("prikazSvihArtikala",cbSamoSaPromenama.isSelected());
		if (rbIzborCene1.isSelected())
			hm.put("vrstaCene",0);
		else
			hm.put("vrstaCene",1);
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			//reportPane.setCenter(printViewPane);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Pregled stanja zaliha",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
			printViewPane = null;
			
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	private void loadDepartment(){
		cbDepartment.getItems().clear();
		// cbDepartment.getItems().add(svaOdeljenja);
		for (Department d : Department.getList(cbObject.getSelectionModel().getSelectedItem(), 
						   					   controller.getSettings().getConnection())){
			cbDepartment.getItems().add(d);
		}
		if (cbDepartment.getItems().size() == 1){
			cbDepartment.getSelectionModel().select(0);
		}
		/*
		else{
			cbDepartment.getSelectionModel().select(svaOdeljenja);
		}
		*/
	}
}
