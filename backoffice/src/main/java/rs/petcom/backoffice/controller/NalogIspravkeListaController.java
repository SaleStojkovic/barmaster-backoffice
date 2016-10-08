package rs.petcom.backoffice.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PaymentMethod;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentCorrection;
import rs.petcom.master.dal.document.DocumentDetails;
import rs.petcom.master.dal.document.DocumentType;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.fical.FiscalPrinter;
import rs.petcom.print.JRPrintPreviewPane;

public class NalogIspravkeListaController implements ControlledScreen{
	ScreensController controller;
	HashMap<String, Object> hm;
	DocumentType docType;
	
	ObservableList<Document> documentList;
	ObservableList<DocumentDetails> detailesList;
	
	
	public void initialize(){
		cbObject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<rs.petcom.master.dal.Object>() {
			@Override
			public void changed(ObservableValue<? extends rs.petcom.master.dal.Object> observable,
					rs.petcom.master.dal.Object oldValue, rs.petcom.master.dal.Object newValue) {
				//loadDepartment();	
				loadPointOfSale();
			}
		});
		
		documentsTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Document>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Document> c) {
				loadDocDetTable();
			}
		});
		
		tcDocBroj.setCellValueFactory(new PropertyValueFactory<Document, Integer>("Number"));
		tcDocDatum.setCellValueFactory(new PropertyValueFactory<Document, Date>("Date"));
		tcDocDatum.setCellFactory((TableColumn<Document, Date> column) -> {
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
		
		tcDocKomitent.setCellValueFactory(new PropertyValueFactory<Document, String>("PersonName"));
		
		tcBI.setCellValueFactory(new PropertyValueFactory<Document, String>("FullNumber"));
		
		tcDocVrednost.setCellValueFactory(new PropertyValueFactory<Document, Double>("SumWithDiscount"));
		tcDocVrednost.setCellFactory((TableColumn<Document, Double> column) -> {
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
		
		tcDocDetProduct.setCellValueFactory(new PropertyValueFactory<DocumentDetails, String>("ProductName"));
		tcDocDetQuantity.setCellValueFactory(new PropertyValueFactory<DocumentDetails, Double>("Quantity"));
		tcDocDetQuantity.setCellFactory((TableColumn<DocumentDetails, Double> column) -> {
			   return new TableCell<DocumentDetails, Double>() {
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
		tcDocDetPrice.setCellValueFactory(new PropertyValueFactory<DocumentDetails, Double>("Price"));
		tcDocDetPrice.setCellFactory((TableColumn<DocumentDetails, Double> column) -> {
			   return new TableCell<DocumentDetails, Double>() {
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
		tcDocDetValue.setCellValueFactory(new PropertyValueFactory<DocumentDetails, Double>("Value"));
		tcDocDetValue.setCellFactory((TableColumn<DocumentDetails, Double> column) -> {
			   return new TableCell<DocumentDetails, Double>() {
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
		
		dpWorkDay.valueProperty().addListener((obs, oldDate, newDate) -> {    
			loadDocTable();
		});    
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.controller = screenPage;
	}

	@Override
	public void init() {
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			cbObject.getItems().add(o);
		}
		if (cbObject.getItems().size() > 0){
			cbObject.getSelectionModel().select(0);
		}	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParameter(Object parameter) {
		hm = (HashMap<String, Object>)parameter;
		docType = DocumentType.getByCode(hm.get("documentCode").toString(),controller.getSettings().getConnection());
//		if (docType.getCode().equals("R")){
//			popuniDugmiceZaRacune();
//		}
		if (docType.getCode().equals("NI")){
			popuniDugmiceNalogeZaIspravku();
		}
		
		cbObject.setDisable(false);
		if (controller.getPointOfSale() != null){
			for (rs.petcom.master.dal.Object o : cbObject.getItems()){
				if (o.getObjectID() == controller.getPointOfSale().getObjectID()){
					cbObject.getSelectionModel().select(o);
					//cbObject.setDisable(true);
					break;
				}
			}
		}
		if (controller.getObject() != null){
			for (rs.petcom.master.dal.Object o : cbObject.getItems()){
				if (o.getObjectID() == controller.getObject().getObjectID()){
					cbObject.getSelectionModel().select(o);
					//cbObject.setDisable(true);
					break;
				}
			}
		}
		
		dpWorkDay.setValue(Settings.fromDate(new Date()));
		
		//cbPos.getSelectionModel().select(allPos);
		
		loadDocTable();
	}

	@Override
	public Object getParameter() {
		return null;
	}

	@FXML private BorderPane rootPane;
	@FXML private VBox actionPanel;
	@FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	
	@FXML TableView<Document> documentsTable;
	@FXML TableColumn<Document, Integer> tcDocBroj;
	@FXML TableColumn<Document, Date> tcDocDatum;
	@FXML TableColumn<Document, String> tcDocKomitent;
	@FXML TableColumn<Document, Double> tcDocVrednost;
	@FXML TableColumn<Document, String> tcBI;
		
	@FXML TableView<DocumentDetails> docDetailesTable;
	@FXML TableColumn<DocumentDetails, String> tcDocDetProduct;
	@FXML TableColumn<DocumentDetails, Double> tcDocDetQuantity;
	@FXML TableColumn<DocumentDetails, Double> tcDocDetPrice;
	@FXML TableColumn<DocumentDetails, Double> tcDocDetValue;
	
	@FXML DatePicker dpWorkDay;
	@FXML ComboBox<PointOfSale> cbPos;
	
	@FXML Label lDocCount;
	@FXML Label lDocValue;
		
	private void actionStampajKomadno() {
//		HashMap<String, Object> hm = new HashMap<>();
//		hm.put("controller", controller);	
//		hm.put("documentType", docType);	
//		hm.put("document", null);
//		controller.showModalDialog("Detalji dokumenta",ScreensController.DIALOG_DOCUMENT_DETAILES, hm,rootPane.getScene().getWindow());
		
		
		Document doc = documentsTable.getSelectionModel().getSelectedItem();
		String reportFileName = "rs/petcom/backoffice/jasper/NalogZaIspravku.jasper";

		Map<String, Object> hm1 = new HashMap<>();
		hm1.put("documetnId", doc.getDocumentID());
	
		PointOfSale pos = new PointOfSale(doc.getPointOfSaleID(), controller.getSettings().getConnection());
		rs.petcom.master.dal.Object o = rs.petcom.master.dal.Object.getById(pos.getObjectID(), 
																			controller.getSettings().getConnection());
		Address address = Address.getByID(o.getAddressID(), controller.getSettings().getConnection());
		
		Person p = Person.getByCode("1", controller.getSettings().getConnection());
		String noteTrazeni = "Broj fiskalnog isečka: 233414 Datum fiskalnog isečka: 23.08.2016 Ime i prezime: MILICA IVIC JMBG: 1010977450126";
		
		String  ime = noteTrazeni.substring(80, 92);
		String  jmbg = noteTrazeni.substring(98, 111);

		hm1.put("object", o);
		hm1.put("address", address);
		hm1.put("person", p);
		hm1.put("memorandum", controller.getSettings().getSetting("company.memorandum", controller.getPointOfSale()));
		hm1.put("document", doc);
		hm1.put("parentDocumentId", doc.getParentID(2, controller.getSettings().getConnection()));
		hm1.put("datumRacuna", doc.getDate());
		hm1.put("nacinPlacanja", PaymentMethod.getByID(doc.getPaymentMethodID(), controller.getSettings().getConnection()).getName());
		hm1.put("ImePrezime", ime);
		hm1.put("JMBG", jmbg);
		hm1.put("BI", doc.getFullNumber());
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm1, controller.getSettings().getConnection());
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
		
	}
	
//	@FXML private void actionStampajListu(){
//		HashMap<String, Object> hm = new HashMap<>();
//		hm.put("controller", controller);	
//		hm.put("documentType", docType);	
//		hm.put("document", documentsTable.getSelectionModel().getSelectedItem());
//		controller.showModalDialog("Detalji dokumenta",ScreensController.DIALOG_DOCUMENT_DETAILES, hm,rootPane.getScene().getWindow());
//		loadDocTable();
//	}
	
	@FXML private void actionCbPosChange(){
		loadDocTable();
	}
	
	
	@FXML private void actionCbObject(){
		loadDocTable();
	}
	
	@FXML private void dpWorkDayClick(){
		loadDocTable();
	}
	
	private void loadDocTable(){
		if ((cbObject.getSelectionModel().getSelectedItem() != null) &&
			(cbPos.getSelectionModel().getSelectedItem() != null) &&
			(dpWorkDay.getValue() != null))
		
			documentsTable.setItems(Document.getObeservableListNalogIspravka(12, 
													   cbObject.getSelectionModel().getSelectedItem(),
													   cbPos.getSelectionModel().getSelectedItem(),
													   Settings.fromLocalDate(dpWorkDay.getValue()), 
													   true,
													   controller.getSettings().getConnection()));	
		
		double sum = 0;
		for (Document d : documentsTable.getItems()){
			sum += d.getSumWithDiscount();
		}
		lDocCount.setText(String.valueOf(documentsTable.getItems().size()));
		lDocValue.setText(String.valueOf(sum));
	}
	
	private void loadDocDetTable(){
		try{
			
			detailesList = DocumentDetails.getObeservableList(documentsTable.getSelectionModel().getSelectedItem().getParentID(2, 
																															controller.getSettings().getConnection()), 
															  controller.getSettings().getConnection());
		} catch (NullPointerException e){
			detailesList = FXCollections.observableArrayList();
		}
		docDetailesTable.setItems(detailesList);
	}
	
	private void loadPointOfSale(){
	
		PointOfSale allPos = new PointOfSale();
		allPos.setPointOfSaleID(0);
		allPos.setName("Sva prodajna mesta");
		
		cbPos.getItems().clear();
		cbPos.getItems().add(allPos);
		
		
		for(PointOfSale ps : PointOfSale.getPosList(cbObject.getSelectionModel().getSelectedItem(), controller.getSettings().getConnection())){
			cbPos.getItems().add(ps);
		}
		cbPos.getSelectionModel().select(0);
		loadDocTable();
	}
	
	
	private void popuniDugmiceNalogeZaIspravku(){
		actionPanel.getChildren().clear();
		Button btnStampa = new Button("Štampa naloga za ispravku");
		btnStampa.setPrefWidth(195);
		btnStampa.setWrapText(true);
		btnStampa.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int result = controller.yesNoDialog("Štampanje kopije naloga za ispravku računa",
													"Da li ste sigurni da želite da ponovite\n štampu naloga za ispravku računa?\n",
										   			rootPane.getScene().getWindow());
				if (result == ScreensController.MODAL_RESULT_YES){
					actionStampajKomadno();
					FiscalPrinter.printCopy(documentsTable.getSelectionModel().getSelectedItem(), 
											controller.getSettings().getFiscalFolder(), 
											controller.getUser(), 
											controller.getSettings().getConnection());
					boolean update = true;
					DocumentCorrection dc = DocumentCorrection.getDocument(documentsTable.getSelectionModel().getSelectedItem().getDocumentID(),
																		   controller.getSettings().getConnection());
					if (dc == null){
						dc = new DocumentCorrection();
						update = false;
					}
					dc.setCorrectionTypeID(2);
					dc.setFiscalPrinterID(FiscalPrinter.getIBFM(controller.getSettings().getFiscalFolder()));
					dc.setDate(documentsTable.getSelectionModel().getSelectedItem().getDate());
					dc.setDoucmentID(documentsTable.getSelectionModel().getSelectedItem().getDocumentID());
					dc.setWorkDayID(documentsTable.getSelectionModel().getSelectedItem().getWorkDayID());
					dc.setUserCreated(documentsTable.getSelectionModel().getSelectedItem().getSysUserAddID());
					dc.setReciptNumber(documentsTable.getSelectionModel().getSelectedItem().getFullNumber());
					dc.setPrintTime(new Date());
					dc.setUserPrinted(controller.getUser().getUserID());
					try {
						if (update)
							dc.update(controller.getUser(), controller.getSettings().getConnection());
						else
							dc.insert(controller.getUser(), controller.getSettings().getConnection());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		actionPanel.getChildren().clear();
		Button btnStampajSve = new Button("Štampaj listu naloga");
		btnStampajSve.setPrefWidth(195);
		btnStampajSve.setWrapText(true);
		btnStampajSve.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if (documentsTable.getSelectionModel().getSelectedItem().getPersonID() == 0){
					HashMap<String, Object> hm = new HashMap<>();
					hm.put("controller", controller);
					int result = controller.showModalDialog("Izbor komitenta",ScreensController.DIALOG_SELECT_PERSON, hm,rootPane.getScene().getWindow());
					if (result == ScreensController.MODAL_RESULT_OK){
						try{
							documentsTable.getSelectionModel().getSelectedItem().setPersonID(((Person)hm.get("person")).getPersonID());
							documentsTable.getSelectionModel().getSelectedItem().update(controller);
						} catch (SQLException e){
							e.printStackTrace();
						}
					}
				}
				if (documentsTable.getSelectionModel().getSelectedItem().getPersonID() > 0)
					documentsTable.getSelectionModel()
								  .getSelectedItem()
								  .printInvoice(Person.getById(documentsTable.getSelectionModel().getSelectedItem().getPersonID(), 
										  					   controller.getSettings().getConnection()), 
										  		controller,
										  		rootPane.getScene().getWindow());
			}
		});
		//loadDocTable();
		actionPanel.getChildren().addAll(btnStampa, btnStampajSve);
	}
}
