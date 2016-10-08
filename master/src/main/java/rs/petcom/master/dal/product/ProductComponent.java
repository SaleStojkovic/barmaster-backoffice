package rs.petcom.master.dal.product;

import java.util.ArrayList;

public class ProductComponent {
	private Product product;
	private double normative;
	
	public ProductComponent(){
		this.product = null;
		this.normative = 0;
	}
	
	private ArrayList<ProductComponent> componentList;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getNormative() {
		return normative;
	}
	public void setNormative(double normative) {
		this.normative = normative;
	}
	public ArrayList<ProductComponent> getComponentList() {
		return componentList;
	}
	public void setComponentList(ArrayList<ProductComponent> componentList) {
		this.componentList = componentList;
	}
}
