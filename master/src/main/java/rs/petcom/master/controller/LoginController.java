package rs.petcom.master.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.Faktura;

public class LoginController implements ControlledScreen{
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()){
			case ENTER:
				commitCode(null);
				break;
			case BACK_SPACE:
			case DELETE:
				if (password.getText().length() > 0)
					password.setText(password.getText().substring(0, password.getText().length()-1));
			default:
				if (event.getCode().isDigitKey()){
					password.setText(password.getText()+event.getText());
				}
			}
		}
	};
	
	@FXML private BorderPane rootPane;
	@FXML private BorderPane borderPane;
	@FXML private GridPane mainGrid;
	@FXML private ImageView logoImage;
	
	@FXML private GridPane numericPane;
	@FXML private PasswordField password;
	@FXML private Button button0;
	@FXML private Button button1;
	@FXML private Button button2;
	@FXML private Button button3;
	@FXML private Button button4;
	@FXML private Button button5;
	@FXML private Button button6;
	@FXML private Button button7;
	@FXML private Button button8;
	@FXML private Button button9;
	@FXML private Button buttonEnter;
	@FXML private Button buttonDelete;
	
	@FXML Label labelClock;
	@FXML Label labelDate;
	
	ScreensController controller;
	
	final SimpleDateFormat formatClock = new SimpleDateFormat("HH:mm:ss");
	final SimpleDateFormat formatDate = new SimpleDateFormat("EEEEEEEEE, d MMM yyyy");
	final Timeline timerSat;
	
	public LoginController(){
		timerSat = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) {    
		          labelClock.setText(formatClock.format(new Date()));  
		          labelDate.setText(formatDate.format(new Date())); 
		     }  
		})); 
		timerSat.setCycleCount(Animation.INDEFINITE);
		timerSat.play();
		
	}
	
	
	public void setScreenParent(ScreensController screenPage){
		this.controller = screenPage;
	}
	@Override
	public void setParameter(Object parameter) {
		// TODO Auto-generated method stub
	}
	@Override
	public Object getParameter() {
		return null;
	}
	
	public void init(){
		this.controller.getRoot().removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
		this.controller.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
		password.setText("");
	}
	
	@FXML
	private void commitCode(ActionEvent event){
		if (password.getText().length() > 0){
			
			boolean loginOK = false;
			switch (controller.getVrstaPrograma()){
			case ScreensController.FASTFOOD:
				controller.setUser(User.getFromDb(password.getText(),controller.getSettings().getPosCode(), controller.getSettings().getConnection()));
				if ((controller.getUser() != null) && controller.getUser().isActive()){
					if (controller.getUser().hasRole("cashbox") || 
						controller.getUser().hasRole("admin") ||
						controller.getUser().hasRole("manager") ||
						controller.getUser().hasRole("posadmin")){
						loginOK = true;
					}
				}
				
				break;
			case ScreensController.BACKOFFICE:
				controller.setUser(User.getFromDb(password.getText(),controller.getSettings().getConnection()));
				if ((controller.getUser() != null) && 
					 controller.getUser().isActive() &&
					 (controller.getUser().hasRole("backoffice"))){
					loginOK = true;
				}
				break;
				
			case ScreensController.RESTAURANT:
				controller.setUser(User.getFromDb(password.getText(),controller.getSettings().getConnection()));
				if ((controller.getUser() != null) && 
					 controller.getUser().isActive() &&
					 (controller.getUser().hasRole("cashbox"))){
					loginOK = true;
				}
				break;
			}
			
			if (loginOK){
				controller.getRoot().removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
				
				switch (controller.getVrstaPrograma()){
				case ScreensController.FASTFOOD:
					controller.getRoot().removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					
					if (controller.getUser().hasRole("cashbox")){
						
						// Traženje poslednjeg radnog dana 
						WorkDay wd = WorkDay.getFromDb(controller.getPointOfSale(), 
													   controller.getSettings().getConnection());
						if  (wd == null)
							// Ne postoji ni jedan zapis za POS. Otvara se novi dan.
							WorkDay.openWorkDay(controller.getPointOfSale().getPointOfSaleID(),
												controller.getUser().getUserID(),
												controller.getSettings().getConnection());
						else{
							int result = ScreensController.MODAL_RESULT_CANCEL;
							if (wd.getDateTo() != null){
								while (result == ScreensController.MODAL_RESULT_CANCEL){
									// Radni dan je zatvoren
									
									HashMap<String, Object> hm = new HashMap<String, Object>();
									hm.put("title", "Radni dan je zatvoren.");
									hm.put( "text", "\nDa li želite da otvorite novi dan? \n\n "
										  + "*Ukoliko izaberete Ne nastaviće se unos za " + 
											Settings.getDateFromatter().format(wd.getDate()) + "\n\n");
									result = controller.showModalDialog("Otvaranje radnog dana", 
															   				ScreensController.DIALOG_YES_NO, hm, 
															   				rootPane.getScene().getWindow());
									if (result == ScreensController.MODAL_RESULT_YES){
										WorkDay.openWorkDay(controller.getPointOfSale().getPointOfSaleID(),
														controller.getUser().getUserID(),
														controller.getSettings().getConnection());
									}
									else if(result == ScreensController.MODAL_RESULT_NO){
										controller.setAuthorizationRequest(AuthorisationScreenController.REOPEN_WORKDAY);
										HashMap<String, Object> hm2 = new HashMap<>();
										hm2.put("controller", controller);
										int mr = controller.showModalDialog("Autorizacija",ScreensController.SCREEN_AUTHORISATION,hm2,rootPane.getScene().getWindow());
										if (mr == ScreensController.MODAL_RESULT_OK){
											try {
												wd.reopen(controller.getUser().getUserID(), 
														  controller.getPointOfSale().getPointOfSaleID(),
														  controller.getSettings().getConnection());
											} catch (SQLException e) {
												Log.writeLog(controller.getUser(), "Error: " + e.getMessage(), controller.getSettings().getConnection());
												System.exit(0);
											}
										}
										else{
											result = ScreensController.MODAL_RESULT_CANCEL;
										}
									}
								}
							}
						}
						LOGGER.info("Uspešan login. Korisnik: " + controller.getUser().getUserName());
						controller.getScreen(ScreensController.SCREEN_FASTFOOD, 
													controller.getRoot(),
													ScreensController.SCREEN_LOGIN);
					}
					else if (controller.getUser().hasRole("admin")){
						controller.getScreen(ScreensController.SCREEN_BACKOFFICE_MASTER, 
													controller.getRoot(),
													ScreensController.SCREEN_LOGIN);
					}
					break;
				case ScreensController.BACKOFFICE:
					
					controller.getScreen(ScreensController.SCREEN_BACKOFFICE, 
										 controller.getRoot(),
										 ScreensController.SCREEN_LOGIN);
					break;
					
				case ScreensController.RESTAURANT:
					controller.getScreen(ScreensController.SCREEN_RESTAURANT_STOLOVI, 
										 controller.getRoot(), 
										 ScreensController.SCREEN_LOGIN);
				}
				
				
			}
			else{
				password.setText("");
				
				controller.setDialogTitle("alert.greskaPrilikomprijave.title");
				controller.setDialogText("alert.greskaPrilikomprijave.text");
				String[] buttons = {"OK"};
				controller.setDialogButtonText(buttons);
				
				controller.getScreen(ScreensController.SCREEN_ALLERT, 
											controller.getRoot(),
											ScreensController.SCREEN_LOGIN);
				
			}
		}
	}
	
	@FXML
	 private void handleButtonAction(ActionEvent event) {
		if (event.getSource() != buttonDelete){
			password.setText(password.getText()+((Button)event.getSource()).getText());
		}
		else {
			try{
				String code = password.getText().substring(0, password.getText().length()-1);
				password.setText(code);
			}catch (StringIndexOutOfBoundsException e){}
			
		}
	 }

	@FXML private void supprotClicked(){
		/*
		Document doc = new Document();
		doc.setDocumentTypeID(4);
		doc.setPaymentMethodID(5);
		doc.setNumber(Document.getNextNumber(4,controller.getObject(), controller));
		doc.setSum(controller.getCurrentBill().getSumWithDiscout());
		doc.setDiscount(0);
		doc.setSumWithDiscount(controller.getCurrentBill().getSumWithDiscout());
		
		doc.setPersonID(21);
		
		doc.setDocumentStatusID(1);
		doc.setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
		doc.setSysUserAddID(controller.getUser().getUserID());
		doc.insert(controller.getSettings().getConnection());
		doc.insertParent(1, 1269921, controller.getSettings().getConnection());
		
		Faktura f = new Faktura();
		f.setDocument(doc);
		f.setParentDocumentID(1269921);
		f.print(controller, rootPane.getScene().getWindow());
		*/
	}
	
	@FXML 
	private void toggleNumeric(){
		numericPane.setVisible(!numericPane.isVisible());
		timerSat.setCycleCount(Animation.INDEFINITE);
		timerSat.play();
	}

	@FXML
	private void exitClick(){
		Platform.exit();
	    System.exit(0);	
	}
}
