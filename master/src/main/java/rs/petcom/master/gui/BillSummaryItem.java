package rs.petcom.master.gui;

import java.text.NumberFormat;
import java.util.ArrayList;

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

public class BillSummaryItem  extends GridPane{

	private RoundItem roundItem;
	private boolean selected;
	
	private ArrayList<BillSummaryItemAditional> additionalList = new ArrayList<BillSummaryItemAditional>();
	private ArrayList<BillSummaryItemDescriptional> descriptionalList = new ArrayList<BillSummaryItemDescriptional>();
	
	Label lArtikal = new Label();
    Label lKolicina = new Label();
    Label lCena = new Label();
    
    NumberFormat nf = NumberFormat.getNumberInstance();
	
	private int itemHeight = 0;
	public static int summaryItemHeight = 20;
	
	public BillSummaryItem(RoundItem roundItem){
		this.roundItem = roundItem;
		
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);

		lArtikal.setText(roundItem.getProductDepartment().getProduct().getName());
		lKolicina.setText(nf.format(roundItem.getQuantity()));
		lCena.setText(nf.format(roundItem.getProductDepartment().getProduct().getPrice() * roundItem.getQuantity()));
		
		this.getStyleClass().add("billSummaryItem");
		
		setMinHeight(itemHeight);
		setPrefHeight(itemHeight);
		setMaxHeight(itemHeight);
				
		ColumnConstraints column1 = new ColumnConstraints(150,150,Double.MAX_VALUE);
		column1.setFillWidth(true);
		column1.setHgrow(Priority.ALWAYS);
	    
		ColumnConstraints column2 = new ColumnConstraints(60,60,30);
		column2.setHgrow(Priority.NEVER);
	    
		ColumnConstraints column3 = new ColumnConstraints(100,100,100);
		column3.setHgrow(Priority.NEVER);
	    		
		getColumnConstraints().addAll(column1, column2, column3);
		
	    RowConstraints row1 = new RowConstraints(summaryItemHeight,summaryItemHeight,summaryItemHeight);
	    row1.setVgrow(Priority.ALWAYS);;
	    row1.setValignment(VPos.CENTER);
	    getRowConstraints().add(row1);
	    for(int i=0;i<this.roundItem.getAdditionalList().size();i++){
	    	getRowConstraints().add(row1);
	    }
	    for(int i=0;i<this.roundItem.getDescriptionalList().size();i++){
	    	getRowConstraints().add(row1);
	    }
	    
	    lArtikal.getStyleClass().add("billSummaryItemLabel");
	    lArtikal.setPadding(new Insets(0,0,0,5));
	    lArtikal.setAlignment(Pos.CENTER_LEFT);
	    lArtikal.setMinWidth(150);
	    lArtikal.setPrefWidth(150);
	    lArtikal.setMaxWidth(Double.MAX_VALUE);
	    lArtikal.setMinHeight(summaryItemHeight);
	    lArtikal.setPrefHeight(summaryItemHeight);
	    lArtikal.setMaxHeight(summaryItemHeight);
	    
	    lKolicina.getStyleClass().add("billSummaryItemLabel");
	    lKolicina.setAlignment(Pos.CENTER_RIGHT);
	    lKolicina.setMinWidth(30);
	    lKolicina.setPrefWidth(60);
	    lKolicina.setMaxWidth(60);
	    lKolicina.setMinHeight(summaryItemHeight);
	    lKolicina.setPrefHeight(summaryItemHeight);
	    lKolicina.setMaxHeight(summaryItemHeight);
	    
	    lCena.getStyleClass().add("billSummaryItemLabel");
	    lCena.setAlignment(Pos.CENTER_RIGHT);
	    lCena.setMinWidth(100);
	    lCena.setPrefWidth(100);
	    lCena.setMaxWidth(100);
	    lCena.setMinHeight(summaryItemHeight);
	    lCena.setPrefHeight(summaryItemHeight);
	    lCena.setMaxHeight(summaryItemHeight);
	    
		add(lArtikal,0,0);
		add(lKolicina,1,0);
		add(lCena,2,0);
		
		additionalList.clear();
		itemHeight = summaryItemHeight;
		for(int i=0;i<this.roundItem.getAdditionalList().size();i++){
			BillSummaryItemAditional aditional = new BillSummaryItemAditional(this.roundItem.getAdditionalList().get(i));
			GridPane.setColumnSpan(aditional, 3);
			add(aditional,0,i+1);
			additionalList.add(aditional);
			itemHeight += summaryItemHeight;
		}
		for(int i=0;i<this.roundItem.getDescriptionalList().size();i++){
			BillSummaryItemDescriptional descriptional = new BillSummaryItemDescriptional(this.roundItem.getDescriptionalList().get(i),i);
			GridPane.setColumnSpan(descriptional, 3);
			add(descriptional,0,additionalList.size() + i+1);
			descriptionalList.add(descriptional);
			itemHeight += summaryItemHeight;
		}
		
