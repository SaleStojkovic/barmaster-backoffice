package rs.petcom.master.controller.backoffice.fiscal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PaymentMethod;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentRelationship;
import rs.petcom.master.dal.document.DocumentType;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;
import rs.petcom.print.JRPrintPreviewPane;

public class NInalogDialogController implements DialogController{

	private int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	ScreensController controller;
	HashMap<String, Object> parameters;
	Document document;
	
	@FXML BorderPane rootPane;
	@FXML TextField tfBrojIsecka;
	@FXML DatePicker dcDatumIsecka;
	@FXML TextField tfIBFM;
	@FXML TextField tfIme;
	@FXML TextField tfJMBG;
	
	@FXML
	private void cancel(ActionEvent actionEvent){
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) ((Node)  actionEvent.getSource()).getScene().getWindow()).close();
	}
	
	@FXML
	private void confirm(ActionEvent actionEvent){
		
		Date datumRacuna = document.getDate();
		long docId = document.getDocumentID();
		document.setDocumentTypeID(DocumentType.getByCode("NI", controller.getSettings().getConnection()).getDocumentTypeID());
		document.setNumber(Document.getNextNumber(12, document.getPointOfSaleID(),controller));
		document.setDate(new java.util.Date());
		document.setFullNumber(tfBrojIsecka.getText());
		document.setAlternativeFullNumber(tfIBFM.getText());
		document.setSysDTCreated(new java.util.Date());
		document.setSysDTEdit(null);
		
		if((document.getSum()>500.0)){
			if(tfJMBG.getLength()==13){
				document.setNote( "Broj fiskalnog isečka: " + tfBrojIsecka.getText() + "\n" 
						+ "Datum fiskalnog isečka: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dcDatumIsecka.getValue())) + "\n"
						+ "Ime i prezime: " + tfIme.getText() + "\n"
						+ "JMBG: " + tfJMBG.getText());
				document.insert(controller.getSettings().getConnection());
			} else {
				controller.alert("Upozorenje", "Niste uneli odgovarajući JMBG", rootPane.getScene().getWindow());
				return;
			}
		} else {
			if(tfJMBG.getLength()==13||tfJMBG.getText().equals("")){
				document.setNote( "Broj fiskalnog isečka: " + tfBrojIsecka.getText() + "\n" 
						+ "Datum fiskalnog isečka: " + Settings.getDateFromatter().format(Settings.fromLocalDate(dcDatumIsecka.getValue())) + "\n"
						+ "Ime i prezime: " + tfIme.getText() + "\n"
						+ "JMBG: " + tfJMBG.getText());
				document.insert(controller.getSettings().getConnection());
			} else {
				controller.alert("Upozorenje", "Niste uneli odgovarajući JMBG", rootPane.getScene().getWindow());
				return;
			}
		}
			
		
		
		
		DocumentRelationship dr = new DocumentRelationship();
		dr.setRelationshipTypeID(2); // Račun - Minusni račun
		dr.setDocumentParentID(docId);
		dr.setDocumentChildID(document.getDocumentID());
		dr.setActive(true);
		dr.setSysDTCreated(new Date());
		dr.setSysUserAddID(((User) parameters.get("user")).getUserID());
		dr.insert(controller.getSettings().getConnection());
		
		String reportFileName = "rs/petcom/backoffice/jasper/NalogZaIspravku.jasper";
		
		Map<String, Object> hm = new HashMap<>();
		hm.put("documetnId", dr.getDocumentChildID());
		
		/*
		SimpleDateFormat sdf = new SimpleDateFormat("yy");
		String god = sdf.format(WorkDay.getFromDb(document.getWorkDayID(), controller.getSettings().getConnection()).getDate());
		hm.put("god", god);
		*/
		PointOfSale pos = new PointOfSale(document.getPointOfSaleID(), controller.getSettings().getConnection());
		rs.petcom.master.dal.Object o = rs.petcom.master.dal.Object.getById(pos.getObjectID(), 
																			controller.getSettings().getConnection());
		Address address = Address.getByID(o.getAddressID(), controller.getSettings().getConnection());
		
		Person p = Person.getByCode("1", controller.getSettings().getConnection());
		
		
		hm.put("object", o);
		hm.put("address", address);
		hm.put("person", p);
		hm.put("memorandum", controller.getSettings().getSetting("company.memorandum", controller.getPointOfSale()));
		hm.put("document", document);
		hm.put("parentDocumentId", dr.getDocumentParentID());
		hm.put("datumRacuna", datumRacuna);
		hm.put("nacinPlacanja", PaymentMethod.getByID(document.getPaymentMethodID(), controller.getSettings().getConnection()).getName());
		hm.put("ImePrezime", tfIme.getText());
		hm.put("JMBG", tfJMBG.getText());
		hm.put("BI", tfBrojIsecka.getText());
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Nalog za ispravku",
									   ScreensController.DIALOG_PRINT_PREVIEW, 
									   hm2,
									   rootPane.getScene().getWindow());
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		modalResult = ScreensController.MODAL_RESULT_OK;
		Node  source = (Node)  actionEvent.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	@Override
	public void setParameter(HashMap<String, Object> parameters) {
		this.parameters = parameters;
		this.controller = (ScreensController) parameters.get("controller");
		this.document = (Document) parameters.get("document");
		if (document != null){
			dcDatumIsecka.setValue(Settings.fromDate(document.getDate()));
			tfBrojIsecka.setText(document.getFullNumber());
			tfIBFM.setText(document.getAlternativeFullNumber());
		}
		tfBrojIsecka.getProperties().put("vkType", "numeric");
		tfIme.getProperties().put("vkType", "text");
		tfJMBG.getProperties().put("vkType", "text");
	}
	@Override
	public HashMap<String, Object> getParameter() {
		return null;
	}
	@Override
	public int getModalResult() {
		return modalResult;
	}
	@Override
	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}
}
