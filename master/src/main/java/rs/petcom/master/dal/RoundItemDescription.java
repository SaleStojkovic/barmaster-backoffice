package rs.petcom.master.dal;

import rs.petcom.master.dal.product.Description;

public class RoundItemDescription {

	private Description description;
	private double quantity = 0;
	
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public RoundItemDescription(Description description, double quantity){
		this.description = description;
		this.quantity = quantity;
	}
}
