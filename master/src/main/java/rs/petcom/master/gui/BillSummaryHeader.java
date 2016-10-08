package rs.petcom.master.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class BillSummaryHeader extends GridPane{
	
	private static int height = 25 ;
	public BillSummaryHeader(){
		
		setMinHeight(height+5);
		setPrefHeight(height+5);
		setMaxHeight(height+5);
		
		getStyleClass().add("billSummaryHeader");
		getStyleClass().add("borderTopBottom");
		
		ColumnConstraints column1 = new ColumnConstraints(150,150,Double.MAX_VALUE);
		column1.setFillWidth(true);
		column1.setHgrow(Priority.ALWAYS);
	    
		ColumnConstraints column2 = new ColumnConstraints(60,60,30);
		column2.setHgrow(Priority.NEVER);
	    
		ColumnConstraints column3 = new ColumnConstraints(100,100,100);
		column3.setHgrow(Priority.NEVER);
	    
		ColumnConstraints column4 = new ColumnConstraints(10,10,10);
		column4.setHgrow(Priority.NEVER);
		
		getColumnConstraints().addAll(column1, column2, column3, column4);
		
	    RowConstraints row1 = new RowConstraints(height-1,height-1,height-1);
	    row1.setVgrow(Priority.ALWAYS);;
	    row1.setValignment(VPos.CENTER);
	    getRowConstraints().add(row1);
	    
	    Label lArtikal = new Label("Artikal");
	    lArtikal.getStyleClass().add("billSummaryHeaderLabel");
	    lArtikal.setPadding(new Insets(0,0,0,5));
	    lArtikal.setAlignment(Pos.CENTER_LEFT);
	    lArtikal.setMinWidth(100);
	    lArtikal.setPrefWidth(100);
	    lArtikal.setMaxWidth(Double.MAX_VALUE);
	    lArtikal.setMinHeight(height);
	    lArtikal.setPrefHeight(height);
	    lArtikal.setMaxHeight(height);
	    
	    Label lKolicina = new Label("Količina");
	    lKolicina.getStyleClass().add("billSummaryHeaderLabel");
	    lKolicina.setAlignment(Pos.CENTER);
	    lKolicina.setMinWidth(30);
	    lKolicina.setPrefWidth(60);
	    lKolicina.setMaxWidth(60);
	    lKolicina.setMinHeight(height);
	    lKolicina.setPrefHeight(height);
	    lKolicina.setMaxHeight(height);
	    
	    Label lCena = new Label("Cena");
	    lCena.getStyleClass().add("billSummaryHeaderLabel");
	    lCena.setAlignment(Pos.CENTER);
	    lCena.setMinWidth(100);
	    lCena.setPrefWidth(100);
	    lCena.setMaxWidth(100);
	    lCena.setMinHeight(height);
	    lCena.setPrefHeight(height);
	    lCena.setMaxHeight(height);
	    
		add(lArtikal,0,0);
		add(lKolicina,1,0);
		add(lCena,2,0);
	}

}
