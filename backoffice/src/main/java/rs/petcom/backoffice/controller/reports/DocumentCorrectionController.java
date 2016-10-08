package rs.petcom.backoffice.controller.reports;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class DocumentCorrectionController implements ControlledScreen{
	ScreensController controller;	
	
	rs.petcom.master.dal.Object sviObjekti = new rs.petcom.master.dal.Object(-1, "Svi objekti");
			
			
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void init() {
		cbObject.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		//cbObject.getItems().add(0, sviObjekti);
		if (cbObject.getItems().size() > 0) 
			cbObject.getSelectionModel().select(0);
		
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@FXML private BorderPane rootPane;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private DatePicker dpDateFrom;
	@FXML private DatePicker dpDateTo;
	@FXML private Button btnDisplay;
	@FXML private void display(){
		System.out.println("Display");
		String reportFileName = "rs/petcom/backoffice/jasper/DocumentCorrection.jasper";
		try {
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("danOd", new java.sql.Date(Settings.fromLocalDate(dpDateFrom.getValue()).getTime()));
			hm.put("danDo", new java.sql.Date(Settings.fromLocalDate(dpDateTo.getValue()).getTime()));
			hm.put("objekatId", cbObject.getSelectionModel().getSelectedItem().getObjectID());
			hm.put("restoran", cbObject.getSelectionModel().getSelectedItem().getName());
			hm.put("napravio", controller.getUser().getUserName());
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Presek smene",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	

}
