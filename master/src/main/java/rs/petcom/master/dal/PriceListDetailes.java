package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriceListDetailes {
	
	private long PriceListDetailsID;
	private long PriceListID;
	private long ProductID;
    private double Price;
    private double Discount;
    private String Code;
	private String Name;
	
	public Long getPriceListDetailsID() {
		return PriceListDetailsID;
	}
	public void setPriceListDetailsID(Long priceListDetailsID) {
		PriceListDetailsID = priceListDetailsID;
	}
	public Long getPriceListID() {
		return PriceListID;
	}
	public void setPriceListID(Long priceListID) {
		PriceListID = priceListID;
	}
	public Long getProductID() {
		return ProductID;
	}
	public void setProductID(Long productID) {
		ProductID = productID;
	}
	public Double getPrice() {
		return Price;
	}
	public void setPrice(Double price) {
		Price = price;
	}
	public Double getDiscount() {
		return Discount;
	}
	public void setDiscount(Double discount) {
		Discount = discount;
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
	
	public long insert(Connection connection){
		long result = 0;
		String query = "insert into pricelistdetails(`PriceListID`,`ProductID`,`Price`,`Discount`) values(?,?,?,?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, PriceListID);
			ps.setLong(2, ProductID);
			ps.setDouble(3, Price);
			ps.setDouble(4, Discount);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			result = rs.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void update(Connection connection){
		String query = "update pricelistdetails set "
					 + "	PriceListID = ?, ProductID = ?, Price = ?, Discount = ? "
					 + " where PriceListDetailsID = ?";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, PriceListID);
			ps.setLong(2, ProductID);
			ps.setDouble(3, Price);
			ps.setDouble(4, Discount);
			ps.setLong(5, PriceListDetailsID);
			ps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Connection connection){
		if (PriceListDetailsID > 0){
			String query = "delete from pricelistdetails where PriceListDetailsID = ?";
			try {
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setLong(1, PriceListDetailsID);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ObservableList<PriceListDetailes> getList(PriceList priceList, Connection connection){
		ObservableList<PriceListDetailes> list = FXCollections.observableArrayList();
		
		String upit = "select pld.*, p.Code, p.Name"
				    + "  from pricelistdetails pld"
				    + "  left join product p on p.ProductID = pld.ProductID"
				    + " where pld.PriceListID = ?"
				    + " order by p.Code";
		try {
			PreparedStatement ps = connection.prepareStatement(upit);
			ps.setLong(1, priceList.getPriceListID());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PriceListDetailes pld = new PriceListDetailes();
				pld.setPriceListDetailsID(rs.getLong("PriceListDetailsID"));
				pld.setPriceListID(rs.getLong("PriceListID"));
				pld.setProductID(rs.getLong("ProductID"));
				pld.setPrice(rs.getDouble("Price"));
				pld.setDiscount(rs.getDouble("Discount"));
				pld.setCode(rs.getString("Code"));
				pld.setName(rs.getString("Name"));
				list.add(pld);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	
}
