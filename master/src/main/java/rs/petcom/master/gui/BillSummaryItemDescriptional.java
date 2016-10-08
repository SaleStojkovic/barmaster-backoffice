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
import rs.petcom.master.dal.RoundItemDescription;

public class BillSummaryItemDescriptional  extends GridPane{

	private RoundItemDescription description;
	
	private boolean selected;
	private int index;
	
	Label lDescription = new Label();
	Label lQuantity = new Label();
    
    NumberFormat nf = NumberFormat.getNumberInstance();
		
	public RoundItemDescription getDescription() {
		return description;
	}
	public int getIndex(){
		return index;
	}

	// Constructor
	public BillSummaryItemDescriptional(RoundItemDescription description, int index){
		this.description = description;
		this.index = index;
		
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);
		
		setMinHeight(BillSummaryItem.summaryItemHeight);
		setPrefHeight(BillSummaryItem.summaryItemHeight);
		setMaxHeight(BillSummaryItem.summaryItemHeight);
				
		ColumnConstraints column1 = new ColumnConstraints(150,150,Double.MAX_VALUE);
		column1.setFillWidth(true);
		column1.setHgrow(Priority.ALWAYS);
	    
		ColumnConstraints column2 = new ColumnConstraints(60,60,30);
		column2.setHgrow(Priority.NEVER);
	    
		ColumnConstraints column3 = new ColumnConstraints(100,100,100);
		column3.setHgrow(Priority.NEVER);
	    		
		getColumnConstraints().addAll(column1, column2, column3);
		
	    RowConstraints row1 = new RowConstraints(BillSummaryItem.summaryItemHeight-1,BillSummaryItem.summaryItemHeight-1,BillSummaryItem.summaryItemHeight-1);
	    row1.setVgrow(Priority.ALWAYS);;
	    row1.setValignment(VPos.CENTER);
	    getRowConstraints().add(row1);
	    
	    lDescription.getStyleClass().add("billSummaryItemLabel");
	    lDescription.setPadding(new Insets(0,0,0,5));
	    lDescription.setAlignment(Pos.CENTER_LEFT);
	    lDescription.setMinWidth(100);
	    lDescription.setPrefWidth(100);
	    lDescription.setMaxWidth(Double.MAX_VALUE);
	    lDescription.setMinHeight(BillSummaryItem.summaryItemHeight);
	    lDescription.setPrefHeight(BillSummaryItem.summaryItemHeight);
	    lDescription.setMaxHeight(BillSummaryItem.summaryItemHeight);
	    
	    lQuantity.getStyleClass().add("billSummaryItemLabel");
	    lQuantity.setAlignment(Pos.CENTER_RIGHT);
	    lQuantity.setMinWidth(30);
	    lQuantity.setPrefWidth(60);
	    lQuantity.setMaxWidth(60);
	    lQuantity.setMinHeight(BillSummaryItem.summaryItemHeight);
	    lQuantity.setPrefHeight(BillSummaryItem.summaryItemHeight);
	    lQuantity.setMaxHeight(BillSummaryItem.summaryItemHeight);
	    
	    add(lDescription,0,0);
	    add(lQuantity,1,0);
	    
		addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
			public void handle(Event event) {
				selected = !selected;
				setSelected(selected);
			}
		});
	}
	public void setSelected(boolean selected){
		this.selected = selected;
		if (selected){
			BillSummaryItem bsi = ((BillSummaryItem) getParent());
			bsi.unselectAll();
			((BillSummaryRound) bsi.getParent()).unselectAllExcept(bsi);
			getStyleClass().remove("billSummaryItem");
			getStyleClass().add("billSummaryItemSelected");
			this.selected = selected;
		}
		else{
			getStyleClass().remove("billSummaryItemSelected");
			getStyleClass().add("billSummaryItem");
		}
	}
	public boolean isSelected(){
		return selected;
	}
	
	public void refresh(){
		lDescription.setText(" -> " + description.getDescription().getName());
		lQuantity.setText(nf.format(description.getQuantity()));
	}
}
