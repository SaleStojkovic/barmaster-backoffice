package rs.petcom.master.gui;

import java.util.ArrayList;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import rs.petcom.master.dal.Bill;

public class BillSummaryPane extends BorderPane{
	
	private static BillSummaryHeader bsh = new BillSummaryHeader();
	private static ScrollPane scrollPane = new ScrollPane();
	private static AnchorPane roundPane = new AnchorPane();
	private static BillSummarySum bss = new BillSummarySum();
		
	public void setPaneWidth(double width){
		roundPane.setPrefWidth(width-20);
	}
	
	Bill bill;
	
	int selectedRoundIndex = -1;
	int selectedItemIndex = -1;
	int selectedAditionalIndex = -1;
	int selectedDescriptionalIndex = -1;
	
	ArrayList<BillSummaryRound> roundList = new ArrayList<BillSummaryRound>();
	
	public BillSummaryPane(){
		setTop(bsh);
		setCenter(scrollPane);
		scrollPane.setContent(roundPane);
		setBottom(bss);
	}
	
	public void setBill(Bill bill){
		this.bill = bill;
		refresh();
	}
	
	public void refresh(){
		
		roundList.clear();
		for (int i=0;i<bill.getRoundList().size();i++){
			BillSummaryRound bsr = new BillSummaryRound();
			bsr.setRound(bill.getRoundList().get(i));
			bsr.refresh();
			roundList.add(bsr);
		}
		
		double offset = 0;
		roundPane.getChildren().clear();
		for(int i=0;i<roundList.size();i++){
			AnchorPane.setTopAnchor(roundList.get(i), offset);
			AnchorPane.setLeftAnchor(roundList.get(i), 0.0);
			AnchorPane.setRightAnchor(roundList.get(i), 0.0);
			roundPane.getChildren().add(roundList.get(i));
			offset += roundList.get(i).getItemHeight();
		}
		bss.refresh(bill);
	}
	
	public void unselectAll(){
		for (int i=0;i<roundList.size();i++){
			roundList.get(i).unselectAll();
		}
	}
	
	public boolean hasSelectedItems(){
		for (int i=0;i<roundList.size();i++){
			if (roundList.get(i).hasSelectedItems())
				return true;
		}
		return false;
	}
	/*
	public void decreseSelected(){
		for (int i=0;i<roundList.size();i++){
			roundList.get(i).decreseSelected();
		}
	}
	*/
	
	public void locateSelected(){
		selectedRoundIndex = -1;
		selectedItemIndex = -1;
		selectedAditionalIndex = -1;
		selectedDescriptionalIndex = -1;
		
		if (roundList.size() > 1){
			for (int i=0;i<roundList.size();i++){
				if (roundList.get(i).hasSelectedItems()){
					selectedRoundIndex = i;
					break;
				}
			}
		}
		else{
			selectedRoundIndex = 0;
		}
		if (selectedRoundIndex > -1){
			for (int i=0;i<roundList.get(selectedRoundIndex).itemsList.size();i++){
				BillSummaryItem bsi = roundList.get(selectedRoundIndex).itemsList.get(i);
				if (bsi.isSelected()){
					selectedItemIndex = i;
					break;
				}
				if (bsi.hasSelectedItems()){
					selectedItemIndex = i;
					for (int j=0;j<bsi.getAdditionalList().size();j++){
						if (bsi.getAdditionalList().get(j).isSelected()){
							selectedAditionalIndex = j;
							break;
						}
					}
					for (int k=0;k<bsi.getDescriptionalList().size();k++){
						if (bsi.getDescriptionalList().get(k).isSelected()){
							selectedDescriptionalIndex = bsi.getDescriptionalList().get(k).getIndex();
						}
					}
				}
			}
		}
		
	}

	public int getSelectedRoundIndex() {
		return selectedRoundIndex;
	}

	public void setSelectedRoundIndex(int selectedRoundIndex) {
		this.selectedRoundIndex = selectedRoundIndex;
	}

	public int getSelectedItemIndex() {
		return selectedItemIndex;
	}

	public void setSelectedItemIndex(int selectedItemIndex) {
		this.selectedItemIndex = selectedItemIndex;
	}

	public int getSelectedAditionalIndex() {
		return selectedAditionalIndex;
	}

	public void setSelectedAditionalIndex(int selectedAditionalIndex) {
		this.selectedAditionalIndex = selectedAditionalIndex;
	}

	public int getSelectedDescriptionalIndex() {
		return selectedDescriptionalIndex;
	}

	public void setSelectedDescriptionalIndex(int selectedDescriptionalIndex) {
		this.selectedDescriptionalIndex = selectedDescriptionalIndex;
	}
	
	
	public void selectItem(int roundIndex, int itemIndex){
		roundList.get(roundIndex).itemsList.get(itemIndex).setSelected(true);
	}
	
}
