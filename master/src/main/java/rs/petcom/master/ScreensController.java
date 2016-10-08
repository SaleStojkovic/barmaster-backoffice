package rs.petcom.master;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import rs.petcom.master.dal.Bill;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.User;

public class ScreensController {
	
	ResourceBundle bundle;
	
	public static final int BACKOFFICE = 0;
	public static final int FASTFOOD = 1;
	public static final int RESTAURANT = 2;
	public static final int KITCHEN_MONITOR = 3;
	
	public static int MODAL_RESULT_CANCEL = -1;
	public static int MODAL_RESULT_OK = 0;
	public static int MODAL_RESULT_YES = 1;
	public static int MODAL_RESULT_NO = 2;
	
	public static String DIALOG_YES_NO = "/rs/petcom/master/fxml/YesNoDialog.fxml";
	public static String DIALOG_SELECT_DEPARTMENT = "/rs/petcom/master/fxml/backoffice/DepartmentSelectDialog.fxml";
	public static String DIALOG_ALLERT = "/rs/petcom/master/fxml/Alert.fxml";
	public static String DIALOG_PRINT_PREVIEW = "/rs/petcom/master/fxml/backoffice/PrintPreview.fxml";

	public static String DIALOG_SELECT_PRODUCT = "/rs/petcom/master/fxml/backoffice/ProductSelectDialog.fxml";
	public static String DIALOG_COPY_NORMATIV = "/rs/petcom/backoffice/fxml/NormativCopyDialog.fxml";
	public static String DIALOG_SELECT_PERSON = "/rs/petcom/master/fxml/backoffice/PersonSelectDialog.fxml";
	public static String DIALOG_ADD_PERSON = "/rs/petcom/master/fxml/backoffice/PersonNewDialog.fxml";
	public static String DIALOG_DOCUMENT_DETAILES = "/rs/petcom/master/fxml/backoffice/DocumentDetailes.fxml";
	
	public static String SCREEN_LOGIN = "/rs/petcom/master/fxml/login.fxml";
	public static String SCREEN_FASTFOOD = "/rs/petcom/fastfood/fxml/fastfood.fxml";
	public static String SCREEN_PAYMENT = "/rs/petcom/master/fxml/payment.fxml";
	public static String SCREEN_LOYALITY = "/rs/petcom/master/fxml/loyality.fxml";
	public static String SCREEN_BACKOFFICE_MASTER = "/rs/petcom/master/fxml/backoffice.fxml";
	public static String SCREEN_BACKOFFICE_PERSONS = "/rs/petcom/master/fxml/backoffice/Persons.fxml";
	public static String SCREEN_ALLERT = "/rs/petcom/master/fxml/AlertScreen.fxml";
	public static String SCREEN_AUTHORISATION = "/rs/petcom/master/fxml/AuthorisationScreen.fxml";
	public static String SCREEN_PAYMENT_METHOD_SELECTION = "/rs/petcom/master/fxml/PaymentMethodSelection.fxml";
	
	
	public static String SCREEN_BACKOFFICE_FISCAL_CORRECTION = "/rs/petcom/master/fxml/backoffice/fiscal/NalogIspravke.fxml";
	public static String SCREEN_BACKOFFICE_FISCAL_CORRECTION_DIALOG = "/rs/petcom/master/fxml/backoffice/fiscal/NInalogDialog.fxml";
		
	public static String DIALOG_SELECT_GROUP = "/rs/petcom/master/fxml/backoffice/GroupSelectDialog.fxml";
	public static String DIALOG_SELECT_OPISNI = "/rs/petcom/master/fxml/backoffice/OpisniSelectDialog.fxml";
	
	public static String SCREEN_BACKOFFICE_PRICE_LIST = "/rs/petcom/master/fxml/backoffice/BoPriceList.fxml";
	//public static String SCREEN_MASTER_BACKOFFICE_PRODUCTS = "/rs/petcom/master/fxml/backoffice/BoProducts.fxml";
	
	public static String DIALOG_SELECT_NORMATIVE_PRODUCT = "/rs/petcom/master/fxml/backoffice/NormativeProductSelectDialog.fxml";
	public static String SCREEN_BACKOFFICE_SERVICE_EXPORT = "/rs/petcom/master/fxml/backoffice/DataExport.fxml";
	
	
	
