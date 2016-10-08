package rs.petcom.restaurant.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.restaurant.dal.SalePlace;
import rs.petcom.restaurant.dal.SaleTable;
import rs.petcom.restaurant.gui.SalePlaceToggleButton;
import rs.petcom.restaurant.gui.SaleTableButton;

public class StoloviController implements ControlledScreen {

	ScreensController controller;
	
	
	SalePlace selectedSalePlace = null;
	
	double scaleX = 1;
	double scaleY = 1;
	
	ArrayList<SalePlace> salePlaceList;
	ArrayList<SalePlaceToggleButton> salePlaceButtonList = new ArrayList<>();
	ArrayList<SaleTableButton> saleTableButtonList = new ArrayList<>();
	
	
	public void initialize(){
		salePlaceButtonList.clear();
		salePlaceButtonList.add(salePlaceButton0);
		salePlaceButtonList.add(salePlaceButton1);
		salePlaceButtonList.add(salePlaceButton2);
		salePlaceButtonList.add(salePlaceButton3);
		salePlaceButtonList.add(salePlaceButton4);
		salePlaceButtonList.add(salePlaceButton5);
		salePlaceButtonList.add(salePlaceButton6);
		salePlaceButtonList.add(salePlaceButton7);
		salePlaceButtonList.add(salePlaceButton8);
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void init() {
		imageViewSala.minWidth(0);
		imageViewSala.maxWidth(Double.MAX_VALUE);
		imageViewSala.minHeight(0);
		imageViewSala.maxHeight(Double.MAX_VALUE);
		imageViewSala.autosize();
		
		backGrounPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imageViewSala.prefWidth(newValue.doubleValue());
				imageViewSala.setFitWidth(newValue.doubleValue());
				scaleX = imageViewSala.getImage().getWidth() / imageViewSala.getFitWidth();
				recalculateTables();
			}
			
		});
		backGrounPane.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				imageViewSala.prefHeight(newValue.doubleValue());
				imageViewSala.setFitHeight(newValue.doubleValue());
				scaleY = imageViewSala.getImage().getHeight() / imageViewSala.getFitHeight();
				recalculateTables();
			}
		});
		
		loadSalePlaceButtons();
		
	}

	@Override
	public void setParameter(Object parameter) {
		
	}

	@Override
	public Object getParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@FXML private BorderPane rootPane;
	@FXML private BorderPane backGrounPane;
	
	@FXML private ImageView imageViewSala;
	@FXML private SalePlaceToggleButton salePlaceButton0, salePlaceButton1, salePlaceButton2, salePlaceButton3,
										salePlaceButton4, salePlaceButton5, salePlaceButton6, salePlaceButton7,
										salePlaceButton8;
	@FXML private AnchorPane saleTablePane;
	
	@FXML private void logout(){
		controller.getBillList().clear();
		controller.getScreen(ScreensController.SCREEN_LOGIN,
									controller.getRoot(),
									ScreensController.SCREEN_RESTAURANT_STOLOVI);
	}
	
	@FXML private void selectSalePlace(ActionEvent event){
	}
	
	private void loadSalePlaceButtons(){
		try {
			salePlaceList = SalePlace.getList(controller.getPointOfSale(), controller.getSettings().getConnection());
			if (selectedSalePlace == null && salePlaceList.size()>0){
				for(int i=0;i<9;i++){
					try{
						salePlaceButtonList.get(i).setSalePlace(salePlaceList.get(i));
					}catch (IndexOutOfBoundsException e){
						salePlaceButtonList.get(i).setSalePlace(null);
					}
				}
			}
			if (salePlaceList.size() > 0){
				selectSalePlace(0);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			controller.alert("Greška pri učitavanju sala", e.getMessage(), rootPane.getScene().getWindow());
		}
	}
	private void selectSalePlace(int i){
		selectedSalePlace = salePlaceButtonList.get(i).getSalePlace();
		salePlaceButtonList.get(i).setSelected(true);
		
		imageViewSala.setImage(selectedSalePlace.getImage());
		
		loadTables();
	}
	
	private void loadTables(){
		System.out.println("Load tables");
		try {
			ArrayList<SaleTable> saleTableList = SaleTable.getList(selectedSalePlace, controller.getSettings().getConnection());
			saleTablePane.getChildren().clear();
			for(SaleTable st : saleTableList){
				SaleTableButton stb = new SaleTableButton();
				stb.setSaleTable(st);
				saleTablePane.getChildren().add(stb);
			}
			recalculateTables();
		} catch (SQLException e) {
			controller.alert("Greška pri učitavanju liste stolova!", e.getMessage(), rootPane.getScene().getWindow());
		}
	}
	
	private void recalculateTables(){
		for (Node n : saleTablePane.getChildren()){
			SaleTableButton button = (SaleTableButton) n;
			
			double scale = (scaleX > scaleY) ? scaleX : scaleY;
						
			button.setMinWidth(button.getSaleTable().getWidth() *  1 / scale );
			button.setPrefWidth(button.getSaleTable().getWidth() *  1 / scale );
			button.setMaxWidth(button.getSaleTable().getWidth() *  1 / scale );
			
			
			button.setTranslateX((backGrounPane.getWidth() - imageViewSala.getImage().getWidth() * 1 / scale) / 2 + 
								 button.getSaleTable().getOffsetX() * 1 / scale);
			button.setTranslateY((backGrounPane.getHeight() - imageViewSala.getImage().getHeight() * 1 / scale) / 2 + 
					 			  button.getSaleTable().getOffsetY() * 1 / scale);
			
			button.setMinHeight(button.getSaleTable().getHeight() * 1 / scale );		
			button.setPrefHeight(button.getSaleTable().getHeight() * 1 / scale );	
			button.setMaxHeight(button.getSaleTable().getHeight() * 1 / scale );	
		}
	}
}
