package rs.petcom.backoffice.controller;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.PriceList;
import rs.petcom.master.dal.PriceListType;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.gui.TimeTextField;
import tornadofx.control.DateTimePicker;

public class PriceListsController implements ControlledScreen{
	
	ScreensController controller;
	
	public void initialize(){
		cbType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PriceListType>() {
			  @Override public void changed(ObservableValue<? extends PriceListType> selected, 
					  						PriceListType oldValue, 
					  						PriceListType newValue) {
			    switch(newValue.getType()){
			    case 1: // Redovni cenovnik
			    	cbDomOd.setDisable(true);
			    	cbDomDo.setDisable(true);
			    	tfTimeOd.setDisable(true);
			    	tfTimeDo.setDisable(true);
			    	cbMonday.setDisable(true);
			    	cbTuesday.setDisable(true);
			    	cbWednesday.setDisable(true);
			    	cbThursday.setDisable(true);
			    	cbFriday.setDisable(true);
			    	cbSaturday.setDisable(true);
			    	cbSunday.setDisable(true);
			    	break;
			    case 2: // Mesečni
			    	cbDomOd.setDisable(false);
			    	cbDomDo.setDisable(false);
			    	tfTimeOd.setDisable(true);
			    	tfTimeDo.setDisable(true);
			    	cbMonday.setDisable(true);
			    	cbTuesday.setDisable(true);
			    	cbWednesday.setDisable(true);
			    	cbThursday.setDisable(true);
			    	cbFriday.setDisable(true);
			    	cbSaturday.setDisable(true);
			    	cbSunday.setDisable(true);
			    	break;
			    case 3: // Nedeljni
			    	cbDomOd.setDisable(true);
			    	cbDomDo.setDisable(true);
			    	tfTimeOd.setDisable(true);
			    	tfTimeDo.setDisable(true);
			    	cbMonday.setDisable(false);
			    	cbTuesday.setDisable(false);
			    	cbWednesday.setDisable(false);
			    	cbThursday.setDisable(false);
			    	cbFriday.setDisable(false);
			    	cbSaturday.setDisable(false);
			    	cbSunday.setDisable(false);
			    	break;
			    case 4: // Happy Hour
			    	cbDomOd.setDisable(true);
			    	cbDomDo.setDisable(true);
			    	tfTimeOd.setDisable(false);
			    	tfTimeDo.setDisable(false);
			    	cbMonday.setDisable(true);
			    	cbTuesday.setDisable(true);
			    	cbWednesday.setDisable(true);
			    	cbThursday.setDisable(true);
			    	cbFriday.setDisable(true);
			    	cbSaturday.setDisable(true);
			    	cbSunday.setDisable(true);
			    	break;
			    }
			  }
			});
		
