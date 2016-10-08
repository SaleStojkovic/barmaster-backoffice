package rs.petcom.master.dal;

import java.util.ArrayList;

import rs.petcom.master.dal.product.ProductDepartment;

public class RoundItem {
	
	private ProductDepartment productDepartment;
	private double quantity;
	
	private ArrayList<RoundItem> additionalList = new ArrayList<RoundItem>();
	private ArrayList<RoundItemDescription> descriptionalList = new ArrayList<RoundItemDescription>();
	
	public ProductDepartment getProductDepartment() {
		return productDepartment;
	}
	public void setProductDepartment(ProductDepartment productDepartment) {
		this.productDepartment = productDepartment;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public ArrayList<RoundItem> getAdditionalList() {
		return additionalList;
	}
	public void setAdditionalList(ArrayList<RoundItem> additionalList) {
		this.additionalList = additionalList;
	}
	public ArrayList<RoundItemDescription> getDescriptionalList() {
		return descriptionalList;
	}
	public void setDescriptionalList(ArrayList<RoundItemDescription> descriptionalList) {
		this.descriptionalList = descriptionalList;
	}
	
	public double getSumWithDiscount(){
		double sum = 0;
		sum = productDepartment.getProduct().getPrice() * quantity;
		for (RoundItem ri : additionalList){
			sum += ri.getQuantity() * ri.getProductDepartment().getProduct().getPrice();
		}
		return sum;
	}
	public double getDiscount(){
		double sum = 0;
		sum = productDepartment.getProduct().getDiscount() * quantity;
		for (RoundItem ri : additionalList){
			sum += ri.getQuantity() * ri.getProductDepartment().getProduct().getDiscount();
		}
		return sum;
	}
	
	public void decreese(double value){
		quantity -= value;
		if (quantity < 0) quantity = 0;
	}

	public boolean isEqual(RoundItem newRoundItem){
		if (   productDepartment.getProduct().equals(newRoundItem.getProductDepartment().getProduct())
			&& additionalList.size() == 0
			&& descriptionalList.size() == 0){
			/*
			boolean additionalExists = false;
			for(int i=0;i<additionalList.size();i++){
				for (int j=0;j<newRoundItem.getAdditionalList().size();j++){
					if (additionalList.get(i).getProduct().equals(newRoundItem.getAdditionalList().get(j).getProduct())){
						additionalExists = true;
						break;
					}
				}
				if (additionalExists) break;
			}
			boolean descriptionaExists = false;
			for(int i=0;i<descriptionalList.size();i++){
				for (int j=0;j<newRoundItem.getDescriptionalList().size();j++){
					if (descriptionalList.get(i).getDescription().equals(newRoundItem.getDescriptionalList().get(j).getDescription())){
						descriptionaExists = true;
						break;
					}
				}
				if (descriptionaExists) break;
			}
			*/
			return (true);
		}
		
		
		
		return false;
	}
	
}
