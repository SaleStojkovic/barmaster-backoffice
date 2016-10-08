package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductGroup {
	private int ProductGroupID;
	private String Name;
	private int ProductGroupTypeID;
	private boolean selected;
	
	public ProductGroup(int ProductGroupID, String Name, int ProductGroupTypeID){
		this.ProductGroupID = ProductGroupID;
		this.Name = Name;
		this.ProductGroupTypeID = ProductGroupTypeID;
	}
	
	public int getProductGroupID() {
		return ProductGroupID;
	}
	public void setProductGroupID(int productGroupID) {
		ProductGroupID = productGroupID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getProductGroupTypeID() {
		return ProductGroupTypeID;
	}
	public void setProductGroupTypeID(int productGroupTypeID) {
		ProductGroupTypeID = productGroupTypeID;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String toString(){
		return Name;
	}
	
	public static ObservableList<ProductGroup> getList(int groupType, Connection connection){
		ObservableList<ProductGroup> list = FXCollections.observableArrayList();
		String query = "select * from productgroup where ProductGroupTypeID = ? order by Name";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, groupType);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductGroup pg = new ProductGroup(rs.getInt("ProductGroupID"), 
												   rs.getString("Name"), 
												   rs.getInt("ProductGroupTypeID"));
				list.add(pg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
