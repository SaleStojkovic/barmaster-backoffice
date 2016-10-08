package rs.petcom.backoffice.controller.reports;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.product.ProductGroup;
import rs.petcom.master.gui.TreeProductGroupCell;
import rs.petcom.print.JRPrintPreviewPane;

public class SalesReportController  implements ControlledScreen{
	
	ScreensController controller;
	
	TreeItem<ProductGroup> treeRoot = new TreeItem<>();
	
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
	
    @FXML private BorderPane rootPane;
	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private BorderPane reportPane;
	@FXML private ComboBox<PointOfSale> cbPos;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private CheckBox cbRazdvajanje;
	@FXML private TreeView<ProductGroup> treeGroup;
	@FXML private CheckBox cbIzborGrupa;
		
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
		treeGroup.setCellFactory(g -> new TreeProductGroupCell());
		treeGroup.setShowRoot(false);
	}

	public void initialize(){
		cbObject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<rs.petcom.master.dal.Object>() {
			@Override
			public void changed(ObservableValue<? extends rs.petcom.master.dal.Object> observable,
					rs.petcom.master.dal.Object oldValue, rs.petcom.master.dal.Object newValue) {
				ucitajPOS();				
			}
		});
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
		
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			cbObject.getItems().add(o);
		}
		if (cbObject.getItems().size() > 0){
			cbObject.getSelectionModel().select(0);
		}
		
		
		dateFrom.setConverter(converter);
		dateTo.setConverter(converter);
		
		dateFrom.setValue(LocalDate.now());
		dateTo.setValue(LocalDate.now());
		
		// Učitavanje grupa
		treeRoot = new CheckBoxTreeItem<>();
		treeRoot.setValue(new ProductGroup(-1,"Grupe",1));
		for (ProductGroup g : ProductGroup.getList(1, controller.getSettings().getConnection())){
			//g.setSelected(true);
			TreeItem<ProductGroup> groupItem = new TreeItem<>();
			groupItem.setValue(g);
		    treeRoot.getChildren().add(groupItem);
		    //groupItem.getValue().setSelected(true);
		    groupItem.setExpanded(true);
		}
		treeRoot.setExpanded(true);
		treeGroup.setRoot(treeRoot);
	}

	@FXML 
	private void showReport(){
		
		String reportFileName = "rs/petcom/backoffice/jasper/SalesReport.jasper";
		
		String uslovi = controller.getBundle().getString("dateFrom") + converter.toString(dateFrom.getValue()) + " - "
					  + controller.getBundle().getString("dateTo") + converter.toString(dateTo.getValue());

		if (cbIzborGrupa.isSelected()) uslovi = "Sve grupe";
		
		Map<String, Object> hm = new HashMap<>();
		hm.put("datumOd", new java.sql.Date(Settings.fromLocalDate(dateFrom.getValue()).getTime()));
		hm.put("datumDo", new java.sql.Date(Settings.fromLocalDate(dateTo.getValue()).getTime()));
		hm.put("pos",cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		hm.put("objekat", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("uslovi", uslovi);
		hm.put("izborGrupa",!cbIzborGrupa.isSelected());
		String grupe = "";
		String grupeNazivi = "";
		for (TreeItem<ProductGroup> g : treeRoot.getChildren()){
			if (g.getValue().isSelected()){
				if (!grupe.equals("")){
					grupe += ",";
					grupeNazivi += ", ";
				}
				grupe += String.valueOf(g.getValue().getProductGroupID());
				grupeNazivi += String.valueOf(g.getValue().getName());
			}			
		}
		
		hm.put("grupe", grupe);
		hm.put("grupeNazivi", grupeNazivi);
		if (!cbRazdvajanje.isSelected())
			hm.put("grupisanjeSlozenih", 1);
		else
			hm.put("grupisanjeSlozenih", 0);
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Pregeld prodaje",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
			printViewPane = null;
			
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	private void showReport2(){
		
		String reportFileName = "rs/petcom/backoffice/jasper/SumarySalesReport.jasper";
		
		String uslovi = controller.getBundle().getString("dateFrom") + converter.toString(dateFrom.getValue()) + " - "
					  + controller.getBundle().getString("dateTo") + converter.toString(dateTo.getValue());

		Map<String, Object> hm = new HashMap<>();
		hm.put("datumOd", new java.sql.Date(Settings.fromLocalDate(dateFrom.getValue()).getTime()));
		hm.put("datumDo", new java.sql.Date(Settings.fromLocalDate(dateTo.getValue()).getTime()));
		hm.put("pos",cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		hm.put("objekat", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("uslovi", uslovi);
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Pregeld prodaje",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
			printViewPane = null;
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	private void showReport3(){
		
		String reportFileName = "rs/petcom/backoffice/jasper/SalesReportExpense.jasper";
		
		String uslovi = controller.getBundle().getString("dateFrom") + converter.toString(dateFrom.getValue()) + " - "
					  + controller.getBundle().getString("dateTo") + converter.toString(dateTo.getValue());

		Map<String, Object> hm = new HashMap<>();
		hm.put("datumOd", new java.sql.Date(Settings.fromLocalDate(dateFrom.getValue()).getTime()));
		hm.put("datumDo", new java.sql.Date(Settings.fromLocalDate(dateTo.getValue()).getTime()));
		hm.put("pos",cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		hm.put("objekat", cbObject.getSelectionModel().getSelectedItem().getObjectID());
		hm.put("uslovi", uslovi);
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Pregeld rashoda i davanja",
									   ScreensController.DIALOG_PRINT_PREVIEW, 
									   hm2,rootPane.getScene().getWindow());
			printViewPane = null;
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@FXML private void izborGrupaAction(){
		treeGroup.setDisable(cbIzborGrupa.isSelected());
	}

	private void ucitajPOS(){
		cbPos.getItems().clear();
		PointOfSale svaProdajnaMesta = new PointOfSale();
		svaProdajnaMesta.setPointOfSaleID(-1);
		svaProdajnaMesta.setName("Sva prodajna mesta");
		cbPos.getItems().add(svaProdajnaMesta);
		
		for (PointOfSale pos : PointOfSale.getPosList(cbObject.getSelectionModel().getSelectedItem(), 
													  controller.getSettings().getConnection())){
			cbPos.getItems().add(pos);
		}
		cbPos.getSelectionModel().select(svaProdajnaMesta);
	}
}
