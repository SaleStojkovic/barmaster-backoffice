package rs.petcom.fastfood.controller;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.controller.AuthorisationScreenController;
import rs.petcom.master.dal.Bill;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.PointOfSaleProductDescription;
import rs.petcom.master.dal.Round;
import rs.petcom.master.dal.RoundItem;
import rs.petcom.master.dal.RoundItemDescription;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.product.Description;
import rs.petcom.master.dal.product.Group;
import rs.petcom.master.dal.product.ProductDepartment;
import rs.petcom.master.gui.BillSummaryPane;
import rs.petcom.master.gui.ProductButton;

public class FastFoodController  implements ControlledScreen{
	
	private final SimpleDateFormat formatClock = new SimpleDateFormat("HH:mm:ss");
	private NumberFormat numberFormat = NumberFormat.getNumberInstance();
	
	private Timeline timerSat;
	private Timeline timerGroup;
	private Timeline timerWorkDay;
	private Timeline timerGroupRefresh;
	
	private BillSummaryPane summaryPane = new BillSummaryPane();
	
	private ScreensController controller;
	private ArrayList<Group> listaGrupa = new ArrayList<Group>();
	private ArrayList<Group> listaPodGrupa = new ArrayList<Group>();
	private ArrayList<ProductDepartment> listaArtikala = new ArrayList<ProductDepartment>();
	private ArrayList<ProductDepartment> additionalList = new ArrayList<>();
	private ArrayList<Description> descriptionalList = new ArrayList<Description>();
	private ObservableList<PointOfSaleProductDescription> descriptionalPOSList;
	
	private ProductDepartment selectedProduct;
	private int selectedGroupID;
	private Group selectedGroup = null;
	
	private ArrayList<ProductButton> additionalButtonList = new ArrayList<ProductButton>();
	private ArrayList<Button> descriptionalButtonList = new ArrayList<Button>();
	private ArrayList<ProductButton> productButtonList = new ArrayList<>();
	
	
	private int prvaPrikazanaGrupa = 0;
	private int prvaPrikazanaPodGrupa = 0;
	
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	
	@Override
	public void setParameter(Object parameter) {}
	@Override
	public Object getParameter() {
		return null;
	}
	
