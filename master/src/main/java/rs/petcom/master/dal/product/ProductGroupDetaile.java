package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductGroupDetaile {
	private int ProductGroupDetailsID;
	private int ProductGroupID;
	private long ProductID;
	public int getProductGroupDetailsID() {
		return ProductGroupDetailsID;
	}
	public void setProductGroupDetailsID(int productGroupDetailsID) {
		ProductGroupDetailsID = productGroupDetailsID;
	}
	public int getProductGroupID() {
		return ProductGroupID;
	}
	public void setProductGroupID(int productGroupID) {
		ProductGroupID = productGroupID;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	
	public static ObservableList<Product> getProductList(int productGroup, Connection connection){
		ObservableList<Product> list = FXCollections.observableArrayList();
		String query = "select p.* "
					 + "  from productgroupdetails pg"
					 + "  left join product p on p.ProductID = pg.ProductID"
					 + " where pg.ProductGroupID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, productGroup);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.setProductID(rs.getLong("ProductID"));
				product.setProductTypeID(rs.getInt("ProductTypeID"));
				product.setProductCategoryID(rs.getInt("ProductCategoryID"));
				product.setCode(rs.getString("Code"));
				product.setName(rs.getString("Name"));
				product.setShortName(rs.getString("ShortName"));
				product.setVatID(rs.getInt("VatID"));
				product.setUnitID(rs.getInt("UnitID"));
				product.setActive(rs.getBoolean("Active"));
				product.setSysDTCreated(new Date(rs.getDate("sysDTCreated").getTime()));
				try{
					product.setSysDTEdit(new Date(rs.getDate("sysDTEdit").getTime()));
				} catch (NullPointerException e){}
				product.setSysUserAddID(rs.getLong("sysUserAddID"));
				product.setSysUserEditID(rs.getLong("sysUserEditID"));
				product.setNutritionalValue(rs.getDouble("NutritionalValue"));
				product.setEnergyValue(rs.getDouble("EnergyValue"));
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
