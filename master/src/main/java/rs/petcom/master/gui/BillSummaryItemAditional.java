package rs.petcom.master.gui;

import java.text.NumberFormat;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import rs.petcom.master.dal.RoundItem;

public class BillSummaryItemAditional  extends GridPane{

	private RoundItem roundItem;
	
	private boolean selected;
	
	Label lArtikal = new Label();
    Label lKolicina = new Label();
    Label lCena = new Label();
    
    NumberFormat nf = NumberFormat.getNumberInstance();
	
	private int itmeHeight = BillSummaryItem.summaryItemHeight;	
	
	public RoundItem getRoundItem() {
		return roundItem;
	}
	public void setRoundItem(RoundItem roundItem) {
		this.roundItem = roundItem;
		lArtikal.setText(this.roundItem.getProductDepartment().getProduct().getName());
		lKolicina.setText(nf.format(this.roundItem.getQuantity()));
		lCena.setText(nf.format(this.roundItem.getSumWithDiscount()));
	}
	
	public int getItemHeight() {
		return itmeHeight;
	}
	public void setItemHeight(int height) {
		this.itmeHeight = height;
	}
	
	public BillSummaryItemAditional(RoundItem roundItem){
		this.roundItem = roundItem;
		
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);
		
		setMinHeight(itmeHeight);
		setPrefHeight(itmeHeight);
		setMaxHeight(itmeHeight);
				
		ColumnConstraints column1 = new ColumnConstraints(150,150,Double.MAX_VALUE);
		column1.setFillWidth(true);
		column1.setHgrow(Priority.ALWAYS);
	    
		ColumnConstraints column2 = new ColumnConstraints(60,60,30);
		column2.setHgrow(Priority.NEVER);
	    
		ColumnConstraints column3 = new ColumnConstraints(100,100,100);
		column3.setHgrow(Priority.NEVER);
	    		
		getColumnConstraints().addAll(column1, column2, column3);
		
	    RowConstraints row1 = new RowConstraints(itmeHeight-1,itmeHeight-1,itmeHeight-1);
	    row1.setVgrow(Priority.ALWAYS);;
	    row1.setValignment(VPos.CENTER);
	    getRowConstraints().add(row1);
	    
	    lArtikal.getStyleClass().add("billSummaryItemLabel");
	    lArtikal.setPadding(new Insets(0,0,0,5));
	    lArtikal.setAlignment(Pos.CENTER_LEFT);
	    lArtikal.setMinWidth(100);
	    lArtikal.setPrefWidth(100);
	    lArtikal.setMaxWidth(Double.MAX_VALUE);
	    lArtikal.setMinHeight(itmeHeight);
	    lArtikal.setPrefHeight(itmeHeight);
	    lArtikal.setMaxHeight(itmeHeight);
	    
	    lKolicina.getStyleClass().add("billSummaryItemLabel");
	    lKolicina.setAlignment(Pos.CENTER_RIGHT);
	    lKolicina.setMinWidth(30);
	    lKolicina.setPrefWidth(60);
	    lKolicina.setMaxWidth(60);
	    lKolicina.setMinHeight(itmeHeight);
	    lKolicina.setPrefHeight(itmeHeight);
	    lKolicina.setMaxHeight(itmeHeight);
	    
	    lCena.getStyleClass().add("billSummaryItemLabel");
	    lCena.setAlignment(Pos.CENTER_RIGHT);
	    lCena.setMinWidth(100);
	    lCena.setPrefWidth(100);
	    lCena.setMaxWidth(100);
	    lCena.setMinHeight(itmeHeight);
	    lCena.setPrefHeight(itmeHeight);
	    lCena.setMaxHeight(itmeHeight);
	    
		add(lArtikal,0,0);
		add(lKolicina,1,0);
		add(lCena,2,0);
		addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
			public void handle(Event event) {
				if (!selected){
					((BillSummaryItem) getParent()).unselectAll();
					getStyleClass().remove("billSummaryItem");
					getStyleClass().add("billSummaryItemSelected");
					selected = true;
				}
				else{
					getStyleClass().remove("billSummaryItemSelected");
					getStyleClass().add("billSummaryItem");
					selected = false;
				}
			}
		});
	}
	
	public void refresh(){
		lArtikal.setText(" - " + roundItem.getProductDepartment().getProduct().getName());
		lKolicina.setText(nf.format(roundItem.getQuantity()));
		lCena.setText(nf.format(roundItem.getQuantity() * roundItem.getProductDepartment().getProduct().getPrice()));
	}

	public void setSelected(boolean selected){
		this.selected = selected;
		if (selected){
			getStyleClass().remove("billSummaryItem");
			getStyleClass().add("billSummaryItemSelected");
		}
		else{
			getStyleClass().remove("billSummaryItemSelected");
			getStyleClass().add("billSummaryItem");
		}
	}
	public boolean isSelected(){
		return selected;
	}
}