		dcDateTo.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals(""))
					dcDateTo.setValue(null);
			}
		});
		cbPos.setDisable(true);
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void init() {
		tcTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
		tcDateFrom.setCellValueFactory(new PropertyValueFactory<PriceList, Date>("DateFrom"));
		tcDateFrom.setCellFactory((TableColumn<PriceList, Date> column) -> {
			   return new TableCell<PriceList, Date>() {
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
		
		tcDateTo.setCellValueFactory(new PropertyValueFactory<PriceList, Date>("DateTo"));
		tcDateTo.setCellFactory((TableColumn<PriceList, Date> column) -> {
			   return new TableCell<PriceList, Date>() {
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
		
		tcType.setCellValueFactory(new PropertyValueFactory<PriceList, String>("Type"));
		
		tcPriority.setCellValueFactory(new PropertyValueFactory<PriceList, Integer>("Priority"));
		
		rs.petcom.master.dal.Object sviObjekti = new rs.petcom.master.dal.Object();
		sviObjekti.setObjectID(-1);
		sviObjekti.setName("Svi objekti");
		cbObjekat.getItems().add(sviObjekti);
		for (rs.petcom.master.dal.Object o : rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection())){
			cbObjekat.getItems().add(o);
		}
		cbObjekat.getSelectionModel().select(sviObjekti);
		
		
		cbType.setItems(PriceListType.getList(controller.getSettings().getConnection()));
		
		cbDomOd.getItems().clear();
		cbDomDo.getItems().clear();
		for (int i=1;i<32;i++){
			cbDomOd.getItems().add(new Integer(i));
			cbDomDo.getItems().add(new Integer(i));
		}
		
		popuniFilterPos();
		loadTable();
		
		table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<PriceList>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends PriceList> c) {
				fillPriceListData();
			}
		});
	}

	@Override
	public void setParameter(Object parameter) {}

	@Override
	public Object getParameter() {
		return null;
	}

	@FXML private BorderPane rootPane;
	@FXML private TableView<PriceList> table;
	@FXML private TableColumn<PriceList, String> tcTitle;
	@FXML private TableColumn<PriceList, Date> tcDateFrom;
	@FXML private TableColumn<PriceList, Date> tcDateTo;
	@FXML private TableColumn<PriceList, String> tcType;
	@FXML private TableColumn<PriceList, Integer> tcPriority;
	
	@FXML private TextField tfTitle;
	@FXML private TextField tfComent;
	
	@FXML private CheckBox cbActive;
	
	@FXML private DateTimePicker dcDateFrom;
	@FXML private DateTimePicker dcDateTo;
	
	@FXML private TextField tfPriority;
	
	@FXML private ComboBox<Integer> cbDomOd;
	@FXML private ComboBox<Integer> cbDomDo;
	@FXML private ComboBox<PriceListType> cbType;
	@FXML private TimeTextField tfTimeOd;
	@FXML private TimeTextField tfTimeDo;
	
	@FXML private CheckBox cbMonday;
	@FXML private CheckBox cbTuesday;
	@FXML private CheckBox cbWednesday;
	@FXML private CheckBox cbThursday;
	@FXML private CheckBox cbFriday;
	@FXML private CheckBox cbSaturday;
	@FXML private CheckBox cbSunday;
		
	// Filteri
	@FXML ComboBox<rs.petcom.master.dal.Object> cbObjekat;
	@FXML ComboBox<PointOfSale> cbPos;

	private void popuniFilterPos(){
		cbPos.getItems().clear();
		PointOfSale sviPosovi = new PointOfSale();
		sviPosovi.setPointOfSaleID(-1);
		sviPosovi.setName("Sva prodajna mesta");
		cbPos.getItems().add(sviPosovi);
		cbPos.getSelectionModel().select(sviPosovi);
		for (PointOfSale p : PointOfSale.getPosList(cbObjekat.getSelectionModel().getSelectedItem(),
													controller.getSettings().getConnection())){
			cbPos.getItems().add(p);
		}
	}
	
	@FXML private void objectChange(){
		popuniFilterPos();
	}
	
	@FXML private void podChange(){
		loadTable();
	}
	
	private void loadTable(){
		table.getItems().clear();
		table.setItems(PriceList.getObeservableList(cbObjekat.getSelectionModel().getSelectedItem(), 
													controller.getSettings().getConnection()));
	}
	
	@FXML private void showDetailes(){
		if (table.getSelectionModel().getSelectedItem() != null){
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("controller", controller);
			hm.put("priceList", table.getSelectionModel().getSelectedItem());
			
			controller.showModalDialog("Cenovnik - Detalji", 
									   ScreensController.DIALOG_PRICE_LISTS_DETAILES, hm, 
									   rootPane.getScene().getWindow());
		}
	}
	
	private void setType(){
		try{
			for (PriceListType plt : cbType.getItems()){
				if (plt.getType() == table.getSelectionModel().getSelectedItem().getPriceListTypeID()){
					cbType.getSelectionModel().select(plt);
				}
			}
		} catch (NullPointerException e){}
	}
	
	private void setDomFrom(){
		try{
			for (Integer b : cbDomOd.getItems()){
				if (b == table.getSelectionModel().getSelectedItem().getDomFrom()){
					cbDomOd.getSelectionModel().select(b);
				}
			}
		} catch (NullPointerException e){}
	}
	private void setDomTo(){
		try{
			for (Integer b : cbDomDo.getItems()){
				if (b == table.getSelectionModel().getSelectedItem().getDomTo()){
					cbDomDo.getSelectionModel().select(b);
				}
			}
		} catch (NullPointerException e){}
	}
	
	private void fillPriceListData(){
		if (table.getSelectionModel().getSelectedItem() != null){
			PriceList pl = table.getSelectionModel().getSelectedItem();
			setType();
			tfPriority.setText(String.valueOf(pl.getPriority()));
			cbActive.setSelected(pl.isActive());
			dcDateFrom.setDateTimeValue(Settings.LocalDateTimefromDate(pl.getDateFrom()));
			if (pl.getDateTo() != null)
				dcDateTo.setValue(Settings.fromDate(pl.getDateTo()));
			else
				dcDateTo.setValue(null);
			tfTitle.setText(pl.getTitle());
			tfComent.setText(pl.getComment());
			
			// Specifično za Mesečni
			if (pl.getPriceListTypeID() == 2){
				setDomFrom();
				setDomTo();
			}
			else{
				cbDomOd.getSelectionModel().select(-1);
				cbDomDo.getSelectionModel().select(-1);
			}
			
			// Specifično za Nedeljni
			if (pl.getPriceListTypeID() == 3){
				cbMonday.setSelected(pl.isMonday());
				cbTuesday.setSelected(pl.isTuesday());
				cbWednesday.setSelected(pl.isWednesday());
				cbThursday.setSelected(pl.isThursday());
				cbFriday.setSelected(pl.isFriday());
				cbSaturday.setSelected(pl.isSaturday());
				cbSunday.setSelected(pl.isSunday());
			}
			else{
				cbMonday.setSelected(false);
				cbTuesday.setSelected(false);
				cbWednesday.setSelected(false);
				cbThursday.setSelected(false);
				cbFriday.setSelected(false);
				cbSaturday.setSelected(false);
				cbSunday.setSelected(false);
			}
			
			// Specifično za HappyHour
			if (pl.getPriceListTypeID() == 4){
				if (pl.getTimeFrom() != null)
					tfTimeOd.setText(Settings.getTimeFromatter().format(pl.getTimeFrom()));
				if (pl.getTimeTo() != null)
					tfTimeDo.setText(Settings.getTimeFromatter().format(pl.getTimeTo()));
			}
			else{
				tfTimeOd.setText("00:00:00");
				tfTimeDo.setText("00:00:00");
			}
		}
		else{
			setType();
			tfPriority.setText("");
			cbActive.setSelected(false);
			dcDateFrom.setValue(null);
			dcDateTo.setValue(null);
			tfTitle.setText("");
			tfComent.setText("");
			cbMonday.setSelected(false);
			cbTuesday.setSelected(false);
			cbWednesday.setSelected(false);
			cbThursday.setSelected(false);
			cbFriday.setSelected(false);
			cbSaturday.setSelected(false);
			cbSunday.setSelected(false);
			tfTimeOd.setText("");
			tfTimeDo.setText("");
		}
	}
	
	@FXML private void save(){
		
		PriceList pl = table.getSelectionModel().getSelectedItem();
		
		if (pl.getPriceListID() < 1)
			pl.setObjectID(cbObjekat.getSelectionModel().getSelectedItem().getObjectID());
		
		pl.setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		
		pl.setPriceListTypeID(cbType.getSelectionModel().getSelectedItem().getType());
		pl.setTitle(tfTitle.getText());
		pl.setComment(tfComent.getText());
		pl.setDateFrom(Settings.DatefromLocalDateTime(dcDateFrom.getDateTimeValue()));
		if (!dcDateTo.getEditor().getText().equals(""))
			pl.setDateTo(Settings.DatefromLocalDateTime(dcDateTo.getDateTimeValue()));
		else
			pl.setDateTo(null);
		
		// Podaci specifični za Mesečni
		if (pl.getPriceListTypeID() == 2){
			pl.setDomFrom(cbDomOd.getSelectionModel().getSelectedItem().byteValue());
			pl.setDomTo(cbDomDo.getSelectionModel().getSelectedItem().byteValue());
		}
		else{
			pl.setDomFrom((byte)0);
			pl.setDomTo((byte)0);
		}

		// Podaci specifični za Nedeljnji
		if (pl.getPriceListTypeID() == 3){
			pl.setMonday(cbMonday.isSelected());
			pl.setTuesday(cbTuesday.isSelected());
			pl.setWednesday(cbWednesday.isSelected());
			pl.setThursday(cbThursday.isSelected());
			pl.setFriday(cbFriday.isSelected());
			pl.setSaturday(cbSaturday.isSelected());
			pl.setSunday(cbSunday.isSelected());
		}
		else{
			pl.setMonday(false);
			pl.setTuesday(false);
			pl.setWednesday(false);
			pl.setThursday(false);
			pl.setFriday(false);
			pl.setSaturday(false);
			pl.setSunday(false);	
		}
		
		// Podaci specifični za HappyHour
		if (pl.getPriceListTypeID() == 4){
			pl.setTimeFrom(Time.valueOf(tfTimeOd.getText()));
			pl.setTimeTo(Time.valueOf(tfTimeDo.getText()));
		}
		else{
			pl.setTimeFrom(null);
			pl.setTimeTo(null);
		}
		
		pl.setPriority(Byte.parseByte(tfPriority.getText()));
		pl.setActive(cbActive.isSelected());
		
		if (pl.getPriceListID() > 0)
			pl.update(controller.getSettings().getConnection());
		else
			pl.insert(controller.getSettings().getConnection());
		
		table.refresh();
	}
	
	@FXML private void newPricelist(){
		PriceList pl = new PriceList();
		pl.setPriceListID(new Long(-1));
		pl.setPriceListTypeID(1);
		pl.setActive(false);
		pl.setDateFrom(new Date());
		pl.setObjectID(cbObjekat.getSelectionModel().getSelectedItem().getObjectID());
		pl.setPointOfSaleID(cbPos.getSelectionModel().getSelectedItem().getPointOfSaleID());
		pl.insert(controller.getSettings().getConnection());
		table.getItems().add(pl);
		table.scrollTo(pl);
		table.getSelectionModel().select(pl);
		fillPriceListData();
	}
	@FXML private void deletePricelist(){
		try{
			table.getSelectionModel().getSelectedItem().delete(controller.getSettings().getConnection());
			table.getItems().remove(table.getSelectionModel().getSelectedItem());
		} catch (SQLException e){
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("title", "Brisanje cenovnika nije moguće!");
			hm.put("text", "\nPoruka o grešci:\n\n" + e.getLocalizedMessage());
			controller.showModalDialog("Brisanje cenovnika nije moguće!",ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
		}
	}

	@FXML private void dcDateToChange(){}
}
