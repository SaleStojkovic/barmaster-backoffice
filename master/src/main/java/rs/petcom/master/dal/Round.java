package rs.petcom.master.dal;

import java.util.ArrayList;
import java.util.Date;

import rs.petcom.master.dal.product.ProductDepartment;

public class Round {
	
	Date vreme;
	
	ArrayList<RoundItem> itemList = new ArrayList<RoundItem>();
		
	public ArrayList<RoundItem> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<RoundItem> itemList) {
		this.itemList = itemList;
	}
	
	public void addRoundItem(RoundItem roundItem){
		boolean inserted = false;
		//boolean possibleAdditiona = roundItem.getProduct().getAdditionalList(connection).size() > 0;
		
		//Ako nema dodatnih ni opisnih proveri da li postoji i uvećaj količinu
		if ((roundItem.getAdditionalList().size() == 0) && roundItem.getDescriptionalList().size() == 0 ){
			for(int i=0;i<itemList.size();i++){
				if ((itemList.get(i).getProductDepartment().getProduct().getProductID() == roundItem.getProductDepartment().getProduct().getProductID())
					&&
					(itemList.get(i).getAdditionalList().size() == 0)){
					
					itemList.get(i).setQuantity(itemList.get(i).getQuantity() +  roundItem.getQuantity());
					inserted = true;
					break;
				}
			}
		}
		if (!inserted){
			itemList.add(roundItem);
		}
		
	}
	
	public void addAdtitionalToLastItem(ProductDepartment product, double quantity){
		boolean inserted = false;
		
		RoundItem lastItem = itemList.get(itemList.size()-1);
		for(int i=0;i<lastItem.getAdditionalList().size();i++){
			if (lastItem.getAdditionalList().get(i).getProductDepartment().getProduct().getProductID() == product.getProductID()){
				lastItem.getAdditionalList().get(i).setQuantity(lastItem.getAdditionalList().get(i).getQuantity() + quantity);
				inserted = true;
				break;
			}
		}
		
		if (!inserted) {
			RoundItem newItem = new RoundItem();
			newItem.setProductDepartment(product);
			newItem.setQuantity(quantity);
			lastItem.getAdditionalList().add(newItem);
		}
	}
	
	public void addDescriptionalToLastItem(RoundItemDescription description){
		boolean inserted = false;
		RoundItem lastItem = itemList.get(itemList.size()-1);
		double quantityOfDescriptionItems=0;
		
		for(int i=0; i<lastItem.getDescriptionalList().size();i++)
			quantityOfDescriptionItems+=lastItem.getDescriptionalList().get(i).getQuantity();
		
		if(quantityOfDescriptionItems<lastItem.getQuantity()){
			for(int i=0;i<lastItem.getDescriptionalList().size();i++){
				if (lastItem.getDescriptionalList().get(i).getDescription().getDescriptionID() == description.getDescription().getDescriptionID()){
					lastItem.getDescriptionalList().get(i).setQuantity(lastItem.getDescriptionalList().get(i).getQuantity() + description.getQuantity());
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				if((lastItem.getQuantity()-quantityOfDescriptionItems) >= description.getQuantity())
					lastItem.getDescriptionalList().add(description);
				else{
					description.setQuantity(lastItem.getQuantity()-quantityOfDescriptionItems);
					lastItem.getDescriptionalList().add(description);
				}
					
			}
		}	
		
		
	}
	
	public void addDescriptionalToItem(int index, RoundItemDescription description){
		boolean inserted = false;
		RoundItem item = itemList.get(index);
		
		double quantityOfDescriptionItems=0;
		for(int i=0; i<item.getDescriptionalList().size();i++)
			quantityOfDescriptionItems+=item.getDescriptionalList().get(i).getQuantity();
		
		if(quantityOfDescriptionItems<item.getQuantity()){
		
			for(int i=0;i<item.getDescriptionalList().size();i++){
				if (item.getDescriptionalList().get(i).getDescription().getDescriptionID() == description.getDescription().getDescriptionID()){
					item.getDescriptionalList().get(i).setQuantity(item.getDescriptionalList().get(i).getQuantity() + description.getQuantity());
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				item.getDescriptionalList().add(description);
			}
		}
	}
	
	public boolean cleanupLast(){
		boolean cleanupDone = false;
		try{
		RoundItem lastItem = itemList.get(itemList.size()-1);
		
		
		if (lastItem.getAdditionalList().size() == 0 && lastItem.getDescriptionalList().size() == 0){
			for (int i=0;i<itemList.size()-1;i++){
				if ((itemList.get(i).getProductDepartment().getProduct().equals(lastItem.getProductDepartment().getProduct())
					&& (itemList.get(i).getAdditionalList().size() == 0)
					&& (itemList.get(i).getDescriptionalList().size() == 0))){
					itemList.get(i).setQuantity(itemList.get(i).getQuantity() + lastItem.getQuantity());
					cleanupDone = true;
					break;
				}
			}
		}
		if (cleanupDone) itemList.remove(itemList.size()-1);
		} catch (ArrayIndexOutOfBoundsException e){}
		return cleanupDone;
	}
	
	public double getSumWithDiscount(){
		double sum = 0;
		for (RoundItem ri : itemList){
			sum += ri.getSumWithDiscount();
		}
		return sum;
	}
	public double getDiscount(){
		double sum = 0;
		for (RoundItem ri : itemList){
			sum += ri.getDiscount();
		}
		return sum;
	}
	
	public void removeAllItems(){
		itemList.clear();
		//TODO uraditi logovanje
	}
	
	public boolean eqals(Round newRound){
		return this.vreme == newRound.vreme;
	}
	
	public boolean createOrder(){
		boolean result = false;
		for(RoundItem ri : itemList){
			if (ri.getProductDepartment().getKitchenDisplayID() > 0){
				result = true;
				break;
			}
		}
		return result;
	}
}
