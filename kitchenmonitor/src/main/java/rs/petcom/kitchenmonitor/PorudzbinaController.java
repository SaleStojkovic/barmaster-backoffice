package rs.petcom.kitchenmonitor;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.document.Order;
import rs.petcom.master.dal.document.OrderDetaile;

public class PorudzbinaController implements ControlledScreen{
	
	ScreensController controller;
	
	final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
	     public void handle(ActionEvent event) {    
	          refreshTims();
	     }  
	})); ;
	
	
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	public void init() {
	}

	public void setParameter(Object parameter) {
	}

	public Object getParameter() {
		return null;
	}
	
	@FXML private Button button;
	@FXML private TableView<OrderDetaile> table;
	@FXML private TableColumn<OrderDetaile, String> tcName;
	@FXML private TableColumn<OrderDetaile, Double> tcQuantity;
	@FXML private TableColumn<OrderDetaile, Date> tcStatus;
	@FXML private BorderPane statusPane;
	@FXML private Label lblStatus;
	@FXML private Label lblTime;
	@FXML private Label labelBroj;
	@FXML private Label lableVreme;
	
	public void initialize(){
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		tcName.setCellValueFactory(new PropertyValueFactory<OrderDetaile, String>("Name"));
		tcQuantity.setCellValueFactory(new PropertyValueFactory<OrderDetaile, Double>("Quantity"));
		tcQuantity.setCellFactory((TableColumn<OrderDetaile, Double> column) -> {
			   return new TableCell<OrderDetaile, Double>() {
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
		
		tcStatus.setCellValueFactory(new PropertyValueFactory<OrderDetaile, Date>("PreostaloVreme"));
		
		tcStatus.setCellFactory((TableColumn<OrderDetaile, Date> column) -> {
			   return new TableCell<OrderDetaile, Date>() {
					private TableRow<OrderDetaile> tableRow2;

					@Override
				      protected void updateItem(Date item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getTimeFromatter().format(item));
				         }
						TableRow<OrderDetaile> tableRow = tableRow2;
						TableRow<OrderDetaile> currentRow = tableRow;
		                if (!isEmpty()) {
		                	try{
		                		if(currentRow.getItem().getFinishTime() != null){ 
			                        currentRow.getStyleClass().clear();
			                        currentRow.getStyleClass().add("finished");
			                    }
		                		if((currentRow.getItem().getStartTime() != null) &&
		                		   (currentRow.getItem().getFinishTime() == null)){ 
			                        currentRow.getStyleClass().clear();
			                        currentRow.getStyleClass().add("started");
			                    }
		                		if(currentRow.getItem().getStartTime() == null){ 
					               currentRow.getStyleClass().clear();
					               currentRow.getStyleClass().add("waiting");
					            }
		                		
			                    
		                		
			                    Date preostaloVreme = new Date(currentRow.getItem().getStartTime().getTime() + 
			                    							   currentRow.getItem().getPreparationTime().getTime() -
			                    							   new Date().getTime());
			
			                    if ((currentRow.getItem().getStartTime() != null) &&
		                		    (currentRow.getItem().getFinishTime() == null) &&
		                		    (3600000 + preostaloVreme.getTime()) < 0){
			                    	 currentRow.getStyleClass().clear();
				                     currentRow.getStyleClass().add("late");
			                    }
		                	} catch (NullPointerException e){}
		                }
				      }
				   };
				});		
	}
	
	@FXML public void buttonClick(ActionEvent event) {
		for(OrderDetaile od : table.getItems()){
			try {
				if (od.getStartTime() == null){
					od.setStartTime(new Date());
					od.start(controller.getSettings().getConnection());
					
				}
				if (od.getFinishTime() == null){
					od.setFinishTime(new Date());
					od.finish(controller.getSettings().getConnection());
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOrder(Order order){
		if (table.getItems().size() == 0){
			labelBroj.setText(Long.toString(order.getOrderID()));
			table.setItems( OrderDetaile.getList(order.getOrderID(), controller));
			lableVreme.setText(Settings.getDateTimeFromatter().format(table.getItems().get(0).getCreateTime()));
		}
			
	}
	
	
	@FXML private void mouseClick(MouseEvent event){
		@SuppressWarnings("unchecked")
		OrderDetaile izabranaSavka = ((TableView<OrderDetaile>)event.getSource()).getSelectionModel().getSelectedItem();
		try{
			if ((izabranaSavka.getStartTime() != null) &&
				(izabranaSavka.getFinishTime() == null)){
				izabranaSavka.setFinishTime(new Date());
				try {
					izabranaSavka.finish(controller.getSettings().getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				table.refresh();
			}
			
			if ((izabranaSavka.getStartTime() == null) &&
				(izabranaSavka.getFinishTime() == null)){
				izabranaSavka.setStartTime(new Date());
				try {
					izabranaSavka.start(controller.getSettings().getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				table.refresh();
			}
		} catch(NullPointerException e){}
	}
	
	private void refreshTims(){
		for(int i=0;i<table.getItems().size();i++){
			if ((table.getItems().get(i).getStartTime() != null)&&
				(table.getItems().get(i).getFinishTime() == null)){
				Date preostaloVreme = new Date(table.getItems().get(i).getStartTime().getTime() + 
									  		   table.getItems().get(i).getPreparationTime().getTime() -
									  		   new Date().getTime());
				
				if ((3600000 + preostaloVreme.getTime()) < 0){
					try {
						preostaloVreme = Settings.getTimeFromatter().parse("00:00:00");
					} catch (ParseException e) {}
				}
				table.getItems().get(i).setPreostaloVreme(preostaloVreme);
				
			}
			else if ((table.getItems().get(i).getStartTime() == null)&&
				(table.getItems().get(i).getFinishTime() == null)){
				table.getItems().get(i).setPreostaloVreme(table.getItems().get(i).getPreparationTime());
			}
			table.refresh();
		}
	}
}
