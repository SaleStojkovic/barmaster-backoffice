package rs.petcom.backoffice.controller.reports;

import javafx.fxml.FXML;

import java.util.Date;

import javafx.event.ActionEvent;

import javafx.scene.layout.BorderPane;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.fical.FiscalPrinter;
import javafx.scene.control.DatePicker;

public class FiscalReportsController  implements ControlledScreen{
	
	ScreensController controller;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void init() {
		dpDateOd.setValue(Settings.fromDate(new Date()));
		dpDateDo.setValue(Settings.fromDate(new Date()));
	}
	@Override
	public void setParameter(Object parameter) {
		
	}
	@Override
	public Object getParameter() {
		return null;
	}
	
	@FXML private BorderPane rootPane;
	@FXML private DatePicker dpDateOd;
	@FXML private DatePicker dpDateDo;

	@FXML public void actionPresekStanja(ActionEvent event) {
		FiscalPrinter.printReport(controller, "1");
	}
	
	@FXML public void actionPrometArtikala(ActionEvent event) {
		FiscalPrinter.printReport(controller, "2");
	}

	@FXML public void actionPeriodicni(ActionEvent event) {
		FiscalPrinter.printPeriodicni(Settings.fromLocalDate(dpDateOd.getValue()), 
									  Settings.fromLocalDate(dpDateDo.getValue()), 
									  controller);
	}
	
}
