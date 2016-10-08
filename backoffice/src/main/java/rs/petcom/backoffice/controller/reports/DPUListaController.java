package rs.petcom.backoffice.controller.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.DPUList;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class DPUListaController implements ControlledScreen{
	
	private ScreensController controller;
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
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void init() {
		dpOd.setConverter(converter);
		dpDo.setConverter(converter);
		dpOd.setValue(LocalDate.ofYearDay(LocalDate.now().getYear(), 1));
		dpDo.setValue(LocalDate.now());
		
		cbObjekat.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		cbObjekat.getSelectionModel().select(0);
	}

	@Override
	public void setParameter(Object parameter) {
		refreshList();
		cbObjekat.setDisable(false);
		if (controller.getPointOfSale() != null){
			for (rs.petcom.master.dal.Object o : cbObjekat.getItems()){
				if (o.getObjectID() == controller.getPointOfSale().getObjectID()){
					cbObjekat.getSelectionModel().select(o);
					cbObjekat.setDisable(true);
					break;
				}
			}
		}
		if ((controller.getVrstaPrograma() == ScreensController.BACKOFFICE) && 
				(controller.getObject() != null)){
				for (rs.petcom.master.dal.Object o : cbObjekat.getItems()){
					if (o.getObjectID() == controller.getObject().getObjectID()){
						cbObjekat.getSelectionModel().select(o);
						cbObjekat.setDisable(true);
						break;
					}
				}
			}
		refreshList();
	}

	@Override
	public Object getParameter() {
		return null;
	}
	
	@FXML private BorderPane rootPane;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObjekat;
	@FXML private DatePicker dpOd;
	@FXML private DatePicker dpDo;
	@FXML private ListView<DPUList> lvDPU;

	@FXML public void objectChange(ActionEvent event) {
		refreshList();
	}
	@FXML public void create(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("controller", controller);
		hm.put("object", cbObjekat.getSelectionModel().getSelectedItem());
		controller.showModalDialog("DPU Lista - Detalji", 
								   ScreensController.DIALOG_DPU_LIST_DETAILES, hm, 
								   rootPane.getScene().getWindow());
		refreshList();
	}
	@FXML public void open(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("controller", controller);
		hm.put("object", cbObjekat.getSelectionModel().getSelectedItem());
		hm.put("dpuList", lvDPU.getSelectionModel().getSelectedItem());
		controller.showModalDialog("DPU Lista - Detalji", 
								   ScreensController.DIALOG_DPU_LIST_DETAILES, hm, 
								   rootPane.getScene().getWindow());
	}
	@FXML public void delete(ActionEvent event) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("title", "Brisanje DPU liste.");
		hm.put( "text", "\nDa li sigurni da želite da obrišete DPU listu? \n\n");
		int result = controller.showModalDialog("Brisanje DPU liste", 
								   				ScreensController.DIALOG_YES_NO, hm, 
								   				rootPane.getScene().getWindow());
		if (result == ScreensController.MODAL_RESULT_YES){
			lvDPU.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			refreshList();
		}
	}
	
	@FXML public void refreshList() {
		lvDPU.setItems(DPUList.getList(cbObjekat.getSelectionModel().getSelectedItem().getObjectID(), 
									   Settings.fromLocalDate(dpOd.getValue()), 
									   Settings.fromLocalDate(dpDo.getValue()), 
									   controller.getSettings().getConnection()));
	}
	
	@FXML private void printDPUAction() {
			
		String reportFileName = "rs/petcom/backoffice/jasper/DPUv2.jasper";
		

		if(lvDPU.getSelectionModel().getSelectedItem() != null) {
			
			try {
				
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("datum", lvDPU.getSelectionModel().getSelectedItem().getDate());
				hm.put("dpuListId", lvDPU.getSelectionModel().getSelectedItem().getDPUListID());
				hm.put("nazivFirme", cbObjekat.getSelectionModel().getSelectedItem().getName());
				hm.put("adresaFirme", "");
				hm.put("objekatId", cbObjekat.getSelectionModel().getSelectedItem().getObjectID());
				hm.put("objekat", cbObjekat.getSelectionModel().getSelectedItem());				
				
				JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
				JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
				
				HashMap<String, Object> hm2 = new HashMap<>();
				hm2.put("report", printViewPane);
				controller.showModalDialog("DPU lista",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
				printViewPane = null;
				
			} catch (JRException e) {
				controller.alert("Greška", e.getMessage(), rootPane.getScene().getWindow());
				e.printStackTrace();
			}
		} else {
			System.out.println("?");
		}
	}
	
	@FXML private void export(){
		if (lvDPU.getSelectionModel().getSelectedItem() != null){
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			DPUList dpuLista = lvDPU.getSelectionModel().getSelectedItem();
			
			rs.petcom.master.dal.Object o = rs.petcom.master.dal.Object.getById(dpuLista.getObjectID(), 
																				controller.getSettings().getConnection());
			
			FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export DPU liste");
            fileChooser.setInitialFileName("DPU lista-"  + 
            							   o.getCode() + "-" +  
            							   sdf.format(dpuLista.getDate()) + ".xml");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    		fileChooser.getExtensionFilters().add(extFilter);
    		
            File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
            
            if (file != null) {
            	try {
	            	if (file.getAbsolutePath().toUpperCase().endsWith(".XML"))
						
							dpuLista.exportToXml(file.getAbsolutePath(), controller.getSettings().getConnection());	
					else
	            		dpuLista.exportToXml(file.getAbsolutePath()+".xml", controller.getSettings().getConnection());
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
}
