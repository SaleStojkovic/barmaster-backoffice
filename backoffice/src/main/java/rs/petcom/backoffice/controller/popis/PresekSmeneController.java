package rs.petcom.backoffice.controller.popis;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
import rs.petcom.master.dal.ControllData;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.gui.EditingTableCellDouble;
import rs.petcom.print.JRPrintPreviewPane;

public class PresekSmeneController implements DialogController{
	ScreensController controller;
	HashMap<String,Object> parameter;
	Timestamp date;
	ObservableList<ControllData> controllList;
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;
	
	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = (HashMap<String,Object>) parameter;
		controller = (ScreensController) this.parameter.get("controller");
		date = (Timestamp) this.parameter.get("date");
		rs.petcom.master.dal.Object object = (rs.petcom.master.dal.Object) this.parameter.get("object");
		Department department = (Department) parameter.get("department");
		try{
			controllList = ControllData.createNewControll(date, 
														  department, 
														  object, 
														  WorkDay.getFromDb(controller.getPointOfSale(), 
																  			controller.getSettings().getConnection()),
														  controller);
			table.setItems(controllList);
		} catch (SQLException e){
			controller.alert("Greška pri upisu u bazu!", e.getMessage(), rootPane.getScene().getWindow());
		}
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
	
	@FXML private BorderPane rootPane;
	@FXML private TableView<ControllData> table;
	@FXML private TableColumn<ControllData, Integer> tcKontrola;
	@FXML private TableColumn<ControllData, String> tcArtikal;
	@FXML private TableColumn<Object, Double> tcLager;
	@FXML private TableColumn<Object, Double> tcPopisano;
	@FXML private TableColumn<Object, Double> tcRazlika;
	@FXML private TableColumn<ControllData, String> tcComment;

	@FXML
	public void cancel(ActionEvent event) {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		((Stage) rootPane.getScene().getWindow()).close();
	}

	@FXML
	public void post(ActionEvent event) {
		for (ControllData cd : controllList){
			cd.insert(date, controller.getSettings().getConnection(),(rs.petcom.master.dal.Object)parameter.get("object"));
		}
		String reportFileName = "rs/petcom/backoffice/jasper/PresekSmene.jasper";
		try {
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("time", date);
			hm.put("object", controller.getObject());
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Presek smene",ScreensController.DIALOG_PRINT_PREVIEW, hm2,rootPane.getScene().getWindow());
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void initialize(){
		//table.getSelectionModel().setCellSelectionEnabled(true);
        table.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(Event event) {
				@SuppressWarnings("rawtypes")
				TablePosition focusedCellPosition = table.getFocusModel().getFocusedCell();
		        table.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
		        
			}
		});
        
		tcKontrola.setCellValueFactory(new PropertyValueFactory<ControllData, Integer>("Group"));
		tcArtikal.setCellValueFactory(new PropertyValueFactory<ControllData, String>("Name"));
		
		
		Callback<TableColumn<Object, Double>, TableCell<Object, Double>> doubleCellFactory =
	            new Callback<TableColumn<Object, Double>, TableCell<Object, Double>>() {
	                public TableCell<Object, Double> call(TableColumn<Object, Double> p) {
	                    return new EditingTableCellDouble(2, true);
	                }
	            };
	    
	    tcLager.setCellValueFactory(new PropertyValueFactory<Object, Double>("CalculatedValue"));
	    tcLager.setCellFactory(doubleCellFactory);  		
	    tcLager.setOnEditCommit(
	            (CellEditEvent<Object, Double> t) -> {
	            	ControllData cd = controllList.get(t.getTablePosition().getRow());
	            	cd.setCalculatedValue(t.getNewValue());
	            	cd.setDifference(cd.getEnteredValue() - cd.getCalculatedValue());
	            	controllList.set(t.getTablePosition().getRow(), cd);
	        });
	    
	    tcPopisano.setCellValueFactory(new PropertyValueFactory<Object, Double>("EnteredValue"));
		tcPopisano.setCellFactory(doubleCellFactory);
		tcPopisano.setOnEditCommit(
            (CellEditEvent<Object, Double> t) -> {
            	ControllData cd = controllList.get(t.getTablePosition().getRow());
            	cd.setEnteredValue(t.getNewValue());
            	cd.setDifference(cd.getEnteredValue() - cd.getCalculatedValue());
            	controllList.set(t.getTablePosition().getRow(), cd);
        });
		

		tcRazlika.setCellValueFactory(new PropertyValueFactory<Object, Double>("Difference"));
		tcRazlika.setCellFactory(doubleCellFactory); 
		
		tcComment.setCellValueFactory(new PropertyValueFactory<ControllData, String>("Comment"));
		tcComment.setCellFactory(TextFieldTableCell.<ControllData>forTableColumn());
		tcComment.setOnEditCommit(
	            (CellEditEvent<ControllData, String> t) -> {
	                ((ControllData) t.getTableView().getItems().get(
	                        t.getTablePosition().getRow())
	                        ).setComment(t.getNewValue());
	        });
	}
}
