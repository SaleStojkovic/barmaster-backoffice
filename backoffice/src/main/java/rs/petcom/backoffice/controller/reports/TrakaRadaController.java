package rs.petcom.backoffice.controller.reports;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class TrakaRadaController implements ControlledScreen{
	
	ScreensController controller;
	
	PointOfSale sviPosovi = new PointOfSale(-1,"Sva prodajna mesta");
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void init() {
		cbObject.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		/*
		for (rs.petcom.master.dal.Object o :cbObject.getItems()){
			if (o.getObjectID() == controller.getObject().getObjectID()){
				cbObject.getSelectionModel().select(o);
				break;
			}
		}
		*/
		if (cbObject.getItems().size() > 0)
			cbObject.getSelectionModel().select(0);
		
		popuniFilterPos();
		dpDateFrom.setValue(LocalDate.now());
		dpDateTo.setValue(LocalDate.now());
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
	
	@FXML private BorderPane rootPane;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private ComboBox<PointOfSale> cbPos;
	@FXML private DatePicker dpDateFrom;
	@FXML private DatePicker dpDateTo;

	@FXML private void objekatChange(){
		popuniFilterPos();
	}
	
	@FXML public void showReport(ActionEvent event) {
		String reportFileName = "rs/petcom/backoffice/jasper/TrakaRada.jasper";
		
		Map<String, Object> hm = new HashMap<>();
		
		hm.put("datumOd", Settings.fromLocalDate(dpDateFrom.getValue()));
		hm.put("datumDo", Settings.fromLocalDate(dpDateTo.getValue()));
		hm.put("objekatId", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("posId", cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		hm.put("uslovi", "Objekat :" + cbObject.getSelectionModel().getSelectedItem().getName() + "\n" +
						 "Prodajno mesto: " + cbPos.getSelectionModel().getSelectedItem().getName() + "\n" +
						 "Za perod od: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dpDateFrom.getValue())) +
						 " do: "+ "Za perod od: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dpDateTo.getValue())));
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Traka rada",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
			printViewPane = null;
			
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	private void popuniFilterPos(){
		cbPos.getItems().clear();
		cbPos.getItems().add(sviPosovi);
		for (PointOfSale p : PointOfSale.getPosList(cbObject.getSelectionModel().getSelectedItem(),controller.getSettings().getConnection())){
			cbPos.getItems().add(p);
		}
		
		cbPos.getSelectionModel().select(sviPosovi);
	}
	
	@FXML private void export(){
		String reportFileName = "rs/petcom/backoffice/jasper/TrakaRada.jasper";
		
		Map<String, Object> hm = new HashMap<>();
		
		hm.put("datumOd", Settings.fromLocalDate(dpDateFrom.getValue()));
		hm.put("datumDo", Settings.fromLocalDate(dpDateTo.getValue()));
		hm.put("objekatId", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("posId", cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		hm.put("uslovi", "Objekat :" + cbObject.getSelectionModel().getSelectedItem().getName() + "\n" +
						 "Prodajno mesto: " + cbPos.getSelectionModel().getSelectedItem().getName() + "\n" +
						 "Za perod od: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dpDateFrom.getValue())) +
						 " do: "+ "Za perod od: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dpDateTo.getValue())));
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			
			FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            File file = fileChooser.showSaveDialog((Stage)rootPane.getScene().getWindow());
            if (file != null) {
            	if (file.getAbsolutePath().toUpperCase().endsWith(".PDF"))
            		JasperExportManager.exportReportToPdfFile(jprint, file.getAbsolutePath());
            	else
            		JasperExportManager.exportReportToPdfFile(jprint, file.getAbsolutePath()+".pdf");
            }
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
