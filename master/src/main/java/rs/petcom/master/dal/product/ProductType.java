package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductType {
	private int ProductTypeID;
	private String Name;
	private int Stock;
	private int PriceLeveling;
	private boolean Active;

	public int getProductTypeID() {
		return ProductTypeID;
	}

	public void setProductTypeID(int productTypeID) {
		ProductTypeID = productTypeID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}

	public int getPriceLeveling() {
		return PriceLeveling;
	}

	public void setPriceLeveling(int priceLeveling) {
		PriceLeveling = priceLeveling;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}
	
	@Override
	public String toString(){
		return Name;
	}

	public static ObservableList<ProductType> getObeservableList(boolean activeOnly, Connection connection) {

		ObservableList<ProductType> list = FXCollections.observableArrayList();

		String query = "select * from productType";
		if (activeOnly)
			query += " where Active = true ";
		query += " order by ProductTypeID";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ProductType pt = new ProductType();
				pt.ProductTypeID = rs.getInt("ProductTypeID");
				pt.Name = rs.getString("Name");
				pt.Stock = rs.getInt("Stock");
				pt.PriceLeveling = rs.getInt("PriceLeveling");
				pt.Active = rs.getBoolean("Active");
				list.add(pt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
