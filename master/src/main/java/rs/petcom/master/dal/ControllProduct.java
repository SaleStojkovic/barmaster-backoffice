package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.product.Product;

public class ControllProduct {
	private int ControllProductID;
	private int ControllID;
	private long ProductID;
	private String Code;
	private String Name;
	public int getControllProductID() {
		return ControllProductID;
	}
	public void setControllProductID(int controllProductID) {
		ControllProductID = controllProductID;
	}
	public int getControllID() {
		return ControllID;
	}
	public void setControllID(int controllID) {
		ControllID = controllID;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
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
	
	public static ObservableList<ControllProduct> getList(Controll controll, Connection connection){
		ObservableList<ControllProduct> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	cp.*,"
					 + "	p.Code,"
					 + "	p.Name"
					 + "  from controllproduct cp"
					 + "  left join product p on p.ProductID = cp.ProductID"
					 + " where cp.ControllID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, controll.getControllID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ControllProduct cp = new ControllProduct();
				cp.setControllProductID(rs.getInt("ControllProductID"));
				cp.setControllID(rs.getInt("ControllID"));
				cp.setProductID(rs.getLong("ProductID"));
				cp.setCode(rs.getString("Code"));
				cp.setName(rs.getString("Name"));
				list.add(cp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void delete(Connection connection){
		String query = "delete from controllproduct"
				 	 + "  where ControllProductID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, ControllProductID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insert(Controll controll, Product prodcut, Connection connection){
		String query = "insert into controllproduct(`ControllID`,	`ProductID`) values(?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, controll.getControllID());
			ps.setLong(2, prodcut.getProductID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
