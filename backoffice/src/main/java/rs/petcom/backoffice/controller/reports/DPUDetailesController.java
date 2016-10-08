package rs.petcom.backoffice.controller.reports;

import java.util.Date;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.DPULIstData;
import rs.petcom.master.dal.DPUList;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.gui.EditingTableCellDouble;
import rs.petcom.print.JRPrintPreviewPane;

public class DPUDetailesController implements DialogController{
	
	private ScreensController controller;
	private HashMap<String, Object> parameter;
	private int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	private rs.petcom.master.dal.Object object;
	private DPUList dpuList;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		controller = (ScreensController) parameter.get("controller");
		dpuList = (DPUList) parameter.get("dpuList");
		object = (rs.petcom.master.dal.Object) parameter.get("object");
		if (dpuList == null){
			dpuList = new DPUList();
			dpuList.setDate(new Date());
			dpuList.setObjectID(((rs.petcom.master.dal.Object) parameter.get("object")).getObjectID());
			
			loadTables();
		}
		else{
			
			loadTables();
		}
		dpDatum.setValue(Settings.fromDate(dpuList.getDate()));
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
	
	public void initialize(){
		
		EventHandler<TableColumn.CellEditEvent<DPULIstData,String>> stringHandler = 
				new EventHandler<TableColumn.CellEditEvent<DPULIstData,String>>() {
					@Override
					public void handle(CellEditEvent<DPULIstData, String> event) {
						 ((DPULIstData) event.getTableView().getItems()
								 			 .get(event.getTablePosition().getRow()))
						 					 .setNumber(event.getNewValue());
					}
				};
		
		Callback<TableColumn<Object, Double>, TableCell<Object, Double>> doubleCellFactory =
	             new Callback<TableColumn<Object, Double>, TableCell<Object, Double>>() {
	                 public TableCell<Object, Double> call(TableColumn<Object, Double> p) {
	                    return new EditingTableCellDouble(2,true);
	                 }
	             };
		
		tcDiNumber.setCellValueFactory(new PropertyValueFactory<DPULIstData, String>("Number"));
		tcDiNumber.setCellFactory(TextFieldTableCell.forTableColumn());
		tcDiNumber.setOnEditCommit(stringHandler);
		
		tcDiSum.setCellValueFactory(new PropertyValueFactory<Object, Double>("Sum"));
		tcDiSum.setCellFactory(doubleCellFactory);
		
		tcDiCash.setCellValueFactory(new PropertyValueFactory<Object, Double>("Cash"));
		tcDiCash.setCellFactory(doubleCellFactory);
		tcDiCash.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableDI.getItems().get(t.getTablePosition().getRow());
	            	dd.setCash(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableDI.getItems().set(t.getTablePosition().getRow(), dd);
	        });
		tcDiCard.setCellValueFactory(new PropertyValueFactory<Object, Double>("Card"));
		tcDiCard.setCellFactory(doubleCellFactory);
		tcDiCard.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableDI.getItems().get(t.getTablePosition().getRow());
	            	dd.setCard(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableDI.getItems().set(t.getTablePosition().getRow(), dd);
	        });
		
		tcDiRest.setCellValueFactory(new PropertyValueFactory<Object, Double>("NonFiscal"));
		tcDiRest.setCellFactory(doubleCellFactory);
		tcDiRest.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableDI.getItems().get(t.getTablePosition().getRow());
	            	dd.setNonFiscal(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableDI.getItems().set(t.getTablePosition().getRow(), dd);
	        });
		
		tcNiNumber.setCellValueFactory(new PropertyValueFactory<DPULIstData, String>("Number"));
		tcNiSum.setCellValueFactory(new PropertyValueFactory<Object, Double>("Sum"));
		tcNiCash.setCellValueFactory(new PropertyValueFactory<Object, Double>("Cash"));
		tcNiCard.setCellValueFactory(new PropertyValueFactory<Object, Double>("Card"));
		tcNiRest.setCellValueFactory(new PropertyValueFactory<Object, Double>("NonFiscal"));
		
		tcSfNumber.setCellValueFactory(new PropertyValueFactory<DPULIstData, String>("Number"));
		tcSfNumber.setCellFactory(TextFieldTableCell.forTableColumn());
		tcSfNumber.setOnEditCommit(stringHandler);
		
		tcSfSum.setCellValueFactory(new PropertyValueFactory<Object, Double>("Sum"));
		
		tcSfCash.setCellValueFactory(new PropertyValueFactory<Object, Double>("Cash"));
		tcSfCash.setCellFactory(doubleCellFactory);
		tcSfCash.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableSF.getItems().get(t.getTablePosition().getRow());
	            	dd.setCash(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableSF.getItems().set(t.getTablePosition().getRow(), dd);
	        });
		
		tcSfCard.setCellValueFactory(new PropertyValueFactory<Object, Double>("Card"));
		tcSfCard.setCellFactory(doubleCellFactory);
		tcSfCard.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableSF.getItems().get(t.getTablePosition().getRow());
	            	dd.setCard(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableSF.getItems().set(t.getTablePosition().getRow(), dd);
	        });
		
		tcSfRest.setCellValueFactory(new PropertyValueFactory<Object, Double>("NonFiscal"));
		tcSfRest.setCellFactory(doubleCellFactory);
		tcSfRest.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	DPULIstData dd = tableSF.getItems().get(t.getTablePosition().getRow());
	            	dd.setNonFiscal(t.getNewValue());
	            	dd.setSum(dd.getCash() + dd.getCard() + dd.getNonFiscal());
	            	tableSF.getItems().set(t.getTablePosition().getRow(), dd);
	        });
	}
	
	@FXML private BorderPane rootPane;
	
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private DatePicker dpDatum;
	@FXML private TableView<DPULIstData> tableDI;
	@FXML private TableColumn<DPULIstData, String> tcDiNumber;
	@FXML private TableColumn<Object, Double> tcDiSum;
	@FXML private TableColumn<Object, Double> tcDiCash;
	@FXML private TableColumn<Object, Double> tcDiCard;
	@FXML private TableColumn<Object, Double> tcDiRest;
	@FXML private TableView<DPULIstData> tableNI;
	@FXML private TableColumn<DPULIstData, String> tcNiNumber;
	@FXML private TableColumn<Object, Double> tcNiSum;
	@FXML private TableColumn<Object, Double> tcNiCash;
	@FXML private TableColumn<Object, Double> tcNiCard;
	@FXML private TableColumn<Object, Double> tcNiRest;
	@FXML private TableView<DPULIstData> tableSF;
	@FXML private TableColumn<DPULIstData, String> tcSfNumber;
	@FXML private TableColumn<Object, Double> tcSfSum;
	@FXML private TableColumn<Object, Double> tcSfCash;
	@FXML private TableColumn<Object, Double> tcSfCard;
	@FXML private TableColumn<Object, Double> tcSfRest;
	
	@FXML private Button btnClose;
	@FXML private Button btnSave;
	@FXML private Button btnPrint;

	
	@FXML private void dpDatumChange(){
		if (dpuList.getDPUListID() == 0)
			dpuList.setDate(Settings.fromLocalDate(dpDatum.getValue()));
		loadNiTable();
	}
	@FXML public void diAddClick(ActionEvent event) {
		DPULIstData dud = new DPULIstData();
		dud.setDPUListID(dpuList.getDPUListID());
		dud.setNumber("");
		dud.setCash(0);
		dud.setCard(0);
		dud.setNonFiscal(0);
		tableDI.getItems().add(dud);
	}
	@FXML public void diRemoveClick(ActionEvent event) {
		try{
			tableDI.getItems().remove(tableDI.getSelectionModel().getSelectedIndex());
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	@FXML public void sfAddClick(ActionEvent event) {
		DPULIstData dud = new DPULIstData();
		dud.setDPUListID(dpuList.getDPUListID());
		dud.setNumber("");
		dud.setCash(0);
		dud.setCard(0);
		dud.setNonFiscal(0);
		tableSF.getItems().add(dud);
	}
	@FXML public void sfRemoveClick(ActionEvent event) {
		try{
			tableSF.getItems().remove(tableSF.getSelectionModel().getSelectedIndex());
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	@FXML public void cancelClick(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void saveClick(ActionEvent event) {
		dpuList.setDate(Settings.fromLocalDate(dpDatum.getValue()));
		if (dpuList.getDPUListID() == 0){
			int dpuListId = dpuList.insert(controller.getSettings().getConnection());
			dpuList.setDPUListID(dpuListId);
		}
		else{
			dpuList.update(controller.getSettings().getConnection());
			DPULIstData.delete(dpuList.getDPUListID(), controller.getSettings().getConnection());
		}
		
		for (DPULIstData dd : tableDI.getItems()){
			dd.setType(0);
			dd.setDPUListID(dpuList.getDPUListID());
			dd.insert(controller.getSettings().getConnection());
		}
		/*
		for (DPULIstData dd : tableSF.getItems()){
			dd.setType(2);
			dd.setDPUListID(dpuList.getDPUListID());
			dd.insert(controller.getSettings().getConnection());
		}
		*/
		
		((Stage) rootPane.getScene().getWindow()).close();
	}
	@FXML public void printClick(ActionEvent event) {
		
		if (tableDI.getItems().size() >= object.getPosCount(controller.getSettings().getConnection())){
			
			btnClose.setDisable(true);
			btnSave.setDisable(true);
			btnPrint.setDisable(true);
			
			saveClick(null);
			String reportFileName = "rs/petcom/backoffice/jasper/DPUv2.jasper";
			try {
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("datum", dpuList.getDate());
				hm.put("dpuListId", dpuList.getDPUListID());
				hm.put("nazivFirme", object.getName());
				hm.put("adresaFirme", "");
				hm.put("objekatId", object.getObjectID());
				hm.put("objekat", object);
				
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
			
			btnClose.setDisable(false);
			btnSave.setDisable(false);
			btnPrint.setDisable(false);
		}
		else{
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("title", controller.getBundle().getObject("alert.minimalNumberOfDailyReports.title").toString());
			hm.put("text", controller.getBundle().getObject("alert.minimalNumberOfDailyReports.text").toString());
			controller.showModalDialog(controller.getBundle().getObject("alert.minimalNumberOfDailyReports.title").toString(),
									   ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
		}
	}
	
	private void loadTables(){
		tableDI.getItems().clear();
		tableDI.setItems(DPULIstData.getList(dpuList, 0, controller.getSettings().getConnection()));
		loadNiTable();
		
		tableSF.getItems().clear();
		tableSF.setItems(DPULIstData.getRfList(dpuList.getDate(),
											   object,
											   controller.getSettings().getConnection()));
		
		/*
		if (dpuList.getDPUListID() == 0){
			DPULIstData dd = new DPULIstData();
			dd.setType(0);
			dd.setCard(0);
			dd.setCard(0);
			dd.setNonFiscal(0);
			tableDI.getItems().add(dd);	
			
			DPULIstData dd1 = new DPULIstData();
			dd1.setType(2);
			dd1.setCard(0);
			dd1.setCard(0);
			dd1.setNonFiscal(0);
			tableSF.getItems().add(dd1);
		}
		*/
	}
	private void loadNiTable(){
		try{
			tableNI.setItems(DPULIstData.getNiList(dpuList.getDate(),
												   object.getObjectID(),
												   controller.getSettings().getConnection()));
		} catch (NullPointerException e){}
	}
}
