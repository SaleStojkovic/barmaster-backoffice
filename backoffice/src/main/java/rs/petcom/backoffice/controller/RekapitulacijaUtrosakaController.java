package rs.petcom.backoffice.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.WorkDay;

public class RekapitulacijaUtrosakaController  implements ControlledScreen{
	
	ScreensController controller;
	
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
    @FXML private ComboBox<rs.petcom.master.dal.Object> cbObject;
	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private BorderPane reportPane;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void setParameter(Object parameter) {
		// TODO Auto-generated method stub
	}
	@Override
	public Object getParameter() {
		return null;
	}
	
	@Override
	public void init() {
		dateFrom.setConverter(converter);		
		dateFrom.setValue(LocalDate.now());
		dateTo.setConverter(converter);		
		dateTo.setValue(LocalDate.now());
		cbObject.setItems(rs.petcom.master.dal.Object.getList(controller.getSettings().getConnection()));
		for (rs.petcom.master.dal.Object o :cbObject.getItems()){
			if (o.getObjectID() == controller.getObject().getObjectID()){
				cbObject.getSelectionModel().select(o);
				break;
			}
		}
	}

	@FXML 
	private void showReport(){
	
		try{
			for (WorkDay wd : WorkDay.getList(dateFrom.getValue(),dateTo.getValue(), 
											  cbObject.getSelectionModel().getSelectedItem().getObjectID(),
											  controller.getSettings().getConnection())){
				wd.saveRecap(controller);
			}
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("title", "Rekalkulacija utrošaka");
			hm.put("text", "Rekalkulacija utrošaka završena");
			controller.showModalDialog("RekalkulacijaUtrošaka",ScreensController.DIALOG_ALLERT,hm,rootPane.getScene().getWindow());
	
		} catch (SQLException e){
			controller.alert("Greška prilikom upisa u bazu!", 
							 "Došlo je do greške prilikom upisa u bazu\n\n" + e.getMessage(), 
							 rootPane.getScene().getWindow());
		}
	}
}