	public void initialize(){

        warningLabel.setVisible(true);
        warningLabel.setOpacity(0);
		
		timerSat = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) {    
		          clockLabel.setText(formatClock.format(new Date()));  
		     }  
		})); 
		timerSat.setCycleCount(Animation.INDEFINITE);
		timerSat.play();
		
		timerGroup = new Timeline(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) {   
		    	setSelectedGroup();
		    	boolean enabled = true;
		    	if ((selectedGroup != null) && (selectedGroup.getAvailableFrom() != null) && (selectedGroup.getAvailableTo() != null)){
		    		LocalTime lcf = LocalTime.parse(selectedGroup.getAvailableFrom().toString());
			    	LocalTime lct = LocalTime.parse(selectedGroup.getAvailableTo().toString());
			    	LocalTime lcc = LocalTime.now();
			    	if ((lcc.compareTo(lcf) < 0) || (lcc.compareTo(lct) > 0))
			    		enabled = false;
		    	}	
		    	for (ProductDepartment pd : listaArtikala){
		    		if (pd != null)
		    			pd.setEnabled(enabled);
		    	}
		     }  
		})); 
		timerGroup.setCycleCount(Animation.INDEFINITE);
		timerGroup.play();
		
		timerGroupRefresh = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) { 
		    	if (selectedGroup.getParentGroupID() == 0){
		    		ucitajListuGrupa();
		    		prikaziArtikle(false);
		    	}
		    	else{
		    		ucitajListuSubGrupa(selectedGroup.getParentGroupID());
		    		prikaziSubGrupe();
		    		prikaziArtikle(true);
		    	}
		    	
		     }  
		})); 
		timerGroupRefresh.setCycleCount(Animation.INDEFINITE);
		timerGroupRefresh.play();
		
		
		timerWorkDay = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {  
		     public void handle(ActionEvent event) {    
		    	 WorkDay wd = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
		    	 Date trenutnoVreme = new Date();
	    		 double razlika = trenutnoVreme.getTime() - wd.getDateFrom().getTime();
	    		 if ((wd.getDateTo() == null) && (razlika/1000/60/60/24) > 1){
	    			 warningLabel.setText("Aktuelni dan je otvoren pre više od 24 sada!");
		    		 FadeTransition ft = new FadeTransition(Duration.millis(1000), warningLabel);
			         ft.setFromValue(0.0);
			         ft.setToValue(1.0);
			         ft.setAutoReverse(true);
			         ft.setCycleCount(Animation.INDEFINITE);
			         ft.play();
		    	 }
		    	 else{
		    		 warningLabel.setText("");
			         warningLabel.setVisible(false);
		    	 }
		          
		     }  
		})); 
		timerWorkDay.setCycleCount(Animation.INDEFINITE);
		timerWorkDay.play();
	}
	
	public void init() {
		if (controller.getSettings().isFullscreen()){
			rootPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
			rootPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight());
		}
		
		addButtonsToLists();
		for(int i=0; i<additionalButtonList.size(); i++){
			additionalButtonList.get(i).setWrapText(true);
		}
		
		Bill bill = new Bill(controller.getUser());
		Round round = new Round();
		bill.getRoundList().add(round);
		controller.getBillList().add(bill);
		controller.setActiveBill(0);
		
		summaryPane.setBill(controller.getBillList().get(0));
		summaryPane.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				summaryPane.setPaneWidth(summaryPane.getWidth());
			}
		});
		billSummaryPane.setCenter(summaryPane);
		
		userLabel.setText(controller.getUser().getUserName());
		lokacijaLabel.setText(controller.getPointOfSale().getName());
		clockLabel.setText(formatClock.format(new Date())); 
		
		ucitajListuGrupa();
		prikaziGrupe();
		groupButtonClick(null);
		try{
			labelGuest.setText(controller.getBillList().get(controller.getActiveBill()).getPerson().getFirstLastName());
		} catch (NullPointerException e){
			labelGuest.setText("");
		} catch (ArrayIndexOutOfBoundsException e){
			labelGuest.setText("");
		}
		infoLabel.setText(String.format("%d", controller.getSettings().getDefaultQuantity()));
		
		adminButton.setVisible(controller.getUser().hasRole("posadmin"));
		
		loyalityButton.setDisable(true);
	}
	
	private void ucitajListuGrupa(){
		ArrayList<Group> lista = Group.getList(controller.getPointOfSale().getPointOfSaleID(),0, 
											   controller.getSettings().getConnection());
		int maxPos = -1;
		for (int i=0;i<lista.size();i++){
			if (lista.get(i).getPosition() > maxPos)
				maxPos = lista.get(i).getPosition();
		}
		listaGrupa = new ArrayList<Group>();
		for (int i=0;i<maxPos;i++){
			Group g = null;
			for (int j=0;j<lista.size();j++){
				if (lista.get(j).getPosition() == i+1)
					g = lista.get(j);
			}
			listaGrupa.add(g);
		}
	}
	
	private int groupByName(String name){
		int index = -1;
		for (int i=0;i<listaGrupa.size();i++){
			if ((listaGrupa.get(i) != null) && (listaGrupa.get(i).getName().equals(name))){
				index = listaGrupa.get(i).getGroupID();
			}
		}
		return index;
	}
	private void prikaziGrupe(){
		for (int i=0;i<5;i++){
			try{
				popuniDugmeGrupe(i, listaGrupa.get(prvaPrikazanaGrupa+i).getName());
			} catch (IndexOutOfBoundsException|NullPointerException e){
				popuniDugmeGrupe(i, "");
			}
		}

		groupScrollLeft.setVisible( prvaPrikazanaGrupa > 0);
		groupScrollRight.setVisible( prvaPrikazanaGrupa + 5 < listaGrupa.size());
		unselectAllGroups();
		popuniArtikle(-1);
	}
	private void popuniDugmeGrupe(int i, String name){
		switch(i){
		case 0: group0.setText(name); break;
		case 1: group1.setText(name); break;
		case 2: group2.setText(name); break;
		case 3: group3.setText(name); break;
		case 4: group4.setText(name); break;
		}
	}
		
	private void ucitajListuSubGrupa(int grupa){
		ArrayList<Group> lista = Group.getList(controller.getPointOfSale().getPointOfSaleID(),
											   grupa,
											   controller.getSettings().getConnection());
		int maxPos = -1;
		for (int i=0;i<lista.size();i++){
			if (lista.get(i).getPosition() > maxPos)
				maxPos = lista.get(i).getPosition();
		}
		listaPodGrupa = new ArrayList<Group>();
		for (int i=0;i<maxPos;i++){
			Group g = null;
			for (int j=0;j<lista.size();j++){
				if (lista.get(j).getPosition() == i+1)
					g = lista.get(j);
			}
			listaPodGrupa.add(g);
		}
	}
	
	private int subGroupByName(String name){
		int index = -1;
		for (int i=0;i<listaPodGrupa.size();i++){
			if ((listaPodGrupa.get(i) != null) && (listaPodGrupa.get(i).getName().equals(name))){
				index = listaPodGrupa.get(i).getGroupID();
			}
		}
		return index;
	}
	private void prikaziSubGrupe(){
		for (int i=0;i<20;i++){
			try{
				popuniDugmeSubGrupe(i, listaPodGrupa.get(prvaPrikazanaPodGrupa+i).getName());
			} catch (IndexOutOfBoundsException e){
				popuniDugmeSubGrupe(i, "");
			} catch (NullPointerException e){
				popuniDugmeSubGrupe(i, "");
			}
		}
	}
	private void popuniDugmeSubGrupe(int i, String name){
		switch(i){
		case 0: podgrupa0.setText(name); break;
		case 1: podgrupa1.setText(name); break;
		case 2: podgrupa2.setText(name); break;
		case 3: podgrupa3.setText(name); break;
		case 4: podgrupa4.setText(name); break;
		case 5: podgrupa5.setText(name); break;
		case 6: podgrupa6.setText(name); break;
		case 7: podgrupa7.setText(name); break;
		case 8: podgrupa8.setText(name); break;
		case 9: podgrupa9.setText(name); break;
		case 10: podgrupa10.setText(name); break;
		case 11: podgrupa11.setText(name); break;
		case 12: podgrupa12.setText(name); break;
		case 13: podgrupa13.setText(name); break;
		case 14: podgrupa14.setText(name); break;
		case 15: podgrupa15.setText(name); break;
		case 16: podgrupa16.setText(name); break;
		case 17: podgrupa17.setText(name); break;
		case 18: podgrupa18.setText(name); break;
		case 19: podgrupa19.setText(name); break;
		}
	}
	private void unselectAllGroups(){
		group0.setSelected(false);
		group1.setSelected(false);
		group2.setSelected(false);
		group3.setSelected(false);
		group4.setSelected(false);
	}
	private void unselectAllSubGrupe(){
		podgrupa0.setSelected(false);
		podgrupa1.setSelected(false);
		podgrupa2.setSelected(false);
		podgrupa3.setSelected(false);
		podgrupa4.setSelected(false);
		podgrupa5.setSelected(false);
		podgrupa6.setSelected(false);
		podgrupa7.setSelected(false);
		podgrupa8.setSelected(false);
		podgrupa9.setSelected(false);
		podgrupa10.setSelected(false);
		podgrupa11.setSelected(false);
		podgrupa12.setSelected(false);
		podgrupa13.setSelected(false);
		podgrupa14.setSelected(false);
		podgrupa15.setSelected(false);
		podgrupa16.setSelected(false);
		podgrupa17.setSelected(false);
		podgrupa18.setSelected(false);
		podgrupa19.setSelected(false);
	}
	
	private void popuniArtikle(int grupa){
		ArrayList<ProductDepartment> lista = ProductDepartment.getList(controller.getPointOfSale(), 
																	   grupa, 
																	   controller.getSettings().getConnection());
		int maxPos = -1;
		for (int i=0;i<lista.size();i++){
			if (lista.get(i).getPosition() > maxPos)
				maxPos = lista.get(i).getPosition();
		}
		listaArtikala = new ArrayList<ProductDepartment>();
		for (int i=0;i<maxPos;i++){
			ProductDepartment p = null;
			for (int j=0;j<lista.size();j++){
				if (lista.get(j).getPosition() == i+1)
					p = lista.get(j);
			}
			if (p != null) p.setEnabled(true);
			listaArtikala.add(p);
		}
	}
	private void prikaziArtikle(boolean imaPodgrupa){
		int pocetnaVrednost = 0;
		if (imaPodgrupa){
			for(int i=0;i<20;i++){
				popuniDugmeArtikla(i, null);
			}
			pocetnaVrednost = 20;
			setPodgrupeVisible(true);
		}
		else{
			setPodgrupeVisible(false);
		}
		for(int i=pocetnaVrednost; i<45;i++){
			try{
				popuniDugmeArtikla( i, 
									listaArtikala.get(i-pocetnaVrednost));
			} catch (IndexOutOfBoundsException e){
				popuniDugmeArtikla(i, null);
			} catch (NullPointerException e){
				popuniDugmeArtikla(i, null);
			}
		}
	}
	
	private void popuniDugmeArtikla(int i, ProductDepartment productDepartemnt){
		switch(i){
		case 0:		fillArticalButton(artikal0,productDepartemnt); break;
		case 1:		fillArticalButton(artikal1,productDepartemnt); break;
		case 2:		fillArticalButton(artikal2,productDepartemnt); break;
		case 3:		fillArticalButton(artikal3,productDepartemnt); break;
		case 4:		fillArticalButton(artikal4,productDepartemnt); break;
		case 5:		fillArticalButton(artikal5,productDepartemnt); break;
		case 6:		fillArticalButton(artikal6,productDepartemnt); break;
		case 7:		fillArticalButton(artikal7,productDepartemnt); break;
		case 8:		fillArticalButton(artikal8,productDepartemnt); break;
		case 9:		fillArticalButton(artikal9,productDepartemnt); break;
		case 10:	fillArticalButton(artikal10,productDepartemnt); break;
		case 11:	fillArticalButton(artikal11,productDepartemnt); break;
		case 12:	fillArticalButton(artikal12,productDepartemnt); break;
		case 13:	fillArticalButton(artikal13,productDepartemnt); break;
		case 14:	fillArticalButton(artikal14,productDepartemnt); break;
		case 15:	fillArticalButton(artikal15,productDepartemnt); break;
		case 16:	fillArticalButton(artikal16,productDepartemnt); break;
		case 17:	fillArticalButton(artikal17,productDepartemnt); break;
		case 18:	fillArticalButton(artikal18,productDepartemnt); break;
		case 19:	fillArticalButton(artikal19,productDepartemnt); break;
		case 20:	fillArticalButton(artikal20,productDepartemnt); break;
		case 21:	fillArticalButton(artikal21,productDepartemnt); break;
		case 22:	fillArticalButton(artikal22,productDepartemnt); break;
		case 23:	fillArticalButton(artikal23,productDepartemnt); break;
		case 24:	fillArticalButton(artikal24,productDepartemnt); break;
		case 25:	fillArticalButton(artikal25,productDepartemnt); break;
		case 26:	fillArticalButton(artikal26,productDepartemnt); break;
		case 27:	fillArticalButton(artikal27,productDepartemnt); break;
		case 28:	fillArticalButton(artikal28,productDepartemnt); break;
		case 29:	fillArticalButton(artikal29,productDepartemnt); break;
		case 30:	fillArticalButton(artikal30,productDepartemnt); break;
		case 31:	fillArticalButton(artikal31,productDepartemnt); break;
		case 32:	fillArticalButton(artikal32,productDepartemnt); break;
		case 33:	fillArticalButton(artikal33,productDepartemnt); break;
		case 34:	fillArticalButton(artikal34,productDepartemnt); break;
		case 35:	fillArticalButton(artikal35,productDepartemnt); break;
		case 36:	fillArticalButton(artikal36,productDepartemnt); break;
		case 37:	fillArticalButton(artikal37,productDepartemnt); break;
		case 38:	fillArticalButton(artikal38,productDepartemnt); break;
		case 39:	fillArticalButton(artikal39,productDepartemnt); break;
		case 40:	fillArticalButton(artikal40,productDepartemnt); break;
		case 41:	fillArticalButton(artikal41,productDepartemnt); break;
		case 42:	fillArticalButton(artikal42,productDepartemnt); break;
		case 43:	fillArticalButton(artikal43,productDepartemnt); break;
		case 44:	fillArticalButton(artikal44,productDepartemnt); break;
		}
	}
	
	private void fillArticalButton(ProductButton button, ProductDepartment productDepartemnt){
		try{
			button.setProductDepartemnt(productDepartemnt);
			if (!(productDepartemnt.getColor() != null && !productDepartemnt.getColor().equals("")))
				productDepartemnt.setColor(controller.getSettings().getDefaultArticleColor());
			
			Color c = Color.web(productDepartemnt.getColor());
			
			String style = "-fx-background-color: " + productDepartemnt.getColor() + ";";
			if (c.getBrightness() < 0.6 )
				style += " -fx-text-fill : white;";
			else
				style += " -fx-text-fill : black;";
			
			button.setStyle(style);
			button.setText(productDepartemnt.getProduct().getShortName());
		} catch (NullPointerException e){
			String style = "-fx-background-color: " + controller.getSettings().getDefaultArticleColor() + ";";
			button.setStyle(style);
			button.setText("");
		}
	}
	
	private void setPodgrupeVisible(boolean vidljivo){
		artikal0.setVisible(!vidljivo);
		artikal1.setVisible(!vidljivo);
		artikal2.setVisible(!vidljivo);
		artikal3.setVisible(!vidljivo);
		artikal4.setVisible(!vidljivo);
		artikal5.setVisible(!vidljivo);
		artikal6.setVisible(!vidljivo);
		artikal7.setVisible(!vidljivo);
		artikal8.setVisible(!vidljivo);
		artikal9.setVisible(!vidljivo);
		artikal10.setVisible(!vidljivo);
		artikal11.setVisible(!vidljivo);
		artikal12.setVisible(!vidljivo);
		artikal13.setVisible(!vidljivo);
		artikal14.setVisible(!vidljivo);
		artikal15.setVisible(!vidljivo);
		artikal16.setVisible(!vidljivo);
		artikal17.setVisible(!vidljivo);
		artikal18.setVisible(!vidljivo);
		artikal19.setVisible(!vidljivo);
		
		podgrupa0.setVisible(vidljivo);
		podgrupa1.setVisible(vidljivo);
		podgrupa2.setVisible(vidljivo);
		podgrupa3.setVisible(vidljivo);
		podgrupa4.setVisible(vidljivo);
		podgrupa5.setVisible(vidljivo);
		podgrupa6.setVisible(vidljivo);
		podgrupa7.setVisible(vidljivo);
		podgrupa8.setVisible(vidljivo);
		podgrupa9.setVisible(vidljivo);
		podgrupa10.setVisible(vidljivo);
		podgrupa11.setVisible(vidljivo);
		podgrupa12.setVisible(vidljivo);
		podgrupa13.setVisible(vidljivo);
		podgrupa14.setVisible(vidljivo);
		podgrupa15.setVisible(vidljivo);
		podgrupa16.setVisible(vidljivo);
		podgrupa17.setVisible(vidljivo);
		podgrupa18.setVisible(vidljivo);
		podgrupa19.setVisible(vidljivo);
	}
	
	@FXML BorderPane rootPane;
	@FXML Label infoLabel;
	@FXML Label userLabel;
	@FXML Label lokacijaLabel;
	@FXML Label labelGuest;
	@FXML Label clockLabel;
	@FXML BorderPane billSummaryPane;
	@FXML GridPane aditionalDescriptionalPane;
	@FXML Button cancelButton;
	@FXML Button adminButton;
	@FXML Button loyalityButton;
	

	@FXML public Label warningLabel;
	
	@FXML ToggleButton group0,group1,group2,group3,group4;
	@FXML Button groupScrollLeft,groupScrollRight;
	
	@FXML ProductButton artikal0,artikal1,artikal2,artikal3,artikal4,
	             		artikal5,artikal6,artikal7,artikal8,artikal9,
	             		artikal10,artikal11,artikal12,artikal13,artikal14,
	             		artikal15,artikal16,artikal17,artikal18,artikal19,
	             		artikal20,artikal21,artikal22,artikal23,artikal24,
	             		artikal25,artikal26,artikal27,artikal28,artikal29,
	             		artikal30,artikal31,artikal32,artikal33,artikal34,
	             		artikal35,artikal36,artikal37,artikal38,artikal39,
	             		artikal40,artikal41,artikal42,artikal43,artikal44;
	
	@FXML ToggleButton podgrupa0,podgrupa1,podgrupa2,podgrupa3,podgrupa4,
					   podgrupa5,podgrupa6,podgrupa7,podgrupa8,podgrupa9,
					   podgrupa10,podgrupa11,podgrupa12,podgrupa13,podgrupa14,
					   podgrupa15,podgrupa16,podgrupa17,podgrupa18,podgrupa19;
	
	@FXML ProductButton 
				 aditionalButton0,aditionalButton1,aditionalButton2,aditionalButton3,aditionalButton4,
			  	 aditionalButton5,aditionalButton6,aditionalButton7,aditionalButton8,aditionalButton9,
				 aditionalButton10,aditionalButton11,aditionalButton12,aditionalButton13,aditionalButton14,
				 aditionalButton15,aditionalButton16,aditionalButton17,aditionalButton18,aditionalButton19,
				 aditionalButton20,aditionalButton21,aditionalButton22,aditionalButton23,aditionalButton24;
				 
	@FXML Button descriptionalButton0,descriptionalButton1,descriptionalButton2,descriptionalButton3,descriptionalButton4,
				 descriptionalButton5,descriptionalButton6,descriptionalButton7,descriptionalButton8,descriptionalButton9,
				 descriptionalButton10,descriptionalButton11,descriptionalButton12,descriptionalButton13,descriptionalButton14,
				 descriptionalButton15,descriptionalButton16,descriptionalButton17,descriptionalButton18,descriptionalButton19,
				 descriptionalButton20,descriptionalButton21,descriptionalButton22,descriptionalButton23,descriptionalButton24;
	
	@FXML Button numpad0,numpad1,numpad2,numpad3,numpad4,
				 numpad5,numpad6,numpad7,numpad8,numpad9,
				 numpadDecimal,numpadDel;
	
	
	@FXML
	private void logout(){
		if (controller.getCurrentBill().getLastRound().getItemList().size() > 0){
			controller.setAuthorizationRequest(AuthorisationScreenController.LOGOUT);
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("controller", controller);
			int mr = controller.showModalDialog("Autorizacija",ScreensController.SCREEN_AUTHORISATION,hm2,rootPane.getScene().getWindow());
			if (mr == ScreensController.MODAL_RESULT_OK){
				Log.logOut(controller.getUser().getUserID(), 
						   controller.getPointOfSale().getPointOfSaleID(),
						   controller.getSettings().getConnection());
				controller.setUser(null);
				controller.getBillList().clear();
				controller.getScreen(ScreensController.SCREEN_LOGIN,
											controller.getRoot(),
											ScreensController.SCREEN_FASTFOOD);
			}
		}
		try{
			if (controller.getCurrentBill().getLastRound().getItemList().size() == 0){
				Log.logOut(controller.getUser().getUserID(), 
						   controller.getPointOfSale().getPointOfSaleID(),
						   controller.getSettings().getConnection());
				controller.setUser(null);
				controller.getBillList().clear();
				controller.getScreen(ScreensController.SCREEN_LOGIN,
											controller.getRoot(),
											ScreensController.SCREEN_FASTFOOD);
			}
		}catch(IndexOutOfBoundsException e){}
	}
	
	@FXML 
	private void groupScrollLeftClick(){
		prvaPrikazanaGrupa -= 5;
		if (prvaPrikazanaGrupa < 0)
			prvaPrikazanaGrupa = 0;
		prikaziGrupe();
		unselectAllGroups();
		prikaziArtikle(false);
	}
	@FXML 
	private void groupScrollRightClick(){
		prvaPrikazanaGrupa += 5;
		prikaziGrupe();
		unselectAllGroups();
		prikaziArtikle(false);
	}
	
	
	@FXML
	private void groupButtonClick(ActionEvent event){
		
		int index = -1;
		try{
			((ToggleButton)event.getSource()).setSelected(true);
			index = groupByName(((ToggleButton)event.getSource()).getText());
		}catch (NullPointerException e){
			group0.setSelected(true);
			index = groupByName(group0.getText());
		}
		selectedGroupID = index;
		setSelectedGroup();
		ucitajListuSubGrupa(index);
		unselectAllSubGrupe();
		if (Group.imaPodgrupa(index,controller.getSettings().getConnection())){
			popuniArtikle(index);
			prikaziSubGrupe();
			prikaziArtikle(true);
		}
		else{
			popuniArtikle(index);
			prikaziArtikle(false);
		}
	}
	
	@FXML
	private void subGroupButtonClick(ActionEvent event){
		((ToggleButton)event.getSource()).setSelected(true);
		int index = subGroupByName(((ToggleButton)event.getSource()).getText());
		popuniArtikle(index);
		prikaziArtikle(true);
		selectedGroupID = index;
		setSelectedGroup();
	}
	
	@FXML 
	private void numButtonCliclk(ActionEvent event){
		if (infoLabel.getText().equals("0")){
			infoLabel.setText("");
		}
		if (((Button) event.getSource()) == numpad0){
			infoLabel.setText(infoLabel.getText()+"0");
		}
		if (((Button) event.getSource()) == numpad1){
			infoLabel.setText(infoLabel.getText()+"1");
		}
		if (((Button) event.getSource()) == numpad2){
			infoLabel.setText(infoLabel.getText()+"2");
		}
		if (((Button) event.getSource()) == numpad3){
			infoLabel.setText(infoLabel.getText()+"3");
		}
		if (((Button) event.getSource()) == numpad4){
			infoLabel.setText(infoLabel.getText()+"4");
		}
		if (((Button) event.getSource()) == numpad5){
			infoLabel.setText(infoLabel.getText()+"5");
		}
		if (((Button) event.getSource()) == numpad6){
			infoLabel.setText(infoLabel.getText()+"6");
		}
		if (((Button) event.getSource()) == numpad7){
			infoLabel.setText(infoLabel.getText()+"7");
		}
		if (((Button) event.getSource()) == numpad8){
			infoLabel.setText(infoLabel.getText()+"8");
		}
		if (((Button) event.getSource()) == numpad9){
			infoLabel.setText(infoLabel.getText()+"9");
		}
		if ((((Button) event.getSource()) == numpadDecimal) &&
			((controller.getSettings().getSetting("kolicina.ceo.broj", controller.getPointOfSale()) == null) ||
			 (!controller.getSettings().getSetting("kolicina.ceo.broj", controller.getPointOfSale()).equals("true")))){
			if (infoLabel.getText().equals("")){
				infoLabel.setText("0");
			}
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			infoLabel.setText(infoLabel.getText()+dfs.getDecimalSeparator());
		}
		if (((Button) event.getSource()) == numpadDel){
			if (infoLabel.getText().equals("")){
				infoLabel.setText("0");
			}
			else{
				infoLabel.setText(infoLabel.getText().substring(0, infoLabel.getText().length()-1));
			}
			if (infoLabel.getText().equals("")){
				infoLabel.setText("0");
			}
		}
	}

	@FXML
	private void artikalButtonClick(ActionEvent event){
		if (!infoLabel.getText().equals("0") && !((Button)event.getSource()).getText().equals("") ){
			
			selectedProduct = ((ProductButton)event.getSource()).getProductDepartemnt();
			selectedProduct.getProduct().getCurrentPriceAndDiscount(controller.getPointOfSale(), controller.getSettings().getConnection());
			try {
				RoundItem ri = new RoundItem();
				//ri.getProductDepartment().setProduct(selectedProduct);
				ri.setProductDepartment(selectedProduct);
				ri.setQuantity(numberFormat.parse(infoLabel.getText()).doubleValue());
				controller.getBillList().get(0).getRoundList().get(0).getItemList().add(ri);
			} catch (ParseException e) {
				Log.writeLog(controller.getUser(), "Error: " + e.getMessage(), controller.getSettings().getConnection());
				
			}
			
			//additionalList = selectedProduct.getAdditionalList(controller.getSettings().getConnection());
			descriptionalPOSList = PointOfSaleProductDescription.getListOpisni(controller.getPointOfSale().getPointOfSaleID(), selectedProduct.getProduct().getProductID(), controller.getSettings().getConnection());
			int brojDodatnih = loadAdtitionalList();
			descriptionalList = selectedProduct.getProduct().getDescriptionList(controller.getPointOfSale().getPointOfSaleID(), controller.getSettings().getConnection());
			// provera da li postoje dodatni ili opisni
			if (brojDodatnih>0 || descriptionalPOSList.size()>0){
				infoLabel.setText("0");
				summaryPane.setBill(controller.getBillList().get(0));
				showAditionaDescriptional();
			}
			else{
				controller.getBillList().get(0).getRoundList().get(0).cleanupLast();
				summaryPane.setBill(controller.getBillList().get(0));
				infoLabel.setText(String.format("%d", controller.getSettings().getDefaultQuantity()));
			}
		}	
	}
	
	private int loadAdtitionalList(){
		int broj = 0;
		additionalList.clear();
		ArrayList<ProductDepartment> list = selectedProduct.getAdditionalList(controller.getPointOfSale(),
																			  selectedGroup,
																			  controller.getSettings().getConnection()); 
		
		
		
		for (int i=0;i<25;i++){
			additionalList.add(null);
		}
		
		for (ProductDepartment pd : list){
			additionalList.set(pd.getSort(),pd);
			broj++;
		}
		return broj;
	}
	
	private void showAditionaDescriptional(){
		fillAditionalDescriptional();
		aditionalDescriptionalPane.setVisible(true);
	}
	
	private void fillAditionalDescriptional(){
		for (int i=0;i<25;i++){
			try{
				//
				fillAditionalButtons(i, additionalList.get(i),additionalList.get(i).getColor());
			} catch (IndexOutOfBoundsException | NullPointerException e){
				fillAditionalButtons(i, null, null);
			}	
		}
		int broj = 0;
		for (int i=1;i<26;i++){	
			try {
				if(descriptionalPOSList.get(broj).getPositionBtn() == i) {
					for (int j = 0; j < descriptionalPOSList.size(); j++) {
						fillDescritpionalButtons(descriptionalPOSList.get(j).getPositionBtn(), descriptionalPOSList.get(j).getName(),descriptionalPOSList.get(j).getColor());
					}				
					broj++;
				} else {
					fillDescritpionalButtons(i,"",null);
				}		
			} catch (IndexOutOfBoundsException e) {
				fillDescritpionalButtons(i,"",null);
			}
		}
	}
	
	@FXML
	private void postClicked(){
		if (aditionalDescriptionalPane.isVisible()){
			aditionalDescriptionalPane.setVisible(false);
			
			try{
				controller.getCurrentBill().getRoundList().get(0).cleanupLast();
			}catch (NullPointerException e){}
			summaryPane.setBill(controller.getBillList().get(0));
			
			summaryPane.locateSelected();
			controller.setAuthBill(0);
			controller.setAuthRound(summaryPane.getSelectedRoundIndex());
			controller.setAuthItem(summaryPane.getSelectedItemIndex());
			controller.setAuthAdditional(summaryPane.getSelectedAditionalIndex());
			controller.setAuthDesciptional(summaryPane.getSelectedDescriptionalIndex());
			infoLabel.setText("0");
			
		}
		else{
			if (!controller.getPointOfSale().isPosDescriptionalMandatory()
				|| controller.getCurrentBill().getLastRound().getItemList().size() == 0
				|| descriptionalCheck()
					){
				controller.setActiveBill(0);
				controller.getScreen(ScreensController.SCREEN_PAYMENT, 
											controller.getRoot(),
											ScreensController.SCREEN_FASTFOOD);
			}
			else{
				controller.setDialogTitle("alert.descriptionalControll.title");
				controller.setDialogText("alert.descriptionalControll.text");
				
				controller.getScreen(ScreensController.SCREEN_ALLERT, 
											controller.getRoot(),
											ScreensController.SCREEN_FASTFOOD);
			}
			
		}
	}
	
	@FXML
	private void aditionalButtonClick(ActionEvent event){
		ProductButton button = (ProductButton) event.getSource();
		double kol = 0;
		if (infoLabel.getText().equals(String.valueOf(controller.getSettings().getDefaultQuantity()))) kol = 1;
		else{
			kol = Double.parseDouble(infoLabel.getText());
		}
		//RoundItemDescription d = new RoundItemDescription(new Description( ((Button)event.getSource()).getText(),controller.getSettings().getConnection()),kol);
		
		//int roundIndex = summaryPane.getSelectedRoundIndex();
		//int itemIndex = summaryPane.getSelectedAditionalIndex();
		
		//if (summaryPane.getSelectedItemIndex() == -1)
		button.getProductDepartemnt().getProduct().getCurrentPriceAndDiscount(controller.getPointOfSale(), controller.getSettings().getConnection());
		controller.getBillList().get(0).getRoundList().get(0).addAdtitionalToLastItem(button.getProductDepartemnt(), kol);
		/*
		else {
			controller.getBillList().get(0).getRoundList().get(0).addAdtitionalToLastItem(button.getProductDepartemnt(), kol);
			summaryPane.selectItem(roundIndex, itemIndex);
		}
		*/
		summaryPane.setBill(controller.getBillList().get(0));
		infoLabel.setText(String.format("%d", controller.getSettings().getDefaultQuantity()));
	}
	
	@FXML
	private void descriptionalButtonClick(ActionEvent event){
		if (!((Button) event.getSource()).getText().equals("")){
			double kol = 0;
			if (infoLabel.getText().equals(String.valueOf(controller.getSettings().getDefaultQuantity()))) kol = 1;
			else{
				kol = Double.parseDouble(infoLabel.getText());
			}
			RoundItemDescription d = new RoundItemDescription(new Description( ((Button)event.getSource()).getText(),controller.getSettings().getConnection()),kol);
			
			int roundIndex = summaryPane.getSelectedRoundIndex();
			int itemIndex = summaryPane.getSelectedItemIndex();
			
			if (summaryPane.getSelectedItemIndex() == -1)
				controller.getBillList().get(0).getRoundList().get(0).addDescriptionalToLastItem(d);
			else {
				controller.getBillList().get(0).getRoundList().get(0).addDescriptionalToItem(summaryPane.getSelectedItemIndex(), d);
				summaryPane.selectItem(roundIndex, itemIndex);
			}
			summaryPane.setBill(controller.getBillList().get(0));
			infoLabel.setText(String.format("%d", controller.getSettings().getDefaultQuantity()));
		}
	}
	
	@FXML
	private void cancelButtonClick(){
		
		summaryPane.locateSelected();
				
		try {
			controller.setAuthQuantity(numberFormat.parse(infoLabel.getText()).doubleValue());
		} catch (ParseException e) {
			controller.setAuthQuantity(0);
		}
		controller.setAuthorizationRequest(AuthorisationScreenController.STORNO_ACTION);
		
		HashMap<String, Object> hm = new HashMap<>();
		
		hm.put("controller", controller);
		int result = controller.showModalDialog("",ScreensController.SCREEN_AUTHORISATION, hm, rootPane.getScene().getWindow());
		
		if (result == ScreensController.MODAL_RESULT_OK){
			summaryPane.setBill(controller.getBillList().get(0));
			infoLabel.setText("0");
		}
		
	}
	
	@FXML
	private void loyalityButtonClick(){
		controller.setActiveBill(0);
		controller.getScreen(ScreensController.SCREEN_LOYALITY,
									controller.getRoot(),
									ScreensController.SCREEN_FASTFOOD);
	}
	
	@FXML
	private void adminButtonClick(){
		controller.getScreen(ScreensController.SCREEN_BACKOFFICE,
									controller.getRoot(),
									ScreensController.SCREEN_FASTFOOD);
	}
	
	@FXML
	private void billSummaryPaneClicked(){
		summaryPane.locateSelected();
		
		controller.setAuthBill(0);
		controller.setAuthRound(summaryPane.getSelectedRoundIndex());
		controller.setAuthItem(summaryPane.getSelectedItemIndex());
		controller.setAuthAdditional(summaryPane.getSelectedAditionalIndex());
		controller.setAuthDesciptional(summaryPane.getSelectedDescriptionalIndex());

		if ((summaryPane.getSelectedItemIndex() != -1) &&
			(summaryPane.getSelectedAditionalIndex() == -1) &&
			(summaryPane.getSelectedDescriptionalIndex() == -1)
		   ){
			
			
			selectedProduct = controller.getCurrentBill()
											   .getLastRound().getItemList()
											   .get(summaryPane.getSelectedItemIndex()).getProductDepartment();
			
			int brojDodatnih = loadAdtitionalList();
			descriptionalList = selectedProduct.getProduct().getDescriptionList(controller.getPointOfSale().getPointOfSaleID(), controller.getSettings().getConnection());
			
			// provera da li postoje dodatni ili opisni
			if (brojDodatnih>0 || descriptionalList.size()>0){
				infoLabel.setText("0");
				showAditionaDescriptional();
			}
		}
		
	}
	
	private void addButtonsToLists(){
		additionalButtonList.add(aditionalButton0);
		additionalButtonList.add(aditionalButton1);
		additionalButtonList.add(aditionalButton2);
		additionalButtonList.add(aditionalButton3);
		additionalButtonList.add(aditionalButton4);
		additionalButtonList.add(aditionalButton5);
		additionalButtonList.add(aditionalButton6);
		additionalButtonList.add(aditionalButton7);
		additionalButtonList.add(aditionalButton8);
		additionalButtonList.add(aditionalButton9);
		additionalButtonList.add(aditionalButton10);
		additionalButtonList.add(aditionalButton11);
		additionalButtonList.add(aditionalButton12);
		additionalButtonList.add(aditionalButton13);
		additionalButtonList.add(aditionalButton14);
		additionalButtonList.add(aditionalButton15);
		additionalButtonList.add(aditionalButton16);
		additionalButtonList.add(aditionalButton17);
		additionalButtonList.add(aditionalButton18);
		additionalButtonList.add(aditionalButton19);
		additionalButtonList.add(aditionalButton20);
		additionalButtonList.add(aditionalButton21);
		additionalButtonList.add(aditionalButton22);
		additionalButtonList.add(aditionalButton23);
		additionalButtonList.add(aditionalButton24);
		
		descriptionalButtonList.add(descriptionalButton0);
		descriptionalButtonList.add(descriptionalButton1);
		descriptionalButtonList.add(descriptionalButton2);
		descriptionalButtonList.add(descriptionalButton3);
		descriptionalButtonList.add(descriptionalButton4);
		descriptionalButtonList.add(descriptionalButton5);
		descriptionalButtonList.add(descriptionalButton6);
		descriptionalButtonList.add(descriptionalButton7);
		descriptionalButtonList.add(descriptionalButton8);
		descriptionalButtonList.add(descriptionalButton9);
		descriptionalButtonList.add(descriptionalButton10);
		descriptionalButtonList.add(descriptionalButton11);
		descriptionalButtonList.add(descriptionalButton12);
		descriptionalButtonList.add(descriptionalButton13);
		descriptionalButtonList.add(descriptionalButton14);
		descriptionalButtonList.add(descriptionalButton15);
		descriptionalButtonList.add(descriptionalButton16);
		descriptionalButtonList.add(descriptionalButton17);
		descriptionalButtonList.add(descriptionalButton18);
		descriptionalButtonList.add(descriptionalButton19);
		descriptionalButtonList.add(descriptionalButton20);
		descriptionalButtonList.add(descriptionalButton21);
		descriptionalButtonList.add(descriptionalButton22);
		descriptionalButtonList.add(descriptionalButton23);
		descriptionalButtonList.add(descriptionalButton24);
		
		productButtonList.add(artikal0);
		productButtonList.add(artikal1);
		productButtonList.add(artikal2);
		productButtonList.add(artikal3);
		productButtonList.add(artikal4);
		productButtonList.add(artikal5);
		productButtonList.add(artikal6);
		productButtonList.add(artikal7);
		productButtonList.add(artikal8);
		productButtonList.add(artikal9);
		productButtonList.add(artikal10);
		productButtonList.add(artikal11);
		productButtonList.add(artikal12);
		productButtonList.add(artikal13);
		productButtonList.add(artikal14);
		productButtonList.add(artikal15);
		productButtonList.add(artikal16);
		productButtonList.add(artikal17);
		productButtonList.add(artikal18);
		productButtonList.add(artikal19);
		productButtonList.add(artikal20);
		productButtonList.add(artikal21);
		productButtonList.add(artikal22);
		productButtonList.add(artikal23);
		productButtonList.add(artikal24);
		productButtonList.add(artikal25);
		productButtonList.add(artikal26);
		productButtonList.add(artikal27);
		productButtonList.add(artikal28);
		productButtonList.add(artikal29);
		productButtonList.add(artikal30);
		productButtonList.add(artikal31);
		productButtonList.add(artikal32);
		productButtonList.add(artikal33);
		productButtonList.add(artikal34);
		productButtonList.add(artikal35);
		productButtonList.add(artikal36);
		productButtonList.add(artikal37);
		productButtonList.add(artikal38);
		productButtonList.add(artikal39);
		productButtonList.add(artikal40);
		productButtonList.add(artikal41);
		productButtonList.add(artikal42);
		productButtonList.add(artikal43);
		productButtonList.add(artikal44);
	}
	private void fillAditionalButtons(int i, ProductDepartment pd, String color){
		
		if (!(color != null && !color.equals("")))
			color = controller.getSettings().getDefaultAdditionalColor();
		if (!(color != null && !color.equals("")))
			color = controller.getSettings().getDefaultAdditionalColor();
		
		Color c = Color.web(color);
		
		String style = "-fx-background-color: " + color + ";";
		if (c.getBrightness() < 0.5 )
			style += " -fx-text-fill : white;";
		else
			style += " -fx-text-fill : black;";
		
		additionalButtonList.get(i).setStyle(style);
		
		if (pd != null){
			pd.setEnabled(true);
			additionalButtonList.get(i).setText(pd.getProduct().getName());
			additionalButtonList.get(i).setProductDepartemnt(pd);
		}
		else{
			additionalButtonList.get(i).setText("");
		}
	}
	
	private void fillDescritpionalButtons(int i, String name,String color){
		int g = i-1;
		if (!(color != null && !color.equals("")))
			color = controller.getSettings().getDefaultDescriptionalColor();
		if (!(color != null && !color.equals("")))
			color = controller.getSettings().getDefaultDescriptionalColor();
		
		Color c = Color.web(color);
		
		String style = "-fx-background-color: " + color + ";";
		if (c.getBrightness() < 0.5 )
			style += " -fx-text-fill : white;";
		else
			style += " -fx-text-fill : black;";
		
		descriptionalButtonList.get(g).setStyle(style);
		descriptionalButtonList.get(g).setText(name);
	}
	
	private boolean descriptionalCheck(){
		boolean result = false;
		
		for(RoundItem ri : controller.getCurrentBill().getLastRound().getItemList()){
			ArrayList<Description> dList = ri.getProductDepartment().getProduct().getDescriptionList(controller.getPointOfSale().getPointOfSaleID(), 
																			  controller.getSettings().getConnection());
			double descSum = 0;
			for (RoundItemDescription rid : ri.getDescriptionalList()){
				descSum += rid.getQuantity();
			}
			if ((dList.size()>0) && (ri.getQuantity() != descSum)){
				result = false;
				break;
			}
			else{
				result = true;
			}
		}
		
		return result;
	}
	
	private void setSelectedGroup(){
		
		for(Group g : listaGrupa){
			if ((g != null) && (g.getGroupID() == selectedGroupID)){
				selectedGroup = g;
				break;
			}
		}
		for(Group g : listaPodGrupa){
			if ((g != null) && (g.getGroupID() == selectedGroupID)){
				selectedGroup = g;
				break;
			}
		}
	}
}
