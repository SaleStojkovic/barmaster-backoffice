package rs.petcom.master.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.Localization;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.person.Person;

public class LoyalityController implements ControlledScreen{

	ScreensController controller;
	private Timeline timerSat;
	private final SimpleDateFormat formatClock = new SimpleDateFormat("HH:mm:ss");
	private NumberFormat numberFormat = NumberFormat.getNumberInstance();
	
	@FXML private Label clockLabel;
	@FXML private Label userLabel;
	@FXML private Label lokacijaLabel;
	@FXML private GridPane letterPane;
	@FXML private TableView<Person> personTable;
	@FXML private TextField searchTextField;
	@FXML private Label labelLastName;
	@FXML private Label labelFirstName;
	@FXML private Label labelDiscount;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
		this.controller.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()){
				case ENTER:
					searchAction();
					break;
				case BACK_SPACE:
				case DELETE:
					if (searchTextField.getText().length() > 0)
						searchTextField.setText(searchTextField.getText().substring(0, searchTextField.getText().length()-1));
				default:
					if (event.getCode().isDigitKey()){
						searchTextField.setText(searchTextField.getText()+event.getText());
					}
				}
			}
		});
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
		lokacijaLabel.setText(controller.getPointOfSale().getName());
		clockLabel.setText(formatClock.format(new Date())); 
		
		addLetters();
		
		TableColumn<Person, String> personCol = new TableColumn<Person, String>("");
		personCol.setCellValueFactory(new PropertyValueFactory<Person, String>("FirstLastName"));
		personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		personTable.getColumns().add(personCol);
		personTable.setPlaceholder(new Label(""));
		
		personTable.setItems(Person.getObeservableList("","",controller.getSettings().getConnection()));
		
		personTable.getSelectionModel().setCellSelectionEnabled(false); 
		personTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
		personTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Person>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Person> c) {
				try{
					labelLastName.setText(personTable.getSelectionModel().getSelectedItem().getLastName());
					labelFirstName.setText(personTable.getSelectionModel().getSelectedItem().getFirstName());
					labelDiscount.setText(numberFormat.format(personTable.getSelectionModel().getSelectedItem().getDiscount())+ "%");
				} catch (NullPointerException e){
					labelLastName.setText("");
					labelFirstName.setText("");
					labelDiscount.setText("");
				}
			}
			
		});
		
		searchTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				personTable.setItems(Person.getObeservableList("",newValue,controller.getSettings().getConnection()));
			}
		});
		
		searchTextField.getProperties().put("vkType", "text");
	}
	
	private void addLetters(){
		for(int i =0; i<Localization.getLetters().length;i++){
			ColumnConstraints column = new ColumnConstraints(30,30,Double.MAX_VALUE);
			column.setFillWidth(true);
			column.setHgrow(Priority.ALWAYS);
			
			letterPane.getColumnConstraints().add(column);
			
			Button b = new Button(Localization.getLetters()[i]);
			b.setMinHeight(64);
			b.setPrefHeight(64);
			b.setMaxHeight(Double.MAX_VALUE);
			b.setMinWidth(30);
			b.setPrefWidth(30);
			b.setMaxWidth(Double.MAX_VALUE);
			b.setPadding(Insets.EMPTY);
			b.getStyleClass().add("letterButton");
			
			b.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					try{
						personTable.setItems(Person.getObeservableList(((Button)event.getSource()).getText(),"", controller.getSettings().getConnection()));
					} catch (NullPointerException e){}
				}
			});
			
			letterPane.add(b,i,0);
		}
	}
	
	@FXML 
	private void postClicked(){
		controller.getCurrentBill().setPerson(personTable.getSelectionModel().getSelectedItem());
		controller.getScreen(ScreensController.SCREEN_FASTFOOD,
								controller.getRoot(),
								ScreensController.SCREEN_LOYALITY);
	}
	@FXML 
	private void cancelButtonClick(){
		controller.getScreen(ScreensController.SCREEN_FASTFOOD,
								controller.getRoot(),
								ScreensController.SCREEN_LOYALITY);
	}
	@FXML 
	private void logout(){
		Log.logOut(controller.getUser().getUserID(), 
				   controller.getPointOfSale().getPointOfSaleID(),
				   controller.getSettings().getConnection());
		controller.setUser(null);
		controller.getBillList().clear();
		controller.getScreen(ScreensController.SCREEN_LOGIN,
								controller.getRoot(),
								ScreensController.SCREEN_LOYALITY);
	}
	@FXML
	private void searchAction(){
		System.out.println("Search Action");
		controller.getCurrentBill().setPerson(Person.getByCode(searchTextField.getText(), controller.getSettings().getConnection()));
		controller.getScreen(ScreensController.SCREEN_FASTFOOD,
								controller.getRoot(),
								ScreensController.SCREEN_LOYALITY);
	}

}