	/// BackOffice skrinovi
	public static String SCREEN_BACKOFFICE = "/rs/petcom/backoffice/fxml/BackOffice.fxml";
	public static String SCREEN_BACKOFFICE_PRODUCTS = "/rs/petcom/backoffice/fxml/Products.fxml";
	public static String DIALOG_BACKOFFICE_ADD_PRODUCT = "/rs/petcom/backoffice/fxml/ProductNewDialog.fxml";
	public static String SCREEN_PRICE_LISTS = "/rs/petcom/backoffice/fxml/PriceLists.fxml"; 
	public static String DIALOG_PRICE_LISTS_DETAILES = "/rs/petcom/backoffice/fxml/PriceListDetailes.fxml"; 
	public static String SCREEN_BACKOFFICE_REKAPITULACIJA_UTROSAKA = "/rs/petcom/backoffice/fxml/RekapitulacijaUtrosaka.fxml";
	public static String SCREEN_BACKOFFICE_DOCUMENTS = "/rs/petcom/backoffice/fxml/Documents.fxml";
	public static String SCREEN_BACKOFFICE_REPORTS_PREGELD_PRODAJE = "/rs/petcom/backoffice/fxml/reports/SalesReport.fxml";
	public static String SCREEN_BACKOFFICE_REPORTS_STOCK_REPORT = "/rs/petcom/backoffice/fxml/reports/StockReport.fxml";
	public static String SCREEN_BACKOFFICE_REPORTS_TRAKA_RADA = "/rs/petcom/backoffice/fxml/reports/TrakaRada.fxml";
	public static String SCREEN_BACKOFFICE_REPORTS_PREGLED_KOREKCIJA = "/rs/petcom/backoffice/fxml/reports/DocumentCorrection.fxml";
	public static String SCREEN_BACKOFFICE_REPORTS_DPU_LIST = "/rs/petcom/backoffice/fxml/reports/DPULista.fxml";
	public static String DIALOG_DPU_LIST_DETAILES = "/rs/petcom/backoffice/fxml/reports/DPUDetailes.fxml";
	public static String SCREEN_BACKOFFICE_POPIS_KONTROLA_LIST = "/rs/petcom/backoffice/fxml/popis/ControllList.fxml";
	public static String SCREEN_BACKOFFICE_POPIS_KONTROLA_EDIT = "/rs/petcom/backoffice/fxml/popis/ControllEdit.fxml";
	public static String SCREEN_BACKOFFICE_POPIS_PRESEK_SMENE =  "/rs/petcom/backoffice/fxml/popis/PresekSmene.fxml";
	public static String SCREEN_BACKOFFICE_DOC_PRIJEMNCIE = "/rs/petcom/backoffice/fxml/document/Prijemnice.fxml";
	
	public static String SCREEN_BACKOFFICE_DOCUMENT_CORRECTION_LIST = "/rs/petcom/backoffice/fxml/NalogIspravkeLista.fxml";
	
	public static String SCREEN_BACKOFFICE_DOC_PRIJEMNCIE_DETAILES = "/rs/petcom/backoffice/fxml/document/PrijemnicaDetailes.fxml";
	public static String SCREEN_BACKOFFICE_DOC_POCETNO_STANJE =  "/rs/petcom/backoffice/fxml/document/PocetnoStanje.fxml";
	public static String SCREEN_BACKOFFICE_DOC_POCETNO_STANJE_DETAILES =  "/rs/petcom/backoffice/fxml/document/PocetnoStanjeDetailes.fxml";
	public static String SCREEN_BACKOFFICE_DOC_SRAVNJENJE_PO_POPISU =  "/rs/petcom/backoffice/fxml/document/SravnjenjePoPopisu.fxml";
	public static String SCREEN_BACKOFFICE_DOC_SRAVNJENJE_PO_POPISU_DETAILES =  "/rs/petcom/backoffice/fxml/document/SravnjenjePoPopisuDetailes.fxml";
	public static String SCREEN_BACKOFFICE_DOC_PRESIFRIRANJE = "/rs/petcom/backoffice/fxml/document/Presifriranje.fxml";
	public static String SCREEN_BACKOFFICE_DOC_PRESIFRIRANJE_DETAILES =  "/rs/petcom/backoffice/fxml/document/PresifriranjeDetailes.fxml";
	public static String SCREEN_BACKOFFICE_FISKALNI_IZVESTAJI =  "/rs/petcom/backoffice/fxml/reports/FiscalReports.fxml";
	
	
	// Kitchen Monitor skrinovi
	public static String SCREEN_KITCHEN_MONITOR =  "/rs/petcom/kitchenmonitor/fxml/KitchenMonitor.fxml";
	public static String SCREEN_KITCHEN_PORUDZBINA =  "/rs/petcom/kitchenmonitor/fxml/Porudzbina.fxml";
	
