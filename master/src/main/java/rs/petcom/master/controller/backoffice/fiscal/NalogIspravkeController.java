package rs.petcom.master.controller.backoffice.fiscal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PaymentMethod;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentDetails;

public class NalogIspravkeController  implements ControlledScreen{
	
	ScreensController controller;
	
	PointOfSale sviPosovi = new PointOfSale(0,"Sva prodajna mesta");
	
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
	@FXML private ComboBox<PointOfSale> cbPos;
	@FXML private BorderPane reportPane;
	
	@FXML TableView<Document> reciptTable;
	@FXML TableColumn<Document, Integer> colBroj;
	@FXML TableColumn<Document, Date> colDatum;
	@FXML TableColumn<Document, Double> colBillValue;
	
	@FXML Label lblValue;
	@FXML Label lblDiscount;
	@FXML Label lblValueWithDiscount;
	@FXML Label lblValueNacinPlacanja;
	@FXML TableView<DocumentDetails> tableStavke;
	@FXML TableColumn<Document, String> tcNaziv;
	@FXML TableColumn<Document, Double> tcKol;
	@FXML TableColumn<Document, Double> tcCena;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	
	@Override
	public void setParameter(Object parameter){}
	@Override
	public Object getParameter() {
		return null;
	}

	@Override
	public void init() {
		dateFrom.setConverter(converter);
		
		dateFrom.setValue(LocalDate.now());
		
		colDatum.setCellValueFactory(new PropertyValueFactory<Document, Date>("Date"));
		colDatum.setCellFactory((TableColumn<Document, Date> column) -> {
			   return new TableCell<Document, Date>() {
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
				
		colBroj.setCellValueFactory(new PropertyValueFactory<Document, Integer>("Number"));
		colBillValue.setCellValueFactory(new PropertyValueFactory<Document, Double>("SumWithDiscount"));
		colBillValue.setCellFactory((TableColumn<Document, Double> column) -> {
			   return new TableCell<Document, Double>() {
				      @Override
				      protected void updateItem(Double item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getNumberFormat(2, true).format(item));
				         }
				      }
				   };
				});
		
		reciptTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Document>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Document> c) {
				Document d = reciptTable.getSelectionModel().getSelectedItem();
				
				if (d != null){
					lblValue.setText(Settings.getNumberFormat(2, true).format(d.getSum()));
					lblDiscount.setText(Settings.getNumberFormat(2, true).format(d.getDiscount()));
					lblValueWithDiscount.setText(Settings.getNumberFormat(2, true).format(d.getSumWithDiscount()));
					lblValueNacinPlacanja.setText(PaymentMethod.getByID(d.getPaymentMethodID(), 
																		controller.getSettings().getConnection()).getName());
					tableStavke.setItems(DocumentDetails.getObeservableList(d.getDocumentID(), 
							controller.getSettings().getConnection()));
				}
			}
		});
		
		tcNaziv.setCellValueFactory(new PropertyValueFactory<Document, String>("ProductName"));
		tcKol.setCellValueFactory(new PropertyValueFactory<Document, Double>("Quantity"));
		tcKol.setCellFactory((TableColumn<Document, Double> column) -> {
		   return new TableCell<Document, Double>() {
			      @Override
			      protected void updateItem(Double item, boolean empty) {
			         super.updateItem(item, empty);
			         if (item == null || empty) {
			            setText(null);
			         } else {
			            setText( Settings.getNumberFormat(4, true).format(item));
			         }
			      }
			   };
			});
		tcCena.setCellValueFactory(new PropertyValueFactory<Document, Double>("Value"));
		tcCena.setCellFactory((TableColumn<Document, Double> column) -> {
			   return new TableCell<Document, Double>() {
				      @Override
				      protected void updateItem(Double item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getNumberFormat(2, true).format(item));
				         }
				      }
				   };
				});
		
		popuniFilterPosova();
		popuniDokumente();
	}

	@FXML
	private void dateFromChange(){
		popuniDokumente();
	}
	
	@FXML
	private void createCorrection(){
		if(WorkDay.isActive(reciptTable.getSelectionModel().getSelectedItem().getWorkDayID(), controller.getSettings().getConnection())){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("document", reciptTable.getSelectionModel().getSelectedItem());
			hm.put("user",controller.getUser());
			hm.put("controller",controller);
			
			int result = controller.showModalDialog("Nalog za ispravku",ScreensController.SCREEN_BACKOFFICE_FISCAL_CORRECTION_DIALOG,hm,rootPane.getScene().getWindow());
			if (result == ScreensController.MODAL_RESULT_OK)
				popuniDokumente();
		}else{
			controller.alert("Upozorenje", "Nije dozvoljena izmena na završenom danu", rootPane.getScene().getWindow());
		}
			
		
	}
	
	private void popuniFilterPosova(){
		ObservableList<PointOfSale> listaPosova = FXCollections.observableArrayList();
		listaPosova.add(sviPosovi);
		for (PointOfSale pos : PointOfSale.getPosList(controller.getObject(), controller.getSettings().getConnection())){
			listaPosova.add(pos);
		}
		cbPos.setItems(listaPosova);
		cbPos.getSelectionModel().select(0);
	}
	
	private void popuniDokumente(){
		reciptTable.setItems(Document.getObeservableList(3,
						  	 controller.getObject(),
						  	 cbPos.getSelectionModel().getSelectedItem(),
							 Settings.fromLocalDate(dateFrom.getValue()),
							 true,
							 false,
							 controller.getSettings().getConnection()));
	}
}
