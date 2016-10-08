package rs.petcom.backoffice.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.controller.AuthorisationScreenController;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.PosMenu;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.DocumentDetails;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.fical.FiscalPrinter;
import rs.petcom.master.fical.Izvestaji;
import rs.petcom.master.gui.menu.MenuButton;

public class BackofficeController implements ControlledScreen{
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	ScreensController controller;
	private Timeline timerSat;
	private final SimpleDateFormat formatClock = new SimpleDateFormat("HH:mm:ss");
	
	@FXML private BorderPane rootPane;
	
	@FXML private Label clockLabel;
	@FXML private Label userLabel;
	@FXML private Label lokacijaLabel;
	
	@FXML private Accordion menuAccordion;
	@FXML private TitledPane fiscalActions;
	@FXML private TitledPane reportActions;
	@FXML private BorderPane workPane;
	@FXML private ImageView jasperImageView;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void setParameter(Object parameter) {}
	@Override
	public Object getParameter() {
		return null;
	}
	@Override
	public void init() {
		timerSat = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) {    
		          clockLabel.setText(formatClock.format(new Date()));  
		     }  
		})); 
		timerSat.setCycleCount(Animation.INDEFINITE);
		timerSat.play();
		
		userLabel.setText(controller.getUser().getUserName());
		lokacijaLabel.setText("");
		clockLabel.setText(formatClock.format(new Date())); 
		
		loadMenu();
	}
	@FXML 
	private void logout(){
		controller.setUser(null);
		controller.getBillList().clear();
		controller.getScreen(ScreensController.SCREEN_LOGIN,
								controller.getRoot(),
								ScreensController.SCREEN_BACKOFFICE);
	}
	
	private void loadMenu(){
		menuAccordion.getPanes().clear();
		ArrayList<PosMenu> menuList = PosMenu.getList(controller.getUser().getUserID(), controller.getSettings().getConnection());
		for(int i=0;i<menuList.size();i++){
			TitledPane tp = new TitledPane();
			tp.setText(menuList.get(i).getTitle());
			loadMenuItems(menuList.get(i).getPosMenuID(), tp);
			menuAccordion.getPanes().add(tp);
		}
	}
	private void loadMenuItems(int PosMenuID, TitledPane titledPane){
		ScrollPane sb = new ScrollPane();
		VBox vBox = new VBox();
		vBox.getStyleClass().add("menuVbox");
		for (PosMenu pm : PosMenu.getList(PosMenuID, controller.getUser().getUserID(), controller.getSettings().getConnection())){
			if (pm.isIsCommand()){
				MenuButton mb = new MenuButton(pm.getTitle(), pm.getCode());
				mb.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						
						menuAction(((MenuButton) event.getSource()).getMenuCode(),event.getSource());
					}
				});
				vBox.getChildren().add(mb);
			}
		}
		sb.setFitToHeight(true);
		sb.setFitToWidth(true);
		sb.setContent(vBox);
		sb.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		titledPane.setContent(sb);
	}
	
	private void menuAction(String code, Object sender){
		Parent work = null;
		HashMap<String, Object> hm = new HashMap<>();
		switch(code){
		/*
		case "FIPS":
			FiscalPrinter.printReport(controller, "1");
			break;
		case "FIPA":
			FiscalPrinter.printReport(controller, "2");
			break;
		case "FIDI":
			FiscalPrinter.printReport(controller, "3");
			break;
		*/
		case "FINI":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_FISCAL_CORRECTION,null);
			workPane.setCenter(work);
			break;
			
		case "FIIZ":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_FISKALNI_IZVESTAJI,null);
			workPane.setCenter(work);
			break;
			
		case "IZPPO":
			Izvestaji.PrometPoOperaterima(controller.getSettings().getFiscalFolder(),
										  controller.getUser().getUserID(),
										  controller.getPointOfSale().getPointOfSaleID(),
										  controller.getSettings().getConnection());
			break;
		case "IZPSO":
			Izvestaji.PrometPoOperaterima(controller.getSettings().getFiscalFolder(),
										  new Long(0),
										  controller.getPointOfSale().getPointOfSaleID(),
										  controller.getSettings().getConnection());
			break;
		case "IZPAO":
			Izvestaji.ProdatiArtikli( controller.getSettings().getFiscalFolder(),
									  controller.getUser().getUserID(),
									  controller.getPointOfSale(),
									  controller.getSettings().getConnection());
			break;
		case "IZPA":
			Izvestaji.ProdatiArtikli( controller.getSettings().getFiscalFolder(),
									  new Long(0),
									  controller.getPointOfSale(),
									  controller.getSettings().getConnection());
			break;
		case "IZPP":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REPORTS_PREGELD_PRODAJE,null);
			workPane.setCenter(work);
			break;
		
		case "IZDPU":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REPORTS_DPU_LIST,null);
			workPane.setCenter(work);
			break;
			
		case "IZSZ":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REPORTS_STOCK_REPORT,null);
			workPane.setCenter(work);
			break;
		case "IZTR":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REPORTS_TRAKA_RADA,null);
			workPane.setCenter(work);
			break;
		case "IZPKR":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REPORTS_PREGLED_KOREKCIJA,null);
			workPane.setCenter(work);
			break;
			
		case "ADKO":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_PERSONS,null);
			workPane.setCenter(work);
			break;
		case "SIPL":
			work = controller.loadFxml(ScreensController.SCREEN_PRICE_LISTS,null);
			workPane.setCenter(work);
			break;
		case "SOEX":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_SERVICE_EXPORT,null);
			workPane.setCenter(work);
			break;
			
		case "ZDZD":
			
			int jesNo = controller.yesNoDialog("Zatvaranje dana!", 
					   						   "Da li ste sigurni da želite da zatvorite dan?\n\n",
					   						   rootPane.getScene().getWindow());
			if (jesNo != ScreensController.MODAL_RESULT_YES)
				return;
			boolean odobreno = false;
			boolean greska = false;
			try{
				if ((!controller.getSettings().getSetting("kontrola.zakljucenja.dana", 
						 								  controller.getPointOfSale()).equals("true"))){
					odobreno = true;
				}
				if (!odobreno){
					hm = WorkDay.allowClose(controller);
					if (((boolean)hm.get("allowed")) == true)
						odobreno = true;
				}
			} catch (IOException e){
				controller.alert("Greška prilikom očitavanja sa fiskalne kase!", 
								 "Zatvaranje dana nije moguće jer je došlo do greške pri očitavanju " + 
								 "podataka sa fiskalnog pritnera!\n\n", 
								 rootPane.getScene().getWindow());
				odobreno = false;
				greska = true;
			}
			
			if (!odobreno){
				jesNo = controller.yesNoDialog("Zatvaranje dana bez sravnjenja!", 
												   "Iznosi na fiskalnom printeru i u bazi podataka se razlikuju!\n\n" +
												   hm.get("text") + "\n\n"
												   + "Da li ste sigurni da želite da zatvorite dan?", 
												   rootPane.getScene().getWindow());
				if (jesNo == ScreensController.MODAL_RESULT_YES){
					controller.setAuthorizationRequest(AuthorisationScreenController.ZAKLJUCENJE_DANA);
					HashMap<String, Object> hm2 = new HashMap<>();
					hm2.put("controller", controller);
					int mr = controller.showModalDialog("Autorizacija",
														ScreensController.SCREEN_AUTHORISATION,
														hm2,
														rootPane.getScene().getWindow());
					
					if (mr == ScreensController.MODAL_RESULT_OK){		
						LOGGER.info("Odobrenje za zatvaranje neispravnog dana. Neslaganje fiskala i programa. Odobrio: " + ((User)hm2.get("user")).getUserName());
						Log.writeLog(((User)hm2.get("user")), 
									 "PointOfSaleID: " + controller.getPointOfSale().getName() + " - Odobrenje za zatvaranje spornog dana.", 
									 controller.getSettings().getConnection());
						odobreno = true;
					}
				}
			}
			
			// Provera minusa u magacinu
			
			WorkDay w = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
			
			// Rekalkulacija utrošaka za sve kase na odeljenju
			try{
				for (WorkDay wd : WorkDay.getList(Settings.fromDate(w.getDate()),Settings.fromDate(w.getDate()), 
												  controller.getPointOfSale().getObjectID(),
												  controller.getSettings().getConnection())){
					wd.saveRecap(controller);
				}
				} catch (SQLException e){
				controller.alert("Greška prilikom rekalkulacije utrošaka!", 
								 "Došlo je do greške prilikom rekalkulacije utrošaka\n\n" + e.getMessage(), 
								 rootPane.getScene().getWindow());
			}
			
			ArrayList<DocumentDetails> listaMinusa = new ArrayList<>();
			if(odobreno){
				
				try{
					listaMinusa = Product.getNegativeSctockList(controller.getPointOfSale(),
															    controller.getSettings().getConnection());
					if (listaMinusa.size() == 0)
						odobreno = true;
					else
						odobreno = false;
				} catch (SQLException e){
					odobreno = false;
					controller.alert("Greška prilikom pristupa bazi", 
									 e.getMessage(), 
									 rootPane.getScene().getWindow());
				}
			}
			
			if (!odobreno){
				String text = "";
				for(DocumentDetails dd : listaMinusa){
					text += String.format("%-6s %-40s %10.4f \n", dd.getProductCode(),dd.getProductName(),dd.getQuantity());
				}
				jesNo = controller.yesNoDialog("Zatvaranje dana bez sravnjenja magacina!", 
												   "Ima artiklala sa stanjem manjim od 0!\n\n" +
												   "Da li ste sigurni da želite da zatvorite dan? \n\n" +
												   text
												   , 
												   rootPane.getScene().getWindow());
				if (jesNo == ScreensController.MODAL_RESULT_YES){
					controller.setAuthorizationRequest(AuthorisationScreenController.ZAKLJUCENJE_DANA_SA_MINUSOM);
					HashMap<String, Object> hm2 = new HashMap<>();
					hm2.put("controller", controller);
					int mr = controller.showModalDialog("Autorizacija",
														ScreensController.SCREEN_AUTHORISATION,
														hm2,
														rootPane.getScene().getWindow());
					
					if (mr == ScreensController.MODAL_RESULT_OK){	
						LOGGER.info("Odobrenje za zatvaranje neispravnog dana. Minusi na stanju. Odobrio: " + ((User)hm2.get("user")).getUserName());
						Log.writeLog(((User)hm2.get("user")), 
									 "PointOfSaleID: " + controller.getPointOfSale().getName() + " - Odobrenje za zatvaranje spornog dana.", 
									 controller.getSettings().getConnection());
						odobreno = true;
					}
				}
			
			}
			
			if (odobreno){
				LOGGER.info("Korisnik: " + controller.getUser().getUserName() + " Zatvaranje dana");
				for (int i=0;i<controller.getSettings().getWdProductsReport();i++){
					Izvestaji.ProdatiArtikli( controller.getSettings().getFiscalFolder(),
											  new Long(0),
											  controller.getPointOfSale(),
											  controller.getSettings().getConnection());
				}
				
				for (int i=0;i<controller.getSettings().getWdOperatorReport();i++){
					Izvestaji.PrometPoOperaterima(controller.getSettings().getFiscalFolder(),
												  new Long(0),
												  controller.getPointOfSale().getPointOfSaleID(),
												  controller.getSettings().getConnection());
				}
				
				for (int i=0;i<controller.getSettings().getWdStateReport();i++){
					FiscalPrinter.printReport(controller, "1");
				}
				
				for (int i=0;i<controller.getSettings().getWdPluReport();i++){
					FiscalPrinter.printReport(controller, "2");
				}
				
				for (int i=0;i<controller.getSettings().getWdDailyReport();i++){
					FiscalPrinter.printReport(controller, "3");
				}
				
				WorkDay wd = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
				wd.closeWorkDay(controller.getUser(), controller.getPointOfSale(), controller.getSettings().getConnection());
				try{
					wd.saveRecap(controller);
				logout();
				} catch (SQLException e){
					controller.alert("Greška prilikom upisa u bazu!", "Došlo je do greške prilikom upisa u bazu", rootPane.getScene().getWindow());
				}
			}
			else{
				if (!greska)
					controller.alert("Zatvaranje dana nije dozvoljeno!", 
									 "Zatvaranje dana nije moguće jer se unosi na fiskalu i u bazi ne slažu!", 
									 rootPane.getScene().getWindow());
			}
			break;
			
			// Zatvaranje dana bez stampe fiskalnog racuna
		case "ZDZDBF":
			
			int i = controller.yesNoDialog("Zatvaranje dana bez fiskalnog računa", 
					"Da li ste sigurni?", 
					rootPane.getScene().getWindow());
			
			if(i==ScreensController.MODAL_RESULT_NO)
				return;
			
			controller.setAuthorizationRequest(AuthorisationScreenController.ZAKLJUCENJE_DANA_BEZ_FISKALA);
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("controller", controller);
			int mr = controller.showModalDialog("Autorizacija",
												ScreensController.SCREEN_AUTHORISATION,
												hm2,
												rootPane.getScene().getWindow());
			
			if (mr == ScreensController.MODAL_RESULT_OK){	
				WorkDay wd = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
				wd.closeWorkDay(controller.getUser(), controller.getPointOfSale(), controller.getSettings().getConnection());
				
				try{
					wd.saveRecap(controller);
				logout();
				} catch (SQLException e){
					controller.alert("Greška prilikom upisa u bazu!", "Došlo je do greške prilikom upisa u bazu", rootPane.getScene().getWindow());
				}
			}
			
			break;
			
		case "JRTS":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_PRICE_LIST,null);
			workPane.setCenter(work);
			break;	
		case "ARTI":
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_PRODUCTS,null);
			workPane.setCenter(work);
			break;	
		
		case "DOIR":
			hm = new HashMap<>();
			hm.put("documentCode", "R");		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOCUMENTS,hm);
			workPane.setCenter(work);
			break;
			
		case "DLNZI":
			hm = new HashMap<>(); 
			hm.put("documentCode", "NI");
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOCUMENT_CORRECTION_LIST,hm);
			workPane.setCenter(work);
			break;
			
		case "DONI":
			hm = new HashMap<>();
			hm.put("documentCode", "NI");		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOCUMENTS,hm);
			workPane.setCenter(work);
			break;	
		case "DOPR":
			hm = new HashMap<>(); 
			hm.put("controller", controller);		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOC_PRIJEMNCIE,hm);
			workPane.setCenter(work);
			break;	
		case "DOPS":
			hm = new HashMap<>();		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOC_POCETNO_STANJE,hm);
			workPane.setCenter(work);
			break;
		case "DOSPP":
			hm = new HashMap<>();		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOC_SRAVNJENJE_PO_POPISU,hm);
			workPane.setCenter(work);
			break;
		case "DOPRE":
			hm = new HashMap<>();		
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_DOC_PRESIFRIRANJE,hm);
			workPane.setCenter(work);
			break;
		case "COCE":
			hm = new HashMap<>();
			hm.put("controller", controller);
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_POPIS_KONTROLA_EDIT,hm);
			workPane.setCenter(work);
			break;
			
		case "COPS":
			hm = new HashMap<>();
			hm.put("controller", controller);
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_POPIS_KONTROLA_LIST,hm);
			workPane.setCenter(work);
			break;
		case "ADRU":
			hm = new HashMap<>();
			hm.put("controller", controller);
			work = controller.loadFxml(ScreensController.SCREEN_BACKOFFICE_REKAPITULACIJA_UTROSAKA,hm);
			workPane.setCenter(work);
			break;	
		
		case "EXIT":
			workPane.getChildren().clear();
			controller.getScreen(controller.getSender(),
									controller.getRoot(),
									ScreensController.SCREEN_BACKOFFICE_MASTER);
			break;
		}
	}	
}
