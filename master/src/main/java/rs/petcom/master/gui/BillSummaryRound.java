package rs.petcom.master.gui;

import java.util.ArrayList;

import javafx.scene.layout.AnchorPane;
import rs.petcom.master.dal.Round;

public class BillSummaryRound extends AnchorPane{
	
	private double itemHeihght;
	
	ArrayList<BillSummaryItem> itemsList = new ArrayList<BillSummaryItem>();
	
	private Round round = new Round();
		
	public BillSummaryRound(){
		setPrefWidth(200);
		getStyleClass().add("billSummaryRound");
	}
	
	public void setRound(Round round){
		this.round = round;
	}
	public Round getRound(){
		return round;
	}
	
	public void refresh(){
		itemsList.clear();
		getChildren().clear();
		for(int i=0;i<round.getItemList().size();i++){
			BillSummaryItem bsi = new BillSummaryItem(round.getItemList().get(i));
			bsi.refresh();
			itemsList.add(bsi);
			getChildren().add(bsi);
		}
		
		double offset = 0;
		for(int i=0;i<itemsList.size();i++){
			AnchorPane.setTopAnchor(itemsList.get(i), offset);
			AnchorPane.setLeftAnchor(itemsList.get(i), 0.0);
			AnchorPane.setRightAnchor(itemsList.get(i), 0.0);
			offset += itemsList.get(i).getItemHeight();
			setMinHeight(offset);
			setPrefHeight(offset);
			setMaxHeight(offset);
			itemHeihght =  offset;
		}
	}
	
	public double getItemHeight(){
		return itemHeihght;
	}
	
	public void unselectAll(){
		for (int i=0;i<itemsList.size();i++){
			itemsList.get(i).unselectAll();
		}
	}
	public void unselectAllExcept(BillSummaryItem bsi){
		for (int i=0;i<itemsList.size();i++){
			if (!bsi.equals(itemsList.get(i))) itemsList.get(i).unselectAll();
		}
	}
	public boolean hasSelectedItems(){
		for (int i=0;i<itemsList.size();i++){
			if (itemsList.get(i).hasSelectedItems())
				return true;
		}
		return false;
	}
	/*
	public void decreseSelected(){
		for(int i=0;i<itemsList.size();i++){
			if (itemsList.get(i).isSelected()){
				if (itemsList.get(i).getRoundItem().getQuantity() > 1){
					itemsList.get(i).getRoundItem().setQuantity(itemsList.get(i).getRoundItem().getQuantity()-1);
					break;
				}
				else{
					itemsList.remove(i);
					break;
				}
			}
			itemsList.get(i).decreseSelected();
		}
	}
	*/
}
