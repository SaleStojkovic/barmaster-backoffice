package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductCategory {
	
	private int ProductCategoryID;
	private String Name;
	public int getProductCategoryID() {
		return ProductCategoryID;
	}
	public void setProductCategoryID(int productCategoryID) {
		ProductCategoryID = productCategoryID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	@Override
	public String toString(){
		return Name;
	}
	
	public static ObservableList<ProductCategory> getObeservableList(Connection connection){

		ObservableList<ProductCategory> list = FXCollections.observableArrayList();

		String query = "select * from productcategory";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductCategory pc = new ProductCategory();
				pc.ProductCategoryID = rs.getInt("ProductCategoryID");
				pc.Name = rs.getString("Name");

				list.add(pc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