	// Restauriant screenovi
	public static String SCREEN_RESTAURANT_STOLOVI =  "/rs/petcom/restaurant/fxml/Stolovi.fxml";
	
	private Settings settings;
	private User user = new User();
	private PointOfSale pos;
	private rs.petcom.master.dal.Object object;
	private String sender = "";
	
	private int activeBill = -1;
	private ArrayList<Bill> billList = new ArrayList<Bill>();
	
	private int modalResult = -1;
	private Object modalResultObject = null;
	
	private String dialogTitle;
	private String dialogText;
	private String[] dialogButtonText;
	
	private int authorizationRequest = -1;
	private int authBill = -1;
	private int authRound = -1;
	private int authItem = -1;
	private int authAdditional = -1;
	private int authDesciptional = -1;
	private double authQuantity = 0;
		
	HashMap<String, ScreenBundle> listaEkrana = new HashMap<String, ScreenBundle>();
	HashMap<String, DialogBundle> listaDialog = new HashMap<String, DialogBundle>();
	
	private StackPane root;
	BorderPane dialogPane = new BorderPane();
	
	private int vrstaPrograma;
		
	public int getVrstaPrograma() {
		return vrstaPrograma;
	}
	
	public ScreensController(int vrstaPrograma){
		this.vrstaPrograma = vrstaPrograma;
		try {
			this.settings = new Settings();
			if ((vrstaPrograma == ScreensController.FASTFOOD) || 
				(vrstaPrograma == ScreensController.RESTAURANT)){
				this.pos = new PointOfSale(settings.getPosCode(), settings.getConnection());
				this.object = rs.petcom.master.dal.Object.getById(pos.getObjectID(), settings.getConnection());
				if (this.pos.getCode() == null){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Greška u konfiguraciji");
					alert.setHeaderText("Greška u konfiguraciji");
					alert.setContentText("Neispravan kod POS-a!");
					alert.showAndWait();
					Platform.exit();
					System.exit(0);
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Greška u konfiguraciji");
			alert.setHeaderText("Greška u konfiguraciji");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		
		if (vrstaPrograma == ScreensController.BACKOFFICE){
			this.object = rs.petcom.master.dal.Object.getById(settings.getObjectId(), settings.getConnection());
			/*
			if (this.object == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Greška u konfiguraciji");
				alert.setHeaderText("Greška u konfiguraciji");
				alert.setContentText("Neispravan id objekta!");
				alert.showAndWait();
				Platform.exit();
				System.exit(0);
			}
			*/
		}
	}
	
	public StackPane getRoot(){
		return root;
	}
	public void setRoot(StackPane root){
		this.root = root;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public PointOfSale getPointOfSale() {
		return pos;
	}
	public void setPointOfSale(PointOfSale pos) {
		this.pos = pos;
	}
	public rs.petcom.master.dal.Object getObject() {
		return object;
	}

	public void setObject(rs.petcom.master.dal.Object object) {
		this.object = object;
	}

	public ArrayList<Bill> getBillList() {
		return billList;
	}
	public void setBillList(ArrayList<Bill> billList) {
		this.billList = billList;
	}

	public int getActiveBill() {
		return activeBill;
	}

	public void setActiveBill(int activeBill) {
		this.activeBill = activeBill;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getDialogTitle() {
		return dialogTitle;
	}
	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}
	public String getDialogText() {
		return dialogText;
	}
	public void setDialogText(String dialogText) {
		this.dialogText = dialogText;
	}
	public String[] getDialogButtonText() {
		return dialogButtonText;
	}
	public void setDialogButtonText(String[] dialogButtonText) {
		this.dialogButtonText = dialogButtonText;
	}
	public int getAuthorizationRequest() {
		return authorizationRequest;
	}
	public void setAuthorizationRequest(int authorizationRequest) {
		this.authorizationRequest = authorizationRequest;
	}
	public int getAuthBill() {
		return authBill;
	}
	public void setAuthBill(int authBill) {
		this.authBill = authBill;
	}
	public int getAuthRound() {
		return authRound;
	}
	public void setAuthRound(int authRound) {
		this.authRound = authRound;
	}
	public int getAuthItem() {
		return authItem;
	}
	public void setAuthItem(int authItem) {
		this.authItem = authItem;
	}
	public int getAuthAdditional() {
		return authAdditional;
	}
	public void setAuthAdditional(int authAdditional) {
		this.authAdditional = authAdditional;
	}
	public int getAuthDesciptional() {
		return authDesciptional;
	}
	public void setAuthDesciptional(int authDesciptional) {
		this.authDesciptional = authDesciptional;
	}
	public double getAuthQuantity() {
		return authQuantity;
	}
	public void setAuthQuantity(double authQuantity) {
		this.authQuantity = authQuantity;
	}
	
	public Bill getCurrentBill(){
		return billList.get(activeBill);
	}
	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public int getModalResult() {
		return modalResult;
	}

	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}

	public Object getModalResultObject() {
		return modalResultObject;
	}

	public void setModalResultObject(Object modalResultObject) {
		this.modalResultObject = modalResultObject;
	}

	public void getScreen(String resource, StackPane root, String sender){
		this.root = root;
		this.sender = sender;
		try {
			BorderPane loadedScreen = null;
			if (listaEkrana.get(resource) == null){
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource), bundle);
				loadedScreen = (BorderPane) fxmlLoader.load();
				ControlledScreen screenController = ((ControlledScreen)fxmlLoader.getController());
				screenController.setScreenParent(this);
				screenController.init();
				ScreenBundle sb = new ScreenBundle(loadedScreen, screenController);
				listaEkrana.put(resource, sb);
				root.getChildren().clear();
				root.getChildren().add(loadedScreen);
				dialogPane.setVisible(false);
				root.getChildren().add(dialogPane);
			}
			else{
				root.getChildren().clear();
				root.getChildren().add(listaEkrana.get(resource).getMainPane());
				listaEkrana.get(resource).getController().init();
				dialogPane.setVisible(false);
				root.getChildren().add(dialogPane);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.writeLog(this.getUser(), "Error: " + e.getMessage(), getSettings().getConnection());
		}
	}
	public void loadDialog(String resource){
		DialogBundle db = new DialogBundle(resource, bundle, settings);
		listaDialog.put(resource, db);
	}
	public int showModalDialog(String title, String resource, HashMap<String, Object> parameter, Window owner){
		parameter.put("widnow.title", title);
		int result = ScreensController.MODAL_RESULT_CANCEL;
		if (listaDialog.get(resource) != null){
			listaDialog.get(resource).setParameters(parameter);
			try{
				listaDialog.get(resource).getStage().initOwner(owner);
			} catch (IllegalStateException e){}
			listaDialog.get(resource).getStage().setTitle("Rmaster - " + ((String) parameter.get("widnow.title")));
			listaDialog.get(resource).getStage().getIcons().add(new Image("rs/petcom/master/img/R.jpg"));
			listaDialog.get(resource).getStage().showAndWait();
			
			result =  listaDialog.get(resource).getModalResult();
		}
		else{
			DialogBundle db = new DialogBundle(resource, bundle, settings);
			db.setParameters(parameter);
			try{
				db.getStage().initOwner(owner);
			} catch (IllegalStateException e){}
			db.getStage().setTitle("Rmaster - " + ((String) parameter.get("widnow.title")));
			db.getStage().getIcons().add(new Image("rs/petcom/master/img/R.jpg"));
			db.showAndWait();
			result =  db.getModalResult();
		}
		return result;
	}
	
	public void alert(String title, String text, Window owner){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("title", title);
		hm.put( "text", text);
		showModalDialog(title, 
						ScreensController.DIALOG_ALLERT, 
						hm, 
						owner);
	}
	
	public int yesNoDialog(String title, String text, Window owner){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("title", title);
		hm.put( "text", text);
		int result = showModalDialog(title, 
				   			   ScreensController.DIALOG_YES_NO, 
				   			   hm, 
				   			   owner);
		((Stage) owner).setFullScreen(getSettings().isFullscreen());
		return result;
	}
	
	public Parent loadFxml(String resource,HashMap<String, Object> parameters){
		try { 
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource),bundle);
			Parent work = (Parent) fxmlLoader.load();
			ControlledScreen screenController = ((ControlledScreen)fxmlLoader.getController());
			screenController.setScreenParent(this);
			screenController.init();
			screenController.setParameter(parameters);
			return work;
		} catch (IOException e){
			e.printStackTrace();
		    Log.writeLog(this.getUser(), "Error: " + e.getMessage(), getSettings().getConnection());
		    return null;
		}
	}
}