		lArtikal.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
			public void handle(Event event) {
				setSelected(!selected);
			}
		});
		lKolicina.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
			public void handle(Event event) {
				setSelected(!selected);
			}
		});
		lCena.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<Event>() {
			public void handle(Event event) {
				setSelected(!selected);
			}
		});
		
	}
	
	public ArrayList<BillSummaryItemAditional> getAdditionalList() {
		return additionalList;
	}

	public void setAdditionalList(ArrayList<BillSummaryItemAditional> additionalList) {
		this.additionalList = additionalList;
	}

	public ArrayList<BillSummaryItemDescriptional> getDescriptionalList() {
		return descriptionalList;
	}

	public void setDescriptionalList(ArrayList<BillSummaryItemDescriptional> descriptionalList) {
		this.descriptionalList = descriptionalList;
	}

	public int getItemHeight() {
		return itemHeight;
	}
	public void setItemHeight(int height) {
		this.itemHeight = height;
	}
	public RoundItem getRoundItem() {
		return roundItem;
	}

	public void setRoundItem(RoundItem roundItem) {
		this.roundItem = roundItem;
	}

	public void refresh(){
		getChildren().clear();
		add(lArtikal,0,0);
		add(lKolicina,1,0);
		add(lCena,2,0);
		
		lKolicina.setText(nf.format(roundItem.getQuantity()));
		lCena.setText(nf.format(roundItem.getQuantity() * roundItem.getProductDepartment().getProduct().getPrice()));
		itemHeight = BillSummaryItem.summaryItemHeight * (roundItem.getAdditionalList().size() + 1)  ;
		
		additionalList.clear();
		itemHeight = summaryItemHeight;
		for(int i=0;i<this.roundItem.getAdditionalList().size();i++){
			BillSummaryItemAditional additional = new BillSummaryItemAditional(this.roundItem.getAdditionalList().get(i));
			additional.refresh();
			GridPane.setColumnSpan(additional, 3);
			add(additional,0,i+1);
			additionalList.add(additional);
			itemHeight += summaryItemHeight;
		}
		for(int i=0;i<this.roundItem.getDescriptionalList().size();i++){
			BillSummaryItemDescriptional descriptional = new BillSummaryItemDescriptional(this.roundItem.getDescriptionalList().get(i),i);
			descriptional.refresh();
			GridPane.setColumnSpan(descriptional, 3);
			add(descriptional,0,additionalList.size() + i+1);
			descriptionalList.add(descriptional);
			itemHeight += summaryItemHeight;
		}
	}
	
	public void setSelected(boolean selected){
		unselectAll();
		if (selected){
			this.selected = true;
			((BillSummaryRound) getParent()).unselectAllExcept(this);
			lArtikal.getStyleClass().remove("backGroundTransparent");
			lArtikal.getStyleClass().add("backGroundOrange");
			lKolicina.getStyleClass().remove("backGroundTransparent");
			lKolicina.getStyleClass().add("backGroundOrange");
			lCena.getStyleClass().remove("backGroundTransparent");
			lCena.getStyleClass().add("backGroundOrange");
		}
		else{
			this.selected = false;
			lArtikal.getStyleClass().remove("backGroundOrange");
			lArtikal.getStyleClass().add("backGroundTransparent");
			lKolicina.getStyleClass().remove("backGroundOrange");
			lKolicina.getStyleClass().add("backGroundTransparent");
			lCena.getStyleClass().remove("backGroundOrange");
			lCena.getStyleClass().add("backGroundTransparent");
		}
	}
	public boolean isSelected() {
		return selected;
	}

	public void unselectAll(){
		selected = false;
		lArtikal.getStyleClass().remove("backGroundOrange");
		lArtikal.getStyleClass().add("backGroundTransparent");
		lKolicina.getStyleClass().remove("backGroundOrange");
		lKolicina.getStyleClass().add("backGroundTransparent");
		lCena.getStyleClass().remove("backGroundOrange");
		lCena.getStyleClass().add("backGroundTransparent");
		
		for (int i=0;i<additionalList.size();i++){
			additionalList.get(i).setSelected(false);
		}
		for (int i=0;i<descriptionalList.size();i++){
			descriptionalList.get(i).setSelected(false);
		}
	}
	
	public boolean hasSelectedItems(){
		if (selected) return true;
		for (int i=0;i<additionalList.size();i++){
			if (additionalList.get(i).isSelected())
				return true;
		}
		for (int i=0;i<descriptionalList.size();i++){
			if (descriptionalList.get(i).isSelected())
				return true;
		}
		return false;
	}
}
