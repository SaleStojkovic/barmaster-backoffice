package rs.petcom.master.dal.product;

public class ProductGroupType {
	private int ProductGroupTypeID;
	private String Name;
	public int getProductGroupTypeID() {
		return ProductGroupTypeID;
	}
	public void setProductGroupTypeID(int productGroupTypeID) {
		ProductGroupTypeID = productGroupTypeID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public String toString(){
		return Name;
	}
}
